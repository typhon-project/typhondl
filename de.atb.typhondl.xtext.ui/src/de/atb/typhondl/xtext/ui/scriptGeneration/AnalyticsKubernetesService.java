package de.atb.typhondl.xtext.ui.scriptGeneration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.FileService;

public class AnalyticsKubernetesService {

    private static final String ANALYTICS_KUBERNETES_ZIP_FILENAME = "analyticsKubernetes.zip";
    private static final String ANALYTICS_ZIP_ADDRESS = "http://typhon.clmsuk.com/static/"
            + ANALYTICS_KUBERNETES_ZIP_FILENAME;
    private static final String DEPENDENCY_JAR_ADDRESS = "http://archiva.clmsuk.com:8090/repository/internal/typhon/ac.york.typhon.analytics/0.0.1-SNAPSHOT/";
    private static final String DEPENDENCY_JAR_INFO = "maven-metadata.xml";
    private static final String DEPENDENCY_JAR_NAME = "jar-with-dependencies";

    public static void addAnalyticsFiles(DeploymentModel model, String outputFolder, Properties properties) {
        try {
            File dir = new File(outputFolder);
            if (!Files.exists(Paths.get(outputFolder))) {
                dir.mkdir();
            }
            String analyticsZipPath = outputFolder + File.separator + ANALYTICS_KUBERNETES_ZIP_FILENAME;
            IWorkbench wb = PlatformUI.getWorkbench();
            IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
            MessageDialog.openInformation(win.getShell(), "Scripts", "Analytics files are now getting downloaded");
            InputStream input = FileService.downloadFiles(analyticsZipPath, ANALYTICS_ZIP_ADDRESS, "Analytics");
            if (input != null) {
                FileService.unzip(analyticsZipPath, outputFolder);
            }
            applyPropertiesToAnalyticsFiles(analyticsZipPath.substring(0, analyticsZipPath.lastIndexOf('.')),
                    properties, dir);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

    }

    private static void applyPropertiesToAnalyticsFiles(String analyticsZipPath, Properties properties, File dir)
            throws IOException, ParserConfigurationException, SAXException {

        FileService.applyMapToFile(properties, Paths.get(
                analyticsZipPath + File.separator + "flink" + File.separator + "flink-configuration-configmap.yaml"),
                flinkConfiguration());

        if (!properties.get(PropertiesService.ANALYTICS_FLINK_REST_PORT).equals("automatic")) {
            Path flinkRestServicePath = Paths
                    .get(analyticsZipPath + File.separator + "flink" + File.separator + "jobmanager-rest-service.yaml");
            FileService.save(flinkRestServicePath, restService(properties, flinkRestServicePath));
        }

        if (!properties.get(PropertiesService.ANALYTICS_FLINK_TASKMANAGER_REPLICAS).equals("2")) {
            Path flinkTaskmanagerPath = Paths
                    .get(analyticsZipPath + File.separator + "flink" + File.separator + "taskmanager-deployment.yaml");
            FileService.save(flinkTaskmanagerPath, taskmanager(properties, flinkTaskmanagerPath));
        }

        Path kafkaClusterPath = Paths
                .get(analyticsZipPath + File.separator + "kafka" + File.separator + "typhon-cluster.yml");
        FileService.save(kafkaClusterPath, cluster(properties, kafkaClusterPath));

        Path flinkJobmanagerPath = Paths
                .get(analyticsZipPath + File.separator + "flink" + File.separator + "jobmanager-deployment.yaml");
        FileService.save(flinkJobmanagerPath, jobmanager(flinkJobmanagerPath, dir));
    }

    private static List<String> jobmanager(Path flinkJobmanagerPath, File outputFolder)
            throws IOException, ParserConfigurationException, SAXException {
        List<String> jobmanagerLines = Files.readAllLines(flinkJobmanagerPath);
        final String path = outputFolder.getAbsolutePath() + File.separator + "temp.xml";
        InputStream input = FileService.downloadFiles(path, DEPENDENCY_JAR_ADDRESS + DEPENDENCY_JAR_INFO, "Analytics");
        if (input != null) {
            jobmanagerLines.addAll(getIndex(jobmanagerLines, "volumes:"), initContainer(getLatestJarName(path)));
            jobmanagerLines.addAll(getIndex(jobmanagerLines, "volumes:") + 1, initVolume());
        }
        return jobmanagerLines;
    }

    private static ArrayList<String> initVolume() {
        ArrayList<String> initVolume = new ArrayList<>();
        initVolume.add("      - name: workdir");
        initVolume.add("        emptyDir: {}");
        return initVolume;
    }

    private static ArrayList<String> initContainer(String fileName) {
        ArrayList<String> initContainer = new ArrayList<>();
        initContainer.add("      initContainers:");
        initContainer.add("      - name: load-jar");
        initContainer.add("        image: busybox");
        initContainer.add("        command:");
        initContainer.add("        - wget");
        initContainer.add("        - \"-O\"");
        initContainer.add("        - \"/opt/flink/lib/ac.york.typhon.analytics-jar-with-dependencies.jar\"");
        initContainer.add("        - " + DEPENDENCY_JAR_ADDRESS + "ac.york.typhon.analytics-" + fileName
                + "-jar-with-dependencies.jar");
        initContainer.add("        volumeMounts:");
        initContainer.add("        - name: workdir");
        initContainer.add("          mountPath: \"/work-dir\"");
        return initContainer;
    }

    private static String getLatestJarName(String path) throws ParserConfigurationException, IOException, SAXException {
        NodeList nList = getVersionNodes(path);
        Node toSearch = null;
        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            NodeList childNodes = node.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                Node item = childNodes.item(j);
                if (item.getTextContent().equals(DEPENDENCY_JAR_NAME)) {
                    toSearch = node;
                }
            }
        }
        if (toSearch != null) {
            NodeList childNodes = toSearch.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                if (item.getNodeName().equalsIgnoreCase("value")) {
                    return item.getTextContent();
                }
            }
        }
        return null;
    }

    private static NodeList getVersionNodes(String path)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        ByteArrayInputStream input = new ByteArrayInputStream(encoded);
        new File(path).delete();
        Document doc = builder.parse(input);
        return doc.getElementsByTagName("snapshotVersion");
    }

    private static List<String> cluster(Properties properties, Path kafkaClusterPath) throws IOException {
        HashMap<String, String> kafkaClusterPropertyMap = new HashMap<>();
        kafkaClusterPropertyMap.put("replicas:", PropertiesService.ANALYTICS_KAFKA_CLUSTER_REPLICAS);
        kafkaClusterPropertyMap.put("version:", PropertiesService.ANALYTICS_KAFKA_VERSION);
        kafkaClusterPropertyMap.put("size:", PropertiesService.ANALYTICS_KAFKA_STORAGECLAIM);

        List<String> kafkaCluster = Files.readAllLines(kafkaClusterPath);
        int zookeeperIndex = getIndex(kafkaCluster, "zookeeper");
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
        return kafkaCluster;
    }

    private static List<String> taskmanager(Properties properties, Path flinkTaskmanagerPath) throws IOException {
        List<String> flinkTaskmanager = Files.readAllLines(flinkTaskmanagerPath);
        for (int i = 0; i < flinkTaskmanager.size(); i++) {
            if (flinkTaskmanager.get(i).contains("replicas")) {
                flinkTaskmanager.set(i, FileService.replaceOldValueWithNewValue(flinkTaskmanager.get(i), "replicas:",
                        properties.getProperty(PropertiesService.ANALYTICS_FLINK_TASKMANAGER_REPLICAS)));
            }
        }
        return flinkTaskmanager;
    }

    private static List<String> restService(Properties properties, Path flinkRestServicePath) throws IOException {
        List<String> flinkRestService = Files.readAllLines(flinkRestServicePath);
        List<String> newListWithAddedNodePort = new ArrayList<>();
        for (int i = 0; i < flinkRestService.size(); i++) {
            newListWithAddedNodePort.add(flinkRestService.get(i));
            if (flinkRestService.get(i).contains("targetPort")) {
                newListWithAddedNodePort
                        .add("    nodePort: " + properties.get(PropertiesService.ANALYTICS_FLINK_REST_PORT));
            }
        }
        return newListWithAddedNodePort;
    }

    private static HashMap<String, String> flinkConfiguration() {
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
        return flinkPropertyMap;
    }

    private static int getIndex(List<String> list, String search) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains(search)) {
                return i;
            }
        }
        return 1;
    }

}
