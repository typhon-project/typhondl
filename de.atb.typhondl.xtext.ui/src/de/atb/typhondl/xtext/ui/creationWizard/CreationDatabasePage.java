package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.HelmList;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.Key_KeyValueList;
import de.atb.typhondl.xtext.typhonDL.Key_ValueArray;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Property;
import de.atb.typhondl.xtext.typhonDL.Reference;
import de.atb.typhondl.xtext.typhonDL.Resources;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.utilities.PreferenceReader;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

/**
 * Each Database has a page to define and/or change the image and other
 * properties
 * 
 * @author flug
 *
 */
public class CreationDatabasePage extends MyWizardPage {

    private DB db;
    private Container container;
    private TemplateBuffer buffer;
    private Group parameterGroup;
    private Group templateVariableGroup;
    private Group helmGroup;
    private Text imageText;
    private int chosenTemplate;
    private Group resourceGroup;
    private Group addressGroup;

    public CreationDatabasePage(String pageName, DB db, TemplateBuffer buffer, int chosenTemplate) {
        super(pageName);
        this.db = db;
        this.buffer = buffer;
        this.chosenTemplate = chosenTemplate;
        this.container = createDBContainer();
    }

    private Container createDBContainer() {
        Container newContainer = TyphonDLFactory.eINSTANCE.createContainer();
        String containerName = createContainerName(db.getName());
        newContainer.setName(containerName);
        ContainerType containerType = TyphonDLFactory.eINSTANCE.createContainerType();
        containerType.setName(SupportedTechnologies.values()[chosenTemplate].getContainerType());
        newContainer.setType(containerType);
        Reference reference = TyphonDLFactory.eINSTANCE.createReference();
        reference.setReference(db);
        newContainer.setDeploys(reference);
        return newContainer;
    }

    /**
     * Names of container have to be DNS subdomain names (see DL #31)
     * 
     * @param name
     * @return
     */
    private String createContainerName(String name) {
        name = Pattern.compile("^[^a-zA-Z]*").matcher(name).replaceFirst("");
        name = Pattern.compile("[^a-zA-Z]*$").matcher(name).replaceFirst("");
        name = name.toLowerCase().replaceAll("[^a-z0-9.]", "-");
        return Pattern.compile("-{2,}").matcher(name).replaceAll("-");
    }

    @Override
    public void createControl(Composite parent) {
        setTitle("Database settings for " + db.getName());
        Composite main = new Composite(parent, SWT.NONE);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        main.setLayout(new GridLayout(1, false));

        if (buffer != null && buffer.getVariables().length != 0) {
            templateVariableGroup = new Group(main, SWT.READ_ONLY);
            templateVariableGroup.setLayout(new GridLayout(2, false));
            templateVariableGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            templateVariableGroup.setText("Template Variables");
            templateVariableArea();
        }

        if (SupportedTechnologies.values()[chosenTemplate].getClusterType().equalsIgnoreCase("Kubernetes")
                && db.getHelm() != null) {
            helmGroup = new Group(main, SWT.READ_ONLY);
            helmGroup.setLayout(new GridLayout(2, false));
            helmGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            helmGroup.setText("Helm settings");
            helmArea();
        }

        imageArea(main);

        if (!db.getParameters().isEmpty()) {
            parameterGroup = new Group(main, SWT.READ_ONLY);
            parameterGroup.setLayout(new GridLayout(2, false));
            parameterGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            parameterGroup.setText("Parameters");
            parameterArea();
        }

        if (db.isExternal()) {
            addressGroup = new Group(main, SWT.READ_ONLY);
            addressGroup.setLayout(new GridLayout(2, false));
            addressGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            addressGroup.setText("Database Address");
            addressArea();
        } else {
            resourceGroup = new Group(main, SWT.READ_ONLY);
            resourceGroup.setLayout(new GridLayout(1, false));
            resourceGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            resourceGroup.setText("Container Resources");
            resourceArea();
        }

        setControl(main);
    }

