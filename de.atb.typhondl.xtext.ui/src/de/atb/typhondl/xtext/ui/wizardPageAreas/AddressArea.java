package de.atb.typhondl.xtext.ui.wizardPageAreas;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.typhonDL.URI;

public class AddressArea extends Area {

    public AddressArea(DB db, Composite parent) {
        super(db, null, -1, parent, "Database Address", null);
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
