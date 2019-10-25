package de.atb.typhondl.xtext.ui.utilities;

public enum SupportedTechnologies {
	DockerCompose ("Docker Compose", "Docker", "DockerCompose");//,
	//Kubernetes ("Kubernetes with Docker", "Docker", "Kubernetes");
	
	private final String displayedName;
	private final String containerType;
	private final String clusterType;
	
	public String getDisplayedName() {
		return displayedName;
	}

	private SupportedTechnologies(String displayedName, String containerType, String clusterType) {
		this.displayedName = displayedName;
		this.containerType = containerType;
		this.clusterType = clusterType;
	}

	public String getContainerType() {
		return containerType;
	}
	
	public String getClusterType() {
		return clusterType;
	}
	
}
