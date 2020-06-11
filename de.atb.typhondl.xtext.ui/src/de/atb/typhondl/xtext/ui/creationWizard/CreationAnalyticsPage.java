package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

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
    private int chosenTemplate;
    private Composite main;

    /**
     * Creates an instance of the {@link CreationAnalyticsPage}
     * 
     * @param pageName   the name of the page
     * @param properties the {@link Properties} extracted from the
     *                   polystore.properties file
     */
    protected CreationAnalyticsPage(String pageName, Properties properties, int chosenTemplate) {
        super(pageName);
        this.properties = properties;
        this.chosenTemplate = chosenTemplate;
    }

    /**
     * Helper class for the {@link KafkaConfigEditor}
     * 
     * @author flug
     *
     */
    public class InputField {

        public final String label;
        public final String propertyName;

        /**
         * Creates an InputField.
         * 
         * @param label        The displayed label
         * @param propertyName The name of the property for saving the input as
         *                     {@link Properties}
         */
        public InputField(String label, String propertyName) {
            this.label = label;
            this.propertyName = propertyName;
        }
    }

    /**
     * Helper class for creating input fields.
     * 
     * @author flug
     *
     */
    public class KafkaConfigEditor {
        public List<InputField> dockerComposeFields = Arrays.asList(
                new InputField("Zookeeper Port: ", "analytics.zookeeper.publishedPort"),
                new InputField("Kafka Port: ", "analytics.kafka.publishedPort"),
                new InputField("Kafka Listeners: ", "analytics.kafka.listeners"),
                new InputField("Kafka Inter Broker Listener Name: ", "analytics.kafka.listenerName"));
        public List<InputField> kubernetesFields = Arrays.asList(
                new InputField("Flink jobmanager heap size: ", "analytics.flink.jobmanager.heap.size"),
                new InputField("Flink taskmanager memory process size: ",
                        "analytics.flink.taskmanager.memory.process.size"),
                new InputField("Logglevel rootlogger: ", "analytics.logging.rootlogger"),
                new InputField("Logging root target: ", "analytics.logging.rootlogger.target"),
                new InputField("Logglevel akka: ", "analytics.logging.akka"),
                new InputField("Logglevel kafka: ", "analytics.logging.kafka"),
                new InputField("Logglevel hadoop: ", "analytics.logging.hadoop"),
                new InputField("Logglevel zookeeper: ", "analytics.logging.zookeeper"),
                new InputField("Logglevel flink: ", "analytics.logging.flink"),
                new InputField("Logging flink target: ", "analytics.logging.fling.target"),
                new InputField("Flink jobmanager rest nodeport: ", "analytics.flink.rest.port"),
                new InputField("Flink taskmanager replicas: ", "analytics.flink.taskmanager.replicas"),
                new InputField("Kafka replicas: ", "analytics.kafka.cluster.replicas"),
                new InputField("Kafka version: ", "analytics.kafka.version"),
                new InputField("Kafka nodeport: ", "analytics.kafka.port"));
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
        List<InputField> fields;
        switch (SupportedTechnologies.values()[chosenTemplate].getClusterType()) {
        case "DockerCompose":
            fields = new KafkaConfigEditor().dockerComposeFields;
            break;
        case "Kubernetes":
            fields = new KafkaConfigEditor().kubernetesFields;
            break;
        default:
            fields = new ArrayList<>();
            break;
        }
        for (InputField inputField : fields) {
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

    public void updateFields(int chosenTechnology) {
        createFields();
    }

}
