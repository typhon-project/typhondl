package de.atb.typhondl.xtext.ui.scriptGeneration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.typhonDL.URI;
import de.atb.typhondl.xtext.ui.modelUtils.SoftwareService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.FileService;

public class NLAEService {

    private static final String NLAE_ZIP_FILENAME = "nlae.zip";
    private static final String NLAE_ZIP_ADDRESS = "http://typhon.clmsuk.com/static/" + NLAE_ZIP_FILENAME;

    public static DeploymentModel addNLAE(DeploymentModel model, Properties properties) {
        Software nlae = SoftwareService.create(PropertiesService.NLAE_NAME, null);
        nlae.setExternal(true);
        URI uri = TyphonDLFactory.eINSTANCE.createURI();
        uri.setValue(properties.getProperty(PropertiesService.NLAE_API_HOST) + ":"
                + properties.getProperty(PropertiesService.NLAE_API_PORT));
        nlae.setUri(uri);
        model.getElements().add(nlae);
        return model;
    }

    public static void addNLAEFiles(DeploymentModel model, String outputFolder, Properties properties) {
        try {
            String nlaeZipPath = outputFolder + File.separator + NLAE_ZIP_FILENAME;
            InputStream input = FileService.downloadFiles(nlaeZipPath, NLAE_ZIP_ADDRESS, "NLAE");
            if (input != null) {
                FileService.unzip(nlaeZipPath, outputFolder);
            }
            applyPropertiesToNLAEFiles(nlaeZipPath.substring(0, nlaeZipPath.lastIndexOf('.')), properties);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void applyPropertiesToNLAEFiles(String nlaePath, Properties properties) throws IOException {
        Path flinkConfigPath = Paths.get(nlaePath + File.separator + "conf" + File.separator + "flink-conf.yaml");
        HashMap<String, String> flinkPropertyMap = new HashMap<>();
        flinkPropertyMap.put("jobmanager.heap.size:", PropertiesService.NLAE_JOBMANAGER_HEAPSIZE);
        flinkPropertyMap.put("taskmanager.heap.size:", PropertiesService.NLAE_TASKMANAGER_HEAPSIZE);
        flinkPropertyMap.put("taskmanager.numberOfTaskSlots:", PropertiesService.NLAE_TASKMANAGER_SLOTS);
        flinkPropertyMap.put("parallelism.default:", PropertiesService.NLAE_PARALLELISM);
        FileService.applyMapToFile(properties, flinkConfigPath, flinkPropertyMap);

        Path nlaeDeploymentPath = Paths.get(nlaePath + File.separator + "nlae-compose.yml");
        List<String> deploymentYAML = Files.readAllLines(nlaeDeploymentPath);
        for (int i = 0; i < deploymentYAML.size(); i++) {
            String line = deploymentYAML.get(i);
            if (line.contains("8080")) {
                deploymentYAML.set(i, line.replace("8080", properties.getProperty(PropertiesService.NLAE_API_PORT)));
            }
            if (line.contains("/path/to/models")) {
                deploymentYAML.set(i,
                        line.replace("/path/to/models", properties.getProperty(PropertiesService.NLAE_SHAREDVOLUME)));
            }
        }
        int taskmanagerIndex = getTaskmanagerIndex(deploymentYAML);
        for (int i = taskmanagerIndex; i < deploymentYAML.size(); i++) {
            String line = deploymentYAML.get(i);
            if (line.contains("replicas:")) {
                deploymentYAML.set(i,
                        line.replace("2", properties.getProperty(PropertiesService.NLAE_TASKMANAGER_REPLICAS)));
            }
        }
        Files.write(nlaeDeploymentPath, deploymentYAML, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static int getTaskmanagerIndex(List<String> kafkaCluster) {
        for (int i = 0; i < kafkaCluster.size(); i++) {
            if (kafkaCluster.get(i).contains("taskmanager")) {
                return i;
            }
        }
        return 1;
    }

}
