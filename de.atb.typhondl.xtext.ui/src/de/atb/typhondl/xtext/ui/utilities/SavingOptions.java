package de.atb.typhondl.xtext.ui.utilities;

import java.util.Map;

import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.resource.SaveOptions.Builder;

/**
 * Utility class. Provides saving options including formatting
 * 
 * @author flug
 *
 */
public class SavingOptions {

    /**
     * 
     * @return saving options including formatting
     */
    public static Map<Object, Object> getTDLoptions() {
        Builder builder = SaveOptions.newBuilder();
        builder.format();
        return builder.getOptions().toOptionsMap();
    }
}
