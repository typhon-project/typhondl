package de.atb.typhondl.xtext.ui.modelUtils;

/*-
 * #%L
 * de.atb.typhondl.xtext.ui
 * %%
 * Copyright (C) 2018 - 2020 ATB
 * %%
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * #L%
 */

import java.util.ArrayList;
import java.util.Properties;

import de.atb.typhondl.xtext.typhonDL.Environment;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

/**
 * Utility class for easier {@link Software} model object handling
 * 
 * @author flug
 *
 */
public class SoftwareService {

    /**
     * Creates a {@link Software}
     * 
     * @param name       name of the Software
     * @param imageValue optional image value
     * @return a new {@link Software}
     */
    public static Software create(String name, String imageValue) {
        Software software = TyphonDLFactory.eINSTANCE.createSoftware();
        software.setName(name);
        if (imageValue != null) {
            IMAGE image = TyphonDLFactory.eINSTANCE.createIMAGE();
            image.setValue(imageValue);
            software.setImage(image);
        }
        return software;
    }

    /**
     * Creates an {@link Environment} object from a String array
     * 
     * @param strings [name, value, name, value, ...]
     * @return a new {@link Environment} object
     */
    public static Environment createEnvironment(String[] strings) {
        Environment environment = TyphonDLFactory.eINSTANCE.createEnvironment();
        for (int i = 0; i < strings.length; i = i + 2) {
            Key_Values keyValues = TyphonDLFactory.eINSTANCE.createKey_Values();
            keyValues.setName(strings[i]);
            keyValues.setValue(strings[i + 1]);
            environment.getParameters().add(keyValues);
        }
        return environment;
    }

    /**
     * Gets the name of the environment property. E.g.
     * "qlserver.environment.timestamp" would return "timestamp"
     * 
     * @param propertyName the complete name of an environment property
     * @return just the environment variable name
     */
    public static String getEnvironmentName(String propertyName) {
        String substring = propertyName.substring(propertyName.indexOf("environment"));
        return substring.substring(substring.indexOf('.') + 1);
    }

    /**
     * Searches through {@link Properties} to find environment properties of given
     * component
     * 
     * @param properties    the Properties to search through
     * @param componentName the name of the component to find (e.g. api, ui, ...)
     * @return A String array with [name, value, name, value, ...]
     */
    public static String[] getEnvironmentFromProperties(Properties properties, String componentName) {
        ArrayList<String> list = new ArrayList<>();
        properties.stringPropertyNames().stream().filter(name -> name.contains(componentName))
                .filter(name -> name.contains("environment")).forEach(name -> {
                    list.add(getEnvironmentName(name));
                    list.add(properties.getProperty(name));
                });
        return list.toArray(new String[0]);
    }

}
