package de.atb.typhondl.xtext.ui.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.atb.typhondl.xtext.ui.properties.PropertiesService;

public class EvolutionConfigEditor {
    public List<InputField> dockerComposeFields = Arrays.asList(
            new InputField("Backend published port: ", PropertiesService.EVOLUTION_BACKEND_PUBLISHEDPORT),
            new InputField("Frontend published port: ", PropertiesService.EVOLUTION_FRONTEND_PUBLISHEDPORT));
    public List<InputField> kubernetesFields = Arrays.asList(
            new InputField("Backend published port: ", PropertiesService.EVOLUTION_BACKEND_PUBLISHEDPORT),
            new InputField("Frontend published port: ", PropertiesService.EVOLUTION_FRONTEND_PUBLISHEDPORT));

    public List<InputField> getInputFields(SupportedTechnologies chosenTemplate) {
        switch (chosenTemplate) {
        case DockerCompose:
            return dockerComposeFields;
        case Kubernetes:
            return kubernetesFields;
        default:
            return new ArrayList<>();
        }
    }
}
