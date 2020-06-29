package de.atb.typhondl.xtext.ui.wizardPageAreas;

import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Property;

public class EnvironmentArea extends Area {

    public EnvironmentArea(DB db, int chosenTechnology, Composite parent) {
        super(db, null, chosenTechnology, parent, "Environment", null);
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
