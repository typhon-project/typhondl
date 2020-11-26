package de.atb.typhondl.xtext.ui.technologies;

import de.atb.typhondl.xtext.validation.TyphonDLValidator;

/**
 * Utility class for providing supported Technologies. Included at the moment:
 * <li>Docker Compose</li>
 * <li>Kubernetes with Docker containers</li>
 * 
 * In case of extension a new class implementing {@link ITechnology} has to be
 * created. Additionally a case to create that class has to be added to
 * {@link TechnologyFactory}. Also validation {@link TyphonDLValidator} has to
 * be adjusted
 * 
 * @author flug
 */
public enum SupportedTechnologies {
    DockerCompose("Docker Compose"), Kubernetes("Kubernetes with Docker");

    private String displayedName;

    SupportedTechnologies(String displayedName) {
        this.displayedName = displayedName;
    }

    public String getDisplayedName() {
        return displayedName;
    }
}
