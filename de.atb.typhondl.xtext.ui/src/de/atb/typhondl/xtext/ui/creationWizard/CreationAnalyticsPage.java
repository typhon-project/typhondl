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
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

/**
 * Optional page for the TyphonDL {@link CreateModelWizard}. The properties for
 * the TyphonDL Analytics component, i.e. Kafka and Zookeeper properties can be
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
    private SupportedTechnologies chosenTechnology;
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
    protected CreationAnalyticsPage(String pageName, Properties properties, SupportedTechnologies chosenTechnology) {
        super(pageName);
        this.properties = properties;
        this.chosenTechnology = chosenTechnology;
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

        if (chosenTechnology.canUseKubeConfig()) {
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

        for (InputField inputField : chosenTechnology.kafkaInputFields()) {
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
            for (InputField inputField : evEditor.getInputFields()) {
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
            if (!ContainerService.isPortValidRange(port.getText(), chosenTechnology)) {
                this.setStatus(new Status(IStatus.ERROR, "Change clusterType",
                        "Choose a port between " + chosenTechnology.minPort() + " and " + chosenTechnology.maxPort()));
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
        if (chosenTechnology.canUseKubeConfig()) {
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