    private void addressArea() {
        if (db.isExternal()) {
            if (addressGroup == null) {
                addressGroup = new Group((Composite) this.getControl(), SWT.READ_ONLY);
                addressGroup.setLayout(new GridLayout(2, false));
                addressGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
                addressGroup.setText("Database Address");
            }
            clearAddress();
            Key_Values address = TyphonDLFactory.eINSTANCE.createKey_Values();
            address.setName("address");
            address.setValue("https://example.com");
            db.getParameters().add(address);
            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            new Label(addressGroup, NONE).setText("Database Address: ");
            Text addressText = new Text(addressGroup, SWT.BORDER);
            addressText.setText(address.getValue());
            addressText.setToolTipText("Give the address under which the polystore can reach the database");
            addressText.setLayoutData(gridDataFields);
            addressText.addModifyListener(e -> {
                address.setValue(addressText.getText());
            });
        }
    }

    private void clearAddress() {
        Property address = db.getParameters().stream()
                .filter(parameter -> parameter.getName().equalsIgnoreCase("address")).findFirst().orElse(null);
        if (address != null) {
            db.getParameters().remove(address);
        }
    }

    private void resourceArea() {
        if (!db.isExternal()) {
            if (resourceGroup == null) {
                resourceGroup = new Group((Composite) this.getControl(), SWT.READ_ONLY);
                resourceGroup.setLayout(new GridLayout(1, false));
                resourceGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
                resourceGroup.setText("Container Resources");
            }
            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            GridData gridDataChecks = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            gridDataChecks.horizontalSpan = 2;

            Resources resources = TyphonDLFactory.eINSTANCE.createResources();
//            // create Resource composite in each group
//            GridData resourceGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
//            resourceGridData.horizontalSpan = 2;
//            Composite resourceComposite = new Composite(group, NONE);
//            resourceComposite.setLayout(new GridLayout(1, false));
//            resourceComposite.setLayoutData(resourceGridData);
//
//            Button limitCheck = new Button(resourceComposite, SWT.CHECK);
//            limitCheck.setText("Set resource " + limitList.getName());
//            limitCheck.setLayoutData(gridDataChecks);
//            // create invisible Limit Composite in each resource composite
//            Composite limitComposite = new Composite(resourceComposite, NONE);
//            limitComposite.setLayout(new GridLayout(2, false));
//            GridData limitData = new GridData(SWT.FILL, SWT.FILL, true, true);
//            limitData.exclude = true;
//            limitComposite.setLayoutData(limitData);
//            Label limMemLabel = new Label(limitComposite, NONE);
//            limMemLabel.setText(limMemKeyValue.getName() + ": ");
//            Text limMemText = new Text(limitComposite, SWT.BORDER);
//            limMemText.setText(limMemKeyValue.getValue());
//            limMemText.setLayoutData(gridDataFields);
//            limMemText.addModifyListener(e -> {
//                limMemKeyValue.setValue(limMemText.getText());
//                validate();
//            });
//            notEmptyTexts.add(limMemText);
//            Label limCPULabel = new Label(limitComposite, NONE);
//            limCPULabel.setText(limCPUKeyValue.getName() + ": ");
//            Text limCPUText = new Text(limitComposite, SWT.BORDER);
//            limCPUText.setText(limCPUKeyValue.getValue());
//            limCPUText.setLayoutData(gridDataFields);
//            limCPUText.addModifyListener(e -> {
//                limCPUKeyValue.setValue(limCPUText.getText());
//                validate();
//            });
//            notEmptyTexts.add(limCPUText);
//
//            Button reservationCheck = new Button(resourceComposite, SWT.CHECK);
//            reservationCheck.setText("Set resource " + reservationList.getName());
//            reservationCheck.setLayoutData(gridDataChecks);
//            // create invisible Reservation Composite in each resource composite
//            Composite reservationComposite = new Composite(resourceComposite, NONE);
//            reservationComposite.setLayout(new GridLayout(2, false));
//            GridData reservationData = new GridData(SWT.FILL, SWT.FILL, true, true);
//            reservationData.exclude = true;
//            reservationComposite.setLayoutData(reservationData);
//            Label resMemLabel = new Label(reservationComposite, NONE);
//            resMemLabel.setText(resMemKeyValue.getName() + ": ");
//            Text resMemText = new Text(reservationComposite, SWT.BORDER);
//            resMemText.setText(resMemKeyValue.getValue());
//            resMemText.setLayoutData(gridDataFields);
//            resMemText.addModifyListener(e -> {
//                resMemKeyValue.setValue(resMemText.getText());
//                validate();
//            });
//            notEmptyTexts.add(resMemText);
//            Label resCPULabel = new Label(reservationComposite, NONE);
//            resCPULabel.setText(resCPUKeyValue.getName() + ": ");
//            Text resCPUText = new Text(reservationComposite, SWT.BORDER);
//            resCPUText.setText(resCPUKeyValue.getValue());
//            resCPUText.setLayoutData(gridDataFields);
//            resCPUText.addModifyListener(e -> {
//                resCPUKeyValue.setValue(resCPUText.getText());
//                validate();
//            });
//            notEmptyTexts.add(resCPUText);
//
//            limitCheck.addSelectionListener(new SelectionAdapter() {
//                @Override
//                public void widgetSelected(SelectionEvent e) {
//                    // set the textfields visible, resize window
//                    boolean useLimits = limitCheck.getSelection();
//                    limitData.exclude = !useLimits;
//                    limitComposite.setVisible(useLimits);
//                    main.setSize(main.computeSize(WIDTH, SWT.DEFAULT));
//                    scrolling.setMinSize(main.computeSize(WIDTH, SWT.DEFAULT));
//                    // add or remove objects from model
//                    if (useLimits) {
//                        resourceList.getProperties().add(limitList);
//                        switch (clusterType) {
//                        case "DockerCompose":
//                            addListToList(deployList, resourceList);
//                            addListToContainer(container, deployList);
//                            break;
//                        case "Kubernetes":
//                            addListToContainer(container, resourceList);
//                        default:
//                            break;
//                        }
//                    } else {
//                        switch (clusterType) {
//                        case "DockerCompose":
//                            removePropertyFromList(resourceList, limitList);
//                            if (resourceList.getProperties().isEmpty()) {
//                                removePropertyFromList(deployList, resourceList);
//                                if (deployList.getProperties().isEmpty()) {
//                                    removePropertyFromContainer(container, deployList);
//                                }
//                            }
//                            break;
//                        case "Kubernetes":
//                            removePropertyFromList(resourceList, limitList);
//                            if (resourceList.getProperties().isEmpty()) {
//                                removePropertyFromContainer(container, resourceList);
//                            }
//                        default:
//                            break;
//                        }
//                    }
//                    validate();
//                }
//            });
//
//            reservationCheck.addSelectionListener(new SelectionAdapter() {
//                @Override
//                public void widgetSelected(SelectionEvent e) {
//                    // set the textfields visible, resize window
//                    boolean useReservations = reservationCheck.getSelection();
//                    reservationData.exclude = !useReservations;
//                    reservationComposite.setVisible(useReservations);
//                    main.setSize(main.computeSize(WIDTH, SWT.DEFAULT));
//                    scrolling.setMinSize(main.computeSize(WIDTH, SWT.DEFAULT));
//                    // add or remove objects from model
//                    if (useReservations) {
//                        resourceList.getProperties().add(reservationList);
//                        switch (clusterType) {
//                        case "DockerCompose":
//                            addListToList(deployList, resourceList);
//                            addListToContainer(container, deployList);
//                            break;
//                        case "Kubernetes":
//                            addListToContainer(container, resourceList);
//                        default:
//                            break;
//                        }
//                    } else {
//                        switch (clusterType) {
//                        case "DockerCompose":
//                            removePropertyFromList(resourceList, reservationList);
//                            if (resourceList.getProperties().isEmpty()) {
//                                removePropertyFromList(deployList, resourceList);
//                                if (deployList.getProperties().isEmpty()) {
//                                    removePropertyFromContainer(container, deployList);
//                                }
//                            }
//                            break;
//                        case "Kubernetes":
//                            removePropertyFromList(resourceList, reservationList);
//                            if (resourceList.getProperties().isEmpty()) {
//                                removePropertyFromContainer(container, resourceList);
//                            }
//                        default:
//                            break;
//                        }
//                    }
//                    validate();
//                }
//            });
        }
    }

