package de.atb.typhondl.xtext.ui.wizardPageAreas;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Property;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

public class AddressArea extends Area {

    public AddressArea(DB db, Container container, int chosenTechnology, Composite parent) {
        super(db, container, chosenTechnology, parent, "Database Address", null);
    }

    @Override
    public void createArea() {
        if (db.isExternal()) {
            if (group == null) {
                createGroup("Database Address");
            }
            Key_Values address;
            if (getAddress() == null) {
                address = TyphonDLFactory.eINSTANCE.createKey_Values();
                address.setName("address");
                address.setValue("https://example.com");
                db.getParameters().add(address);
            } else {
                address = getAddress();
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
            clearAddress();
        }
    }

    private Key_Values getAddress() {
        return (Key_Values) db.getParameters().stream()
                .filter(parameter -> parameter.getName().equalsIgnoreCase("address")).findFirst().orElse(null);
    }

    private void clearAddress() {
        Property address = getAddress();
        if (address != null) {
            db.getParameters().remove(address);
        }
    }
}
