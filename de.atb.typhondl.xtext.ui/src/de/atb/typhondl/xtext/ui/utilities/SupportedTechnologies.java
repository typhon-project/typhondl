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
    DockerCompose("Docker Compose", "Docker"), DockerSwarm("Docker Swarm", "Docker"),
    Kubernetes("Kubernetes with Docker", "Docker");

    /**
     * The name that gets displayed in the {@link CreateModelWizard}
     */
    private final String displayedName;

    /**
     * Name of the Container Type for {@link ContainerType}
     */
    private final String containerType;

    /**
     * 
     * @return The name that gets displayed in the {@link CreateModelWizard}
     */
    public String getDisplayedName() {
        return displayedName;
    }

    /**
     * Creates an instance with
     * 
     * @param displayedName The name that gets displayed in the
     *                      {@link CreateModelWizard}
     * @param containerType Name of the Container Type for {@link ContainerType}
     * @param clusterType   Name of the Cluster Type for {@link ClusterType}
     */
    private SupportedTechnologies(String displayedName, String containerType) {
        this.displayedName = displayedName;
        this.containerType = containerType;
    }

    /**
     * 
     * @return Name of the Container Type for {@link ContainerType}
     */
    public String getContainerType() {
        return containerType;
    }

}
