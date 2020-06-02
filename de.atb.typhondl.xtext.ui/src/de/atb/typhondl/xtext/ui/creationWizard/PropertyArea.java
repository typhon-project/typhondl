package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Property;

public class PropertyArea extends Area {

    public PropertyArea(DB db, Container container, int chosenTechnology, Composite parent) {
        super(db, container, chosenTechnology, parent);
        createGroup("Parameters");
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
