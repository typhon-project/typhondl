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
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.utilities.Pair;
import de.atb.typhondl.xtext.ui.utilities.PropertiesLoader;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;
import de.atb.typhondl.xtext.ui.wizardPageAreas.AddressArea;
import de.atb.typhondl.xtext.ui.wizardPageAreas.Area;
import de.atb.typhondl.xtext.ui.wizardPageAreas.HelmArea;
import de.atb.typhondl.xtext.ui.wizardPageAreas.ImageArea;
import de.atb.typhondl.xtext.ui.wizardPageAreas.PropertyArea;
import de.atb.typhondl.xtext.ui.wizardPageAreas.ResourceArea;

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
    private Composite main;

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
        main = new Composite(scrolling, SWT.NONE);
        scrolling.setContent(main);
        scrolling.setExpandVertical(true);
        scrolling.setExpandHorizontal(true);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        main.setLayout(new GridLayout(1, false));

        addAreasToList();

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

    private void addAreasToList() {
        if (!isInList(HelmArea.class)
                && SupportedTechnologies.values()[chosenTechnology].getClusterType().equalsIgnoreCase("Kubernetes")
                && db.getHelm() != null && !db.isExternal()) {
            areas.add(new HelmArea(db, container, chosenTechnology, main));
        }

        if (!isInList(ImageArea.class) && !db.isExternal()) {
            areas.add(new ImageArea(db, container, chosenTechnology, main));
        }

        if (!isInList(PropertyArea.class) && !db.getParameters().isEmpty()) {
            areas.add(new PropertyArea(db, container, chosenTechnology, main));
        }

        if (!isInList(AddressArea.class) && db.isExternal()) {
            areas.add(new AddressArea(db, container, chosenTechnology, main));
        }

        if (!isInList(ResourceArea.class) && !db.isExternal()) {
            areas.add(new ResourceArea(db, container, chosenTechnology, main));
        }

    }

    private <T> boolean isInList(Class<T> areaClass) {
        for (Area area : areas) {
            if (areaClass.isInstance(area)) {
                return true;
            }
        }
        return false;
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
        addAreasToList();
        for (Area area : areas) {
            area.updateArea();
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
