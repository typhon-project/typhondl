package de.atb.typhondl.xtext.ui.utilities;

public enum SupportedTechnologies {
	DockerCompose ("Docker Compose", "Docker"),
	Kubernetes ("Kubernetes with Docker", "Docker");
	
	private final String displayedName;
	private final String containerType;
	
	public String getDisplayedName() {
		return displayedName;
	}

	private SupportedTechnologies(String displayedName, String containerType) {
		this.displayedName = displayedName;
		this.containerType = containerType;
	}

	public String getContainerType() {
		return containerType;
	}
	
}
