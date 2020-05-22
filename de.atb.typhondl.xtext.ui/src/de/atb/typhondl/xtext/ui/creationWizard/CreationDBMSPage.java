package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;
import org.xml.sax.SAXException;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Key_KeyValueList;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Property;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.utilities.MLmodelReader;
import de.atb.typhondl.xtext.ui.utilities.Pair;
import de.atb.typhondl.xtext.ui.utilities.PreferenceReader;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

/**
 * Second page of the TyphonDL {@link CreateModelWizard}. The ML model gets
 * parsed and for each needed database a group is created. Here the user can
 * choose the wanted DBMS. The list of possible DBMSs is taken from the
 * templates.
 * 
 * @author flug
 *
 */
public class CreationDBMSPage extends MyWizardPage {

    /**
     * Each DB has a TemplateBuffer with the pattern and template variables if
     * created from a template, this is given to the wizard to create additional
     * pages
     */
    private HashMap<DB, TemplateBuffer> result;

    /**
     * Store some buttons for validation <br>
     * TODO validation should be better maybe with IInputValidator
     */
    private HashMap<String, Button> validationList;

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

    private int chosenTemplate;

    private XtextResourceSet resourceSet;

    /**
     * Creates a CreationDBMSPage, reading the ML model from the given file.
     * 
     * @param pageName
     * @param file     ML model file
     */
    public CreationDBMSPage(String pageName, IFile file, int chosenTemplate) {
        this(pageName, file, readModel(file), chosenTemplate);
    }

    /**
     * Creates a CreationDBMSPage from the given ML model
     * 
     * @param pageName
     * @param file     ML model file
     * @param MLmodel  List of Pair(name, abstractType) taken from the ML model file
     */
    public CreationDBMSPage(String pageName, IFile file, ArrayList<Pair<String, String>> MLmodel, int chosenTemplate) {
        super(pageName);
        this.MLmodel = MLmodel;
        this.file = file;
        this.result = new HashMap<>();
        this.validationList = new HashMap<>();
        this.chosenTemplate = chosenTemplate;
        addResources();
    }

    /**
     * Gets the provided ResourceSet and adds all .tdl files to the ResourceSet
     */
    private void addResources() {
        this.resourceSet = (XtextResourceSet) Activator.getInstance()
                .getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL)
                .getInstance(XtextLiveScopeResourceSetProvider.class).get(this.file.getProject());
        this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
        IResource members[] = null;
        try {
            members = this.file.getProject().members();
        } catch (CoreException e) {
            e.printStackTrace();
        }
        for (IResource member : members) {
            if (member instanceof IFile) {
                if (((IFile) member).getFileExtension().equals("tdl")) {
                    resourceSet.getResource(URI.createPlatformResourceURI(member.getFullPath().toString(), true), true);
                }
            }
        }
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
        ScrolledComposite scrolling = new ScrolledComposite(parent, SWT.V_SCROLL);
        Composite main = new Composite(scrolling, SWT.NONE);
        scrolling.setContent(main);
        scrolling.setExpandVertical(true);
        scrolling.setExpandHorizontal(true);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        main.setLayout(new GridLayout(1, false));

        for (Pair<String, String> dbFromML : MLmodel) {

            // empty DB model object with the name taken from the ML model
            DB db = getEmptyDB(dbFromML.firstValue);

            // get templates
            ArrayList<Pair<DB, TemplateBuffer>> templates = PreferenceReader.getBuffers(dbFromML.secondValue);
            // no fitting DB is defined in templates
            if (templates.isEmpty()) {
                MessageDialog.openError(getShell(), "Template Error",
                        "There is no template for a " + dbFromML.secondValue
                                + ". Please add or activate a fitting DB template in the Eclipse settings.");
            }

            // create a group for each database
            Group group = new Group(main, SWT.READ_ONLY);
            group.setLayout(new GridLayout(2, false));
            group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            group.setText(db.getName());

            if (SupportedTechnologies.values()[chosenTemplate].getClusterType().equalsIgnoreCase("Kubernetes")) {
                kubernetesComposeControls(group, templates, db);
            } else {
                dockerComposeControls(group, templates, db);
            }
        }
        validate();
        main.setSize(main.computeSize(((CreateModelWizard) this.getWizard()).getPageWidth(), SWT.DEFAULT));
        scrolling.setMinSize(main.computeSize(((CreateModelWizard) this.getWizard()).getPageWidth(), SWT.DEFAULT));

