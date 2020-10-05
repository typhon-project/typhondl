package de.atb.typhondl.xtext.ui.refactoring;

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
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.modelUtils.ModelService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.EvolutionConfigEditor;
import de.atb.typhondl.xtext.ui.utilities.InputField;
import de.atb.typhondl.xtext.ui.utilities.KafkaConfigEditor;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class ChangeAnalyticsDialog extends StatusDialog {

    private Properties properties;
    private Shell shell;
    private SupportedTechnologies clusterType;
    private boolean analyticsContained;
    private ArrayList<Text> portList;

    public ChangeAnalyticsDialog(Shell shell, Properties properties, SupportedTechnologies clusterType,
            boolean analyticsContained) {
        super(shell);
        this.setTitle("Change Analytics Component");
        this.properties = properties;
        this.shell = shell;
        this.clusterType = clusterType;
        this.analyticsContained = analyticsContained;
        this.portList = new ArrayList<>();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite main = new Composite(parent, parent.getStyle());
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        main.setLayout(new GridLayout(2, false));
        GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, true);

        new Label(main, SWT.NONE).setText("Kafka URI: ");
        Text kafkaURIText = new Text(main, SWT.BORDER);
        kafkaURIText.setText(getKafkaUri());
//        kafkaURIText.setLayoutData(gridDataFields);
        properties.setProperty(PropertiesService.ANALYTICS_KAFKA_URI, kafkaURIText.getText());
        kafkaURIText.addModifyListener(
                e -> properties.setProperty(PropertiesService.ANALYTICS_KAFKA_URI, kafkaURIText.getText()));
        KafkaConfigEditor editor = new KafkaConfigEditor();
        for (InputField inputField : editor.getInputFields(clusterType)) {
            new Label(main, SWT.NONE).setText(inputField.label);
            Text text = new Text(main, SWT.BORDER);
            text.setText(properties.getProperty(inputField.propertyName));
            text.setLayoutData(gridDataFields);
            text.addModifyListener(e -> properties.setProperty(inputField.propertyName, text.getText()));
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
            for (InputField inputField : evEditor.getInputFields(clusterType)) {
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
        return main;
    }

    private void validatePorts() {
        this.updateStatus(new Status(IStatus.OK, "Change clusterType", ""));
        for (Text port : portList) {
            if (clusterType == SupportedTechnologies.Kubernetes
                    && !ContainerService.isPortInKubernetesRange(port.getText())) {
                this.updateStatus(
                        new Status(IStatus.ERROR, "Change clusterType", "Choose port between 30000 and 32767"));
            }
        }
    }

    private String getKafkaUri() {
        if (analyticsContained) {
            return ModelService.getKafkaInternalURI(clusterType);
        } else {
            return properties.getProperty(PropertiesService.ANALYTICS_KAFKA_URI);
        }
    }

    public Properties getProperties() {
        return properties;
    }

}
