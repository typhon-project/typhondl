package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
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
import org.xml.sax.SAXException;

import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.utilities.Database;
import de.atb.typhondl.xtext.ui.utilities.MLmodelReader;
import de.atb.typhondl.xtext.ui.utilities.WizardFields;

public class CreationDBMSPage extends MyWizardPage {

	private HashMap<Database, WizardFields> databaseSettings;
	private IFile file;

	public CreationDBMSPage(String pageName, IFile file) {
		this(pageName, file, readModel(file));
	}
	
	public CreationDBMSPage(String pageName, IFile file, ArrayList<Database> MLmodel) {
		super(pageName);
		this.databaseSettings = new HashMap<Database, WizardFields>();
		MLmodel.forEach(db -> this.databaseSettings.put(db, null));
		this.file = file;
	}

	private static ArrayList<Database> readModel(IFile MLmodel) {
		try {
			return MLmodelReader.readXMIFile(MLmodel.getLocationURI());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void createControl(Composite parent) {
		setTitle("Choose a DBMS for each database");
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		main.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gridData.horizontalSpan = 2;

		for (Database database : databaseSettings.keySet()) {

			Group group = new Group(main, SWT.READ_ONLY);
			group.setLayout(new GridLayout(2, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			group.setText(database.getName());

			Button checkbox = new Button(group, SWT.CHECK);
			checkbox.setText("Use existing file");
			checkbox.setSelection(false);
			checkbox.setLayoutData(gridData);
			checkbox.setToolTipText("Check if you already have a model file for " + database.getName());
			checkbox.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					WizardFields wizardField = databaseSettings.get(database);
					wizardField.getTextField().setEnabled(wizardField.getCheckbox().getSelection());
					wizardField.getCombo().setEnabled(!wizardField.getCheckbox().getSelection());
					if (wizardField.getCheckbox().getSelection()) {
						database.setDbms(null); // delete set DBMS in database if an existing file is used
						database.setPathToDBModelFile(wizardField.getTextField().getText());
					} else {
						DBType type = TyphonDLFactory.eINSTANCE.createDBType();
						type.setName(wizardField.getCombo().getText().toLowerCase());
						database.setDbms(type);
						database.setPathToDBModelFile(null);
					}
					validate();
				}
			});

			new Label(group, NONE).setText("Choose DBMS:");
			Combo combo = new Combo(group, SWT.READ_ONLY);
			combo.setItems(database.getType().getPossibleDBMSs());
			combo.setText(database.getType().getPossibleDBMSs()[0]);
			DBType type = TyphonDLFactory.eINSTANCE.createDBType();
			type.setName(database.getType().getPossibleDBMSs()[0].toLowerCase());
			if (!checkbox.getSelection())
				database.setDbms(type);
			combo.setEnabled(!checkbox.getSelection());
			combo.setToolTipText(
					"Choose specific DBMS for " + database.getName() + " of type " + database.getType().name());
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					DBType type = TyphonDLFactory.eINSTANCE.createDBType();
					type.setName(databaseSettings.get(database).getCombo().getText().toLowerCase());
					database.setDbms(type);
					validate();
				}
			});

			new Label(group, NONE).setText("Database file: ");
			Text textField = new Text(group, SWT.BORDER);
			textField.setText(database.getName() + ".tdl");
			textField.setEnabled(checkbox.getSelection());
			textField.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
			textField.setToolTipText("Give the path to your database configuration file");
			if (checkbox.getSelection())
				database.setPathToDBModelFile(textField.getText());
			textField.addModifyListener(e -> {
				database.setPathToDBModelFile(databaseSettings.get(database).getTextField().getText());
				validate();
			});
			databaseSettings.put(database, new WizardFields(checkbox, combo, textField));
		}
		validate();
		setControl(main);
	}

	protected void validate() {
		Status status = null;
		ArrayList<String> warning = new ArrayList<String>();
		URI uri = file.getLocationURI();
		for (Database database : databaseSettings.keySet()) {
			WizardFields fields = databaseSettings.get(database);
			if (fields.getTextField().isEnabled()) {
				String pathToDatabaseFile = fields.getTextField().getText();
				if (!pathToDatabaseFile.endsWith(".tdl")) {
					status = new Status(IStatus.ERROR, "Wizard",
							"Database file (" + pathToDatabaseFile + ") has to end with .tdl");
				}
				
				String pathWithFolder = uri.toString().substring(0, uri.toString().lastIndexOf('/') + 1);
				String path = pathWithFolder + pathToDatabaseFile;
				File file = new File(URI.create(path));
				if (!file.exists()) {
					status = new Status(IStatus.ERROR, "Wizard",
							"Database file " + pathToDatabaseFile + " doesn't exists.");
				}
			} else {
				String pathToDatabaseFile = database.getName() + ".tdl";
				String pathWithFolder = uri.toString().substring(0, uri.toString().lastIndexOf('/') + 1);
				String path = pathWithFolder + pathToDatabaseFile;
				File file = new File(URI.create(path));
				if (file.exists()) {
					warning.add(pathToDatabaseFile);
				}
			}
		}
		if (!warning.isEmpty() && status == null) {
			status = new Status(IStatus.WARNING, "Wizard", "Database file(s) " + Arrays.toString(warning.toArray())
					+ " already exist(s) and will be overwritten if you continue");
		}
		setStatus(status);
	}

	public ArrayList<Database> getDatabases() {
		return new ArrayList<Database>(databaseSettings.keySet());
	}
}
