package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Key_KeyValueList;
import de.atb.typhondl.xtext.typhonDL.Key_ValueArray;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Property;

abstract class Area {

    protected DB db;
    protected Container container;
    protected int chosenTechnology;
    protected Composite parent;
    protected Group group;

    protected Area(DB db, Container container, int chosenTechnology, Composite parent) {
        this.db = db;
        this.container = container;
        this.chosenTechnology = chosenTechnology;
        this.parent = parent;
    }

    public void createGroup(String text) {
        this.group = new Group(this.parent, SWT.READ_ONLY);
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        group.setText(text);
    }

    public void updateData(DB db, Container container, int chosenTechnology) {
        this.db = db;
        this.container = container;
        this.chosenTechnology = chosenTechnology;
    }

    /**
     * Finds all {@link Key_Values} and {@link Key_ValueArray}s inside the property
     * list, gives them the right name and adds them to a list
     * 
     * @param name       Name of the Property
     * @param property   The Property to add to the list
     * @param properties
     */
    protected void addPropertyToList(String name, Property property, HashMap<String, Property> properties) {
        if (Key_KeyValueList.class.isInstance(property)) {
            for (Property subProperty : ((Key_KeyValueList) property).getProperties()) {
                addPropertyToList(name + "." + subProperty.getName(), subProperty, properties);
            }
        } else {
            properties.put(name, property);
        }
    }

    /**
     * Turns Text text into List to be set as {@link Key_ValueArray#getValues()}
     * 
     * @param text the text taken from the Text
     * @return text as List
     */
    protected List<String> toArray(String text) {
        return Arrays.asList(text.split(","));
    }

    /**
     * Turns values of {@link Key_ValueArray} into String with comma separated
     * values
     * 
     * @param values values of a {@link Key_ValueArray}
     * @return Comma separated String
     */
    protected String fromArray(EList<String> values) {
        if (!values.isEmpty()) {
            String arrayString = values.get(0);
            values.remove(0);
            for (String string : values) {
                arrayString += "," + string;
            }
            return arrayString;
        } else
            return "";
    }

    /**
     * Creates a Label and a Text with {@link ModifyListener} for each Property
     * 
     * @param group
     * @param properties
     */
    protected void addPropertyFieldsToGroup(Group group, HashMap<String, Property> properties) {
        GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        ArrayList<String> names = new ArrayList<>(properties.keySet());
        Collections.sort(names);
        for (String name : names) {
            new Label(group, SWT.NONE).setText(name + ":");
            Property property = properties.get(name);
            Text propertyText = new Text(group, SWT.BORDER);
            propertyText.setLayoutData(gridDataFields);
            if (Key_Values.class.isInstance(property)) {
                Key_Values key_Values = (Key_Values) property;
                propertyText.setText(key_Values.getValue());
                propertyText.addModifyListener(e -> key_Values.setValue(propertyText.getText()));
            } else {
                Key_ValueArray key_ValueArray = (Key_ValueArray) property;
                propertyText.setText(fromArray(key_ValueArray.getValues()));
                propertyText.addModifyListener(e -> key_ValueArray.getValues().addAll(toArray(propertyText.getText())));
            }
        }
    }

    public void updateArea() {
        if (group != null) {
            disposeChildren(group);
        }
        areaMethod.run();
        if (group != null) {
            setGroupVisible(group);
            group.layout();
            group.getParent().layout(true);
        }
    }

    public abstract void createArea();

}
