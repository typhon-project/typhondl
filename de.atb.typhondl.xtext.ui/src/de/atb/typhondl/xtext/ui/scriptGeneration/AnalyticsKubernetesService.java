package de.atb.typhondl.xtext.ui.scriptGeneration;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;

public class AnalyticsKubernetesService {

    private static final String ANALYTICS_KUBERNETES_ZIP_FILENAME = "analyticsKubernetes.zip";
    private static final String ANALYTICS_ZIP_ADDRESS = "http://typhon.clmsuk.com/static/"
            + ANALYTICS_KUBERNETES_ZIP_FILENAME;
    private static final int BUFFER_SIZE = 4096;

    public static void addAnalyticsFiles(DeploymentModel model, String outputFolder, Properties properties) {
        try {
            File dir = new File(outputFolder);
            if (!Files.exists(Paths.get(outputFolder))) {
                dir.mkdir();
            }
            String analyticsZipPath = outputFolder + File.separator + ANALYTICS_KUBERNETES_ZIP_FILENAME;
            InputStream input = downloadKafkaFiles(analyticsZipPath);
            if (input != null) {
                unzip(analyticsZipPath, outputFolder);
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
        List<String> flinkConfigmap = Files.readAllLines(flinkConfigMapPath);
        for (int i = 0; i < flinkConfigmap.size(); i++) {
            for (String propertyNameInFile : flinkPropertyMap.keySet()) {
                String string = flinkConfigmap.get(i);
                if (string.contains(propertyNameInFile)) {
                    flinkConfigmap.set(i, replaceOldValueWithNewValue(string, propertyNameInFile,
                            properties.getProperty(flinkPropertyMap.get(propertyNameInFile))));
                }
            }
        }
        Files.write(flinkConfigMapPath, flinkConfigmap, StandardOpenOption.TRUNCATE_EXISTING);

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
                    flinkTaskmanager.set(i, replaceOldValueWithNewValue(flinkTaskmanager.get(i), "replicas:",
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
                    kafkaCluster.set(i, replaceOldValueWithNewValue(string, propertyNameInFile,
                            properties.getProperty(kafkaClusterPropertyMap.get(propertyNameInFile))));
                }
            }
        }
        for (int i = zookeeperIndex; i < kafkaCluster.size(); i++) {
            if (kafkaCluster.get(i).contains("size")) {
                kafkaCluster.set(i, replaceOldValueWithNewValue(kafkaCluster.get(i), "size:",
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

    private static String replaceOldValueWithNewValue(String string, String propertyNameInFile, String property) {
        String separator = propertyNameInFile.substring(propertyNameInFile.length() - 1, propertyNameInFile.length());
        String target = string.substring(string.indexOf(separator) + 1);
        if (separator.equals(":")) {
            return string.replace(target, " " + property);
        } else if (separator.equals("=")) {
            return string.replace(target, property);
        }
        return "ERROR";
    }

    private static InputStream downloadKafkaFiles(String analyticsZipPath) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        IWorkbench wb = PlatformUI.getWorkbench();
        IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
        MessageDialog.openInformation(win.getShell(), "Scripts", "Analytics files are now getting downloaded");
        try {
            URL url = new URL(ANALYTICS_ZIP_ADDRESS);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                MessageDialog.openError(win.getShell(), "Scripts", "Analytics files could not be downloaded at "
                        + ANALYTICS_ZIP_ADDRESS + ", please check your internet connection and try again");
                System.out.println(
                        "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(analyticsZipPath);

            byte data[] = new byte[4096];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
                System.out.println("EXCEPTION!");
            }
            if (connection != null)
                connection.disconnect();
        }
        return input;
    }

    private static void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        // delete zip
        new File(zipFilePath).delete();
    }

    /**
     * Extracts a zip entry (file entry)
     * 
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

}
