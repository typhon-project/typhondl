package de.atb.typhondl.xtext.ui.utilities;

import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.ui.creationWizard.CreateModelWizard;

/**
 * Utility class for providing supported Technologies. Included at the moment:
 * <li>Docker Compose</li>
 * <li>Kubernetes with Docker containers</li>
 * 
 * @author flug
 *
 */
public enum SupportedTechnologies {
    DockerCompose("Docker Compose", "Docker", "DockerCompose"),
    Kubernetes("Kubernetes with Docker", "Docker", "Kubernetes");

    /**
     * The name that gets displayed in the {@link CreateModelWizard}
     */
    private final String displayedName;

    /**
     * Name of the Container Type for {@link ContainerType}
     */
    private final String containerType;

    /**
     * Name of the Cluster Type for {@link ClusterType}
     */
    private final String clusterType;

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
    private SupportedTechnologies(String displayedName, String containerType, String clusterType) {
        this.displayedName = displayedName;
        this.containerType = containerType;
        this.clusterType = clusterType;
    }

    /**
     * 
     * @return Name of the Container Type for {@link ContainerType}
     */
    public String getContainerType() {
        return containerType;
    }

    /**
     * 
     * @return Name of the Cluster Type for {@link ClusterType}
     */
    public String getClusterType() {
        return clusterType;
    }

}
