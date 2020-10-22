package de.atb.typhondl.xtext.ui.utilities;

import java.util.Arrays;
import java.util.List;

import de.atb.typhondl.xtext.ui.properties.PropertiesService;

public class EvolutionConfigEditor {
    public List<InputField> fields = Arrays.asList(
            new InputField("Backend published port: ", PropertiesService.EVOLUTION_BACKEND_PUBLISHEDPORT),
            new InputField("Frontend published port: ", PropertiesService.EVOLUTION_FRONTEND_PUBLISHEDPORT));

    public List<InputField> getInputFields() {
        return fields;
    }
}
