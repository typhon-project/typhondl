package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.templates.TemplateBuffer;
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
import org.xml.sax.SAXException;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.utilities.MLmodelReader;
import de.atb.typhondl.xtext.ui.utilities.Pair;
import de.atb.typhondl.xtext.ui.utilities.PreferenceReader;
import de.atb.typhondl.xtext.ui.utilities.WizardFields;

/**
 * Second page of the TyphonDL Creation Wizard. The ML model gets parsed and for
 * each needed database a group is created. Here the user can choose the wanted
 * DBMS. The list of possible DBMSs is taken from the templates.
 * 
 * @author flug
 *
 */
public class CreationDBMSPage extends MyWizardPage {

	/**
	 * Each DB needs WizardFields to get the wanted DBMS or the path to the already
	 * existing model file
	 */
	private HashMap<DB, WizardFields> databaseSettings;

	/**
	 * Each DB has a TemplateBuffer with the pattern and template variables if
	 * created from a template, this is given to the wizard to create additional
	 * pages
	 */
	private HashMap<DB, TemplateBuffer> result;

	/**
	 * The parsed ML model containing Pairs of (DatabaseName, DatabaseAbstractType)
	 * where DatabaseAbstractType is in (relationaldb, documentdb, keyvaluedb,
	 * graphdb) <br>
	 * firstValue == dbName <br>
	 * second value == abstractType
	 */
	private ArrayList<Pair<String, String>> MLmodel;

	/**
	 * The ML model file
	 */
	private IFile file;

	/**
	 * Creates a CreationDBMSPage, reading the ML model from the given file.
	 * 
	 * @param pageName
	 * @param file     ML model file
	 */
	public CreationDBMSPage(String pageName, IFile file) {
		this(pageName, file, readModel(file));
	}

	/**
	 * Creates a CreationDBMSPage from the given ML model
	 * 
	 * @param pageName
	 * @param file     ML model file
	 * @param MLmodel  List of Pair(name, abstractType) taken from the ML model file
	 */
	public CreationDBMSPage(String pageName, IFile file, ArrayList<Pair<String, String>> MLmodel) {
		super(pageName);
		this.MLmodel = MLmodel;
		this.file = file;
		this.databaseSettings = new HashMap<>();
		this.result = new HashMap<>();
	}

