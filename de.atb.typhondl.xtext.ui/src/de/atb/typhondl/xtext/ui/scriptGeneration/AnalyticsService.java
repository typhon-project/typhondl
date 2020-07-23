package de.atb.typhondl.xtext.ui.scriptGeneration;

import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.xtext.EcoreUtil2;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.modelUtils.SoftwareService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;

public class AnalyticsService {

    private static final String DOCKER_COMPOSE = "DockerCompose";
    private static final String KUBERNETES = "Kubernetes";

    public static DeploymentModel addAnalytics(DeploymentModel model, Properties properties,
            ClusterType clusterTypeObject, ContainerType containerType) {
        String kafkaURI = properties.getProperty(PropertiesService.ANALYTICS_KAFKA_URI);
        String kafkaPort = kafkaURI.substring(kafkaURI.indexOf(':') + 1);
        String kafkaHost = kafkaURI.substring(0, kafkaURI.indexOf(':'));
        de.atb.typhondl.xtext.typhonDL.URI kafkaURIObject = TyphonDLFactory.eINSTANCE.createURI();
        kafkaURIObject.setValue(kafkaURI);

        String clusterType = clusterTypeObject.getName();

        if (clusterType.equalsIgnoreCase(DOCKER_COMPOSE)) {
            String zookeeperPort = properties.getProperty(PropertiesService.ANALYTICS_ZOOKEEPER_PUBLISHEDPORT);
            String zookeeperTargetPort = properties.getProperty(PropertiesService.ANALYTICS_ZOOKEEPER_PORT);
            de.atb.typhondl.xtext.typhonDL.URI zookeeperURI = TyphonDLFactory.eINSTANCE.createURI();
            zookeeperURI.setValue(kafkaHost + ":" + zookeeperPort);

            if (properties.get(PropertiesService.ANALYTICS_DEPLOYMENT_CREATE).equals("true")) {
                // zookeeper
                Software zookeeper = SoftwareService.create("zookeeper",
                        properties.getProperty(PropertiesService.ANALYTICS_ZOOKEEPER_IMAGE));
                model.getElements().add(zookeeper);
                Container zookeeperContainer = ContainerService.create(
                        properties.getProperty(PropertiesService.ANALYTICS_ZOOKEEPER_CONTAINERNAME), containerType,
                        zookeeper, zookeeperURI.getValue());
                zookeeperContainer.setPorts(ContainerService
                        .createPorts(new String[] { "published", zookeeperPort, "target", zookeeperTargetPort }));
                // kafka
                Software kafka = SoftwareService.create("Kafka",
                        "wurstmeister/kafka:" + properties.getProperty(PropertiesService.ANALYTICS_KAFKA_SCALA_VERSION)
                                + "-" + properties.getProperty(PropertiesService.ANALYTICS_KAFKA_VERSION));
                kafka.setEnvironment(SoftwareService.createEnvironment(getKafkaComposeSettings(properties)));
                model.getElements().add(kafka);
                Container kafkaContainer = ContainerService.create(
                        properties.getProperty(PropertiesService.ANALYTICS_KAFKA_CONTAINERNAME), containerType, kafka,
                        kafkaURI);
                kafkaContainer.getDepends_on().add(ContainerService.createDependsOn(zookeeperContainer));
                kafkaContainer.getProperties().add(ContainerService.createKeyValuesArray("volumes",
                        new String[] { "/var/run/docker.sock:/var/run/docker.sock" }));
                if (Integer.parseInt(properties.getProperty(PropertiesService.ANALYTICS_KAFKA_REPLICAS)) > 1) {
                    kafkaContainer.setReplication(ContainerService.createStatelessReplication(
                            Integer.parseInt(properties.getProperty(PropertiesService.ANALYTICS_KAFKA_REPLICAS))));
                    kafkaContainer.setPorts(ContainerService.createPorts(new String[] { "published", kafkaPort,
                            "target", properties.getProperty(PropertiesService.ANALYTICS_KAFKA_PORT), "protocol", "tcp",
                            "mode", "host" }));
                } else {
                    kafkaContainer.setPorts(ContainerService.createPorts(new String[] { "published", kafkaPort,
                            "target", properties.getProperty(PropertiesService.ANALYTICS_KAFKA_PORT) }));
                }
                // flink
                Software flinkJobmanager = SoftwareService.create("FlinkJobmanager", "flink:latest");
                flinkJobmanager.setEnvironment(
                        SoftwareService.createEnvironment(new String[] { "JOB_MANAGER_RPC_ADDRESS", "jobmanager" }));
                model.getElements().add(flinkJobmanager);
                Software flinkTaskmanager = SoftwareService.create("FlinkTaskmanager", "flink:latest");
                flinkTaskmanager.setEnvironment(
                        SoftwareService.createEnvironment(new String[] { "JOB_MANAGER_RPC_ADDRESS", "jobmanager" }));
                model.getElements().add(flinkTaskmanager);
                Container flinkJobmanagerContainer = ContainerService.create("jobmanager", containerType,
                        flinkJobmanager, null);
                flinkJobmanagerContainer
                        .setPorts(ContainerService.createPorts(new String[] { "published", "8081", "target", "8081" }));
                flinkJobmanagerContainer.getProperties()
                        .addAll(ContainerService.createKeyValues(new String[] { "command", "jobmanager" }));
                flinkJobmanagerContainer.getProperties()
                        .add(ContainerService.createKeyValuesArray("expose", new String[] { "6123" }));
                Container flinkTaskmanagerContainer = ContainerService.create("taskmanager", containerType,
                        flinkTaskmanager, null);
                flinkTaskmanagerContainer.getDepends_on()
                        .add(ContainerService.createDependsOn(flinkJobmanagerContainer));
                flinkTaskmanagerContainer.getProperties()
                        .addAll(ContainerService.createKeyValues(new String[] { "command", "taskmanager" }));
                flinkTaskmanagerContainer.getProperties()
                        .add(ContainerService.createKeyValuesArray("expose", new String[] { "6121", "6122" }));
                // authAll
                Software authAll = SoftwareService.create("authAll",
                        properties.getProperty(PropertiesService.ANALYTICS_AUTHALL_IMAGE));
                model.getElements().add(authAll);
                Container authAllContainer = ContainerService.create("authAll", containerType, authAll, null);

                if (properties.get(PropertiesService.ANALYTICS_DEPLOYMENT_CONTAINED).equals("false")) {
                    // separate deployment scripts for analytics get generated. the analytics
                    // containers are in a different cluster
                    kafka.setExternal(true);
                    Cluster analyticsCluster = TyphonDLFactory.eINSTANCE.createCluster();
                    analyticsCluster.setType(clusterTypeObject);
                    analyticsCluster.setName("analyticsCluster");
                    getPlatform(model).getClusters().add(analyticsCluster);
                    Application analyticsApplication = TyphonDLFactory.eINSTANCE.createApplication();
                    analyticsApplication.setName("analytics");
                    analyticsCluster.getApplications().add(analyticsApplication);
                    analyticsApplication.getContainers().add(kafkaContainer);
                    analyticsApplication.getContainers().add(authAllContainer);
                    analyticsApplication.getContainers().add(zookeeperContainer);
                    analyticsApplication.getContainers().add(flinkTaskmanagerContainer);
                    analyticsApplication.getContainers().add(flinkJobmanagerContainer);
                } else {
                    // deployment scripts are included in polystore deployment scripts. the
                    // analytics containers are in the same cluster
                    Application application = EcoreUtil2.getAllContentsOfType(model, Application.class).get(0);
                    application.getContainers().add(kafkaContainer);
                    application.getContainers().add(authAllContainer);
                    application.getContainers().add(zookeeperContainer);
                    application.getContainers().add(flinkTaskmanagerContainer);
                    application.getContainers().add(flinkJobmanagerContainer);
                }

            } else {
                // no analytics deployment scripts get generated, the API still has to know
                // where to find the kafka containers. and the analyticsConfig.properties z
                Software kafka = TyphonDLFactory.eINSTANCE.createSoftware();
                kafka.setName("Kafka");
                kafka.setExternal(true);
                kafka.setUri(kafkaURIObject);
                model.getElements().add(kafka);
                Software zookeeper = TyphonDLFactory.eINSTANCE.createSoftware();
                zookeeper.setName("Zookeeper");
                zookeeper.setExternal(true);
                zookeeper.setUri(zookeeperURI);
                model.getElements().add(zookeeper);
            }
        } else if (clusterType.equalsIgnoreCase(KUBERNETES)) {
            Software kafka = TyphonDLFactory.eINSTANCE.createSoftware();
            kafka.setName("Kafka");
            model.getElements().add(kafka);
            if (properties.get(PropertiesService.ANALYTICS_DEPLOYMENT_CREATE).equals("true")) {
                Container kafkaContainer = ContainerService.create("kafka", containerType, kafka,
                        kafkaURIObject.getValue());
                if (properties.get(PropertiesService.ANALYTICS_DEPLOYMENT_CONTAINED).equals("false")) {
                    kafka.setExternal(true);
                    Cluster analyticsCluster = TyphonDLFactory.eINSTANCE.createCluster();
                    analyticsCluster.setType(clusterTypeObject);
                    analyticsCluster.setName("analyticsCluster");
                    getPlatform(model).getClusters().add(analyticsCluster);
                    Application analyticsApplication = TyphonDLFactory.eINSTANCE.createApplication();
                    analyticsApplication.setName("analytics");
                    analyticsCluster.getApplications().add(analyticsApplication);
                    analyticsApplication.getContainers().add(kafkaContainer);
                } else {
                    EcoreUtil2.getAllContentsOfType(model, Application.class).get(0).getContainers()
                            .add(kafkaContainer);
                }
            } else {
                kafka.setExternal(true);
                kafka.setUri(kafkaURIObject);
            }
        }
        return model;
    }

