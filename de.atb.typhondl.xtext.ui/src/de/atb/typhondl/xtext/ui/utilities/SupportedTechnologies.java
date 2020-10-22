package de.atb.typhondl.xtext.ui.utilities;

import java.util.Properties;

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

import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.ui.creationWizard.CreateModelWizard;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;

/**
 * Utility class for providing supported Technologies. Included at the moment:
 * <li>Docker Compose</li>
 * <li>Docker Swarm</li>
 * <li>Kubernetes with Docker containers</li>
 * 
 * @author flug
 *
 */
public enum SupportedTechnologies {
    DockerCompose("Docker Compose", "Docker", true, "volume", false, false, false) {

        @Override
        public void setConnectionDefaults(Properties properties) {
            properties.setProperty(PropertiesService.API_PUBLISHEDPORT, "8080");
            properties.setProperty(PropertiesService.UI_PUBLISHEDPORT, "4200");
        }
    },
    DockerSwarm("Docker Swarm", "Docker", true, "volume", false, false, true) {
        // TODO TYP-186 volumes in swam
        @Override
        public void setConnectionDefaults(Properties properties) { // TODO
            properties.setProperty(PropertiesService.API_PUBLISHEDPORT, "8080");
            properties.setProperty(PropertiesService.UI_PUBLISHEDPORT, "4200");
        }
    },
    Kubernetes("Kubernetes with Docker", "Docker", false, "persistentVolumeClaim", true, true, true) {
        // TODO proxy to localhost?
        @Override
        public void setConnectionDefaults(Properties properties) {
            properties.setProperty(PropertiesService.API_PUBLISHEDPORT, "30061");
            properties.setProperty(PropertiesService.UI_PUBLISHEDPORT, "30075");
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

    /**
     * Creates an instance with
     * 
     * @param displayedName The name that gets displayed in the
     *                      {@link CreateModelWizard}
     * @param containerType Name of the Container Type for {@link ContainerType}
     * @param clusterType   Name of the Cluster Type for {@link ClusterType} TODO
     *                      TYP-186
     */
    private SupportedTechnologies(String displayedName, String containerType, boolean createAllAnalyticsContainers,
            String defaultVolumesType, boolean canUseHelm, boolean canUseKubeConfig,
            boolean canDoStatelessReplication) {
        this.displayedName = displayedName;
        this.containerType = containerType;
        this.createAllAnalyticsContainers = createAllAnalyticsContainers;
        this.defaultVolumesType = defaultVolumesType;
        this.canUseHelm = canUseHelm;
        this.canUseKubeConfig = canUseKubeConfig;
        this.canDoStatelessReplication = canDoStatelessReplication;
    }

    public abstract void setConnectionDefaults(Properties properties);

    /**
     * Decides, if all Analytics containers (kafka, zookeeper, authAll, flink
     * jobmanager, flink taskmanager) should be created or just a kafka container
     * for the API to reference
     * 
     * @return true if all containers are needed, false if only the kafka reference
     *         should be created
     *
     *         Returns technology dependent default volume type
     **/
    /**
     * if (chosenTemplate == SupportedTechnologies.Kubernetes) {
     * properties.setProperty(PropertiesService.API_HOST, "192.168.99.101");
     * properties.setProperty(PropertiesService.API_PUBLISHEDPORT, "30061");
     * properties.setProperty(PropertiesService.UI_PUBLISHEDPORT, "30075");
     * hiddenData.exclude = false; hidden.setVisible(true); parent.layout(true);
     * properties.setProperty(PropertiesService.POLYSTORE_KUBECONFIG,
     * kubeconfig.getText()); } else if (chosenTemplate ==
     * SupportedTechnologies.DockerCompose) {// TODO TYP-186
     * properties.setProperty(PropertiesService.API_HOST, "localhost");
     * properties.setProperty(PropertiesService.API_PUBLISHEDPORT, "8080");
     * properties.setProperty(PropertiesService.UI_PUBLISHEDPORT, "4200");
     * hiddenData.exclude = true; hidden.setVisible(false); parent.layout(true);
     * properties.setProperty(PropertiesService.POLYSTORE_KUBECONFIG, ""); }
     */
}
