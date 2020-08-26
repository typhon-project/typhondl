package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.ui.properties.PropertiesService;
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
    }

    private void updateKafkaURI() {
        kafkaURIText.setText(properties.getProperty(PropertiesService.ANALYTICS_KAFKA_URI));
    }

}
