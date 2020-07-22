package de.atb.typhondl.xtext.ui.scriptGeneration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.modelUtils.DBService;
import de.atb.typhondl.xtext.ui.modelUtils.SoftwareService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;

public class DeploymentModelService {

    private static final String DOCKER_COMPOSE = "DockerCompose";
    private DeploymentModel model;
    private IFile file;
    private XtextLiveScopeResourceSetProvider provider;
    private Properties properties;
    private ClusterType clusterTypeObject;

    public DeploymentModelService(IFile file, XtextLiveScopeResourceSetProvider provider, Properties properties) {
        this.file = file;
        this.provider = provider;
        this.properties = properties;
        this.model = TyphonDLFactory.eINSTANCE.createDeploymentModel();
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
        if (!EcoreUtil2.getAllContentsOfType(model, Platform.class).isEmpty()) {
            this.clusterTypeObject = EcoreUtil2.getAllContentsOfType(model, ClusterType.class).get(0);
            addDBsToModel();
        } else {
            this.model = null;
        }
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
        String clusterType = clusterTypeObject.getName();

        // Polystore Metadata
        model = DBService.addMongoIfNotExists(model);
        DB polystoreDB = DBService.createPolystoreDB(this.properties, clusterType, DBService.getMongoDBType(model));
        model.getElements().add(polystoreDB);
        Container polystoreDBContainer = ContainerService.create(
                properties.getProperty(PropertiesService.DB_CONTAINERNAME), containerType, polystoreDB,
                properties.getProperty(PropertiesService.DB_CONTAINERNAME) + ":"
                        + properties.getProperty(PropertiesService.DB_PORT));
        if (clusterType.equalsIgnoreCase(DOCKER_COMPOSE)) {
            polystoreDBContainer.getProperties().add(ContainerService.createKeyValuesArray("volumes", new String[] {
                    "./" + properties.getProperty(PropertiesService.DB_VOLUME) + "/:/docker-entrypoint-initdb.d" }));
        }
        application.getContainers().add(polystoreDBContainer);

        // Polystore API
        Software polystoreAPI = SoftwareService.create(properties.getProperty(PropertiesService.API_NAME),
                properties.getProperty(PropertiesService.API_IMAGE));
        model.getElements().add(polystoreAPI);
        Container polystoreAPIContainer = ContainerService.create(
                properties.getProperty(PropertiesService.API_CONTAINERNAME), containerType, polystoreAPI,
                properties.getProperty(PropertiesService.API_CONTAINERNAME) + ":"
                        + properties.getProperty(PropertiesService.API_PORT));
        polystoreAPIContainer.setPorts(ContainerService
                .createPorts(new String[] { "published", properties.getProperty(PropertiesService.API_PUBLISHEDPORT),
                        "target", properties.getProperty(PropertiesService.API_PORT) }));
        if (Integer.parseInt(properties.getProperty(PropertiesService.API_REPLICAS)) > 1) {
            polystoreAPIContainer.setReplication(ContainerService.createStatelessReplication(
                    Integer.parseInt(properties.getProperty(PropertiesService.API_REPLICAS))));
        }
        polystoreAPIContainer.getProperties().add(ContainerService.addAPIEntrypoint(clusterType,
                properties.getProperty(PropertiesService.API_ENTRYPOINT)));
        if (clusterType.equalsIgnoreCase(DOCKER_COMPOSE)) {
            polystoreAPIContainer.getProperties()
                    .addAll(ContainerService.createKeyValues(new String[] { "restart", "always" }));
        }
        application.getContainers().add(polystoreAPIContainer);

        // Polystore UI
        Software polystoreUI = SoftwareService.create(properties.getProperty(PropertiesService.UI_NAME),
                properties.getProperty(PropertiesService.UI_IMAGE));
        polystoreUI.setEnvironment(SoftwareService.createEnvironment(
                new String[] { "API_PORT", properties.getProperty(PropertiesService.UI_ENVIRONMENT_API_PORT),
                        "API_HOST", properties.getProperty(PropertiesService.UI_ENVIRONMENT_API_HOST) }));
        model.getElements().add(polystoreUI);
        Container polystoreUIContainer = ContainerService.create(
                properties.getProperty(PropertiesService.UI_CONTAINERNAME), containerType, polystoreUI,
                properties.getProperty(PropertiesService.UI_CONTAINERNAME) + ":"
                        + properties.getProperty(PropertiesService.UI_PORT));
        polystoreUIContainer.setPorts(ContainerService
                .createPorts(new String[] { "published", properties.getProperty(PropertiesService.UI_PUBLISHEDPORT),
                        "target", properties.getProperty(PropertiesService.UI_PORT) }));
        polystoreUIContainer.getDepends_on().add(ContainerService.createDependsOn(polystoreAPIContainer));
        application.getContainers().add(polystoreUIContainer);

        // QL Server
        Software qlServer = SoftwareService.create(properties.getProperty(PropertiesService.QLSERVER_NAME),
                properties.getProperty(PropertiesService.QLSERVER_IMAGE));
        model.getElements().add(qlServer);
        Container qlServerContainer = ContainerService.create(
                properties.getProperty(PropertiesService.QLSERVER_CONTAINERNAME), containerType, qlServer,
                properties.getProperty(PropertiesService.QLSERVER_CONTAINERNAME) + ":"
                        + properties.getProperty(PropertiesService.QLSERVER_PORT));
        if (Integer.parseInt(properties.getProperty(PropertiesService.QLSERVER_REPLICAS)) > 1) {
            qlServerContainer.setReplication(ContainerService.createStatelessReplication(
                    Integer.parseInt(properties.getProperty(PropertiesService.QLSERVER_REPLICAS))));
        }
        if (clusterType.equalsIgnoreCase(DOCKER_COMPOSE)) {
            qlServerContainer.getProperties()
                    .addAll(ContainerService.createKeyValues(new String[] { "restart", "always" }));
        }
        application.getContainers().add(qlServerContainer);

        // Analytics
        if (properties.get(PropertiesService.POLYSTORE_USEANALYTICS).equals("true")) {
            this.model = AnalyticsService.addAnalytics(model, properties, clusterTypeObject, containerType);
        }
    }

