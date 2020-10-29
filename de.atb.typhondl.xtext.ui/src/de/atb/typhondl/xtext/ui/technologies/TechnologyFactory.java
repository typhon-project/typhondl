package de.atb.typhondl.xtext.ui.technologies;

public class TechnologyFactory {

    public ITechnologies createTechnology(SupportedTechnologies chosenTechnology) {
        switch (chosenTechnology) {
        case DockerCompose:
            return new DockerCompose();
        case KubernetesDocker:
            return new KubernetesDocker();
        default:
            return new DockerCompose();
        }
    }
}
