package de.atb.typhondl.xtext.ui.technologies;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.modelUtils.ModelService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.scriptGeneration.DeploymentModelService;
import de.atb.typhondl.xtext.ui.utilities.InputField;

/**
 * Implements Kubernetes using Docker
 * 
 * @author flug
 *
 */
public class KubernetesDocker implements ITechnology {

    private SupportedTechnologies type;

    public KubernetesDocker(SupportedTechnologies type) {
        this.type = type;
    }

    @Override
    public void setConnectionDefaults(Properties properties) {
        properties.setProperty(PropertiesService.API_PUBLISHEDPORT, "30061");
        properties.setProperty(PropertiesService.UI_PUBLISHEDPORT, "30075");
    }

    @Override
    public void insertModelsToMetadata(Container polystoreMongoContainer, String outputFolder,
            String mongoInsertStatement, Properties properties) {
        DeploymentModelService.addInsertStatementToPolystoreMongoContainer(polystoreMongoContainer,
                mongoInsertStatement, properties);
    }

    @Override
    public List<InputField> kafkaInputFields() {
        return Arrays.asList(new InputField("Flink jobmanager heap size: ", "analytics.flink.jobmanager.heap.size"),
                new InputField("Flink taskmanager memory: ", "analytics.flink.taskmanager.memory.process.size"),
                new InputField("Logglevel rootlogger: ", "analytics.logging.rootlogger"),
                new InputField("Logglevel akka: ", "analytics.logging.akka"),
                new InputField("Logglevel kafka: ", "analytics.logging.kafka"),
                new InputField("Logglevel hadoop: ", "analytics.logging.hadoop"),
                new InputField("Logglevel zookeeper: ", "analytics.logging.zookeeper"),
                new InputField("Logglevel flink: ", "analytics.logging.flink"),
                new InputField("Flink jobmanager rest nodeport: ", "analytics.flink.rest.port"),
                new InputField("Flink taskmanager replicas: ", "analytics.flink.taskmanager.replicas"),
                new InputField("Kafka replicas: ", "analytics.kafka.cluster.replicas"),
                new InputField("Kafka version: ", "analytics.kafka.version"),
                new InputField("Kafka storage claim: ", "analytics.kafka.storageclaim"),
                new InputField("zookeeper storage claim: ", "analytics.zookeeper.storageclaim"));
    }

    @Override
    public String displayedName() {
        return "Kubernetes with Docker";
    }

    @Override
    public String containerType() {
        return "Docker";
    }

    @Override
    public boolean createAllAnalyticsContainers() {
        return false;
    }

    @Override
    public String defaultVolumesType() {
        return "persistentVolumeClaim";
    }

    @Override
    public boolean canUseHelm() {
        return true;
    }

    @Override
    public boolean canUseKubeConfig() {
        return true;
    }

    @Override
    public boolean canDoStatelessReplication() {
        return true;
    }

    @Override
    public int minPort() {
        return 30000;
    }

    @Override
    public int maxPort() {
        return 32767;
    }

    @Override
    public boolean setInitDB() {
        return false;
    }

    @Override
    public boolean waitForMetadata() {
        return false;
    }

    @Override
    public String kafkaInternalURI() {
        return "typhon-cluster-kafka-bootstrap:9092";
    }

    @Override
    public boolean restartIsDefault() {
        return true;
    }

    public SupportedTechnologies getType() {
        return type;
    }

    @Override
    public boolean canDeployEvolution() {
        return false;
    }

    @Override
    public void addLogging(DeploymentModel model, ContainerType containerType, Application application,
            Properties properties) {
        Container fluentd = ContainerService.create("fluentd", containerType, null,
                properties.getProperty(PropertiesService.LOGGING_ELASTICSEARCH_HOST));
        if (Boolean.parseBoolean(properties.getProperty(PropertiesService.LOGGING_ELASTICSEARCH_EXTERNAL))) {
            Cluster elasticsearchCluster = TyphonDLFactory.eINSTANCE.createCluster();
            elasticsearchCluster.setName("elasticsearch");
            elasticsearchCluster.setType(((Cluster) application.eContainer()).getType());
            elasticsearchCluster.getProperties().add(ModelService
                    .createKubeconfig(properties.getProperty(PropertiesService.LOGGING_ELASTICSEARCH_KUBECONFIG)));
            ((Platform) ((Cluster) application.eContainer()).eContainer()).getClusters().add(elasticsearchCluster);
        }
        application.getContainers().add(fluentd);
    }

    @Override
    public String elasticsearchAddress() {
        return "elasticsearch-master";
    }

}