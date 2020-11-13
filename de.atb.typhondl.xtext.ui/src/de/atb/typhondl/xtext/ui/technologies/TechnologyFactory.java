package de.atb.typhondl.xtext.ui.technologies;

/**
 * In case of extension a new class implementing {@link ITechnology} has to be
 * created. Additionally a constant has to be added to
 * {@link SupportedTechnologies}
 * 
 * @author flug
 *
 */
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
