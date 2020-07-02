package de.atb.typhondl.xtext.ui.utilities;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to load properties from classpath
 * 
 * @author flug
 *
 */
public class PropertiesLoader {

    private static final String PROPERTIES_PATH = "de/atb/typhondl/xtext/ui/properties/polystore.properties";

    /**
     * Loads polystore.properties from classpath
     * 
     * @return polystore.properties
     * @throws IOException
     */
    public static Properties loadProperties() throws IOException {

        Properties properties = new Properties();

        InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH);
        properties.load(input);
        return properties;
    }
}
