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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.typhonDL.URI;

/**
 * WizardPage {@link Area} to give address to external database
 * 
 * @author flug
 *
 */
public class AddressArea extends Area {

    /**
     * WizardPage {@link Area} to give address to external database
     * 
     * @param db     external database from DatabasePage
     * @param parent the main control
     */
    public AddressArea(DB db, Composite parent) {
        super(db, null, parent, "Database Address", null);
    }

    @Override
    public void createArea() {
        if (db.isExternal()) {
            if (group == null) {
                createGroup("Database Address");
            }
            URI address;
            if (db.getUri() == null) {
                address = TyphonDLFactory.eINSTANCE.createURI();
                address.setValue("https://example.com");
                db.setUri(address);
            } else {
                address = db.getUri();
            }
            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            new Label(group, SWT.NONE).setText("Database Address: ");
            Text addressText = new Text(group, SWT.BORDER);
            addressText.setText(address.getValue());
            addressText.setToolTipText("Give the address under which the polystore can reach the database");
            addressText.setLayoutData(gridDataFields);
            addressText.addModifyListener(e -> {
                address.setValue(addressText.getText());
            });
        } else {
            db.setUri(null);
        }
    }
}
