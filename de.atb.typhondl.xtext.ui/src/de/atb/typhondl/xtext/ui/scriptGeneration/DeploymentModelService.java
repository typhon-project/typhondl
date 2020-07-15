package de.atb.typhondl.xtext.ui.scriptGeneration;

import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.modelUtils.DBService;
import de.atb.typhondl.xtext.ui.modelUtils.SoftwareService;

public class DeploymentModelService {

    private DeploymentModel model;
    private IFile file;
    private XtextLiveScopeResourceSetProvider provider;
    private Properties properties;
    private String clusterType;

    public DeploymentModelService(IFile file, XtextLiveScopeResourceSetProvider provider, Properties properties) {
        this.file = file;
        this.provider = provider;
        this.properties = properties;
        this.model = TyphonDLFactory.eINSTANCE.createDeploymentModel();
        this.clusterType = EcoreUtil2.getAllContentsOfType(model, ClusterType.class).get(0).getName();
    }

    public void readModel() {
        XtextResourceSet resourceSet = (XtextResourceSet) this.provider.get(this.file.getProject());
        resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
        // adds all .tdl files in project folder to resourceSet
        IResource members[] = null;
        try {
            members = this.file.getProject().members();
        } catch (CoreException e) {
            e.printStackTrace();
        }
        for (IResource member : members) {
            if (member instanceof IFile) {
                if (((IFile) member).getFileExtension().equals("tdl")) {
                    resourceSet.getResource(URI.createPlatformResourceURI(member.getFullPath().toString(), true), true);
                }
            }
        }
        // read DL model and properties
        URI modelURI = URI.createPlatformResourceURI(this.file.getFullPath().toString(), true);
        Resource DLmodelResource = resourceSet.getResource(modelURI, true);
        this.model = (DeploymentModel) DLmodelResource.getContents().get(0);
        addDBsToModel();
    }

    /**
     * Finds and adds the DB models from additional files if given as {@link Import}
     * in the main model file
     */
    private void addDBsToModel() {
        Resource resource = this.model.eResource();
        URI uri = resource.getURI().trimSegments(1);

        EcoreUtil2.getAllContentsOfType(this.model, Import.class).stream()
                .filter(info -> info.getRelativePath().endsWith("tdl")).forEach(info -> {
                    model.getElements()
                            .addAll(((DeploymentModel) resource.getResourceSet()
                                    .getResource(uri.appendSegment(info.getRelativePath()), true).getContents().get(0))
                                            .getElements());
                });
    }

    public void addPolystore() {

        // get Application for polystore containers TODO remove application
        ContainerType containerType = EcoreUtil2.getAllContentsOfType(model, ContainerType.class).get(0);
        Application application = EcoreUtil2.getAllContentsOfType(model, Application.class).get(0);

        // Polystore Metadata
        model = DBService.addMongoIfNotExists(model);
        DB polystoreDB = DBService.createPolystoreDB(this.properties, this.clusterType,
                DBService.getMongoDBType(model));
        model.getElements().add(polystoreDB);
        Container polystoreDBContainer = ContainerService.create(properties.getProperty("db.containername"),
                containerType, polystoreDB,
                properties.getProperty("db.containername") + ":" + properties.getProperty("db.port"));
        if (clusterType.equalsIgnoreCase("DockerCompose")) {
            polystoreDBContainer.getProperties().add(ContainerService.createKeyValuesArray("volumes",
                    new String[] { "./" + properties.getProperty("db.volume") + "/:/docker-entrypoint-initdb.d" }));
        }
        application.getContainers().add(polystoreDBContainer);

        // Polystore API
        Software polystoreAPI = SoftwareService.create(properties.getProperty("api.name"),
                properties.getProperty("api.image"));
        model.getElements().add(polystoreAPI);
        Container polystoreAPIContainer = ContainerService.create(properties.getProperty("api.containername"),
                containerType, polystoreAPI,
                properties.getProperty("api.containername") + ":" + properties.getProperty("api.port"));
        polystoreAPIContainer.setPorts(ContainerService.createPorts(new String[] { "published",
                properties.getProperty("api.publishedPort"), "target", properties.getProperty("api.port") }));
        polystoreAPIContainer.setReplication(
                ContainerService.createStatelessReplication(Integer.parseInt(properties.getProperty("api.replicas"))));
        polystoreAPIContainer.getProperties()
                .add(ContainerService.addAPIEntrypoint(clusterType, properties.getProperty("api.entrypoint")));
        if (clusterType.equalsIgnoreCase("DockerCompose")) {
            polystoreAPIContainer.getProperties()
                    .addAll(ContainerService.createKeyValues(new String[] { "restart", "always" }));
        }
        application.getContainers().add(polystoreAPIContainer);

        // Polystore UI
        Software polystoreUI = SoftwareService.create(properties.getProperty("ui.name"),
                properties.getProperty("ui.image"));
        polystoreUI = SoftwareService.addEnvironment(polystoreUI,
                new String[] { "API_PORT", properties.getProperty("ui.environment.API_PORT"), "API_HOST",
                        properties.getProperty("ui.environment.API_HOST") });
        model.getElements().add(polystoreUI);
        Container polystoreUIContainer = ContainerService.create(properties.getProperty("ui.containername"),
                containerType, polystoreUI,
                properties.getProperty("ui.containername") + ":" + properties.getProperty("ui.port"));
        polystoreUIContainer.setPorts(ContainerService.createPorts(new String[] { "published",
                properties.getProperty("ui.publishedPort"), "target", properties.getProperty("ui.port") }));
        polystoreUIContainer.getDepends_on().add(ContainerService.createDependsOn(polystoreAPIContainer));
        application.getContainers().add(polystoreUIContainer);

        // QL Server
        Software qlServer = SoftwareService.create(properties.getProperty("qlserver.name"),
                properties.getProperty("qlserver.image"));
        model.getElements().add(qlServer);
        Container qlServerContainer = ContainerService.create(properties.getProperty("qlserver.containername"),
                containerType, qlServer,
                properties.getProperty("qlserver.containername") + ":" + properties.getProperty("qlserver.port"));
        qlServerContainer.setReplication(ContainerService
                .createStatelessReplication(Integer.parseInt(properties.getProperty("qlserver.replicas"))));
        if (clusterType.equalsIgnoreCase("DockerCompose")) {
            qlServerContainer.getProperties()
                    .addAll(ContainerService.createKeyValues(new String[] { "restart", "always" }));
        }
        application.getContainers().add(qlServerContainer);

        // Analytics
        if (properties.get("polystore.useAnalytics").equals("true")) {
            this.model = AnalyticsService.addAnalytics(model, properties);
        }
    }

    public void addToMetadata() {
        // TODO Auto-generated method stub
    }

    public DeploymentModel getModel() {
        return this.model;
    }

}
