package de.atb.typhondl.xtext.ui.technologies;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.eclipse.xtext.EcoreUtil2;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Key_KeyValueList;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.modelUtils.ModelService;
import de.atb.typhondl.xtext.ui.modelUtils.SoftwareService;
import de.atb.typhondl.xtext.ui.modelUtils.VolumesService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.scriptGeneration.DeploymentModelService;
import de.atb.typhondl.xtext.ui.utilities.InputField;

/**
 * Implements DockerCompose
 * 
 * @author flug
 *
 */
public class DockerCompose implements ITechnology {

    private SupportedTechnologies type;

    public DockerCompose(SupportedTechnologies type) {
        this.type = type;
    }

    @Override
    public void setConnectionDefaults(Properties properties) {
        properties.setProperty(PropertiesService.API_PUBLISHEDPORT, "8080");
        properties.setProperty(PropertiesService.UI_PUBLISHEDPORT, "4200");
    }

    @Override
    public void insertModelsToMetadata(Container polystoreMongoContainer, String outputFolder,
            String mongoInsertStatement, Properties properties) {
        DeploymentModelService.writeInsertStatementToJavaScriptFile(outputFolder, mongoInsertStatement, properties);
    }

    @Override
    public List<InputField> kafkaInputFields() {
        return Arrays.asList(new InputField("Kafka version: ", "analytics.kafka.version"));
    }

    @Override
    public String displayedName() {
        return "Docker Compose";
    }

    @Override
    public String containerType() {
        return "Docker";
    }

    @Override
    public boolean createAllAnalyticsContainers() {
        return true;
    }

    @Override
    public String defaultVolumesType() {
        return "volume";
    }

    @Override
    public boolean canUseHelm() {
        return false;
    }

    @Override
    public boolean canUseKubeConfig() {
        return false;
    }

    @Override
    public boolean canDoStatelessReplication() {
        return false;
    }

    @Override
    public int minPort() {
        return 1;
    }

    @Override
    public int maxPort() {
        return 65535;
    }

    @Override
    public boolean setInitDB() {
        return true;
    }

    @Override
    public boolean waitForMetadata() {
        return true;
    }

    @Override
    public String kafkaInternalURI() {
        return "localhost:29092";
    }

    @Override
    public boolean restartIsDefault() {
        return false;
    }

    @Override
    public SupportedTechnologies getType() {
        return this.type;
    }

    @Override
    public boolean canDeployEvolution() {
        return true;
    }

    @Override
    public void addLogging(DeploymentModel model, ContainerType containerType, Application application) {
        List<Container> containers = EcoreUtil2.getAllContentsOfType(model, Container.class);
        for (Container container : containers) {
            container.getProperties().add(createComposeLogging(container.getName()));
        }
        // Fluentd
        Software fluentd = SoftwareService.create("fluentd", "quay.io/fluentd_elasticsearch/fluentd:latest");
        Container fluentdContainer = ContainerService.create("fluentd", containerType, fluentd);
        fluentdContainer.setVolumes(
                VolumesService.create(new String[] { "./fluentd/fluent.conf:/etc/fluent/fluent.conf" }, null, null));
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
    }

    /**
     * Creates logging entry for DockerCompose containers
     * 
     * @param name the name of the container (used for the tag)
     * @return a new {@link Key_KeyValueList} containing logging specifics
     */
    private static Key_KeyValueList createComposeLogging(String name) {
        Key_KeyValueList logging = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
        logging.setName("logging");
        logging.getProperties().add(ModelService.createKey_Values("driver", "\"fluentd\"", null));
        Key_KeyValueList options = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
        options.setName("options");
        options.getProperties().add(ModelService.createKey_Values("tag", name, null));
        logging.getProperties().add(options);
        return logging;
    }

}
