package de.atb.typhondl.xtext.ui.technologies;

public class TechnologyFactory {

    public static ITechnology createTechnology(SupportedTechnologies chosenTechnology) {
        switch (chosenTechnology) {
        case DockerCompose:
            return new DockerCompose(chosenTechnology);
        case Kubernetes:
            return new KubernetesDocker(chosenTechnology);
        default:
            return new DockerCompose(chosenTechnology);
        }
    }
}
