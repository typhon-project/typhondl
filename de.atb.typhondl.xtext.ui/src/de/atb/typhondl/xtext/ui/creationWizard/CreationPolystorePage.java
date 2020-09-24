package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.Resources;
import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.InputField;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class CreationPolystorePage extends MyWizardPage {

    private Properties properties;
    private Composite main;
    private HashMap<Text, String> resourceFields;
    private HashMap<Text, String> scalingFields;
    private HashMap<Text, String> portFields;
    private SupportedTechnologies chosenTechnology;

    private static final int pageWidth = 607;

    protected CreationPolystorePage(String pageName, Properties properties, SupportedTechnologies chosenTechnology) {
        super(pageName);
        this.properties = properties;
        this.resourceFields = new HashMap<>();
        this.scalingFields = new HashMap<>();
        this.portFields = new HashMap<>();
        this.chosenTechnology = chosenTechnology;
    }

    @Override
    public void createControl(Composite parent) {
        setTitle("Configure Polystore Components");
        setDescription("API, UI, QL Server");
        ScrolledComposite scrolling = new ScrolledComposite(parent, SWT.V_SCROLL);
        main = new Composite(scrolling, SWT.NONE);
        scrolling.setContent(main);
        scrolling.setExpandVertical(true);
        scrolling.setExpandHorizontal(true);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        main.setLayout(new GridLayout(1, false));

        ResourceEditor editor = new ResourceEditor();
        createQLGroup(editor);
        createAPIGroup(editor);
        createUIGroup();

        main.setSize(main.computeSize(pageWidth, SWT.DEFAULT));
        scrolling.setMinSize(main.computeSize(pageWidth, SWT.DEFAULT));

        setControl(scrolling);

    }

    private void createUIGroup() {
        Group uiGroup = createGroup("UI settings", main);
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        createPortFields(uiGroup, gridData, new InputField("Published port: ", PropertiesService.UI_PUBLISHEDPORT));
    }

    private void createPortFields(Group group, GridData gridData, InputField inputField) {
        Text text = createFields(group, gridData, inputField);
        portFields.put(text, inputField.propertyName);
    }

    private void createAPIGroup(ResourceEditor editor) {
        Group apiGroup = createGroup("API settings", main);
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        createScalingFields(apiGroup, "API", PropertiesService.API_REPLICAS);
        createPortFields(apiGroup, gridData, new InputField("Published port: ", PropertiesService.API_PUBLISHEDPORT));
        Group resourceGroup = createGroupInGroup("Resources (can be left empty)", apiGroup);
        createResourceFields(resourceGroup, gridData, editor.resourceFieldsLimitAPI,
                editor.resourceFieldsReservationAPI);
    }

    private void createResourceFields(Group resourceGroup, GridData gridData, List<InputField> resourceFieldsLimitAPI,
            List<InputField> resourceFieldsReservationAPI) {
        GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        GridData gridDataChecks = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gridDataChecks.horizontalSpan = 2;

        Button limitCheck = new Button(resourceGroup, SWT.CHECK);
        limitCheck.setText("Set container resource limits");
        limitCheck.setLayoutData(gridDataChecks);
        // create invisible Limit Composite in each resource composite
        Composite limitComposite = new Composite(resourceGroup, SWT.NONE);
        limitComposite.setLayout(new GridLayout(2, false));
        GridData limitData = new GridData(SWT.FILL, SWT.FILL, true, true);
        limitData.exclude = true;
        limitComposite.setLayoutData(limitData);
        for (InputField inputField : resourceFieldsLimitAPI) {
            Text text = createFields(limitComposite, gridDataFields, inputField);
            resourceFields.put(text, inputField.propertyName);
        }

        Button reservationCheck = new Button(resourceGroup, SWT.CHECK);
        reservationCheck.setText("Set container resource reservations (this will set limits as well)");
        reservationCheck.setLayoutData(gridDataChecks);
        // create invisible Reservation Composite in each resource composite
        Composite reservationComposite = new Composite(resourceGroup, SWT.NONE);
        reservationComposite.setLayout(new GridLayout(2, false));
        GridData reservationData = new GridData(SWT.FILL, SWT.FILL, true, true);
        reservationData.exclude = true;
        reservationComposite.setLayoutData(reservationData);
        for (InputField inputField : resourceFieldsReservationAPI) {
            Text text = createFields(reservationComposite, gridDataFields, inputField);
            resourceFields.put(text, inputField.propertyName);
        }

        limitCheck.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // set the textfields visible, resize window
                boolean useLimits = limitCheck.getSelection();
                limitData.exclude = !useLimits;
                limitComposite.setVisible(useLimits);
                if (!useLimits) {
                    container.setResources(null);
                }
            }
        });
        reservationCheck.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // set the textfields visible, resize window
                boolean useReservations = reservationCheck.getSelection();
                reservationData.exclude = !useReservations;
                reservationComposite.setVisible(useReservations);
                parent.setSize(parent.computeSize(pageWidth, SWT.DEFAULT));
                ((ScrolledComposite) parent.getParent()).setMinSize(parent.computeSize(pageWidth, SWT.DEFAULT));
                if (useReservations) {
                    limitCheck.setSelection(true);
                    limitCheck.notifyListeners(13, new Event());
                    // TODO check if this can be null:
                    Resources newResources = container.getResources();
                    newResources.setReservationCPU(resCPUText.getText());
                    newResources.setReservationMemory(resMemText.getText());
                    container.setResources(newResources);
                } else {
                    Resources newResources = container.getResources();
                    if (newResources != null) {
                        newResources.setReservationCPU(null);
                        newResources.setReservationMemory(null);
                        container.setResources(newResources);
                    }
                }
            }
        });

    }

    private void createQLGroup(ResourceEditor editor) {
        Group qlGroup = createGroup("QL Settings", main);
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        createScalingFields(qlGroup, "QL Server", PropertiesService.QLSERVER_REPLICAS);
        Group resourceGroup = createGroupInGroup("Resources (can be left empty)", qlGroup);
        createResourceFields(resourceGroup, gridData, editor.resourceFieldsLimitQL, editor.resourceFieldsReservationQL);
    }

