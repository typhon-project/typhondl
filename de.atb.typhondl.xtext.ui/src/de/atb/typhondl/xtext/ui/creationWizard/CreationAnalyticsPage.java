package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.EvolutionConfigEditor;
import de.atb.typhondl.xtext.ui.utilities.InputField;
import de.atb.typhondl.xtext.ui.utilities.KafkaConfigEditor;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

/**
 * Optional page for the TyphonDL {@link CreateModelWizard}. The properties for
 * the TyphonDL Analytics component, i.e. Kafka and Zookeepter properties can be
 * edited and are stored in the polystore.properties
 * 
 * @author flug
 *
 */
public class CreationAnalyticsPage extends MyWizardPage {

    /**
     * The polystore.properties
     */
    private Properties properties;
    private SupportedTechnologies chosenTemplate;
    private Composite main;
    private Text kafkaURIText;
    private GridData hiddenData;
    private Composite hidden;
    private ArrayList<Text> portList;

    /**
     * Creates an instance of the {@link CreationAnalyticsPage}
     * 
     * @param pageName   the name of the page
     * @param properties
     */
    protected CreationAnalyticsPage(String pageName, Properties properties, SupportedTechnologies chosenTemplate) {
        super(pageName);
        this.properties = properties;
        this.chosenTemplate = chosenTemplate;
        this.portList = new ArrayList<>();
    }

    @Override
    public void createControl(Composite parent) {

        setTitle("Configure Data Analytics");
        main = new Composite(parent, SWT.NONE);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        main.setLayout(new GridLayout(2, false));

        createFields();

        setControl(main);
    }

    private void createFields() {
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        new Label(main, NONE).setText("Kafka URI: ");
        kafkaURIText = new Text(main, SWT.BORDER);
        kafkaURIText.setText(properties.getProperty(PropertiesService.ANALYTICS_KAFKA_URI));
        kafkaURIText.setLayoutData(gridData);
        kafkaURIText.setEditable(false);

        if (chosenTemplate == SupportedTechnologies.Kubernetes) {
            hidden = new Composite(main, SWT.NONE);
            hidden.setLayout(new GridLayout(2, false));
            hiddenData = new GridData(SWT.FILL, SWT.FILL, true, false);
            hiddenData.horizontalSpan = 2;
            boolean contained = Boolean
                    .parseBoolean(properties.getProperty(PropertiesService.ANALYTICS_DEPLOYMENT_CONTAINED));
            hiddenData.exclude = contained;
            hidden.setVisible(!contained);
            hidden.setLayoutData(hiddenData);

            new Label(hidden, SWT.NONE).setText("Analytics Cluster kubeconfig: ");
            Text analyticsKubeconfigText = new Text(hidden, SWT.BORDER);
            GridData gridDataHidden = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            analyticsKubeconfigText.setLayoutData(gridDataHidden);
            analyticsKubeconfigText.setText(properties.getProperty(PropertiesService.ANALYTICS_KUBECONFIG));
            analyticsKubeconfigText.addModifyListener(e -> properties
                    .setProperty(PropertiesService.ANALYTICS_KUBECONFIG, analyticsKubeconfigText.getText()));
        }

        KafkaConfigEditor editor = new KafkaConfigEditor();
        for (InputField inputField : editor.getInputFields(chosenTemplate)) {
            new Label(main, NONE).setText(inputField.label);
            Text text = new Text(main, SWT.BORDER);
            text.setText(properties.getProperty(inputField.propertyName));
            text.setLayoutData(gridData);
            text.addModifyListener(e -> {
                properties.setProperty(inputField.propertyName, text.getText());
            });
        }
        if (properties.getProperty(PropertiesService.POLYSTORE_USEEVOLUTION).equals("true")) {
            Group evolution = new Group(main, SWT.READ_ONLY);
            evolution.setLayout(new GridLayout(2, false));
            GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
            GridData gridDataFields2 = new GridData(SWT.FILL, SWT.BEGINNING, true, true);
            layoutData.horizontalSpan = 2;
            evolution.setLayoutData(layoutData);
            evolution.setText("Evolution");
            EvolutionConfigEditor evEditor = new EvolutionConfigEditor();
            for (InputField inputField : evEditor.getInputFields(chosenTemplate)) {
                new Label(evolution, SWT.NONE).setText(inputField.label);
                Text text = new Text(evolution, SWT.BORDER);
                text.setText(properties.getProperty(inputField.propertyName));
                text.setLayoutData(gridDataFields2);
                portList.add(text);
                text.addModifyListener(e -> {
                    properties.setProperty(inputField.propertyName, text.getText());
                    validatePorts();
                });
            }
            validatePorts();
        }
    }

    private void validatePorts() {
        this.setStatus(null);
        for (Text port : portList) {
            if (chosenTemplate == SupportedTechnologies.Kubernetes
                    && !ContainerService.isPortInKubernetesRange(port.getText())) {
                this.setStatus(new Status(IStatus.ERROR, "Change clusterType", "Choose port between 30000 and 32767"));
            }
        }
    }

    /**
     * 
     * @return The {@link Properties} enriched with Typhon Analytics settings
     */
    public Properties getProperties() {
        return properties;
    }

    public void updateData(Properties properties) {
        this.properties = properties;
        updateKafkaURI();
        if (chosenTemplate == SupportedTechnologies.Kubernetes) {
            boolean contained = Boolean
                    .parseBoolean(properties.getProperty(PropertiesService.ANALYTICS_DEPLOYMENT_CONTAINED));
            hiddenData.exclude = contained;
            hidden.setVisible(!contained);
        }
        main.layout(true);
    }

    private void updateKafkaURI() {
        kafkaURIText.setText(properties.getProperty(PropertiesService.ANALYTICS_KAFKA_URI));
    }

}
