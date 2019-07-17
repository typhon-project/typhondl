package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
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
import de.atb.typhondl.xtext.ui.utilities.MLmodelReader;
import de.atb.typhondl.xtext.ui.utilities.WizardFields;
import de.atb.typhondl.xtext.ui.wizard.Database;
import de.atb.typhondl.xtext.ui.wizard.Tuple;

public class CreationDBMSPage extends WizardPage {

	private HashMap<Database, WizardFields> data;
	private URI modelPath;

	protected CreationDBMSPage(String pageName, URI modelPath) {
		super(pageName);
		this.data = new HashMap<Database, WizardFields>();
		this.modelPath = modelPath;
		readModel(modelPath).forEach(db -> this.data.put(db, null));
	}

	private ArrayList<Database> readModel(URI modelPath) {
		try {
			return MLmodelReader.readXMIFile(modelPath);
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

		for (Database database : data.keySet()) {

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
					WizardFields wizardField = data.get(database);
					wizardField.getTextField().setEnabled(wizardField.getCheckbox().getSelection());
					wizardField.getCombo().setEnabled(!wizardField.getCheckbox().getSelection());
					if (wizardField.getCheckbox().getSelection()) {
						database.setDbms(null); // delete set DBMS in database if an existing file is used
						database.setPathToDBModelFile(data.get(database).getTextField().getText());
					} else {
						DBType type = TyphonDLFactory.eINSTANCE.createDBType();
						type.setName(data.get(database).getCombo().getText().toLowerCase());
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
			database.setDbms(type);
			combo.setEnabled(!checkbox.getSelection());
			combo.setToolTipText(
					"Choose specific DBMS for " + database.getName() + " of type " + database.getType().name());
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					DBType type = TyphonDLFactory.eINSTANCE.createDBType();
					type.setName(data.get(database).getCombo().getText().toLowerCase());
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
			textField.addModifyListener(e -> {
				database.setPathToDBModelFile(data.get(database).getTextField().getText());
				validate();
			});
			data.put(database, new WizardFields(checkbox, combo, textField));
		}
		validate();
		setControl(main);
	}

	protected void validate() {
		Status status = null;
		ArrayList<String> warning = new ArrayList<String>();
		for (Database database : data.keySet()) {
			WizardFields fields = data.get(database);
			if (fields.getTextField().isEnabled()) {
				String pathToDatabaseFile = fields.getTextField().getText();
				if (!pathToDatabaseFile.endsWith(".tdl")) {
					status = new Status(IStatus.ERROR, "Wizard",
							"Database file (" + pathToDatabaseFile + ") has to end with .tdl");
				}
				String pathWithFolder = modelPath.toString().substring(0, modelPath.toString().lastIndexOf('/') + 1);
				String path = pathWithFolder + pathToDatabaseFile;
				File file = new File(URI.create(path));
				if (!file.exists()) {
					status = new Status(IStatus.ERROR, "Wizard",
							"Database file " + pathToDatabaseFile + " doesn't exists.");
				}
			} else {
				String pathToDatabaseFile = database.getName() + ".tdl";
				String pathWithFolder = modelPath.toString().substring(0, modelPath.toString().lastIndexOf('/') + 1);
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

	/**
	 * /*******************************************************************************
	 * Copyright (c) 2017, 2018 itemis AG (http://www.itemis.de) and others. All
	 * rights reserved. This program and the accompanying materials are made
	 * available under the terms of the Eclipse Public License v1.0 which
	 * accompanies this distribution, and is available at
	 * http://www.eclipse.org/legal/epl-v10.html
	 * 
	 * org.eclipse.xtext.ui.wizard.template.TemplateParameterPage
	 *******************************************************************************/
	public void setStatus(IStatus status) {
		if (status == null || status.getSeverity() == IStatus.OK) {
			setErrorMessage(null);
			setMessage(null);
			setPageComplete(true);
		} else if (status.getSeverity() == IStatus.ERROR) {
			setErrorMessage(status.getMessage());
			setPageComplete(false);
		} else if (status.getSeverity() == IStatus.WARNING) {
			setErrorMessage(null);
			setMessage(status.getMessage(), IMessageProvider.WARNING);
			setPageComplete(true);
		} else {
			setErrorMessage(null);
			setMessage(status.getMessage(), IMessageProvider.INFORMATION);
			setPageComplete(true);
		}
	}

	public Set<Database> getDatabases() {
		return data.keySet();
	}
}
