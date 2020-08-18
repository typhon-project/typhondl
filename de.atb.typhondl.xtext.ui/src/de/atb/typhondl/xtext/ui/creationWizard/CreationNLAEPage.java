package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.Properties;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class CreationNLAEPage extends WizardPage {

    private Properties properties;
    private Composite main;

    public CreationNLAEPage(String pageName, Properties properties) {
        super(pageName);
        this.properties = properties;
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
        new Label(main, NONE).setText("TEST");

    }

    public void updateData(Properties properties) {
        this.properties = properties;

    }

    public Properties getProperties() {
        return properties;
    }

}
