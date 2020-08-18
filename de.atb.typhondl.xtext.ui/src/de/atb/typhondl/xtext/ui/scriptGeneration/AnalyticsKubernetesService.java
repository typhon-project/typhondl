package de.atb.typhondl.xtext.ui.scriptGeneration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.FileService;

public class AnalyticsKubernetesService {

    private static final String ANALYTICS_KUBERNETES_ZIP_FILENAME = "analyticsKubernetes.zip";
    private static final String ANALYTICS_ZIP_ADDRESS = "http://typhon.clmsuk.com/static/"
            + ANALYTICS_KUBERNETES_ZIP_FILENAME;

    public static void addAnalyticsFiles(DeploymentModel model, String outputFolder, Properties properties) {
        try {
            String analyticsZipPath = outputFolder + File.separator + ANALYTICS_KUBERNETES_ZIP_FILENAME;
            InputStream input = FileService.downloadFiles(analyticsZipPath, ANALYTICS_ZIP_ADDRESS, "Analytics");
            if (input != null) {
                FileService.unzip(analyticsZipPath, outputFolder);
            }
            applyPropertiesToAnalyticsFiles(analyticsZipPath.substring(0, analyticsZipPath.lastIndexOf('.')),
                    properties);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void applyPropertiesToAnalyticsFiles(String analyticsZipPath, Properties properties)
            throws IOException {
        Path flinkConfigMapPath = Paths.get(
                analyticsZipPath + File.separator + "flink" + File.separator + "flink-configuration-configmap.yaml");
        HashMap<String, String> flinkPropertyMap = new HashMap<>();
        flinkPropertyMap.put("jobmanager.heap.size:", PropertiesService.ANALYTICS_FLINK_JOBMANAGER_HEAP_SIZE);
        flinkPropertyMap.put("taskmanager.memory.process.size:",
                PropertiesService.ANALYTICS_FLINK_TASKMANAGER_MEMORY_PROCESS_SIZE);
        flinkPropertyMap.put("log4j.rootLogger=", PropertiesService.ANALYTICS_LOGGING_ROOTLOGGER);
        flinkPropertyMap.put("log4j.logger.akka=", PropertiesService.ANALYTICS_LOGGING_AKKA);
        flinkPropertyMap.put("log4j.logger.org.apache.kafka=", PropertiesService.ANALYTICS_LOGGING_KAFKA);
        flinkPropertyMap.put("log4j.logger.org.apache.hadoop=", PropertiesService.ANALYTICS_LOGGING_HADOOP);
        flinkPropertyMap.put("log4j.logger.org.apache.zookeeper=", PropertiesService.ANALYTICS_LOGGING_ZOOKEEPER);
        flinkPropertyMap.put(
                "log4j.logger.org.apache.flink.shaded.akka.org.jboss.netty.channel.DefaultChannelPipeline=",
                PropertiesService.ANALYTICS_LOGGING_FLINK);
        FileService.applyMapToFile(properties, flinkConfigMapPath, flinkPropertyMap);

        if (!properties.get(PropertiesService.ANALYTICS_FLINK_REST_PORT).equals("automatic")) {
            Path flinkRestServicePath = Paths
                    .get(analyticsZipPath + File.separator + "flink" + File.separator + "jobmanager-rest-service.yaml");
            List<String> flinkRestService = Files.readAllLines(flinkRestServicePath);
            List<String> newListWithAddedNodePort = new ArrayList<>();
            for (int i = 0; i < flinkRestService.size(); i++) {
                newListWithAddedNodePort.add(flinkRestService.get(i));
                if (flinkRestService.get(i).contains("targetPort")) {
                    newListWithAddedNodePort
                            .add("    nodePort: " + properties.get(PropertiesService.ANALYTICS_FLINK_REST_PORT));
                }
            }
            Files.write(flinkRestServicePath, newListWithAddedNodePort, StandardOpenOption.TRUNCATE_EXISTING);
        }

        if (!properties.get(PropertiesService.ANALYTICS_FLINK_TASKMANAGER_REPLICAS).equals("2")) {
            Path flinkTaskmanagerPath = Paths
                    .get(analyticsZipPath + File.separator + "flink" + File.separator + "taskmanager-deployment.yaml");
            List<String> flinkTaskmanager = Files.readAllLines(flinkTaskmanagerPath);
            for (int i = 0; i < flinkTaskmanager.size(); i++) {
                if (flinkTaskmanager.get(i).contains("replicas")) {
                    flinkTaskmanager.set(i,
                            FileService.replaceOldValueWithNewValue(flinkTaskmanager.get(i), "replicas:",
                                    properties.getProperty(PropertiesService.ANALYTICS_FLINK_TASKMANAGER_REPLICAS)));
                }
            }
            Files.write(flinkTaskmanagerPath, flinkTaskmanager, StandardOpenOption.TRUNCATE_EXISTING);
        }

        HashMap<String, String> kafkaClusterPropertyMap = new HashMap<>();
        kafkaClusterPropertyMap.put("replicas:", PropertiesService.ANALYTICS_KAFKA_CLUSTER_REPLICAS);
        kafkaClusterPropertyMap.put("version:", PropertiesService.ANALYTICS_KAFKA_VERSION);
        kafkaClusterPropertyMap.put("size:", PropertiesService.ANALYTICS_KAFKA_STORAGECLAIM);
        Path kafkaClusterPath = Paths
                .get(analyticsZipPath + File.separator + "kafka" + File.separator + "typhon-cluster.yml");
        List<String> kafkaCluster = Files.readAllLines(kafkaClusterPath);
        int zookeeperIndex = getZookeeperIndex(kafkaCluster);
        for (int i = 0; i < zookeeperIndex; i++) {
            for (String propertyNameInFile : kafkaClusterPropertyMap.keySet()) {
                String string = kafkaCluster.get(i);
                if (string.contains(propertyNameInFile)) {
                    kafkaCluster.set(i, FileService.replaceOldValueWithNewValue(string, propertyNameInFile,
                            properties.getProperty(kafkaClusterPropertyMap.get(propertyNameInFile))));
                }
            }
        }
        for (int i = zookeeperIndex; i < kafkaCluster.size(); i++) {
            if (kafkaCluster.get(i).contains("size")) {
                kafkaCluster.set(i, FileService.replaceOldValueWithNewValue(kafkaCluster.get(i), "size:",
                        properties.getProperty(PropertiesService.ANALYTICS_ZOOKEEPER_STORAGECLAIM)));
            }
        }
        Files.write(kafkaClusterPath, kafkaCluster, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static int getZookeeperIndex(List<String> kafkaCluster) {
        for (int i = 0; i < kafkaCluster.size(); i++) {
            if (kafkaCluster.get(i).contains("zookeeper")) {
                return i;
            }
        }
        return 1;
    }

}
