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
import de.atb.typhondl.xtext.ui.utilities.DBMS;
import de.atb.typhondl.xtext.ui.utilities.MLmodelReader;
import de.atb.typhondl.xtext.ui.utilities.SupportedDBMS;
import de.atb.typhondl.xtext.ui.utilities.WizardFields;

public class CreationDBMSPage extends MyWizardPage {

	private HashMap<DBMS, WizardFields> databaseSettings;
	private IFile file;

	public CreationDBMSPage(String pageName, IFile file) {
		this(pageName, file, readModel(file));
	}

	public CreationDBMSPage(String pageName, IFile file, ArrayList<DBMS> MLmodel) {
		super(pageName);
		this.databaseSettings = new HashMap<DBMS, WizardFields>();
		MLmodel.forEach(db -> this.databaseSettings.put(db, null));
		this.file = file;
	}

	private static ArrayList<DBMS> readModel(IFile MLmodel) {
		try {
			// every DBMS in the List has a name and an abstractType
			// every other value == null
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

		for (DBMS dbms : databaseSettings.keySet()) {

			Group group = new Group(main, SWT.READ_ONLY);
			group.setLayout(new GridLayout(2, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			group.setText(dbms.getName());

			Button checkbox = new Button(group, SWT.CHECK);
			checkbox.setText("Use existing file");
			checkbox.setSelection(fileExists(dbms.getName() + ".tdl"));
			checkbox.setLayoutData(gridData);
			checkbox.setToolTipText("Check this box if you already have a model file for " + dbms.getName());
			checkbox.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					WizardFields wizardField = databaseSettings.get(dbms);
					wizardField.getTextField().setEnabled(wizardField.getCheckbox().getSelection());
					wizardField.getCombo().setEnabled(!wizardField.getCheckbox().getSelection());
					if (wizardField.getCheckbox().getSelection()) {
						dbms.removeDBType(); // delete set DBType in DBMS if an existing file is used
						dbms.setPathToDBModelFile(wizardField.getTextField().getText());
					} else {
						dbms.setDBType(SupportedDBMS.valueOf(dbms.getAbstractType())
								.getTypeByTemplateName(wizardField.getCombo().getText()));
						dbms.setPathToDBModelFile(null);
					}
					validate();
				}
			});

			new Label(group, NONE).setText("Choose DBMS:");
			Combo combo = new Combo(group, SWT.READ_ONLY);
			SupportedDBMS abstractType = SupportedDBMS.valueOf(dbms.getAbstractType());
			String[] DBMSTemplateNames = abstractType.getDBMSTemplateNames();
			combo.setItems(DBMSTemplateNames);
			combo.setText(DBMSTemplateNames[0]);
			DBMS[] possibleDBMSs = abstractType.getPossibleDBMSs();
			DBType type = possibleDBMSs[0].getType();
			if (!checkbox.getSelection()) {
				dbms.setDBType(type);
			}
			combo.setEnabled(!checkbox.getSelection());
			combo.setToolTipText("Choose specific DBMS for " + dbms.getName() + " of type " + dbms.getAbstractType());
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					dbms.setDBType(abstractType.getTypeByTemplateName(databaseSettings.get(dbms).getCombo().getText()));
					validate();
				}
			});

			new Label(group, NONE).setText("Database file: ");
			Text textField = new Text(group, SWT.BORDER);
			textField.setText(dbms.getName() + ".tdl");
			textField.setEnabled(checkbox.getSelection());
			textField.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
			textField.setToolTipText("Give the path to your database configuration file");
			if (checkbox.getSelection()) {
				dbms.setPathToDBModelFile(textField.getText());
			}
			textField.addModifyListener(e -> {
				dbms.setPathToDBModelFile(databaseSettings.get(dbms).getTextField().getText());
				validate();
			});
			databaseSettings.put(dbms, new WizardFields(checkbox, combo, textField));
		}
		validate();
		setControl(main);
	}

	protected void validate() {
		Status status = null;
		ArrayList<String> warning = new ArrayList<String>();
		for (DBMS dbms : databaseSettings.keySet()) {
			WizardFields fields = databaseSettings.get(dbms);
			if (fields.getTextField().isEnabled()) {
				String pathToDatabaseFile = fields.getTextField().getText();
				if (!pathToDatabaseFile.endsWith(".tdl")) {
					status = new Status(IStatus.ERROR, "Wizard",
							"Database file (" + pathToDatabaseFile + ") has to end with .tdl");
				}

				if (!fileExists(pathToDatabaseFile)) {
					status = new Status(IStatus.ERROR, "Wizard",
							"Database file " + pathToDatabaseFile + " doesn't exists.");
				}
			} else {
				String pathToDatabaseFile = dbms.getName() + ".tdl";
				if (fileExists(pathToDatabaseFile)) {
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

	private boolean fileExists(String fileName) {
		URI uri = file.getLocationURI();
		String pathWithFolder = uri.toString().substring(0, uri.toString().lastIndexOf('/') + 1);
		String path = pathWithFolder + fileName;
		File file = new File(URI.create(path));
		return file.exists();
	}

	public ArrayList<DBMS> getDatabases() {
		return new ArrayList<DBMS>(databaseSettings.keySet());
	}
}
