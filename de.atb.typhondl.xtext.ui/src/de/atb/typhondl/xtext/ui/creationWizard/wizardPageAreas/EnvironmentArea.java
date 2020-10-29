package de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas;

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

import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Property;
import de.atb.typhondl.xtext.ui.technologies.SupportedTechnologies;

/**
 * WizardPage {@link Area} to alter DB's environment
 * 
 * @author flug
 *
 */
public class EnvironmentArea extends Area {

    /**
     * WizardPage {@link Area} to alter DB's environment
     * 
     * @param db               database from DatabasePage
     * @param chosenTechnology the clusterType
     * @param parent           the main control
     */
    public EnvironmentArea(DB db, SupportedTechnologies chosenTechnology, Composite parent) {
        super(db, null, parent, "Environment", null, null, null);
    }

    @Override
    public void createArea() {
        HashMap<String, Property> properties = new HashMap<>();
        for (Property property : db.getEnvironment().getParameters()) {
            addPropertyToList(property.getName(), property, properties);
        }
        addPropertyFieldsToGroup(group, properties);
    }

}