    /**
     * The Area inside the helm group. Here the {@link HelmList} is handled.
     */
    private void helmArea() {
        if (SupportedTechnologies.values()[chosenTemplate].getClusterType().equalsIgnoreCase("Kubernetes")
                && db.getHelm() != null) {
            if (helmGroup == null) {
                helmGroup = new Group((Composite) this.getControl(), SWT.READ_ONLY);
                helmGroup.setLayout(new GridLayout(2, false));
                helmGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
                helmGroup.setText("Helm settings");
            }
            HelmList helmList = db.getHelm();
            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            new Label(helmGroup, SWT.NONE).setText("Repository Address:");
            Text addressText = new Text(helmGroup, SWT.BORDER);
            addressText.setText(helmList.getRepoAddress());
            addressText.setLayoutData(gridDataFields);
            addressText.addModifyListener(e -> helmList.setRepoAddress(addressText.getText()));
            new Label(helmGroup, SWT.NONE).setText("Repository Name:");
            Text repoNameText = new Text(helmGroup, SWT.BORDER);
            repoNameText.setText(helmList.getRepoName());
            repoNameText.setLayoutData(gridDataFields);
            repoNameText.addModifyListener(e -> helmList.setRepoName(repoNameText.getText()));
            new Label(helmGroup, SWT.NONE).setText("Chart Name:");
            Text nameText = new Text(helmGroup, SWT.BORDER);
            nameText.setText(helmList.getChartName());
            nameText.setLayoutData(gridDataFields);
            nameText.addModifyListener(e -> helmList.setChartName(nameText.getText()));
            HashMap<String, Property> properties = new HashMap<>();
            for (Property property : helmList.getParameters()) {
                addPropertyToList(property.getName(), property, properties);
            }
            if (!helmList.getParameters().isEmpty()) {
                addPropertyFieldsToGroup(helmGroup, properties);
            }
        }
    }

