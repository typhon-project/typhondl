package de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas;

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

import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Ports;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

public class PortArea extends Area {

    public PortArea(DB db, Container container, int chosenTechnology, Composite parent, Properties properties) {
        super(db, container, chosenTechnology, parent, "Ports", properties);
    }

    @Override
    public void createArea() {
        if (!db.isExternal() && db.getHelm() == null) {
            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            GridData gridDataChecks = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            gridDataChecks.horizontalSpan = 2;

            Button checkbox = new Button(group, SWT.CHECK);
            checkbox.setText("Publish database to be reachable outside the Polystore Network");
            checkbox.setLayoutData(gridDataChecks);

            Ports ports = TyphonDLFactory.eINSTANCE.createPorts();
            Key_Values publishedPort = TyphonDLFactory.eINSTANCE.createKey_Values();
            publishedPort.setName("published");
            publishedPort.setValue(createRandomPort());
            Key_Values targetPort = TyphonDLFactory.eINSTANCE.createKey_Values();
            targetPort.setName("target");
            targetPort.setValue(properties.getProperty(db.getType().getName().toLowerCase() + ".port"));
            ports.getKey_values().add(targetPort);
            ports.getKey_values().add(publishedPort);

            Composite hiddenComposite = new Composite(group, SWT.NONE);
            hiddenComposite.setLayout(new GridLayout(2, false));
            GridData hiddenData = new GridData(SWT.FILL, SWT.FILL, true, true);
            hiddenData.exclude = true;
            hiddenComposite.setLayoutData(hiddenData);

            new Label(hiddenComposite, SWT.NONE).setText("Container port: ");
            Text targetPortText = new Text(hiddenComposite, SWT.BORDER);
            targetPortText.setText(targetPort.getValue());
            targetPortText.setToolTipText("This is the port that will be exposed inside the network/cluster");
            targetPortText.setLayoutData(gridDataFields);
            targetPortText.addModifyListener(e -> {
                targetPort.setValue(targetPortText.getText());
            });
            new Label(hiddenComposite, SWT.NONE).setText("Published port: ");
            Text publishedPortText = new Text(hiddenComposite, SWT.BORDER);
            publishedPortText.setText(publishedPort.getValue());
            publishedPortText.setToolTipText("This is the port that will be exposed to the outside");
            publishedPortText.setLayoutData(gridDataFields);
            publishedPortText.addModifyListener(e -> {
                publishedPort.setValue(publishedPortText.getText());
            });

            checkbox.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    // set the textfields visible, resize window
                    boolean publishDatabase = checkbox.getSelection();
                    hiddenData.exclude = !publishDatabase;
                    hiddenComposite.setVisible(publishDatabase);
                    parent.setSize(parent.computeSize(pageWidth, SWT.DEFAULT));
                    ((ScrolledComposite) parent.getParent()).setMinSize(parent.computeSize(pageWidth, SWT.DEFAULT));
                    if (publishDatabase) {
                        Ports newPorts = container.getPorts();
                        if (newPorts == null) {
                            newPorts = ports;
                        }
                        setPort("target", newPorts, targetPortText.getText());
                        setPort("published", newPorts, targetPortText.getText());
                        container.setPorts(newPorts);
                    } else {
                        container.setPorts(null);
                    }
                }
            });
        }
    }

    protected void setPort(String nameOfPort, Ports newPorts, String valueToSet) {
        Key_Values portToSet = newPorts.getKey_values().stream()
                .filter(key -> key.getName().equalsIgnoreCase(nameOfPort)).findFirst().orElse(null);
        if (portToSet != null) {
            portToSet.setValue(valueToSet);
        }

    }

    private String createRandomPort() {
        return Integer.toString(ThreadLocalRandom.current().nextInt(32000, 36000));
    }

}
