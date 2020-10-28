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
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xtext.EcoreUtil2;

import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Ports;
import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.modelUtils.ModelService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class ClusterTypeDialog extends StatusDialog {

    private Properties properties;
    private DeploymentModel model;
    private ClusterType oldClusterType;
    private ClusterType newClusterType;
    private ArrayList<Text> portList;

    public ClusterTypeDialog(Shell parent, Properties properties, DeploymentModel model, ClusterType clusterType) {
        super(parent);
        this.setTitle("Change clustertype");
        this.properties = properties;
        this.model = model;
        this.oldClusterType = clusterType;
        this.newClusterType = EcoreUtil.copy(oldClusterType);
        this.portList = new ArrayList<>();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite main = new Composite(parent, parent.getStyle());
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        main.setLayout(new GridLayout(2, false));
        GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, true);

        new Label(main, SWT.NONE).setText("clustertype: ");
        Combo typeCombo = new Combo(main, SWT.READ_ONLY);
        List<String> itemList = new ArrayList<String>();
        for (SupportedTechnologies tech : SupportedTechnologies.values()) {
            itemList.add(tech.displayedName());
            // templateCombo.setItem(tech.ordinal(), tech.getDisplayedName()); somehow
            // doesn't work
        }
        typeCombo.setItems(itemList.toArray(new String[0]));
        typeCombo.setText(typeCombo.getItem(ModelService.getSupportedTechnology(oldClusterType).ordinal()));
        typeCombo.addModifyListener(e -> {
            SupportedTechnologies chosenTechnology = SupportedTechnologies.values()[typeCombo.getSelectionIndex()];
            newClusterType.setName(chosenTechnology.name());
            for (Text text : portList) {
                if (!ContainerService.isPortValidRange(text.getText(), chosenTechnology)) {
                    text.setText(ContainerService.createRandomPort(chosenTechnology));
                }
            }
            validatePorts();
        });

        new Label(main, SWT.NONE).setText("UI published port: ");
        Text uiPort = new Text(main, SWT.BORDER);
        uiPort.setText(properties.getProperty(PropertiesService.UI_PUBLISHEDPORT));
        uiPort.setLayoutData(gridDataFields);
        portList.add(uiPort);
        uiPort.addModifyListener(e -> {
            properties.setProperty(PropertiesService.UI_PUBLISHEDPORT, uiPort.getText());
            validatePorts();
        });

        new Label(main, SWT.NONE).setText("API published port: ");
        Text apiPortText = new Text(main, SWT.BORDER);
        apiPortText.setText(properties.getProperty(PropertiesService.API_PUBLISHEDPORT));
        apiPortText.setLayoutData(gridDataFields);
        portList.add(apiPortText);
        apiPortText.addModifyListener(e -> {
            properties.setProperty(PropertiesService.API_PUBLISHEDPORT, apiPortText.getText());
            validatePorts();
        });

        EcoreUtil2.getAllContentsOfType(model, Ports.class).stream().flatMap(ports -> ports.getKey_values().stream())
                .filter(port -> port.getName().equalsIgnoreCase("published")).forEach(port -> {
                    new Label(main, SWT.NONE)
                            .setText(((Container) port.eContainer().eContainer()).getName() + " published port: ");
                    Text text = new Text(main, SWT.BORDER);
                    text.setText(port.getValue());
                    text.setLayoutData(gridDataFields);
                    portList.add(text);
                    text.addModifyListener(e -> {
                        port.setValue(text.getText());
                        validatePorts();
                    });
                });
        validatePorts();
        return main;
    }

    private void validatePorts() {
        this.updateStatus(new Status(IStatus.OK, "Change clusterType", ""));
        for (Text port : portList) {
            SupportedTechnologies chosenTechnology = ModelService.getSupportedTechnology(newClusterType);
            if (!ContainerService.isPortValidRange(port.getText(), chosenTechnology)) {
                this.updateStatus(new Status(IStatus.ERROR, "Wizard",
                        "Choose a port between " + chosenTechnology.minPort() + " and " + chosenTechnology.maxPort()));
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public ClusterType getClusterType() {
        return newClusterType;
    }

}
