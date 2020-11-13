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

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.InputField;

/**
 * Page of TyphonDL {@link CreateModelWizard}. Offers possibility to adjust the
 * NLAE component
 * 
 * @author flug
 *
 */
public class CreationNLAEPage extends WizardPage {

    private Properties properties;
    private Composite main;

    public CreationNLAEPage(String pageName, Properties properties) {
        super(pageName);
        this.properties = properties;
    }

    /**
     * NLAE input fields
     * 
     * @author flug
     *
     */
    public class NLAEConfigEditor {
        public List<InputField> fields = Arrays.asList(
                new InputField("NLAE API host: ", PropertiesService.NLAE_API_HOST),
                new InputField("NLAE API port: ", PropertiesService.NLAE_API_PORT),
                new InputField("Flink jobmanager heapsize: ", PropertiesService.NLAE_JOBMANAGER_HEAPSIZE),
                new InputField("Flink taskmanager heapsize: ", PropertiesService.NLAE_TASKMANAGER_HEAPSIZE),
                new InputField("Flink taskmanager replicas: ", PropertiesService.NLAE_TASKMANAGER_REPLICAS),
                new InputField("Flink taskmanager number of task slots: ", PropertiesService.NLAE_TASKMANAGER_SLOTS),
                new InputField("Flink parallelism default: ", PropertiesService.NLAE_PARALLELISM),
                new InputField("Shared volume for models: ", PropertiesService.NLAE_SHAREDVOLUME));
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
        Label info = new Label(main, SWT.NONE);
        info.setText("Enter the address to your docker swarm cluster as NLAE API host.");
        GridData wideData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        wideData.horizontalSpan = 2;
        info.setLayoutData(wideData);
        for (InputField inputField : new NLAEConfigEditor().fields) {
            new Label(main, NONE).setText(inputField.label);
            Text text = new Text(main, SWT.BORDER);
            text.setText(properties.getProperty(inputField.propertyName));
            text.setLayoutData(gridData);
            text.addModifyListener(e -> {
                properties.setProperty(inputField.propertyName, text.getText());
            });
        }
    }

    public void updateData(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

}
