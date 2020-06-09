package de.atb.typhondl.xtext.ui.wizardPageAreas;

import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Property;

public class PropertyArea extends Area {

    public PropertyArea(DB db, Composite parent) {
        super(db, null, -1, parent, "Parameters", null);
    }

    @Override
    public void createArea() {
        HashMap<String, Property> properties = new HashMap<>();
        for (Property property : db.getParameters()) {
            addPropertyToList(property.getName(), property, properties);
        }
        addPropertyFieldsToGroup(group, properties);
    }
}