    private static Platform getPlatform(DeploymentModel model) {
        return EcoreUtil2.getAllContentsOfType(model, Platform.class).get(0);
    }

    private static String[] getKafkaComposeSettings(Properties properties) {
        String zookeeperTargetPort = properties.getProperty(PropertiesService.ANALYTICS_ZOOKEEPER_PORT);
        String kafkaInsidePort = properties.getProperty(PropertiesService.ANALYTICS_KAFKA_INSIDEPORT);
        String kafkaURI = properties.getProperty(PropertiesService.ANALYTICS_KAFKA_URI);
        String kafkaHost = kafkaURI.substring(0, kafkaURI.indexOf(':'));
        String kafkaAdvertisedHost = kafkaHost;
        String[] kafkaListeners = properties.getProperty(PropertiesService.ANALYTICS_KAFKA_LISTENERS)
                .split("\\s*,\\s*");
        String kafkaListenerNameIn = properties.getProperty(PropertiesService.ANALYTICS_KAFKA_LISTENERNAME_IN);
        String kafkaListenerNameOut = properties.getProperty(PropertiesService.ANALYTICS_KAFKA_LISTENERNAME_OUT);
        String kafkaListenersString = "";
        String kafkaAdvertisedListenerString = "";
        for (int i = 0; i < kafkaListeners.length; i++) {
            kafkaListenersString += kafkaListenerNameOut + "://:" + kafkaListeners[i] + ", ";
            kafkaAdvertisedListenerString += kafkaListenerNameOut + "://" + kafkaAdvertisedHost + ":"
                    + kafkaListeners[i] + ", ";
        }
        kafkaListenersString += kafkaListenerNameIn + "://:" + kafkaInsidePort;
        kafkaAdvertisedListenerString += kafkaListenerNameIn + "://:" + kafkaInsidePort;

        ArrayList<String> list = new ArrayList<>();
        list.add("KAFKA_ZOOKEEPER_CONNECT");
        list.add("zookeeper:" + zookeeperTargetPort);
        list.add("KAFKA_ADVERTISED_HOST_NAME");
        list.add(kafkaAdvertisedHost);
        list.add("KAFKA_LISTENERS");
        list.add(kafkaListenersString);
        list.add("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP");
        list.add(kafkaListenerNameIn + ":PLAINTEXT, " + kafkaListenerNameOut + ":PLAINTEXT");
        list.add("KAFKA_INTER_BROKER_LISTENER_NAME");
        list.add(kafkaListenerNameIn);
        list.add("KAFKA_ADVERTISED_LISTENERS");
        list.add(kafkaAdvertisedListenerString);
        list.add("KAFKA_AUTO_CREATE_TOPICS_ENABLE");
        list.add("\"true\"");

        return list.toArray(new String[0]);
    }

}
