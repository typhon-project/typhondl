package de.atb.typhondl.xtext.ui.creationWizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class TestPage extends WizardPage {

    private int groupCount;
    private boolean optionalCheckVisible;

    protected TestPage(String pageName, int groupCount) {
        super(pageName);
        this.groupCount = groupCount;
        this.setOptionalCheckVisible(false);
    }

    @Override
    public void createControl(Composite parent) {
        Composite main = new Composite(parent, SWT.NONE);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        main.setLayout(new GridLayout(1, false));
        // set the composite as the control for this page
        setControl(main);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            Composite main = (Composite) this.getControl();
            GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            gridData.horizontalSpan = 2;
            if (optionalCheckVisible) {
                for (int i = 0; i < this.groupCount; i++) {
                    Group group = new Group(main, SWT.READ_ONLY);
                    group.setLayout(new GridLayout(2, false));
                    group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
                    group.setText("group " + i);

                    Button check1 = new Button(group, SWT.CHECK);
                    check1.setText("Check this");
                    check1.setSelection(false);
                    check1.setLayoutData(gridData);

                    Button check2 = new Button(group, SWT.CHECK);
                    check2.setText("Check this");
                    check2.setSelection(false);
                    check2.setLayoutData(gridData);

                    Button optionalCheck = new Button(group, SWT.CHECK);
                    optionalCheck.setText("Check this optional");
                    optionalCheck.setSelection(false);
                    optionalCheck.setLayoutData(gridData);
                }
            } else {
                for (int i = 0; i < this.groupCount; i++) {
                    Group group = new Group(main, SWT.READ_ONLY);
                    group.setLayout(new GridLayout(2, false));
                    group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
                    group.setText("group " + i);

                    Button check1 = new Button(group, SWT.CHECK);
                    check1.setText("Check this");
                    check1.setSelection(false);
                    check1.setLayoutData(gridData);

                    Button check2 = new Button(group, SWT.CHECK);
                    check2.setText("Check this");
                    check2.setSelection(false);
                    check2.setLayoutData(gridData);
                }
            }
            this.getControl().pack();
        }

    }

    public void setOptionalCheckVisible(boolean optionalCheckVisible) {
        this.optionalCheckVisible = optionalCheckVisible;
    }

}
