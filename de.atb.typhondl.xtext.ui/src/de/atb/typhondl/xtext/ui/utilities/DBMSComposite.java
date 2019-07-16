package de.atb.typhondl.xtext.ui.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.wizard.Database;

public class DBMSComposite extends Composite {

	private ArrayList<Database> MLmodel;
	private HashMap<Database, WizardFields> wizardFields;

	public DBMSComposite(Composite parent, int style, ArrayList<Database> MLmodel) {
		super(parent, style);
		this.MLmodel = MLmodel;
		this.wizardFields = new HashMap<Database, WizardFields>();
	}

	/**
	 * TODO fields won't be displayed
	 */
	public void createFields() {
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gridData.horizontalSpan = 2;

		for (Database database : MLmodel) {

			Group group = new Group(this, SWT.READ_ONLY);
			group.setLayout(new GridLayout(2, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			group.setText(database.getName());
			
			Button checkbox = new Button(group, SWT.CHECK);
			checkbox.setText("Use existing file");
			checkbox.setSelection(false);
			checkbox.setLayoutData(gridData);
			checkbox.setToolTipText(
					"If you already have a " + database.getName() + ".tdl template, enter relative path here");
			checkbox.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					WizardFields wizardField = wizardFields.get(database);
					wizardField.getTextField().setEnabled(wizardField.getCheckbox().getSelection());
					//wizardField.getCombo().setEnabled(!wizardField.getCheckbox().getSelection()); see todo below
				}
			});

			new Label(group, SWT.NONE).setText("Choose DBMS:");
			Combo combo = new Combo(group, SWT.READ_ONLY);
			combo.setItems(database.getType().getPossibleDBMSs());
			combo.setText(database.getType().getPossibleDBMSs()[0]);
			DBType type = TyphonDLFactory.eINSTANCE.createDBType();
			type.setName(database.getType().getPossibleDBMSs()[0].toLowerCase());
			database.setDbms(type);
			//combo.setEnabled(!checkbox.getSelection()); TODO for now the correct DBMS has to be selected
			combo.setToolTipText(
					"Choose specific DBMS for " + database.getName() + " of type " + database.getType().name());
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					DBType type = TyphonDLFactory.eINSTANCE.createDBType();
					type.setName(wizardFields.get(database).getCombo().getText().toLowerCase());
					database.setDbms(type);
				}
			});

			new Label(group, SWT.NONE).setText("Database file: ");
			Text textField = new Text(group, SWT.BORDER);
			textField.setText(database.getName() + ".tdl");
			textField.setEnabled(checkbox.getSelection());
			textField.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
			textField.setToolTipText("Give the path to your database configuration file");
			database.setPathToDBModelFile(textField.getText());
			textField.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					// TODO Where to put validation?
					database.setPathToDBModelFile(wizardFields.get(database).getTextField().getText());
				}
			});
			wizardFields.put(database, new WizardFields(checkbox, combo, textField));
		}
	}

	public HashMap<Database, WizardFields> getWizardFields() {
		return wizardFields;
	}
}