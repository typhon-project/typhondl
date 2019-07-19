package de.atb.typhondl.xtext.ui.utilities;

public enum SupportedTechnologies {
	DockerCompose ("Docker Compose"),
	Kubernetes ("Kubernetes with Docker");
	
	private final String displayedName;
	
	public String getDisplayedName() {
		return displayedName;
	}

	private SupportedTechnologies(String displayedName) {
		this.displayedName = displayedName;
	}
	
}
