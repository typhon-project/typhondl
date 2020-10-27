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
import de.atb.typhondl.xtext.typhonDL.Modes;
import de.atb.typhondl.xtext.typhonDL.Replication;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.modelUtils.ReplicationService;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

/**
 * WizardPage {@link Area} to configure (stateful) replication of databases
 * 
 * @author flug
 *
 */
public class ReplicaArea extends Area {

    private String replicationProperty;

    /**
     * WizardPage {@link Area} to configure (stateful) replication of databases
     * 
     * @param db               database from DatabasePage
     * @param container        the container in which the database shall run
     * @param chosenTechnology the clusterType
     * @param parent           the main control
     * @param properties       properties to extract possible replication method
     *                         from
     */
    public ReplicaArea(DB db, Container container, SupportedTechnologies chosenTechnology, Composite parent,
            Properties properties) {
        super(db, container, parent, "Replication", properties, chosenTechnology, null);
    }

    @Override
    public void createArea() {
        this.replicationProperty = ReplicationService.getReplicationProperty(db.getType().getName().toLowerCase(),
                chosenTechnology, properties);
        if (!db.isExternal() && !replicationProperty.isEmpty() && db.getHelm() == null) {

            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            GridData gridDataChecks = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            gridDataChecks.horizontalSpan = 2;

            Button checkbox = new Button(group, SWT.CHECK);
            checkbox.setText(getCheckboxText());
            checkbox.setLayoutData(gridDataChecks);

            container.setReplication(null);
            Replication replication = ReplicationService.createDefaultReplication(replicationProperty);
            Composite hiddenComposite = new Composite(group, SWT.NONE);
            hiddenComposite.setLayout(new GridLayout(2, false));
            GridData hiddenData = new GridData(SWT.FILL, SWT.FILL, true, true);
            hiddenData.exclude = true;
            hiddenComposite.setLayoutData(hiddenData);

            new Label(hiddenComposite, SWT.NONE).setText("Number of total Replicas:");
            Text replicaText = new Text(hiddenComposite, SWT.BORDER);
            replicaText.setLayoutData(gridDataFields);
            replicaText.setText(Integer.toString(replication.getReplicas()));
            replicaText.addModifyListener(e -> {
                container.getReplication().setReplicas(getNumber(replicaText.getText()));
            });
            new Label(hiddenComposite, SWT.NONE).setText("Replication Mode:");
            // TODO have a combo here in case more modes are available
            new Label(hiddenComposite, SWT.NONE).setText(replication.getMode().getName());

            checkbox.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    // set the textfields visible, resize window
                    boolean useReplication = checkbox.getSelection();
                    hiddenData.exclude = !useReplication;
                    hiddenComposite.setVisible(useReplication);
                    parent.setSize(parent.computeSize(pageWidth, SWT.DEFAULT));
                    ((ScrolledComposite) parent.getParent()).setMinSize(parent.computeSize(pageWidth, SWT.DEFAULT));
                    if (useReplication) {
                        Replication newReplication = container.getReplication();
                        if (newReplication == null) {
                            newReplication = TyphonDLFactory.eINSTANCE.createReplication();
                        }
                        newReplication.setReplicas(getNumber(replicaText.getText()));
                        newReplication.setMode(Modes.getByName(replicationProperty));
                        container.setReplication(newReplication);
                    } else {
                        container.setReplication(null);
                    }
                }
            });

        }
    }

    private int getNumber(String string) {
        return string.isEmpty() ? 0 : Integer.parseInt(string);
    }

    private String getCheckboxText() {
        switch (replicationProperty) {
        case "replicaset":
            return "Create Replicas to serve as backup (Primary/Replica setup)";
        case "multi":
            return "Create a multi Primary setup for high availability";
        default:
            return "";
        }
    }

}
