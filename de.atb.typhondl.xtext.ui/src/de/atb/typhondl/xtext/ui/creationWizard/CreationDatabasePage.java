package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
    private Group parameterGroup;
    private Group templateVariableGroup;
    private Group helmGroup;
    private Text imageText;
    private int chosenTemplate;
    private Group resourceGroup;
    private Group addressGroup;
    private ArrayList<Text> notEmptyTexts;
    private Composite main;
    private int pageWidth;
    private Group imageGroup;

    public CreationDatabasePage(String pageName, DB db, int chosenTemplate, int pageWidth) {
        super(pageName);
        this.db = db;
        this.chosenTemplate = chosenTemplate;
        this.container = createDBContainer();
        this.pageWidth = pageWidth;
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
        setDescription("Database Type: " + db.getType().getName());
        ScrolledComposite scrolling = new ScrolledComposite(parent, SWT.V_SCROLL);
        this.main = new Composite(scrolling, SWT.NONE);
        scrolling.setContent(main);
        scrolling.setExpandVertical(true);
        scrolling.setExpandHorizontal(true);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        main.setLayout(new GridLayout(1, false));

        if (SupportedTechnologies.values()[chosenTemplate].getClusterType().equalsIgnoreCase("Kubernetes")
                && db.getHelm() != null && !db.isExternal()) {
            helmGroup = new Group(main, SWT.READ_ONLY);
            helmGroup.setLayout(new GridLayout(2, false));
            helmGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            helmGroup.setText("Helm settings");
            helmArea();
        }

        if (!db.isExternal()) {
            imageGroup = new Group(main, SWT.READ_ONLY);
            imageGroup.setLayout(new GridLayout(2, false));
            imageGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            imageGroup.setText("Image");
            imageArea();
        }

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

        main.setSize(main.computeSize(pageWidth, SWT.DEFAULT));
        scrolling.setMinSize(main.computeSize(pageWidth, SWT.DEFAULT));

        setControl(scrolling);
    }

    private void addressArea() {
        if (db.isExternal()) {
            if (addressGroup == null) {
                addressGroup = new Group(main, SWT.READ_ONLY);
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
        } else {
            clearAddress();
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
                resourceGroup = new Group(main, SWT.READ_ONLY);
                resourceGroup.setLayout(new GridLayout(1, false));
                resourceGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
                resourceGroup.setText("Container Resources");
            }
            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            GridData gridDataChecks = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            gridDataChecks.horizontalSpan = 2;

            Resources resources = createDefaultResources();
            notEmptyTexts = new ArrayList<>();

            Button limitCheck = new Button(resourceGroup, SWT.CHECK);
            limitCheck.setText("Set container resource limits");
            limitCheck.setLayoutData(gridDataChecks);
            // create invisible Limit Composite in each resource composite
            Composite limitComposite = new Composite(resourceGroup, NONE);
            limitComposite.setLayout(new GridLayout(2, false));
            GridData limitData = new GridData(SWT.FILL, SWT.FILL, true, true);
            limitData.exclude = true;
            limitComposite.setLayoutData(limitData);
            Label limCPULabel = new Label(limitComposite, NONE);
            limCPULabel.setText("limitCPU: ");
            Text limCPUText = new Text(limitComposite, SWT.BORDER);
            limCPUText.setText(resources.getLimitCPU());
            limCPUText.setLayoutData(gridDataFields);
            limCPUText.addModifyListener(e -> {
                resources.setLimitCPU(limCPUText.getText());
                validate();
            });
            notEmptyTexts.add(limCPUText);
            Label limMemLabel = new Label(limitComposite, NONE);
            limMemLabel.setText("limitMemory: ");
            Text limMemText = new Text(limitComposite, SWT.BORDER);
            limMemText.setText(resources.getLimitMemory());
            limMemText.setLayoutData(gridDataFields);
            limMemText.addModifyListener(e -> {
                resources.setLimitMemory(limMemText.getText());
                validate();
            });
            notEmptyTexts.add(limMemText);

            Button reservationCheck = new Button(resourceGroup, SWT.CHECK);
            reservationCheck.setText("Set container resource reservations (this will set limits as well)");
            reservationCheck.setLayoutData(gridDataChecks);
            // create invisible Reservation Composite in each resource composite
            Composite reservationComposite = new Composite(resourceGroup, NONE);
            reservationComposite.setLayout(new GridLayout(2, false));
            GridData reservationData = new GridData(SWT.FILL, SWT.FILL, true, true);
            reservationData.exclude = true;
            reservationComposite.setLayoutData(reservationData);
            Label resCPULabel = new Label(reservationComposite, NONE);
            resCPULabel.setText("reservationCPU: ");
            Text resCPUText = new Text(reservationComposite, SWT.BORDER);
            resCPUText.setText("0.25");
            resCPUText.setLayoutData(gridDataFields);
            resCPUText.addModifyListener(e -> {
                resources.setReservationCPU(resCPUText.getText());
                validate();
            });
            notEmptyTexts.add(resCPUText);
            Label resMemLabel = new Label(reservationComposite, NONE);
            resMemLabel.setText("reservationMemory: ");
            Text resMemText = new Text(reservationComposite, SWT.BORDER);
            resMemText.setText("256M");
            resMemText.setLayoutData(gridDataFields);
            resMemText.addModifyListener(e -> {
                resources.setReservationMemory(resMemText.getText());
                validate();
            });
            notEmptyTexts.add(resMemText);

            limitCheck.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    // set the textfields visible, resize window
                    boolean useLimits = limitCheck.getSelection();
                    limitData.exclude = !useLimits;
                    limitComposite.setVisible(useLimits);
                    main.setSize(main.computeSize(pageWidth, SWT.DEFAULT));
                    ((ScrolledComposite) getControl()).setMinSize(main.computeSize(pageWidth, SWT.DEFAULT));
                }
            });

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

    private void validate() {
        Status status = null;
        for (Text text : notEmptyTexts) {
            if (text.isVisible() && text.getText().isEmpty()) {
                status = new Status(IStatus.ERROR, "Wizard", "Textfields can't be empty");
            }
        }
        setStatus(status);
        // TODO validation resource syntax
    }

    private Resources createDefaultResources() {
        Resources resources = TyphonDLFactory.eINSTANCE.createResources();
        resources.setLimitCPU("0.5");
        resources.setLimitMemory("512M");
        return resources;
    }

    /**
     * The Area inside the helm group. Here the {@link HelmList} is handled.
     */
    private void helmArea() {
        if (SupportedTechnologies.values()[chosenTemplate].getClusterType().equalsIgnoreCase("Kubernetes")
                && db.getHelm() != null) {
            if (helmGroup == null) {
                helmGroup = new Group(main, SWT.READ_ONLY);
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
                parameterGroup = new Group(main, SWT.READ_ONLY);
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
    private void imageArea() {
        if (!db.isExternal()) {
            if (imageGroup == null) {
                imageGroup = new Group(main, SWT.READ_ONLY);
                imageGroup.setLayout(new GridLayout(2, false));
                imageGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
                imageGroup.setText("Image");
            }
            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            new Label(imageGroup, NONE).setText("image used:");
            imageText = new Text(imageGroup, SWT.BORDER);
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

    public String getDBName() {
        return db.getName();
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
            group.setVisible(true);
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
        setDescription("Database Type: " + db.getType().getName());
        updateGroup(helmGroup, this::helmArea);
        updateGroup(parameterGroup, this::parameterArea);
        updateGroup(addressGroup, this::addressArea);
        updateGroup(imageGroup, this::imageArea);
        updateGroup(resourceGroup, this::resourceArea);
        main.layout();
    }

    public int getChosenTemplate() {
        return this.chosenTemplate;
    }

    public void setChosenTemplate(int chosenTemplate) {
        this.chosenTemplate = chosenTemplate;
    }

    public void setDB(DB db) {
        this.db = db;
    }

}
