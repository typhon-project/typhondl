package de.atb.typhondl.xtext.ui.scriptGeneration;

/*-
 * #%L
 * de.atb.typhondl.xtext.ui
 * %%
 * Copyright (C) 2018 - 2020 ATB
 * %%
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * #L%
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;
import org.xml.sax.SAXException;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.modelUtils.DBService;
import de.atb.typhondl.xtext.ui.modelUtils.ModelService;
import de.atb.typhondl.xtext.ui.modelUtils.SoftwareService;
import de.atb.typhondl.xtext.ui.modelUtils.VolumesService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.technologies.ITechnology;

public class DeploymentModelService {

    public static DeploymentModel createModel(IFile file, XtextLiveScopeResourceSetProvider provider,
            Properties properties, String outputFolder) throws ParserConfigurationException, IOException, SAXException {
        DeploymentModel model = readModel(file, provider, properties);
        model = addDBsToModel(model);
        model = addPolystore(properties, model, outputFolder);
        return model;
    }

    public static DeploymentModel readModel(IFile file, XtextLiveScopeResourceSetProvider provider,
            Properties properties) {
        XtextResourceSet resourceSet = ModelService.getResourceSet(provider, file);
        // read DL model and properties
        URI modelURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
        Resource DLmodelResource = resourceSet.getResource(modelURI, true);
        return (DeploymentModel) DLmodelResource.getContents().get(0);
    }

    /**
     * Finds and adds the DB models from additional files if given as {@link Import}
     * in the main model file
     * 
     * @param model
     * @return
     */
    private static DeploymentModel addDBsToModel(DeploymentModel model) {
        Resource resource = model.eResource();
        URI uri = resource.getURI().trimSegments(1);

        EcoreUtil2.getAllContentsOfType(model, Import.class).stream()
                .filter(info -> info.getRelativePath().endsWith("tdl")).forEach(info -> {
                    model.getElements()
                            .addAll(((DeploymentModel) resource.getResourceSet()
                                    .getResource(uri.appendSegment(info.getRelativePath()), true).getContents().get(0))
                                            .getElements());
                });
        return model;
    }

    public static DeploymentModel addPolystore(Properties properties, DeploymentModel model, String outputFolder)
            throws ParserConfigurationException, IOException, SAXException {

        // get Application for polystore containers TODO remove application
        ContainerType containerType = EcoreUtil2.getAllContentsOfType(model, ContainerType.class).get(0);
        Application application = EcoreUtil2.getAllContentsOfType(model, Application.class).get(0);
        ITechnology chosenTechnology = ModelService.getTechnology(ModelService.getClusterType(model));
        // Polystore Metadata
        model = DBService.addMongoIfNotExists(model);
        DB polystoreDB = DBService.createPolystoreDB(properties, chosenTechnology, DBService.getMongoDBType(model));
        model.getElements().add(polystoreDB);
        Container polystoreDBContainer = ContainerService.create(
                properties.getProperty(PropertiesService.DB_CONTAINERNAME), containerType, polystoreDB,
                properties.getProperty(PropertiesService.DB_CONTAINERNAME) + ":"
                        + properties.getProperty(PropertiesService.DB_PORT));
        if (chosenTechnology.setInitDB()) {
            polystoreDBContainer.getProperties().add(ModelService.createKeyValuesArray("volumes", new String[] {
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
        polystoreAPIContainer
                .setResources(ContainerService.createResources(properties.getProperty(PropertiesService.API_LIMIT_CPU),
                        properties.getProperty(PropertiesService.API_LIMIT_MEMORY),
                        properties.getProperty(PropertiesService.API_RESERVATION_CPU),
                        properties.getProperty(PropertiesService.API_RESERVATION_MEMORY)));
        if (chosenTechnology.waitForMetadata()) {
            polystoreAPIContainer.getProperties()
                    .add(ContainerService.addAPIEntrypoint(properties.getProperty(PropertiesService.API_ENTRYPOINT)));
        }
        if (!chosenTechnology.restartIsDefault()) {
            polystoreAPIContainer.getProperties().add(ModelService.createKey_Values("restart", "always", null));
        }
        application.getContainers().add(polystoreAPIContainer);

        // Polystore UI
        Software polystoreUI = SoftwareService.create(properties.getProperty(PropertiesService.UI_NAME),
                properties.getProperty(PropertiesService.UI_IMAGE));
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
        qlServer.setEnvironment(SoftwareService
                .createEnvironment(new String[] { "TZ", properties.getProperty(PropertiesService.QLSERVER_TIMEZONE) }));
        model.getElements().add(qlServer);
        Container qlServerContainer = ContainerService.create(
                properties.getProperty(PropertiesService.QLSERVER_CONTAINERNAME), containerType, qlServer,
                properties.getProperty(PropertiesService.QLSERVER_CONTAINERNAME) + ":"
                        + properties.getProperty(PropertiesService.QLSERVER_PORT));
        if (Integer.parseInt(properties.getProperty(PropertiesService.QLSERVER_REPLICAS)) > 1) {
            qlServerContainer.setReplication(ContainerService.createStatelessReplication(
                    Integer.parseInt(properties.getProperty(PropertiesService.QLSERVER_REPLICAS))));
        }
        qlServerContainer.setResources(
                ContainerService.createResources(properties.getProperty(PropertiesService.QLSERVER_LIMIT_CPU),
                        properties.getProperty(PropertiesService.QLSERVER_LIMIT_MEMORY),
                        properties.getProperty(PropertiesService.QLSERVER_RESERVATION_CPU),
                        properties.getProperty(PropertiesService.QLSERVER_RESERVATION_MEMORY)));
        if (chosenTechnology.restartIsDefault()) {
            qlServerContainer.getProperties()
                    .addAll(ModelService.createListOfKey_Values(new String[] { "restart", "always" }));
        }
        application.getContainers().add(qlServerContainer);

        // Analytics
        if (properties.getProperty(PropertiesService.POLYSTORE_USEANALYTICS).equals("true")) {
            model = AnalyticsService.addAnalytics(model, properties, ModelService.getClusterType(model), containerType,
                    outputFolder);
        }

        // NLAE
        if (properties.getProperty(PropertiesService.POLYSTORE_USENLAE).equals("true")) {
            model = NLAEService.addNLAE(model, properties);
        }
        // NLAEDEV
        if (properties.getProperty(PropertiesService.POLYSTORE_USENLAEDEV).equals("true")) {
            model = NLAEService.addNLAEDEV(model, application, properties, containerType);
        }

        // centralised logging
        if (properties.getProperty(PropertiesService.POLYSTORE_LOGGING).equals("true")) {
            if (clusterType == SupportedTechnologies.DockerCompose) {
                model = addFluentdToAllContainers(model);
                model = addComposeLogging(model, containerType, application);
            }
            if (clusterType == SupportedTechnologies.Kubernetes) {
                addKubernetesLogging(application, containerType);
            }
        }

        return model;
    }

    private static DeploymentModel addFluentdToAllContainers(DeploymentModel model) {
        List<Container> containers = EcoreUtil2.getAllContentsOfType(model, Container.class);
        for (Container container : containers) {
            container.getProperties().add(ContainerService.createComposeLogging(container.getName()));
        }
        return model;
    }

    private static void addKubernetesLogging(Application application, ContainerType containerType) {
        Container fluentd = ContainerService.create("fluentd", containerType, null);
        application.getContainers().add(fluentd);
    }

    private static DeploymentModel addComposeLogging(DeploymentModel model, ContainerType containerType,
            Application application) {
        // Fluentd
        Software fluentd = SoftwareService.create("fluentd", null);
        Container fluentdContainer = ContainerService.create("fluentd", containerType, fluentd);
        fluentdContainer.getProperties().add(ModelService.createKey_Values("build", "./fluentd", null));
        fluentdContainer.setVolumes(VolumesService.create(new String[] { "./fluentd:/fluentd/etc" }, null, null));
        fluentdContainer
                .setPorts(ContainerService.createPorts(new String[] { "target", "24224", "published", "24224" }));
        model.getElements().add(fluentd);
        model = ContainerService.addDependencyToAllContainers(model,
                ContainerService.createDependsOn(fluentdContainer));
        application.getContainers().add(fluentdContainer);
        // Elasticsearch
        Software elasticsearch = SoftwareService.create("elasticsearch",
                "docker.elastic.co/elasticsearch/elasticsearch:7.9.2");
        elasticsearch.setEnvironment(SoftwareService.createEnvironment(
                new String[] { "ES_JAVA_OPTS", "'-Xms256m -Xmx512m'", "discovery.type", "single-node" }));
        Container elasticsearchContainer = ContainerService.create("elasticsearch", containerType, elasticsearch);
        elasticsearchContainer
                .setPorts(ContainerService.createPorts(new String[] { "target", "9200", "published", "9200" }));
        fluentdContainer.getDepends_on().add(ContainerService.createDependsOn(elasticsearchContainer));
        model.getElements().add(elasticsearch);
        application.getContainers().add(elasticsearchContainer);
        // Kibana
        Software kibana = SoftwareService.create("kibana", "docker.elastic.co/kibana/kibana:7.9.2");
        Container kibanaContainer = ContainerService.create("kibana", containerType, kibana);
        kibanaContainer.setPorts(ContainerService.createPorts(new String[] { "target", "5601", "published", "5601" }));
        model.getElements().add(kibana);
        application.getContainers().add(kibanaContainer);
        return model;
    }

    public static DeploymentModel addToMetadata(String outputFolder, String MLName, String dlXMIName, IFile file,
            Properties properties, DeploymentModel model) {
        Path DLPath = Paths.get(file.getLocation().toOSString().replace(file.getName(), dlXMIName));
        Path MLPath = Paths.get(file.getLocation().toOSString().replace(file.getName(), MLName));
        String mongoInsertStatement = createMongoCommands(DLPath, MLPath);
        ModelService.getTechnology(ModelService.getClusterType(model)).insertModelsToMetadata(
                getPolystoreMongoContainer(model, properties), outputFolder, mongoInsertStatement, properties);
        return model;
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
    public static void writeInsertStatementToJavaScriptFile(String outputFolder, String mongoInsertStatement,
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
    public static void addInsertStatementToPolystoreMongoContainer(Container polystoreMongoContainer,
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

}