	/**
	 * reads the given ML model, extracts name and abstract type of the databases
	 * 
	 * @param MLmodel
	 * @return a list of Pair(name, abstractType)
	 */
	private static ArrayList<Pair<String, String>> readModel(IFile MLmodel) {
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

		for (Pair<String, String> dbFromML : MLmodel) {

			DB db = getEmptyDB(dbFromML.firstValue);

			// get templates
			ArrayList<Pair<String, TemplateBuffer>> buffers = PreferenceReader.getBuffers(dbFromML.secondValue);
			// no fitting DB is defined in templates
			if (buffers.isEmpty()) {
				MessageDialog.openError(getShell(), "Template Error", "There is no template for a "
						+ dbFromML.secondValue + ". Please add or activate a fitting DB template.");
			}

			// get Templates from buffer. The DBs have the template's name.
			DB[] dbTemplates = PreferenceReader.getDBs(buffers);
			databaseSettings.put(db, new WizardFields(null, null, buffers));
			String[] dbTemplateNames = Arrays.asList(dbTemplates).stream().map(dbTemplate -> dbTemplate.getName())
					.collect(Collectors.toList()).toArray(new String[0]);

			String dbName = db.getName();

			// create a group for each database
			Group group = new Group(main, SWT.READ_ONLY);
			group.setLayout(new GridLayout(2, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			group.setText(dbName);

			Button checkbox = new Button(group, SWT.CHECK);
			checkbox.setText("Use existing " + dbName + ".tdl file in this project folder");
			checkbox.setSelection(fileExists(dbName + ".tdl"));
			// if an existing file is to be used, there exists no buffer
			if (checkbox.getSelection()) {
				result.put(db, null);
			}
			checkbox.setLayoutData(gridData);
			checkbox.setToolTipText("Check this box if you already have a model file for " + dbName);
			checkbox.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					WizardFields wizardField = databaseSettings.get(db);
					wizardField.getCombo().setEnabled(!wizardField.getCheckbox().getSelection());
					if (wizardField.getCheckbox().getSelection()) {
						clearDB(db); // delete existing db settings
					} else {
						useBufferOnDB(db, getBufferByName(buffers, wizardField.getCombo().getText()));
					}
					validate();
				}
			});
			databaseSettings.get(db).setCheckbox(checkbox);

			new Label(group, NONE).setText("Choose DBMS:");
			Combo combo = new Combo(group, SWT.READ_ONLY);
			combo.setItems(dbTemplateNames);
			combo.setText(dbTemplateNames[0]);
			// set initial dbTemplate
			if (!checkbox.getSelection()) {
				useBufferOnDB(db, buffers.get(0).secondValue);
			}
			combo.setEnabled(!checkbox.getSelection());
			combo.setToolTipText("Choose specific DBMS Template for " + dbName);
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					WizardFields wizardField = databaseSettings.get(db);
					useBufferOnDB(db, getBufferByName(buffers, wizardField.getCombo().getText()));
					validate();
				}
			});
			databaseSettings.get(db).setCombo(combo);
		}
		validate();
		setControl(main);
	}

	/**
	 * Removes DBType and clears Parameter List from DB TODO check if the list is
	 * supposed to be null
	 * 
	 * @param db the DB to clear
	 */
	protected void clearDB(DB db) {
		db.setType(null);
		db.getParameters().clear();
		result.put(db, null);
	}

	/**
	 * Adds the DBType and Parameters from the given TemplateBuffer to the given DB
	 * 
	 * @param db     The DB that should have all attributes from the template
	 * @param buffer The chosen TemplateBuffer
	 */
	protected void useBufferOnDB(DB db, TemplateBuffer buffer) {
		DB template = PreferenceReader.getModelObject(TyphonDLFactory.eINSTANCE.createDB(), buffer);
		db.setType(template.getType());
		db.getParameters().clear();
		db.getParameters().addAll(template.getParameters());
		result.put(db, buffer);
	}

	/**
	 * Finds a TemplateBuffer by name
	 * 
	 * @param buffers      the Pair of Template name and TemplateBuffer
	 * @param templateName the template to find
	 * @return the wanted TemplateBuffer
	 */
	protected TemplateBuffer getBufferByName(ArrayList<Pair<String, TemplateBuffer>> buffers, String templateName) {
		return buffers.stream().filter(pair -> pair.firstValue.equalsIgnoreCase(templateName)).findFirst()
				.orElse(null).secondValue;
	}

	/**
	 * Creates a new empty DB with just a name
	 * 
	 * @param dbName the name of the DB
	 * @return a DB with name dbName
	 */
	private DB getEmptyDB(String dbName) {
		DB db = TyphonDLFactory.eINSTANCE.createDB();
		db.setName(dbName);
		return db;
	}

	/**
	 * Checks if a database file already exists, gives warning if the file exists
	 * and would be overwritten or error if the file doesn't exist but the
	 * fileExists checkbox is checked
	 */
	protected void validate() {
		Status status = null;
		ArrayList<String> warning = new ArrayList<String>();
		for (DB db : databaseSettings.keySet()) {
			WizardFields fields = databaseSettings.get(db);
			String path = db.getName() + ".tdl";
			if (fields.getCheckbox().getSelection()) {
				if (!fileExists(path)) {
					status = new Status(IStatus.ERROR, "Wizard", "Database file " + path + " doesn't exists.");
				}
			} else {
				if (fileExists(path)) {
					warning.add(path);
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
	 * utility for checking if a file exists
	 */
	private boolean fileExists(String fileName) {
		URI uri = file.getLocationURI();
		Path path = Paths.get(uri);
		Path filePath = path.getParent().resolve(fileName);
		return Files.exists(filePath);
	}

	/**
	 * Get a list of DBs taken from the MLmodel enriched with wizard and template
	 * input and the corresponding TemplateVariables
	 * 
	 * @return DBs and their TemplateVariable Array
	 */
	public HashMap<DB, TemplateBuffer> getResult() {
		return result;
	}

	/**
	 * Lets the wizard check if there is the need for additional template variables
	 * pages
	 * 
	 * @return true if there should be additional pages, otherwise false
	 */
	public boolean hasTemplateVariables() {
		TemplateBuffer buffer = result.keySet().stream().map(key -> result.get(key)).filter(value -> value != null)
				.findFirst().orElse(null);
		return buffer != null;
	}
}