    public void addToMetadata(String outputFolder, String MLName) {
        Path DLPath = Paths.get(file.getLocation().toOSString().replace(file.getFileExtension(), "xmi"));
        Path MLPath = Paths.get(file.getLocation().toOSString().replace(file.getName(), MLName));
        String mongoInsertStatement = createMongoCommands(DLPath, MLPath);
        switch (clusterTypeObject.getName()) {
        case "Kubernetes":
            // to be able to add the models to the kubernetes job, the
            // mongo.insert(DLxmi,MLxmi) has to be added to the model here, so that acceleo
            // can access it. It's not nice, maybe we should think about a different plugin
            // to generate our scripts
            addInsertStatementToPolystoreMongoContainer(getPolystoreMongoContainer(model, properties),
                    mongoInsertStatement, properties);
            break;
        case DOCKER_COMPOSE:
            writeInsertStatementToJavaScriptFile(outputFolder, mongoInsertStatement, properties);
            break;
        default:
            break;
        }
    }

    /**
     * Creates a "addModels.js" file inside the db.volume directory. This script is
     * executed the first time the polystore-mongo container is started and adds
     * both the ML and DL model to the database db.environment.MONGO_INITDB_DATABASE
     * 
     * @param DLPath
     * @param mongoInsertStatement
     * @param properties
     */
    private static void writeInsertStatementToJavaScriptFile(String outputFolder, String mongoInsertStatement,
            Properties properties) {
        String modelsFolder = outputFolder + File.separator + properties.getProperty(PropertiesService.DB_VOLUME);
        String path = modelsFolder + File.separator + "addModels.js";
        if (!Files.exists(Paths.get(outputFolder))) {
            try {
                Files.createDirectory(Paths.get(outputFolder));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Files.exists(Paths.get(modelsFolder))) {
            try {
                Files.createDirectory(Paths.get(modelsFolder));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Files.write(Paths.get(path), mongoInsertStatement.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches for the polystore-mongo container in the given model
     * 
     * @param model
     * @param properties
     * @return A container named "polystore-mongo"
     */
    private static Container getPolystoreMongoContainer(DeploymentModel model, Properties properties) {
        Container mongo = EcoreUtil2.getAllContentsOfType(model, Container.class).stream()
                .filter(container -> container.getName()
                        .equalsIgnoreCase(properties.getProperty(PropertiesService.DB_CONTAINERNAME)))
                .findFirst().orElse(null);
        return mongo == null ? null : mongo;
    }

    /**
     * Adds the mongo insert statement to the polystoreMongoContainer
     * 
     * @param polystoreMongoContainer The polystore-mongo container
     * @param addToMongoContainer     The String containing the
     *                                mongo.insert(DLmodel,MLmodel) statement
     * @param properties              The polystore.properties saved in the project
     *                                folder
     */
    private static void addInsertStatementToPolystoreMongoContainer(Container polystoreMongoContainer,
            String addToMongoContainer, Properties properties) {
        Key_Values print = TyphonDLFactory.eINSTANCE.createKey_Values();
        print.setName("print");
        print.setValue(addToMongoContainer);
        polystoreMongoContainer.getProperties().add(print);
    }

    /**
     * Creates a mongo insert statement containing the DL and ML model in a JSON
     * field fitting the API
     * 
     * @param DLmodel The path to the newly created DLmodel.xmi
     * @param MLmodel The path to the source MLmodel.xmi
     * @return The mongo insert statement containing both models
     */
    private static String createMongoCommands(Path DLmodel, Path MLmodel) {
        String DLmodelContent = "";
        String MLmodelContent = "";
        try {
            DLmodelContent = String.join("", Files.readAllLines(DLmodel));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            MLmodelContent = String.join("", Files.readAllLines(MLmodel));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "db.models.insert([{\"_id\":UUID(), \"version\":1, \"initializedDatabases\":"
                + "false, \"initializedConnections\":true, \"contents\":\"" + DLmodelContent.replaceAll("\"", "\\\\\"")
                + "\", \"type\":\"DL\", \"dateReceived\":ISODate(), "
                + "\"_class\":\"com.clms.typhonapi.models.Model\" }, {\"_id\":UUID(), \"version\":1, \"initializedDatabases\":"
                + "false, \"initializedConnections\":false, \"contents\":\"" + MLmodelContent.replaceAll("\"", "\\\\\"")
                + "\", \"type\":\"ML\", \"dateReceived\":ISODate(), "
                + "\"_class\":\"com.clms.typhonapi.models.Model\" }]);";
    }

    public DeploymentModel getModel() {
        return this.model;
    }

}