        setControl(scrolling);
    }

    /**
     * Creates controls for DBMS kubernetes page
     * <li>existing model</li>
     * <li>existing database</li>
     * <li>use helm charts</li>
     * <li>combo for choosing template</li>
     * 
     * @param group
     * @param templates
     * @param db
     */
    private void kubernetesComposeControls(Group group, ArrayList<Pair<DB, TemplateBuffer>> templates, DB db) {
        String dbName = db.getName();
        // get Templates from buffer. The DBs have the template's name.
        DB[] dbTemplates = templates.stream().map(pair -> pair.firstValue).collect(Collectors.toList())
                .toArray(new DB[0]);
        String[] dbTemplateNames = Arrays.asList(dbTemplates).stream().map(dbTemplate -> dbTemplate.getName())
                .collect(Collectors.toList()).toArray(new String[0]);
        GridData wideGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        wideGridData.horizontalSpan = 2;

        Button existingModelCheck = new Button(group, SWT.CHECK);
        existingModelCheck.setText("Use existing " + dbName + ".tdl file in this project folder");
        existingModelCheck.setSelection(fileExists(dbName + ".tdl"));
        // if an existing file is to be used, there exists no buffer
        if (existingModelCheck.getSelection()) {
            result.put(readExistingFile(dbName), null);
        }
        existingModelCheck.setLayoutData(wideGridData);
        existingModelCheck.setToolTipText("Check this box if you already have a model file for " + dbName);
        validationList.put(dbName, existingModelCheck);

        Button externalDatabaseCheck = new Button(group, SWT.CHECK);
        externalDatabaseCheck.setText("Use existing database for " + dbName + " (please select DBMS from Templates)");
        externalDatabaseCheck.setSelection(false);
        externalDatabaseCheck.setLayoutData(wideGridData);
        externalDatabaseCheck
                .setToolTipText("Check this box if you already have this database outside of the polystore");

        Button useHelmChartCheck = new Button(group, SWT.CHECK);
        useHelmChartCheck.setText("Use Helm chart (please select DBMS from Templates)");
        useHelmChartCheck.setSelection(false);
        useHelmChartCheck.setLayoutData(wideGridData);
        useHelmChartCheck.setToolTipText("Use a Helm chart for one of the supported technologies");

        new Label(group, NONE).setText("Choose Template:");
        Combo combo = new Combo(group, SWT.READ_ONLY);
        combo.setItems(dbTemplateNames);
        combo.setText(dbTemplateNames[0]);
        // set initial dbTemplate
        if (!existingModelCheck.getSelection()) {
            Pair<DB, TemplateBuffer> template = templates.get(0);
            result.put(useBufferOnDB(db, template.firstValue), template.secondValue);
        }

        existingModelCheck.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean useExistingModel = existingModelCheck.getSelection();
                combo.setEnabled(!useExistingModel);
                if (externalDatabaseCheck.getSelection()) {
                    externalDatabaseCheck.setSelection(!useExistingModel);
                }
                if (useHelmChartCheck != null && useHelmChartCheck.getSelection()) {
                    useHelmChartCheck.setSelection(!useExistingModel);
                }
                removeDBfromResult(dbName);
                if (useExistingModel) {
                    DB newDB = readExistingFile(dbName);
                    newDB.setExternal(externalDatabaseCheck.getSelection());
                    result.put(newDB, null);
                } else {
                    Pair<DB, TemplateBuffer> template = getDBTemplateByName(templates, combo.getText());
                    DB newDB = useBufferOnDB(db, template.firstValue);
                    newDB.setExternal(externalDatabaseCheck.getSelection());
                    result.put(newDB, template.secondValue);
                }
                validate();
            }
        });

        externalDatabaseCheck.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean useExternalDatabase = externalDatabaseCheck.getSelection();
                combo.setEnabled(true);
                if (existingModelCheck.getSelection()) {
                    existingModelCheck.setSelection(!useExternalDatabase);
                }
                if (useHelmChartCheck != null && useHelmChartCheck.getSelection()) {
                    useHelmChartCheck.setSelection(!useExternalDatabase);
                }
                removeDBfromResult(dbName);
                Pair<DB, TemplateBuffer> template = getDBTemplateByName(templates, combo.getText());
                DB newDB = useBufferOnDB(db, template.firstValue);
                newDB.setExternal(useExternalDatabase);
                result.put(newDB, template.secondValue);
                validate();
            };
        });

        useHelmChartCheck.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean useHelmChart = useHelmChartCheck.getSelection();
                combo.setEnabled(true);
                if (existingModelCheck.getSelection()) {
                    existingModelCheck.setSelection(!useHelmChart);
                }
                if (externalDatabaseCheck.getSelection()) {
                    externalDatabaseCheck.setSelection(!useHelmChart);
                }
                removeDBfromResult(dbName);
                Pair<DB, TemplateBuffer> template = getDBTemplateByName(templates, combo.getText());
                DB newDB = useBufferOnDB(db, template.firstValue);
                newDB.setExternal(externalDatabaseCheck.getSelection());
                if (useHelmChart) {
                    newDB = addHelmChartKeys(newDB);
                }
                result.put(newDB, template.secondValue);
                validate();
            }
        });

        combo.setEnabled(!existingModelCheck.getSelection());
        combo.setToolTipText("Choose specific DBMS Template for " + dbName);
        combo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeDBfromResult(dbName);
                Pair<DB, TemplateBuffer> template = getDBTemplateByName(templates, combo.getText());
                DB newDB = useBufferOnDB(db, template.firstValue);
                newDB.setExternal(externalDatabaseCheck.getSelection());
                if (useHelmChartCheck != null && useHelmChartCheck.getSelection()) {
                    newDB = addHelmChartKeys(newDB);
                }
                result.put(newDB, template.secondValue);
                validate();
            }
        });
    }

    /**
     * Creates controls for DBMS docker compose page
     * <li>existing model</li>
     * <li>existing database</li>
     * <li>combo for choosing template</li>
     * 
     * @param group
     * @param templates
     * @param db
     */
    private void dockerComposeControls(Composite group, ArrayList<Pair<DB, TemplateBuffer>> templates, DB db) {
        String dbName = db.getName();
        // get Templates from buffer. The DBs have the template's name.
        DB[] dbTemplates = templates.stream().map(pair -> pair.firstValue).collect(Collectors.toList())
                .toArray(new DB[0]);
        String[] dbTemplateNames = Arrays.asList(dbTemplates).stream().map(dbTemplate -> dbTemplate.getName())
                .collect(Collectors.toList()).toArray(new String[0]);
        GridData wideGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        wideGridData.horizontalSpan = 2;

        Button existingModelCheck = new Button(group, SWT.CHECK);
        existingModelCheck.setText("Use existing " + dbName + ".tdl file in this project folder");
        existingModelCheck.setSelection(fileExists(dbName + ".tdl"));
        // if an existing file is to be used, there exists no buffer
        if (existingModelCheck.getSelection()) {
            result.put(readExistingFile(dbName), null);
        }
        existingModelCheck.setLayoutData(wideGridData);
        existingModelCheck.setToolTipText("Check this box if you already have a model file for " + dbName);
        validationList.put(dbName, existingModelCheck);

        Button externalDatabaseCheck = new Button(group, SWT.CHECK);
        externalDatabaseCheck.setText("Use existing database for " + dbName + " (please select DBMS from Templates)");
        externalDatabaseCheck.setSelection(false);
        externalDatabaseCheck.setLayoutData(wideGridData);
        externalDatabaseCheck
                .setToolTipText("Check this box if you already have this database outside of the polystore");

        new Label(group, NONE).setText("Choose Template:");
        Combo combo = new Combo(group, SWT.READ_ONLY);
        combo.setItems(dbTemplateNames);
        combo.setText(dbTemplateNames[0]);
        // set initial dbTemplate
        if (!existingModelCheck.getSelection()) {
            Pair<DB, TemplateBuffer> template = templates.get(0);
            result.put(useBufferOnDB(db, template.firstValue), template.secondValue);
        }

        existingModelCheck.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean useExistingModel = existingModelCheck.getSelection();
                combo.setEnabled(!useExistingModel);
                if (externalDatabaseCheck.getSelection()) {
                    externalDatabaseCheck.setSelection(!useExistingModel);
                }
                removeDBfromResult(dbName);
                if (useExistingModel) {
                    DB newDB = readExistingFile(dbName);
                    newDB.setExternal(externalDatabaseCheck.getSelection());
                    result.put(newDB, null);
                } else {
                    Pair<DB, TemplateBuffer> template = getDBTemplateByName(templates, combo.getText());
                    DB newDB = useBufferOnDB(db, template.firstValue);
                    newDB.setExternal(externalDatabaseCheck.getSelection());
                    result.put(newDB, template.secondValue);
                }
                validate();
            }
        });

        externalDatabaseCheck.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean useExternalDatabase = externalDatabaseCheck.getSelection();
                combo.setEnabled(true);
                if (existingModelCheck.getSelection()) {
                    existingModelCheck.setSelection(!useExternalDatabase);
                }
                removeDBfromResult(dbName);
                Pair<DB, TemplateBuffer> template = getDBTemplateByName(templates, combo.getText());
                DB newDB = useBufferOnDB(db, template.firstValue);
                newDB.setExternal(useExternalDatabase);
                result.put(newDB, template.secondValue);
                validate();
            };
        });

        combo.setEnabled(!existingModelCheck.getSelection());
        combo.setToolTipText("Choose specific DBMS Template for " + dbName);
        combo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeDBfromResult(dbName);
                Pair<DB, TemplateBuffer> template = getDBTemplateByName(templates, combo.getText());
                DB newDB = useBufferOnDB(db, template.firstValue);
                newDB.setExternal(externalDatabaseCheck.getSelection());
                result.put(newDB, template.secondValue);
                validate();
            }
        });
    }

    private DB addHelmChartKeys(DB newDB) {
        if (!newDB.getParameters().stream().anyMatch(parameter -> parameter.getName().equalsIgnoreCase("helm"))) {
            Key_KeyValueList helm = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
            helm.setName("helm");
            Key_Values helmRepoAddress = TyphonDLFactory.eINSTANCE.createKey_Values();
            helmRepoAddress.setName("address");
            helmRepoAddress.setValue("https://charts.bitnami.com/bitnami");
            Key_Values helmRepoName = TyphonDLFactory.eINSTANCE.createKey_Values();
            helmRepoName.setName("name");
            helmRepoName.setValue("bitnami");
            Key_Values helmChart = TyphonDLFactory.eINSTANCE.createKey_Values();
            helmChart.setName("chart");
            helmChart.setValue(newDB.getType().getName().toLowerCase());
            helm.getProperties().add(helmRepoName);
            helm.getProperties().add(helmRepoAddress);
            helm.getProperties().add(helmChart);
            newDB.getParameters().add(helm);
        }
        return newDB;
    }

    /**
     * Removes DB with dbName from the result map
     * 
     * @param dbName The name of the DB to delete from result map
     */
    protected void removeDBfromResult(String dbName) {
        DB dbToRemove = null;
        for (DB db : result.keySet()) {
            if (db.getName().equalsIgnoreCase(dbName)) {
                dbToRemove = db;
            }
        }
        if (dbToRemove != null) {
            result.remove(dbToRemove);
        }
    }

    /**
     * Reads model from file
     * 
     * @param dbName Name of the DB to read
     * @return The DB read from file, or an empty DB if file doesn't exist
     */
    private DB readExistingFile(String dbName) {
        String path = dbName + ".tdl";
        if (fileExists(path)) {
            URI dbURI = URI.createPlatformResourceURI(
                    this.file.getFullPath().removeLastSegments(1).append(path).toString(), true);
            DeploymentModel deploymentModel = (DeploymentModel) resourceSet.getResource(dbURI, true).getContents()
                    .get(0);
            List<DB> allContentsOfType = EcoreUtil2.getAllContentsOfType(deploymentModel, DB.class);
            return allContentsOfType.size() == 1 ? allContentsOfType.get(0) : getEmptyDB(dbName);
        } else {
            return getEmptyDB(dbName);
        }
    }

    /**
     * Adds the DBType and Parameters from the given TemplateBuffer to the given DB
     * 
     * @param db     The DB that should have all attributes from the template
     * @param buffer The chosen TemplateBuffer
     */
    protected DB useBufferOnDB(DB db, DB templateDB) {
        db.setType(templateDB.getType());
        db.getParameters().clear();
        Collection<Property> parameters = EcoreUtil.copyAll(templateDB.getParameters());
        db.getParameters().addAll(parameters);
        return db;
    }

    /**
     * Finds a TemplateBuffer by name
     * 
     * @param templates    the Pair of Template DB and TemplateBuffer
     * @param templateName the template to find
     * @return the wanted TemplateBuffer
     */
    protected Pair<DB, TemplateBuffer> getDBTemplateByName(ArrayList<Pair<DB, TemplateBuffer>> templates,
            String templateName) {
        return templates.stream().filter(pair -> pair.firstValue.getName().equalsIgnoreCase(templateName)).findFirst()
                .orElse(null);
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
        for (String dbName : validationList.keySet()) {
            Button existingModelCheck = validationList.get(dbName);
            String path = dbName + ".tdl";
            if (existingModelCheck.getSelection()) {
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
        URI uri = URI.createPlatformResourceURI(
                this.file.getFullPath().removeLastSegments(1).append(fileName).toString(), true);
        return resourceSet.getResource(uri, false) != null;
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
