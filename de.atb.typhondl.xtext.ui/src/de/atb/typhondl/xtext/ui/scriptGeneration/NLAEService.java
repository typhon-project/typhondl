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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.typhonDL.URI;
import de.atb.typhondl.xtext.typhonDL.Volume_Toplevel;
import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.modelUtils.SoftwareService;
import de.atb.typhondl.xtext.ui.modelUtils.VolumesService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.FileService;

public class NLAEService {

    private static final String NLAE_ZIP_FILENAME = "nlae.zip";
    private static final String NLAE_ZIP_ADDRESS = "http://typhon.clmsuk.com/static/" + NLAE_ZIP_FILENAME;

    public static DeploymentModel addNLAE(DeploymentModel model, Properties properties) {
        Software nlae = SoftwareService.create(properties.getProperty(PropertiesService.NLAE_NAME), null);
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
            final String nlaeFolder = nlaeZipPath.substring(0, nlaeZipPath.lastIndexOf('.'));
            if (input != null) {
                FileService.unzip(nlaeZipPath, nlaeFolder);
            }
            applyPropertiesToNLAEFiles(nlaeFolder, properties);
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

    public static DeploymentModel addNLAEDEV(DeploymentModel model, Application application, Properties properties,
            ContainerType containerType) {
        Software nlaeDev = SoftwareService.create(properties.getProperty(PropertiesService.NLAE_NAME),
                properties.getProperty(PropertiesService.NLAEDEV_IMAGE));
        nlaeDev.setExternal(true);
        de.atb.typhondl.xtext.typhonDL.URI nlaeDevURI = TyphonDLFactory.eINSTANCE.createURI();
        nlaeDevURI.setValue("localhost:" + properties.getProperty(PropertiesService.NLAEDEV_PUBLISHEDPORT));
        nlaeDev.setUri(nlaeDevURI);
        model.getElements().add(nlaeDev);
        Container nlaeDevContainer = ContainerService.create("nlaeDEV", containerType, nlaeDev);
        nlaeDevContainer.setPorts(ContainerService.createPorts(new String[] { "target", "8080", "published",
                properties.getProperty(PropertiesService.NLAEDEV_PUBLISHEDPORT) }));
        application.getContainers().add(nlaeDevContainer);
        Software elasticsearch = SoftwareService.create("elasticsearchDEV",
                "docker.elastic.co/elasticsearch/elasticsearch:6.8.1");
        elasticsearch.setEnvironment(SoftwareService.createEnvironment(
                new String[] { "ES_JAVA_OPTS", "'-Xms256m -Xmx512m'", "discovery.type", "single-node" }));
        model.getElements().add(elasticsearch);
        Container elasticsearchContainer = ContainerService.create("elasticsearchDEV", containerType, elasticsearch);
        nlaeDevContainer.getDepends_on().add(ContainerService.createDependsOn(elasticsearchContainer));
        elasticsearchContainer.setVolumes(
                VolumesService.create(new String[] { "esdata1:/usr/share/elasticsearch/data" }, null, null));
        application.getContainers().add(elasticsearchContainer);
        Volume_Toplevel topLevelVolumes = application.getVolumes();
        if (topLevelVolumes == null) {
            topLevelVolumes = TyphonDLFactory.eINSTANCE.createVolume_Toplevel();
            application.setVolumes(topLevelVolumes);
        }
        topLevelVolumes.getNames().add("esdata1");
        return model;
    }

}
