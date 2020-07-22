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
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

public class ImageArea extends Area {

    public ImageArea(DB db, Composite parent) {
        super(db, null, null, parent, "Image used", null);
    }

    @Override
    public void createArea() {
        if (!db.isExternal() && db.getHelm() == null) {
            if (group == null) {
                createGroup("Image used");
            }
            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            new Label(group, SWT.NONE).setText("image used:");
            Text imageText = new Text(group, SWT.BORDER);
            String imageTextValue = getImageValue();
            imageText.setText(imageTextValue);
            imageText.setLayoutData(gridDataFields);
            imageText.addModifyListener(e -> {
                if (imageText.getText().equalsIgnoreCase(db.getType().getImage().getValue())) {
                    db.setImage(null);
                } else {
                    IMAGE image = TyphonDLFactory.eINSTANCE.createIMAGE();
                    image.setValue(imageText.getText());
                    db.setImage(image);
                }
            });
        }
    }

    /**
     * Returns the correct image value
     * 
     * @return The database's image value if an image is given in the database,
     *         otherwise the dbtype's image value
     */
    private String getImageValue() {
        return db.getImage() == null ? db.getType().getImage().getValue() : db.getImage().getValue();
    }
}
