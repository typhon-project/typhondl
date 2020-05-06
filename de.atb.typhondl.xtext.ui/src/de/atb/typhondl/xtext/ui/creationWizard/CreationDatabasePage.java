package de.atb.typhondl.xtext.ui.creationWizard;

import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.atb.typhondl.xtext.typhonDL.DB;

public class CreationDatabasePage extends WizardPage {

    private DB db;
    private TemplateBuffer buffer;

    protected CreationDatabasePage(String pageName, DB db, TemplateBuffer buffer) {
        super(pageName);
        this.db = db;
        this.buffer = buffer;
    }

    @Override
    public void createControl(Composite parent) {
        setTitle("Database settings for " + db.getName());
        Composite main = new Composite(parent, SWT.NONE);
        new Label(main, SWT.None).setText(db.getName());
        setControl(main);
    }

    public String getDBName() {
        return db.getName();
    }

    public void setDB(DB db) {
        this.db = db;
    }

    public void setBuffer(TemplateBuffer templateBuffer) {
        this.buffer = templateBuffer;
    }

}
