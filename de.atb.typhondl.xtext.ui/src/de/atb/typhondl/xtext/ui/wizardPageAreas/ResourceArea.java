package de.atb.typhondl.xtext.ui.wizardPageAreas;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Resources;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

public class ResourceArea extends Area {

    private final int pageWidth = 607;

    public ResourceArea(DB db, Container container, int chosenTechnology, Composite parent) {
        super(db, container, chosenTechnology, parent, "Container Resources");
    }

    @Override
    public void createArea() {
        if (!db.isExternal()) {
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
                resources.setLimitCPU(limCPUText.getText());
            });
            Label limMemLabel = new Label(limitComposite, SWT.NONE);
            limMemLabel.setText("limitMemory: ");
            Text limMemText = new Text(limitComposite, SWT.BORDER);
            limMemText.setText(resources.getLimitMemory());
            limMemText.setLayoutData(gridDataFields);
            limMemText.addModifyListener(e -> {
                resources.setLimitMemory(limMemText.getText());
            });

            Button reservationCheck = new Button(group, SWT.CHECK);
            reservationCheck.setText("Set container resource reservations (this will set limits as well)");
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
                resources.setReservationCPU(resCPUText.getText());
            });
            Label resMemLabel = new Label(reservationComposite, SWT.NONE);
            resMemLabel.setText("reservationMemory: ");
            Text resMemText = new Text(reservationComposite, SWT.BORDER);
            resMemText.setText(resources.getReservationMemory());
            resMemText.setLayoutData(gridDataFields);
            resMemText.addModifyListener(e -> {
                resources.setReservationMemory(resMemText.getText());
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
                    if (useLimits) {
                        Resources newResources = container.getResources();
                        if (newResources == null) {
                            newResources = TyphonDLFactory.eINSTANCE.createResources();
                        }
                        newResources.setLimitCPU(limCPUText.getText());
                        newResources.setLimitMemory(limMemText.getText());
                        container.setResources(newResources);
                    } else {
                        reservationCheck.setSelection(false);
                        reservationCheck.notifyListeners(13, new Event());
                        container.setResources(null);
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
                    if (useReservations) {
                        limitCheck.setSelection(true);
                        limitCheck.notifyListeners(13, new Event());
                        // TODO check if this can be null:
                        Resources newResources = container.getResources();
                        newResources.setReservationCPU(resCPUText.getText());
                        newResources.setReservationMemory(resMemText.getText());
                        container.setResources(newResources);
                    } else {
                        Resources newResources = container.getResources();
                        if (newResources != null) {
                            newResources.setReservationCPU(null);
                            newResources.setReservationMemory(null);
                            container.setResources(newResources);
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