    /**
     * The Area inside the parameter group. Here the {@link DB#getParameters()} are
     * handled.
     */
    private void parameterArea() {
        if (!db.getParameters().isEmpty()) {
            if (parameterGroup == null) {
                parameterGroup = new Group((Composite) this.getControl(), SWT.READ_ONLY);
                parameterGroup.setLayout(new GridLayout(2, false));
                parameterGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
                parameterGroup.setText("Parameters");
            }
            HashMap<String, Property> properties = new HashMap<>();
            for (Property property : db.getParameters()) {
                addPropertyToList(property.getName(), property, properties);
            }
            addPropertyFieldsToGroup(parameterGroup, properties);
        }
    }

    /**
     * Creates a Label and a Text with {@link ModifyListener} for each Property
     * 
     * @param group
     * @param properties
     */
    private void addPropertyFieldsToGroup(Group group, HashMap<String, Property> properties) {
        GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        ArrayList<String> names = new ArrayList<>(properties.keySet());
        Collections.sort(names);
        for (String name : names) {
            new Label(group, NONE).setText(name + ":");
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

    /**
     * Finds all {@link Key_Values} and {@link Key_ValueArray}s inside the property
     * list, gives them the right name and adds them to a list
     * 
     * @param name       Name of the Property
     * @param property   The Property to add to the list
     * @param properties
     */
    private void addPropertyToList(String name, Property property, HashMap<String, Property> properties) {
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
    private List<String> toArray(String text) {
        return Arrays.asList(text.split(","));
    }

    /**
     * Turns values of {@link Key_ValueArray} into String with comma separated
     * values
     * 
     * @param values values of a {@link Key_ValueArray}
     * @return Comma separated String
     */
    private String fromArray(EList<String> values) {
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
     * The image group, here the image can be edited
     * 
     * @param main Composite to put the group in
     */
    private void imageArea(Composite main) {
        Group group = new Group(main, SWT.READ_ONLY);
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        group.setText("Image");

        GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        new Label(group, NONE).setText("image used:");
        imageText = new Text(group, SWT.BORDER);
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

    /**
     * Returns the correct image value
     * 
     * @return The database's image value if an image is given in the database,
     *         otherwise the dbtype's image value
     */
    private String getImageValue() {
        return db.getImage() == null ? db.getType().getImage().getValue() : db.getImage().getValue();
    }

    /**
     * The Area inside the templateCariable group. Here the
     * {@link TemplateBuffer#getVariables()} are handled.
     */
    private void templateVariableArea() {
        if (buffer != null) {
            List<TemplateVariable> variablesList = new ArrayList<>(Arrays.asList(buffer.getVariables()));

            // this is the database.name, which should not be changed, so it is removed from
            // the list:
            if (db.isExternal()) {
                variablesList.removeIf(variable -> variable.getOffsets()[0] == 17);
            } else {
                variablesList.removeIf(variable -> variable.getOffsets()[0] == 9);
            }
            if (templateVariableGroup == null && !variablesList.isEmpty()) {
                templateVariableGroup = new Group(((Composite) this.getControl()), SWT.READ_ONLY);
                templateVariableGroup.setLayout(new GridLayout(2, false));
                templateVariableGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
                templateVariableGroup.setText("Template Variables");
            }

            if (!variablesList.isEmpty()) {
                GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

                // create a field for each variable:
                for (TemplateVariable templateVariable : variablesList) {
                    new Label(templateVariableGroup, NONE).setText(templateVariable.getName() + ":");
                    Text text = new Text(templateVariableGroup, SWT.BORDER);
                    text.setText(templateVariable.getName());
                    text.setLayoutData(gridDataFields);
                    text.addModifyListener(e -> {
                        int variableIndex = variablesList.indexOf(templateVariable);
                        int oldLenght = templateVariable.getLength();
                        // replace old value in template variable
                        templateVariable.setValue(text.getText());
                        int newLength = templateVariable.getLength();
                        // replace old value in pattern string
                        String newPattern = updatePattern(templateVariable, buffer.getString(), oldLenght);
                        // correct offset of other variables if this variable is not the last one
                        if (variableIndex != variablesList.size() - 1) {
                            for (int i = variableIndex + 1; i < variablesList.size(); i++) {
                                variablesList.get(i).setOffsets(
                                        new int[] { variablesList.get(i).getOffsets()[0] + newLength - oldLenght });
                            }
                        }
                        buffer.setContent(newPattern, variablesList.toArray(new TemplateVariable[0]));
                        updateDB(db, buffer);
                    });
                }
            }
        }
    }

    /**
     * Replaces old variable value with new one
     * 
     * @param templateVariable The TemplateVariable with the new value
     * @param oldPattern       The old Pattern
     * @param oldLength        The length of the old variable value
     * @return The updated Pattern
     */
    private String updatePattern(TemplateVariable templateVariable, String oldPattern, int oldLength) {
        int offset = templateVariable.getOffsets()[0];
        return oldPattern.substring(0, offset) + templateVariable.getValues()[0]
                + oldPattern.substring(offset + oldLength);
    }

    /**
     * updates the {@link DB} model entity in the <code>result</code> map
     * 
     * @param db             The {@link DB} to be updated
     * @param templateBuffer The source for the updated {@link TemplateVariable}s
     */
    private void updateDB(DB db, TemplateBuffer templateBuffer) {
        DB newDB = PreferenceReader.getModelObject(TyphonDLFactory.eINSTANCE.createDB(), templateBuffer);
        db.getParameters().clear();
        db.getParameters().addAll(newDB.getParameters());
    }

    public String getDBName() {
        return db.getName();
    }

    /**
     * Updates the Text showing the image value
     */
    public void updateImageValue() {
        imageText.setText(getImageValue());
    }

    /**
     * Updates each group with the given areaMethod
     * 
     * @param group
     * @param areaMethod
     */
    public void updateGroup(Group group, Runnable areaMethod) {
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

    /**
     * If the given group has no children (i.e. no Labels or Texts) the empty group
     * should be hidden.
     * 
     * @param group
     * @param isVisible
     */
    private void setGroupVisible(Group group) {
        if (group.getChildren().length == 0) {
            GridData excludeData = new GridData(SWT.FILL, SWT.FILL, true, false);
            excludeData.exclude = true;
            group.setLayoutData(excludeData);
            group.setVisible(false);
        } else {
            group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            group.setVisible(false);
        }

    }

    /**
     * Disposes all Labels and Texts of the given group
     * 
     * @param group
     */
    private void disposeChildren(Group group) {
        for (Control control : group.getChildren()) {
            control.dispose();
        }
    }

    /**
     * Updates the database page. <br>
     * After something has changed on the DBMS page, the widgets on the database
     * page have to be updated.
     */
    public void updateAllAreas() {
        updateGroup(helmGroup, this::helmArea);
        updateGroup(parameterGroup, this::parameterArea);
        updateGroup(templateVariableGroup, this::templateVariableArea);
        updateImageValue();
        ((Composite) this.getControl()).layout();
    }

    public int getChosenTemplate() {
        return this.chosenTemplate;
    }

    public void setChosenTemplate(int chosenTemplate) {
        this.chosenTemplate = chosenTemplate;
    }

}
