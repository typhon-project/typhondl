package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.InputField;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class CreationPolystorePage extends MyWizardPage {

    private Properties properties;
    private Composite main;
    private SupportedTechnologies chosenTechnology;
    private static final int pageWidth = 607;

    protected CreationPolystorePage(String pageName, Properties properties, SupportedTechnologies chosenTechnology) {
        super(pageName);
        this.properties = properties;
        this.chosenTechnology = chosenTechnology;
    }

    @Override
    public void createControl(Composite parent) {
        setTitle("Configure Polystore Components");
        setDescription("API, QL");
        ScrolledComposite scrolling = new ScrolledComposite(parent, SWT.V_SCROLL);
        main = new Composite(scrolling, SWT.NONE);
        scrolling.setContent(main);
        scrolling.setExpandVertical(true);
        scrolling.setExpandHorizontal(true);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        main.setLayout(new GridLayout(1, false));

        ResourceEditor editor = new ResourceEditor();
        createQLGroup(editor.getResourceFieldsQL());
        createAPIGroup(editor.getResourceFieldsAPI());
        createUIGroup();

        main.setSize(main.computeSize(pageWidth, SWT.DEFAULT));
        scrolling.setMinSize(main.computeSize(pageWidth, SWT.DEFAULT));

        setControl(scrolling);

    }

    private void createUIGroup() {
        Group uiGroup = createGroup("UI settings");
        createPublishedPortFields(uiGroup, PropertiesService.UI_PUBLISHEDPORT);
    }

    private void createAPIGroup(List<InputField> list) {
        Group apiGroup = createGroup("API settings");
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        createScalingFields(apiGroup, "API", PropertiesService.API_REPLICAS);
        createPublishedPortFields(apiGroup, PropertiesService.API_PUBLISHEDPORT);
        for (InputField inputField : list) {
            createResourceFields(apiGroup, gridData, inputField);
        }
    }

    private void createPublishedPortFields(Group group, String property) {
        // TODO Auto-generated method stub

    }

    private void createQLGroup(List<InputField> list) {
        Group qlGroup = createGroup("QL Settings");
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        createScalingFields(qlGroup, "QL Server", PropertiesService.QLSERVER_REPLICAS);
        for (InputField inputField : list) {
            createResourceFields(qlGroup, gridData, inputField);
        }
    }

    private void createResourceFields(Group qlGroup, GridData gridData, InputField inputField) {
        // TODO put in hidden composite, activate with checkbox
        new Label(qlGroup, NONE).setText(inputField.label);
        Text text = new Text(qlGroup, SWT.BORDER);
        text.setText(properties.getProperty(inputField.propertyName));
        text.setLayoutData(gridData);
        text.addModifyListener(e -> {
            properties.setProperty(inputField.propertyName, text.getText());
        });
    }

    private Group createGroup(String name) {
        Group group = new Group(main, SWT.READ_ONLY);
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
        rep.addModifyListener(e -> properties.setProperty(property, rep.getText()));
    }

    private class ResourceEditor {
        public List<InputField> resourceFieldsQL = Arrays.asList(
                new InputField("limit.memory: ", PropertiesService.QLSERVER_LIMIT_MEMORY),
                new InputField("limit.cpu: ", PropertiesService.QLSERVER_LIMIT_CPU),
                new InputField("reservation.memory: ", PropertiesService.QLSERVER_RESERVATION_MEMORY),
                new InputField("reservation.cpu: ", PropertiesService.QLSERVER_RESERVATION_CPU));

        public List<InputField> resourceFieldsAPI = Arrays.asList(
                new InputField("limit.memory: ", PropertiesService.API_LIMIT_MEMORY),
                new InputField("limit.cpu: ", PropertiesService.API_LIMIT_CPU),
                new InputField("reservation.memory: ", PropertiesService.API_RESERVATION_MEMORY),
                new InputField("reservation.cpu: ", PropertiesService.API_RESERVATION_CPU));

        public List<InputField> getResourceFieldsQL() {
            return resourceFieldsQL;
        }

        public List<InputField> getResourceFieldsAPI() {
            return resourceFieldsAPI;
        }
    }
}
