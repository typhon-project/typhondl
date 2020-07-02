package de.atb.typhondl.xtext.ui.creationWizard;

/*-
 * #%L
 * de.atb.typhondl.xtext.ui
 * %%
 * Copyright (C) 2018 - 2020 ATB
 * %%
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * #L%
 */

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

    private static final String ANALYTICS_KAFKA_URI = "analytics.kafka.uri";
    /**
     * The polystore.properties
     */
    private Properties properties;
    private int chosenTemplate;
    private Composite main;
    private Text kafkaURIText;

    /**
     * Creates an instance of the {@link CreationAnalyticsPage}
     * 
     * @param pageName   the name of the page
     * @param properties
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
        public List<InputField> dockerComposeFields = Arrays
                .asList(new InputField("Kafka version: ", "analytics.kafka.version"));
        public List<InputField> kubernetesFields = Arrays.asList(
                new InputField("Flink jobmanager heap size: ", "analytics.flink.jobmanager.heap.size"),
                new InputField("Flink taskmanager memory process size: ",
                        "analytics.flink.taskmanager.memory.process.size"),
                new InputField("Logglevel rootlogger: ", "analytics.logging.rootlogger"),
                new InputField("Logglevel akka: ", "analytics.logging.akka"),
                new InputField("Logglevel kafka: ", "analytics.logging.kafka"),
                new InputField("Logglevel hadoop: ", "analytics.logging.hadoop"),
                new InputField("Logglevel zookeeper: ", "analytics.logging.zookeeper"),
                new InputField("Logglevel flink: ", "analytics.logging.flink"),
                new InputField("Flink jobmanager rest nodeport: ", "analytics.flink.rest.port"),
                new InputField("Flink taskmanager replicas: ", "analytics.flink.taskmanager.replicas"),
                new InputField("Kafka replicas: ", "analytics.kafka.cluster.replicas"),
                new InputField("Kafka version: ", "analytics.kafka.version"),
                new InputField("Kafka storage claim: ", "analytics.kafka.storageclaim"),
                new InputField("zookeeper storage claim: ", "analytics.zookeeper.storageclaim"));
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
        kafkaURIText.setText(properties.getProperty(ANALYTICS_KAFKA_URI));
        kafkaURIText.setLayoutData(gridData);
        kafkaURIText.addModifyListener(e -> {
            properties.setProperty(ANALYTICS_KAFKA_URI, kafkaURIText.getText());
        });

        for (InputField inputField : getInputFields()) {
            new Label(main, NONE).setText(inputField.label);
            Text text = new Text(main, SWT.BORDER);
            text.setText(properties.getProperty(inputField.propertyName));
            text.setLayoutData(gridData);
            text.addModifyListener(e -> {
                properties.setProperty(inputField.propertyName, text.getText());
            });
        }
    }

    private List<InputField> getInputFields() {
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
        return fields;
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
        kafkaURIText.setText(properties.getProperty(ANALYTICS_KAFKA_URI));
    }

}