//    private void createResourceFields(Group resourceGroup, GridData gridData, InputField inputField) {
//        Text text = createFields(resourceGroup, gridData, inputField);
//        this.resourceFields.put(text, inputField.propertyName);
//    }

    private Text createFields(Composite limitComposite, GridData gridData, InputField inputField) {
        // TODO put in hidden composite, activate with checkbox
        new Label(limitComposite, NONE).setText(inputField.label);
        Text text = new Text(limitComposite, SWT.BORDER);
        text.setText(properties.getProperty(inputField.propertyName));
        text.setLayoutData(gridData);
        text.addModifyListener(e -> {
            properties.setProperty(inputField.propertyName, text.getText());
            validate();
        });
        return text;
    }

    private Group createGroupInGroup(String name, Group parent) {
        Group group = createGroup(name, parent);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        group.setLayoutData(gridData);
        return group;
    }

    private Group createGroup(String name, Composite parent) {
        Group group = new Group(parent, SWT.READ_ONLY);
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        group.setText(name);
        return group;
    }

    private void createScalingFields(Group group, String component, String property) {
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        new Label(group, SWT.NONE).setText(component + " replicas: ");
        Text rep = new Text(group, SWT.BORDER);
        rep.setText("1");
        rep.setLayoutData(gridData);
        rep.addModifyListener(e -> {
            properties.setProperty(property, rep.getText());
            validate();
        });
        this.scalingFields.put(rep, property);
    }

    private void validate() {
        setStatus(null);
        if (this.chosenTechnology == SupportedTechnologies.DockerCompose) {
            for (Text text : resourceFields.keySet()) {
                if (!text.getText().isEmpty()) {
                    raiseWarning("resources");
                }
            }
        }
        for (Text text : scalingFields.keySet()) {
            int parseInt = 0;
            try {
                parseInt = Integer.parseInt(text.getText());
            } catch (NumberFormatException exp) {
                raiseError("replication");
            }
            if (this.chosenTechnology == SupportedTechnologies.DockerCompose && parseInt != 1) {
                raiseWarning("replication");
            }
            if (parseInt == 0) {
                raiseError("replication");
            }
        }
        if (this.chosenTechnology == SupportedTechnologies.Kubernetes) {
            for (Text text : portFields.keySet()) {
                if (!ContainerService.isPortInKubernetesRange(text.getText())) {
                    setStatus(new Status(IStatus.ERROR, "Wizard", "Choose a port between 30000 and 32767"));
                }
            }
        }
    }

    private void raiseError(String context) {
        setStatus(new Status(IStatus.ERROR, "Wizard", "Please set a value bigger than 0 as " + context));

    }

    private void raiseWarning(String context) {
        setStatus(new Status(IStatus.WARNING, "Wizard",
                "Setting " + context + " is only possible when using Docker Swarm"));
    }

    private class ResourceEditor {
        public List<InputField> resourceFieldsLimitQL = Arrays.asList(
                new InputField("limit.memory: ", PropertiesService.QLSERVER_LIMIT_MEMORY),
                new InputField("limit.cpu: ", PropertiesService.QLSERVER_LIMIT_CPU));
        public List<InputField> resourceFieldsReservationQL = Arrays.asList(
                new InputField("reservation.memory: ", PropertiesService.QLSERVER_RESERVATION_MEMORY),
                new InputField("reservation.cpu: ", PropertiesService.QLSERVER_RESERVATION_CPU));

        public List<InputField> resourceFieldsLimitAPI = Arrays.asList(
                new InputField("limit.memory: ", PropertiesService.API_LIMIT_MEMORY),
                new InputField("limit.cpu: ", PropertiesService.API_LIMIT_CPU));
        public List<InputField> resourceFieldsReservationAPI = Arrays.asList(
                new InputField("reservation.memory: ", PropertiesService.API_RESERVATION_MEMORY),
                new InputField("reservation.cpu: ", PropertiesService.API_RESERVATION_CPU));

        public List<InputField> getResourceFieldsLimitQL() {
            return resourceFieldsLimitQL;
        }

        public List<InputField> getResourceFieldsLimitAPI() {
            return resourceFieldsLimitAPI;
        }

        public List<InputField> getResourceFieldsReservationQL() {
            return resourceFieldsReservationQL;
        }

        public List<InputField> getResourceFieldsReservationAPI() {
            return resourceFieldsReservationAPI;
        }
    }

    public void updateData(Properties properties, SupportedTechnologies chosenTemplate) {
        this.properties = properties;
        this.chosenTechnology = chosenTemplate;
        if (chosenTechnology == SupportedTechnologies.Kubernetes) {
            setStatus(null);
        }
        for (Text text : this.resourceFields.keySet()) {
            text.setText(properties.getProperty(this.resourceFields.get(text)));
        }
        for (Text text : this.portFields.keySet()) {
            text.setText(properties.getProperty(this.portFields.get(text)));
        }
    }
}
