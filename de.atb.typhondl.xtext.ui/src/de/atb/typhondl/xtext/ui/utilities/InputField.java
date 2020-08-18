package de.atb.typhondl.xtext.ui.utilities;

import java.util.Properties;

import de.atb.typhondl.xtext.ui.creationWizard.CreationAnalyticsPage.KafkaConfigEditor;

/**
 * Helper class for the {@link KafkaConfigEditor}
 * 
 * @author flug
 *
 */
public class InputField {

    public final String label;
    public final String propertyName;

    /**
     * Creates an InputField.
     * 
     * @param label        The displayed label
     * @param propertyName The name of the property for saving the input as
     *                     {@link Properties}
     */
    public InputField(String label, String propertyName) {
        this.label = label;
        this.propertyName = propertyName;
    }
}