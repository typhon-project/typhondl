package de.atb.typhondl.xtext.ui.utilities;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.ui.creationWizard.CreateModelWizard;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.scriptGeneration.DeploymentModelService;

/**
 * Utility class for providing supported Technologies. Included at the moment:
 * <li>Docker Compose</li>
 * <li>Docker Swarm</li>
 * <li>Kubernetes with Docker containers</li>
 * 
 * @author flug TODO TYP-186 test ports
 */
public enum SupportedTechnologies {
    DockerCompose("Docker Compose", "Docker", true, "volume", false, false, false, 1, 99999, true, true,
            "localhost:29092") {

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
    },
    DockerSwarm("Docker Swarm", "Docker", true, "volume", false, false, true, 1, 99999, false, false, "kafka:29092") {
        // TODO TYP-186 volumes in swam
        // TODO TYP-186 find restart in model, add to "deploy"
        // TODO TYP-186 It seems that tests with DockerCompose using "kafka:29092"
        // instead of "localhost:29092" are failing
        @Override
        public void setConnectionDefaults(Properties properties) {
            properties.setProperty(PropertiesService.API_PUBLISHEDPORT, "8080");
            properties.setProperty(PropertiesService.UI_PUBLISHEDPORT, "4200");
        }

        @Override
        public void insertModelsToMetadata(Container polystoreMongoContainer, String outputFolder,
                String mongoInsertStatement, Properties properties) {
            // TODO TYP-186 upload models to metadata in swarm
        }

        @Override
        public List<InputField> kafkaInputFields() {
            return Arrays.asList(new InputField("Kafka version: ", "analytics.kafka.version"),
                    new InputField("Replicas: ", "analytics.kafka.replicas"));
        }
    },
    Kubernetes("Kubernetes with Docker", "Docker", false, "persistentVolumeClaim", true, true, true, 30000, 32767,
            false, false, "typhon-cluster-kafka-bootstrap:9092") {
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
    };

    /**
     * The name that gets displayed in the {@link CreateModelWizard}
     */
    private final String displayedName;

    /**
     * Name of the Container Type for {@link ContainerType}
     */
    private final String containerType;

    private boolean createAllAnalyticsContainers;

    private String defaultVolumesType;

    private boolean canUseHelm;

    private boolean canUseKubeConfig;

    private boolean canDoStatelessReplication;

    private int minPort;

    private int maxPort;

    private boolean setInitDB;

    private boolean waitForMetadata;

    private String kafkaInternalURI;

    public String displayedName() {
        return displayedName;
    }

    public String containerType() {
        return containerType;
    }

    public boolean createAllAnalyticsContainers() {
        return createAllAnalyticsContainers;
    }

    public String defaultVolumesType() {
        return defaultVolumesType;
    }

    public boolean canUseHelm() {
        return canUseHelm;
    }

    public boolean canUseKubeConfig() {
        return canUseKubeConfig;
    }

    public boolean canDoStatelessReplication() {
        return canDoStatelessReplication;
    }

    public int minPort() {
        return minPort;
    }

    public int maxPort() {
        return maxPort;
    }

    public boolean setInitDB() {
        return setInitDB;
    }

    public boolean waitForMetadata() {
        return waitForMetadata;
    }

    public String kafkaInternalURI() {
        return kafkaInternalURI;
    }

    /**
     * Creates enum constant with
     * 
     * @param displayedName                The name to show in Wizard's combo
     * @param containerType                The type of container associated with
     *                                     this technology (e.g. Docker)
     * @param createAllAnalyticsContainers true if all five Analytics containers
     *                                     (kafka, zookeeper, authAll, flink
     *                                     jobmanager, flink taskmanager) should be
     *                                     created, false if just a kafka container
     *                                     for the API to reference should be
     *                                     created
     * @param defaultVolumesType           Default Volumes Type for the technology
     *                                     (e.g. "volume" or
     *                                     "persistentVolumeClaim")
     * @param canUseHelm                   true if Helm Charts can be used
     * @param canUseKubeConfig             true if a kubeconfig file can be used
     * @param canDoStatelessReplication    true if stateless replication is provided
     * @param minPort                      minimal number in published port range
     * @param maxPort                      maximal number in published port range
     * @param setInitDB                    true if the ML and DL model can be
     *                                     inserted into the metadata container via
     *                                     a script in docker-entrypoint-initdb.d
     *                                     when the container is first created
     * @param waitForMetadata              true if the API container has to wait for
     *                                     the metadata container to be ready and
     *                                     have the models uploaded
     * @param kafkaInternalURI             internal kafka URI
     */
    private SupportedTechnologies(String displayedName, String containerType, boolean createAllAnalyticsContainers,
            String defaultVolumesType, boolean canUseHelm, boolean canUseKubeConfig, boolean canDoStatelessReplication,
            int minPort, int maxPort, boolean setInitDB, boolean waitForMetadata, String kafkaInternalURI) {
        this.displayedName = displayedName;
        this.containerType = containerType;
        this.createAllAnalyticsContainers = createAllAnalyticsContainers;
        this.defaultVolumesType = defaultVolumesType;
        this.canUseHelm = canUseHelm;
        this.canUseKubeConfig = canUseKubeConfig;
        this.canDoStatelessReplication = canDoStatelessReplication;
        this.minPort = minPort;
        this.maxPort = maxPort;
        this.setInitDB = setInitDB;
        this.waitForMetadata = waitForMetadata;
        this.kafkaInternalURI = kafkaInternalURI;
    }

    public abstract void setConnectionDefaults(Properties properties);

    public abstract void insertModelsToMetadata(Container polystoreMongoContainer, String outputFolder,
            String mongoInsertStatement, Properties properties);

    public abstract List<InputField> kafkaInputFields();

}
