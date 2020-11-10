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
import de.atb.typhondl.xtext.typhonDL.Resources;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

/**
 * WizardPage {@link Area} to set the db's container's resource limits and
 * reservations
 * 
 * @author flug
 *
 */
public class ResourceArea extends Area {

    /**
     * WizardPage {@link Area} to set the db's container's resource limits and
     * reservations
     * 
     * @param db        database from DatabasePage
     * @param container the container in which the database shall run
     * @param parent    the main control
     */
    public ResourceArea(DB db, Container container, Composite parent) {
        super(db, container, parent, "Container Resources", null, null, null);
    }

    @Override
    public void createArea() {
        if (!db.isExternal() && db.getHelm() == null) {
            if (group == null) {
                createGroup("Container Resources");
            }
            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            GridData gridDataChecks = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            gridDataChecks.horizontalSpan = 2;

            container.setResources(null);
            Resources resources = createDefaultResources();

            Button limitCheck = new Button(group, SWT.CHECK);
            limitCheck.setText("Set container resource limits");
            limitCheck.setLayoutData(gridDataChecks);
            // create invisible Limit Composite in each resource composite
            Composite limitComposite = new Composite(group, SWT.NONE);
            limitComposite.setLayout(new GridLayout(2, false));
            GridData limitData = new GridData(SWT.FILL, SWT.FILL, true, true);
            limitData.exclude = true;
            limitComposite.setLayoutData(limitData);
            Label limCPULabel = new Label(limitComposite, SWT.NONE);
            limCPULabel.setText("limitCPU: ");
            Text limCPUText = new Text(limitComposite, SWT.BORDER);
            limCPUText.setText(resources.getLimitCPU());
            limCPUText.setLayoutData(gridDataFields);
            limCPUText.addModifyListener(e -> {
                String text = limCPUText.getText();
                if (text.isEmpty()) {
                    container.getResources().setLimitCPU(null);
                } else {
                    container.getResources().setLimitCPU(limCPUText.getText());
                }
            });
            Label limMemLabel = new Label(limitComposite, SWT.NONE);
            limMemLabel.setText("limitMemory: ");
            Text limMemText = new Text(limitComposite, SWT.BORDER);
            limMemText.setText(resources.getLimitMemory());
            limMemText.setLayoutData(gridDataFields);
            limMemText.addModifyListener(e -> {
                String text = limMemText.getText();
                if (text.isEmpty()) {
                    container.getResources().setLimitMemory(null);
                } else {
                    container.getResources().setLimitMemory(limMemText.getText());
                }
            });

            Button reservationCheck = new Button(group, SWT.CHECK);
            reservationCheck.setText("Set container resource reservations");
            reservationCheck.setLayoutData(gridDataChecks);
            // create invisible Reservation Composite in each resource composite
            Composite reservationComposite = new Composite(group, SWT.NONE);
            reservationComposite.setLayout(new GridLayout(2, false));
            GridData reservationData = new GridData(SWT.FILL, SWT.FILL, true, true);
            reservationData.exclude = true;
            reservationComposite.setLayoutData(reservationData);
            Label resCPULabel = new Label(reservationComposite, SWT.NONE);
            resCPULabel.setText("reservationCPU: ");
            Text resCPUText = new Text(reservationComposite, SWT.BORDER);
            resCPUText.setText(resources.getReservationCPU());
            resCPUText.setLayoutData(gridDataFields);
            resCPUText.addModifyListener(e -> {
                String text = resCPUText.getText();
                if (text.isEmpty()) {
                    container.getResources().setReservationCPU(null);
                } else {
                    container.getResources().setReservationCPU(resCPUText.getText());
                }
            });
            Label resMemLabel = new Label(reservationComposite, SWT.NONE);
            resMemLabel.setText("reservationMemory: ");
            Text resMemText = new Text(reservationComposite, SWT.BORDER);
            resMemText.setText(resources.getReservationMemory());
            resMemText.setLayoutData(gridDataFields);
            resMemText.addModifyListener(e -> {
                String text = resMemText.getText();
                if (text.isEmpty()) {
                    container.getResources().setReservationMemory(null);
                } else {
                    container.getResources().setReservationMemory(resMemText.getText());
                }
            });

            limitCheck.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    // set the textfields visible, resize window
                    boolean useLimits = limitCheck.getSelection();
                    limitData.exclude = !useLimits;
                    limitComposite.setVisible(useLimits);
                    parent.setSize(parent.computeSize(pageWidth, SWT.DEFAULT));
                    ((ScrolledComposite) parent.getParent()).setMinSize(parent.computeSize(pageWidth, SWT.DEFAULT));
                    Resources newResources = container.getResources();
                    if (newResources == null) {
                        newResources = TyphonDLFactory.eINSTANCE.createResources();
                    }
                    if (useLimits) {
                        newResources.setLimitCPU(limCPUText.getText());
                        newResources.setLimitMemory(limMemText.getText());
                        container.setResources(newResources);
                    } else {
                        newResources.setLimitCPU(null);
                        newResources.setLimitMemory(null);
                        if (newResources.getReservationCPU() == null && newResources.getReservationMemory() == null) {
                            container.setResources(null);
                        }
                    }

                }
            });
            reservationCheck.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    // set the textfields visible, resize window
                    boolean useReservations = reservationCheck.getSelection();
                    reservationData.exclude = !useReservations;
                    reservationComposite.setVisible(useReservations);
                    parent.setSize(parent.computeSize(pageWidth, SWT.DEFAULT));
                    ((ScrolledComposite) parent.getParent()).setMinSize(parent.computeSize(pageWidth, SWT.DEFAULT));
                    Resources newResources = container.getResources();
                    if (newResources == null) {
                        newResources = TyphonDLFactory.eINSTANCE.createResources();
                    }
                    if (useReservations) {
                        newResources.setReservationCPU(resCPUText.getText());
                        newResources.setReservationMemory(resMemText.getText());
                        container.setResources(newResources);
                    } else {
                        newResources.setReservationCPU(null);
                        newResources.setReservationMemory(null);
                        if (newResources.getLimitCPU() == null && newResources.getLimitMemory() == null) {
                            container.setResources(null);
                        }
                    }
                }
            });
        }
    }

    private Resources createDefaultResources() {
        Resources resources = TyphonDLFactory.eINSTANCE.createResources();
        resources.setLimitCPU("0.5");
        resources.setLimitMemory("512M");
        resources.setReservationCPU("0.25");
        resources.setReservationMemory("256M");
        return resources;
    }
}
