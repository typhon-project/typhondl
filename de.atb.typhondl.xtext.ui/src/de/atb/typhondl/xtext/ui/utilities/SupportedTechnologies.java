package de.atb.typhondl.xtext.ui.utilities;

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
    DockerCompose("Docker Compose", "Docker", true, "volume", false) {
    },
    DockerSwarm("Docker Swarm", "Docker", true, "volume", false) {
    },
    Kubernetes("Kubernetes with Docker", "Docker", false, "persistentVolumeClaim", true) {
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
            String defaultVolumesType, boolean canUseHelm) {
        this.displayedName = displayedName;
        this.containerType = containerType;
        this.createAllAnalyticsContainers = createAllAnalyticsContainers;
        this.defaultVolumesType = defaultVolumesType;
        this.canUseHelm = canUseHelm;
    }

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
}
