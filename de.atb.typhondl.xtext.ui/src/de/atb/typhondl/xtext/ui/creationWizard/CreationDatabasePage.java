package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Reference;
import de.atb.typhondl.xtext.typhonDL.Resources;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.utilities.Pair;
import de.atb.typhondl.xtext.ui.utilities.PropertiesLoader;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;
import de.atb.typhondl.xtext.ui.wizardPageAreas.AddressArea;
import de.atb.typhondl.xtext.ui.wizardPageAreas.Area;
import de.atb.typhondl.xtext.ui.wizardPageAreas.HelmArea;
import de.atb.typhondl.xtext.ui.wizardPageAreas.PropertyArea;

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
    private int chosenTechnology;
    private int pageWidth;
    private Properties properties;
    private ArrayList<Area> areas;

    public CreationDatabasePage(String pageName, DB db, int chosenTechnology, int pageWidth) {
        super(pageName);
        this.db = db;
        this.chosenTechnology = chosenTechnology;
        this.container = createDBContainer();
        this.pageWidth = pageWidth;
        areas = new ArrayList<>();
        try {
            this.properties = PropertiesLoader.loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Container createDBContainer() {
        Container newContainer = TyphonDLFactory.eINSTANCE.createContainer();
        String containerName = createContainerName(db.getName());
        newContainer.setName(containerName);
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
        Composite main = new Composite(scrolling, SWT.NONE);
        scrolling.setContent(main);
        scrolling.setExpandVertical(true);
        scrolling.setExpandHorizontal(true);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        main.setLayout(new GridLayout(1, false));

        if (SupportedTechnologies.values()[chosenTechnology].getClusterType().equalsIgnoreCase("Kubernetes")
                && db.getHelm() != null && !db.isExternal()) {
            areas.add(new HelmArea(db, container, chosenTechnology, main));
        }
//
//        if (!db.isExternal()) {
//            imageGroup = new Group(main, SWT.READ_ONLY);
//            imageGroup.setLayout(new GridLayout(2, false));
//            imageGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//            imageGroup.setText("Image");
//            imageArea();
//        }

        if (!db.getParameters().isEmpty()) {
            areas.add(new PropertyArea(db, container, chosenTechnology, main));
        }

        if (db.isExternal()) {
            areas.add(new AddressArea(db, container, chosenTechnology, main));
        }
//        } else {
//            resourceGroup = new Group(main, SWT.READ_ONLY);
//            resourceGroup.setLayout(new GridLayout(1, false));
//            resourceGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//            resourceGroup.setText("Container Resources");
//            resourceArea();
//
//            portGroup = new Group(main, SWT.READ_ONLY);
//            portGroup.setLayout(new GridLayout(1, false));
//            portGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//            portGroup.setText("Container Resources");
//            portArea();
//        }

        main.setSize(main.computeSize(pageWidth, SWT.DEFAULT));
        scrolling.setMinSize(main.computeSize(pageWidth, SWT.DEFAULT));

        setControl(scrolling);
    }

//    private void portArea() {
//        if (!db.isExternal()) {
//            if (portGroup == null) {
//                portGroup = new Group(main, SWT.READ_ONLY);
//                portGroup.setLayout(new GridLayout(1, false));
//                portGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//                portGroup.setText("Port");
//            }
//            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
//            if (container == null) {
//                this.container = createDBContainer();
//            }
//            String targetPort = "";
//            Key_Values port;
//            if (this.container.getPorts() == null) {
//                Ports ports = TyphonDLFactory.eINSTANCE.createPorts();
//                port = TyphonDLFactory.eINSTANCE.createKey_Values();
//                port.setName("target");
//                targetPort = properties.getProperty(db.getType().getName().toLowerCase() + ".port");
//                port.setValue(targetPort);
//                ports.getKey_values().add(port);
//                container.setPorts(ports);
//            } else {
//                port = container.getPorts().getKey_values().stream()
//                        .filter(key -> key.getName().equalsIgnoreCase("target")).findFirst().orElse(null);
//                if (port != null) {
//                    targetPort = port.getValue();
//                }
//            }
//
//            new Label(portGroup, NONE).setText("Container port: ");
//            Text portText = new Text(portGroup, SWT.BORDER);
//            portText.setText(targetPort);
//            portText.setToolTipText("This is the port that will be exposed inside the network/cluster");
//            portText.setLayoutData(gridDataFields);
//            portText.addModifyListener(e -> {
//                port.setValue(portText.getText());
//                validate();
//            });
//            notEmptyTexts.add(portText);
//        }
//    }

//    private void resourceArea() {
//        if (!db.isExternal()) {
//            if (resourceGroup == null) {
//                resourceGroup = new Group(main, SWT.READ_ONLY);
//                resourceGroup.setLayout(new GridLayout(1, false));
//                resourceGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//                resourceGroup.setText("Container Resources");
//            }
//            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
//            GridData gridDataChecks = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
//            gridDataChecks.horizontalSpan = 2;
//
//            if (container == null) {
//                this.container = createDBContainer();
//            }
//
//            Resources resources = createDefaultResources();
//            notEmptyTexts = new ArrayList<>();
//
//            Button limitCheck = new Button(resourceGroup, SWT.CHECK);
//            limitCheck.setText("Set container resource limits");
//            limitCheck.setLayoutData(gridDataChecks);
//            // create invisible Limit Composite in each resource composite
//            Composite limitComposite = new Composite(resourceGroup, NONE);
//            limitComposite.setLayout(new GridLayout(2, false));
//            GridData limitData = new GridData(SWT.FILL, SWT.FILL, true, true);
//            limitData.exclude = true;
//            limitComposite.setLayoutData(limitData);
//            Label limCPULabel = new Label(limitComposite, NONE);
//            limCPULabel.setText("limitCPU: ");
//            Text limCPUText = new Text(limitComposite, SWT.BORDER);
//            limCPUText.setText(resources.getLimitCPU());
//            limCPUText.setLayoutData(gridDataFields);
//            limCPUText.addModifyListener(e -> {
//                resources.setLimitCPU(limCPUText.getText());
//                validate();
//            });
//            notEmptyTexts.add(limCPUText);
//            Label limMemLabel = new Label(limitComposite, NONE);
//            limMemLabel.setText("limitMemory: ");
//            Text limMemText = new Text(limitComposite, SWT.BORDER);
//            limMemText.setText(resources.getLimitMemory());
//            limMemText.setLayoutData(gridDataFields);
//            limMemText.addModifyListener(e -> {
//                resources.setLimitMemory(limMemText.getText());
//                validate();
//            });
//            notEmptyTexts.add(limMemText);
//
//            Button reservationCheck = new Button(resourceGroup, SWT.CHECK);
//            reservationCheck.setText("Set container resource reservations (this will set limits as well)");
//            reservationCheck.setLayoutData(gridDataChecks);
//            // create invisible Reservation Composite in each resource composite
//            Composite reservationComposite = new Composite(resourceGroup, NONE);
//            reservationComposite.setLayout(new GridLayout(2, false));
//            GridData reservationData = new GridData(SWT.FILL, SWT.FILL, true, true);
//            reservationData.exclude = true;
//            reservationComposite.setLayoutData(reservationData);
//            Label resCPULabel = new Label(reservationComposite, NONE);
//            resCPULabel.setText("reservationCPU: ");
//            Text resCPUText = new Text(reservationComposite, SWT.BORDER);
//            resCPUText.setText(resources.getReservationCPU());
//            resCPUText.setLayoutData(gridDataFields);
//            resCPUText.addModifyListener(e -> {
//                resources.setReservationCPU(resCPUText.getText());
//                validate();
//            });
//            notEmptyTexts.add(resCPUText);
//            Label resMemLabel = new Label(reservationComposite, NONE);
//            resMemLabel.setText("reservationMemory: ");
//            Text resMemText = new Text(reservationComposite, SWT.BORDER);
//            resMemText.setText(resources.getReservationMemory());
//            resMemText.setLayoutData(gridDataFields);
//            resMemText.addModifyListener(e -> {
//                resources.setReservationMemory(resMemText.getText());
//                validate();
//            });
//            notEmptyTexts.add(resMemText);
//
//            limitCheck.addSelectionListener(new SelectionAdapter() {
//                @Override
//                public void widgetSelected(SelectionEvent e) {
//                    // set the textfields visible, resize window
//                    boolean useLimits = limitCheck.getSelection();
//                    limitData.exclude = !useLimits;
//                    limitComposite.setVisible(useLimits);
//                    main.setSize(main.computeSize(pageWidth, SWT.DEFAULT));
//                    ((ScrolledComposite) getControl()).setMinSize(main.computeSize(pageWidth, SWT.DEFAULT));
//                    if (useLimits) {
//                        Resources newResources = container.getResources();
//                        if (newResources == null) {
//                            newResources = TyphonDLFactory.eINSTANCE.createResources();
//                        }
//                        newResources.setLimitCPU(limCPUText.getText());
//                        newResources.setLimitMemory(limMemText.getText());
//                        // TODO check if necessary:
//                        container.setResources(newResources);
//                        validate();
//                    } else {
//                        reservationCheck.setSelection(false);
//                        reservationCheck.notifyListeners(13, new Event());
//                        container.setResources(null);
//                    }
//                }
//            });
//            reservationCheck.addSelectionListener(new SelectionAdapter() {
//                @Override
//                public void widgetSelected(SelectionEvent e) {
//                    // set the textfields visible, resize window
//                    boolean useReservations = reservationCheck.getSelection();
//                    reservationData.exclude = !useReservations;
//                    reservationComposite.setVisible(useReservations);
//                    main.setSize(main.computeSize(pageWidth, SWT.DEFAULT));
//                    ((ScrolledComposite) getControl()).setMinSize(main.computeSize(pageWidth, SWT.DEFAULT));
//                    if (useReservations) {
//                        limitCheck.setSelection(true);
//                        limitCheck.notifyListeners(13, new Event());
//                        // TODO check if this can be null:
//                        Resources newResources = container.getResources();
//                        newResources.setReservationCPU(resCPUText.getText());
//                        newResources.setReservationMemory(resMemText.getText());
//                        container.setResources(newResources);
//                        validate();
//                    } else {
//                        Resources newResources = container.getResources();
//                        if (newResources != null) {
//                            newResources.setReservationCPU(null);
//                            newResources.setReservationMemory(null);
//                            container.setResources(newResources);
//                        }
//                    }
//                }
//            });
//        }
//    }

//    private void validate() {
//        Status status = null;
//        for (Text text : notEmptyTexts) {
//            if (text.isVisible() && text.getText().isEmpty()) {
//                status = new Status(IStatus.ERROR, "Wizard", "Textfields can't be empty");
//            }
//        }
//        setStatus(status);
//        // TODO validation resource syntax
//    }

    private Resources createDefaultResources() {
        Resources resources = TyphonDLFactory.eINSTANCE.createResources();
        resources.setLimitCPU("0.5");
        resources.setLimitMemory("512M");
        resources.setReservationCPU("0.25");
        resources.setReservationMemory("256M");
        return resources;
    }

    /**
     * The image group, here the image can be edited
     * 
     * @param main Composite to put the group in
     */
//    private void imageArea() {
//        if (!db.isExternal()) {
//            if (imageGroup == null) {
//                imageGroup = new Group(main, SWT.READ_ONLY);
//                imageGroup.setLayout(new GridLayout(2, false));
//                imageGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//                imageGroup.setText("Image");
//            }
//            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
//            new Label(imageGroup, NONE).setText("image used:");
//            imageText = new Text(imageGroup, SWT.BORDER);
//            String imageTextValue = getImageValue();
//            imageText.setText(imageTextValue);
//            imageText.setLayoutData(gridDataFields);
//            imageText.addModifyListener(e -> {
//                if (imageText.getText().equalsIgnoreCase(db.getType().getImage().getValue())) {
//                    db.setImage(null);
//                } else {
//                    IMAGE image = TyphonDLFactory.eINSTANCE.createIMAGE();
//                    image.setValue(imageText.getText());
//                    db.setImage(image);
//                }
//            });
//        }
//    }

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
     * Updates the database page. <br>
     * After something has changed on the DBMS page, the widgets on the database
     * page have to be updated.
     */
    public void updateAllAreas() {
        setDescription("Database Type: " + db.getType().getName());
        for (Area area : areas) {
            area.updateArea(db, container, chosenTechnology);
        }
        ((Composite) this.getControl()).layout();
    }

    public int getChosenTechnology() {
        return this.chosenTechnology;
    }

    public void setChosenTechnology(int chosenTechnology) {
        this.chosenTechnology = chosenTechnology;
    }

    public void setDB(DB db) {
        this.db = db;
    }

    public Pair<DB, Container> getResultPair() {
        return new Pair<DB, Container>(db, container);
    }

}
