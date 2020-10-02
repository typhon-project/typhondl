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

import java.util.Properties;

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
