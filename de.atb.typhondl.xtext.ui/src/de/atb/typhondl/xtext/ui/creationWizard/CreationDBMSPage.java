package de.atb.typhondl.xtext.ui.creationWizard;

/*-
 * #%L
 * de.atb.typhondl.xtext.ui
 * %%
 * Copyright (C) 2018 - 2020 ATB
 * %%
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * #L%
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;
import org.xml.sax.SAXException;

import de.atb.typhondl.xtext.typhonDL.Credentials;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Environment;
import de.atb.typhondl.xtext.typhonDL.HelmList;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.Property;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.modelUtils.ModelService;
import de.atb.typhondl.xtext.ui.technologies.ITechnology;
import de.atb.typhondl.xtext.ui.technologies.SupportedTechnologies;
import de.atb.typhondl.xtext.ui.technologies.TechnologyFactory;
import de.atb.typhondl.xtext.ui.utilities.MLmodelReader;
import de.atb.typhondl.xtext.ui.utilities.Pair;
import de.atb.typhondl.xtext.ui.utilities.PreferenceReader;
import de.atb.typhondl.xtext.ui.utilities.Quadruple;

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
     * list of DBs given to the next pages
     */
    private ArrayList<DB> result;

    /**
     * Store some buttons for validation <br>
     * TODO validation should be better maybe with IInputValidator
     */
    private HashMap<String, Button> fileNameValidationList;
    private HashMap<DB, Button> helmValidationList;

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

    private XtextResourceSet resourceSet;

    /**
     * Map of every field created on this page. If a field get's changed, the
     * Boolean is set to true and the {@link CreateModelWizard} updates the
     * corresponding {@link CreationDatabasePage}
     */
    private HashMap<String, Boolean> changedField;

    private ITechnology chosenTechnology;

    private Composite main;

    private int pageWidth = 607;

    private ArrayList<Quadruple<Composite, GridData, Combo, String[]>> hiddenThings;

    /**
     * Creates a CreationDBMSPage, reading the ML model from the given file.
     * 
     * @param pageName
     * @param file     ML model file
     */
    public CreationDBMSPage(String pageName, IFile file, ITechnology chosenTechnology) {
        this(pageName, file, readModel(file));
        this.chosenTechnology = chosenTechnology == null
                ? TechnologyFactory.createTechnology(SupportedTechnologies.values()[0])
                : chosenTechnology;
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
        this.result = new ArrayList<>();
        this.fileNameValidationList = new HashMap<>();
        this.changedField = new HashMap<>();
        this.hiddenThings = new ArrayList<>();
        addResources();
    }

    /**
     * Gets the provided ResourceSet and adds all .tdl files to the ResourceSet
     */
    private void addResources() {
        this.resourceSet = ModelService
                .getResourceSet(Activator.getInstance().getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL)
                        .getInstance(XtextLiveScopeResourceSetProvider.class), file);
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
        main = new Composite(scrolling, SWT.NONE);
        scrolling.setContent(main);
        scrolling.setExpandVertical(true);
        scrolling.setExpandHorizontal(true);
        scrolling.setVisible(true);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        main.setLayout(new GridLayout(1, false));
        helmValidationList = new HashMap<>();
        for (Pair<String, String> dbFromML : MLmodel) {

            // empty DB model object with the name taken from the ML model
            DB db = getEmptyDB(dbFromML.firstValue);

            // get templates
            ArrayList<DB> templates = PreferenceReader.getBuffers(dbFromML.secondValue);
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

            createFields(group, templates, db);
        }
        validate();
        main.setSize(main.computeSize(pageWidth, SWT.DEFAULT));
        scrolling.setMinSize(main.computeSize(pageWidth, SWT.DEFAULT));
        setControl(scrolling);
    }

    private void createFields(Group group, ArrayList<DB> templates, DB db) {
        String dbName = db.getName();
        // The DBs have the template's name.
        DB[] dbTemplates = templates.toArray(new DB[0]);
        String[] dbTemplateNames = Arrays.asList(dbTemplates).stream().map(dbTemplate -> dbTemplate.getName())
                .collect(Collectors.toList()).toArray(new String[0]);
        GridData wideGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        wideGridData.horizontalSpan = 2;

        Button existingModelCheck = new Button(group, SWT.CHECK);
        existingModelCheck.setText("Use existing " + dbName + ".tdl file in this project folder");
        existingModelCheck.setSelection(fileExists(dbName + ".tdl"));
        if (existingModelCheck.getSelection()) {
            result.add(readExistingFile(dbName));
        }
        existingModelCheck.setLayoutData(wideGridData);
        existingModelCheck.setToolTipText("Check this box if you already have a model file for " + dbName);
        fileNameValidationList.put(dbName, existingModelCheck);

        Button externalDatabaseCheck = new Button(group, SWT.CHECK);
        externalDatabaseCheck.setText("Use existing database for " + dbName + " (please select DBMS from Templates)");
        externalDatabaseCheck.setSelection(false);
        externalDatabaseCheck.setLayoutData(wideGridData);
        externalDatabaseCheck
                .setToolTipText("Check this box if you already have this database outside of the polystore");

        Composite hidden = new Composite(group, SWT.NONE);
        hidden.setLayout(new GridLayout(1, false));
        GridData hiddenData = new GridData(SWT.FILL, SWT.FILL, true, true);
        hiddenData.exclude = !chosenTechnology.canUseHelm();
        hiddenData.horizontalSpan = 2;
        hidden.setLayoutData(hiddenData);

        Button useHelmChartCheck = new Button(hidden, SWT.CHECK);
        useHelmChartCheck.setText("Use Helm chart (please select DBMS from Templates)");
        useHelmChartCheck.setSelection(false);
        useHelmChartCheck.setToolTipText("Use a Helm chart for one of the supported technologies");
        helmValidationList.put(db, useHelmChartCheck);

        new Label(group, NONE).setText("Choose Template:");
        Combo combo = new Combo(group, SWT.READ_ONLY);
        combo.setItems(chosenTechnology.canUseHelm() ? dbTemplateNames : removeHelmTemplates(dbTemplateNames));
        combo.setText(dbTemplateNames[0]);
        // set initial dbTemplate
        if (!existingModelCheck.getSelection()) {
            result.add(useBufferOnDB(db, templates.get(0)));
        }
        changedField.put(dbName, false);
        hiddenThings
                .add(new Quadruple<Composite, GridData, Combo, String[]>(hidden, hiddenData, combo, dbTemplateNames));

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
                DB newDB;
                removeDBfromResult(dbName);
                if (useExistingModel) {
                    newDB = readExistingFile(dbName);
                } else {
                    DB template = getDBTemplateByName(templates, combo.getText());
                    newDB = useBufferOnDB(db, template);
                }
                newDB.setExternal(externalDatabaseCheck.getSelection());
                result.add(newDB);
                changedField.put(newDB.getName(), true);
                updateHelmList(dbName, newDB);
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
                combo.setText(combo.getItem(0));
                DB template = getDBTemplateByName(templates, combo.getText());
                DB newDB = useBufferOnDB(db, template);
                newDB.setExternal(useExternalDatabase);
                clearEverythingExceptTypeAndCredentials(newDB);
                result.add(newDB);
                changedField.put(newDB.getName(), true);
                updateHelmList(dbName, newDB);
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
                if (useHelmChart) {
                    combo.setText(getFirstHelmTemplate(combo.getItems()));
                } else {
                    combo.setText(combo.getItem(0));
                }
                DB template = getDBTemplateByName(templates, combo.getText());
                DB newDB = useBufferOnDB(db, template);
                newDB.setExternal(externalDatabaseCheck.getSelection());
                result.add(newDB);
                changedField.put(newDB.getName(), true);
                updateHelmList(dbName, newDB);
                validate();
            }
        });

        combo.setEnabled(!existingModelCheck.getSelection());
        combo.setToolTipText("Choose specific DBMS Template for " + dbName);
        combo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeDBfromResult(dbName);
                DB template = getDBTemplateByName(templates, combo.getText());
                DB newDB = useBufferOnDB(db, template);
                if (externalDatabaseCheck.getSelection()) {
                    newDB.setExternal(true);
                    clearEverythingExceptTypeAndCredentials(newDB);
                }
                result.add(newDB);
                changedField.put(newDB.getName(), true);
                updateHelmList(dbName, newDB);
                validate();
            }
        });

    }

    protected void updateHelmList(String dbName, DB newDB) {
        DB db = findDBByNameInList(dbName,
                helmValidationList.keySet().stream().collect(Collectors.toCollection(ArrayList::new)));
        helmValidationList.put(newDB, helmValidationList.get(db));
        if (!db.equals(newDB)) {
            helmValidationList.remove(db);
        }
    }

    protected String getFirstHelmTemplate(String[] items) {
        for (String string : items) {
            if (string.toLowerCase().contains("helm")) {
                return string;
            }
        }
        return "ERROR";
    }

    /**
     * When a database is external, the API still has to know about the username and
     * password so every property except the address and credentials are removed
     * 
     * @param newDB
     */
    protected void clearEverythingExceptTypeAndCredentials(DB newDB) {
        newDB.getParameters().clear();
        newDB.setHelm(null);
        newDB.setImage(null);
    }

    private String[] removeHelmTemplates(String[] dbTemplateNames) {
        ArrayList<String> listWithoutHelm = new ArrayList<>();
        for (String string : dbTemplateNames) {
            if (!string.toLowerCase().contains("helm")) {
                listWithoutHelm.add(string);
            }
        }
        return listWithoutHelm.toArray(new String[0]);
    }

    /**
     * Removes DB with dbName from the result map
     * 
     * @param dbName The name of the DB to delete from result map
     */
    protected void removeDBfromResult(String dbName) {
        DB dbToRemove = findDBByNameInList(dbName, result);
        if (dbToRemove != null) {
            result.remove(dbToRemove);
        }
    }

    private DB findDBByNameInList(String dbName, ArrayList<DB> list) {
        DB dbToRemove = null;
        for (DB db : list) {
            if (db.getName().equalsIgnoreCase(dbName)) {
                dbToRemove = db;
            }
        }
        return dbToRemove;
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
     * Adds the DBType, Parameters and helm (if exists) from the given
     * TemplateBuffer to the given DB
     * 
     * @param db     The DB that should have all attributes from the template
     * @param buffer The chosen TemplateBuffer
     */
    protected DB useBufferOnDB(DB db, DB templateDB) {
        db.setType(templateDB.getType());
        db.getParameters().clear();
        db.setHelm(null);
        Collection<Property> parameters = EcoreUtil.copyAll(templateDB.getParameters());
        db.getParameters().addAll(parameters);
        if (templateDB.getHelm() != null) {
            HelmList helm = EcoreUtil.copy(templateDB.getHelm());
            db.setHelm(helm);
        }
        if (templateDB.getCredentials() != null) {
            Credentials credentials = EcoreUtil.copy(templateDB.getCredentials());
            db.setCredentials(credentials);
        }
        if (templateDB.getEnvironment() != null) {
            Environment environment = EcoreUtil.copy(templateDB.getEnvironment());
            db.setEnvironment(environment);
        }
        if (templateDB.getUri() != null) {
            de.atb.typhondl.xtext.typhonDL.URI uri = EcoreUtil.copy(templateDB.getUri());
            db.setUri(uri);
        }
        if (templateDB.getImage() != null) {
            IMAGE image = EcoreUtil.copy(templateDB.getImage());
            db.setImage(image);
        }
        return db;
    }

    /**
     * Finds a TemplateBuffer by name
     * 
     * @param templates    the Pair of Template DB and TemplateBuffer
     * @param templateName the template to find
     * @return the wanted TemplateBuffer
     */
    protected DB getDBTemplateByName(ArrayList<DB> templates, String templateName) {
        return templates.stream().filter(db -> db.getName().equalsIgnoreCase(templateName)).findFirst().orElse(null);
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
     * 
     * Shows error if the chosen template contains a HelmList but the
     * useHelmChartButton is not checked
     */
    protected void validate() {
        Status status = null;
        ArrayList<String> warning = new ArrayList<String>();
        for (String dbName : fileNameValidationList.keySet()) {
            Button existingModelCheck = fileNameValidationList.get(dbName);
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
        for (DB db : helmValidationList.keySet()) {
            if (db.getHelm() != null && !helmValidationList.get(db).getSelection()) {
                status = new Status(IStatus.ERROR, "Wizzard",
                        "The Template for " + db.getName() + " contains a helm key. Please check \"Use Helm chart\" ");
            }
            if (db.getHelm() == null && helmValidationList.get(db).getSelection()) {
                status = new Status(IStatus.ERROR, "Wizzard",
                        "Please select a template for " + db.getName() + " that contains Helm configuration ");
            }
        }

        if (!warning.isEmpty() && status == null) {
            status = new Status(IStatus.WARNING, "Wizard", "Database file(s) " + Arrays.toString(warning.toArray())
                    + " already exist(s) and will be overwritten if you continue");
        }

        setStatus(status);
    }

    public void updateData(ITechnology chosenTechnology) {
        final boolean canUseHelm = chosenTechnology.canUseHelm();
        if (this.chosenTechnology.canUseHelm() != canUseHelm) {
            this.chosenTechnology = chosenTechnology;
            for (Quadruple<Composite, GridData, Combo, String[]> quadruple : hiddenThings) {
                quadruple.first.setVisible(canUseHelm);
                quadruple.second.exclude = !canUseHelm;
                quadruple.third.setItems(canUseHelm ? quadruple.fourth : removeHelmTemplates(quadruple.fourth));
                quadruple.third.setText(quadruple.third.getItem(0));
            }
            main.layout(true);
            main.setSize(main.computeSize(pageWidth, SWT.DEFAULT));
            ((ScrolledComposite) main.getParent()).setMinSize(main.computeSize(pageWidth, SWT.DEFAULT));
        }
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
     * Get a list of DBs taken from the MLmodel enriched with wizard input
     * 
     * @return DBs
     */
    public ArrayList<DB> getResult() {
        return result;
    }

    /**
     * Gives info whether a field associated with the database has changed
     * 
     * @param databaseName
     * @return
     */
    public boolean hasFieldChanged(String databaseName) {
        return changedField.get(databaseName);
    }

    public void setFieldChanged(String databaseName, boolean changed) {
        changedField.put(databaseName, changed);
    }

}
