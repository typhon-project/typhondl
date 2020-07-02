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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.xtext.ui.util.FileOpener;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.utilities.Pair;
import de.atb.typhondl.xtext.ui.utilities.PropertiesLoader;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

/**
 * The TyphonDL Creation Wizard consists of:
 * <li>A {@link CreationMainPage} to enter the DL model name, select a
 * technology (i.e. Docker Compose or Kubernetes), and specify API IP and
 * port.</li>
 * <li>A {@link CreationDBMSPage} to select the DBMS (i.e. database template)
 * for each database extracted from the ML model.</li>
 * <li>An otional {@link CreationTemplateVariablePage} to enter values for
 * template variables if the previously chosen template contains template
 * variables.</li>
 * <li>An otional {@link CreationAnalyticsPage} to enter Kafka and Zookeeper
 * configuration if the Typhon Analytics component is used.</li>
 * 
 * @author flug
 *
 */
public class CreateModelWizard extends Wizard {

    /**
     * input ML model
     */
    private IFile MLmodel;

    /**
     * The first page of this wizard.
     */
    private CreationMainPage mainPage;

    private final String PAGENAME_DBMS = "DBMS"; // + chosenTemplate.ClusterType
    private final String PAGENAME_DATABASE = "Database"; // + DatabaseName
    private final String PAGENAME_ANALYTICS = "Analytics";

    /**
     * To have a Scrollbar, a minSize has to be set. Somehow the page's width is
     * always 607, independent of what's given here //TODO wtf?
     */
    private final int pageWidth = 607;

    /**
     * The chosen technology template from {@link SupportedTechnologies}
     */
    private int chosenTemplate;

    private Properties properties;

    /**
     * Creates an instance of <code>CreateModelWizard</code>
     * 
     * @param MLmodel the ML model used as input
     */
    public CreateModelWizard(IFile MLmodel) {
        super();
        setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "icons/TYPHON Logo Small.png"));
        setWindowTitle("TyphonDL Creation Wizard");
        this.MLmodel = MLmodel;
        try {
            this.properties = PropertiesLoader.loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Two types of pages are initially added:
     * <li>Main Page {@link CreationMainPage}</li>
     * <li>one DBMS Page {@link CreationDBMSPage} for every
     * {@link SupportedTechnologies}</li>
     */
    @Override
    public void addPages() {
        mainPage = new CreationMainPage("Create new DL model", MLmodel.getLocationURI(), properties);
        mainPage.setWizard(this);
        addPage(mainPage);

        for (SupportedTechnologies value : SupportedTechnologies.values()) {
            CreationDBMSPage newPage = new CreationDBMSPage(PAGENAME_DBMS + value.getClusterType(), MLmodel,
                    value.ordinal());
            newPage.setWizard(this);
            addPage(newPage);
        }
    }

    /**
     * Defines the finish processing of the TyphonDL Creation Wizard. The TyphonDL
     * model is created with a {@link ModelCreator} and opened in the Xtext editor.
     * The entered properties are also saved in a .properties file in the user's
     * project directory
     * 
     */
    @Override
    public boolean performFinish() {
        String message = this.getPage(getDBMSPageName(chosenTemplate)).getMessage();
        if (message != null) {
            if (!MessageDialog.openConfirm(this.getShell(), "Wizard", message)) {
                return false;
            }
        }
        if (mainPage.getMessage() != null) {
            if (!MessageDialog.openConfirm(this.getShell(), "Wizard", mainPage.getMessage())) {
                return false;
            }
        }
        HashMap<DB, Container> result = getDabasesAndContainers();
        // TODO remove, if replication works with authentication:
        result = removeMongoCredentialsIfReplicated(result);
        ModelCreator modelCreator = new ModelCreator(MLmodel, mainPage.getDLmodelName());
        // create DL model
        IFile file = modelCreator.createDLmodel(result, chosenTemplate, properties);
        // get fileOpener
        FileOpener fileOpener = Activator.getInstance().getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL)
                .getInstance(FileOpener.class);
        // main DL model gets selected in Project Explorer
        fileOpener.selectAndReveal(file);
        // main DL model is opened in editor
        fileOpener.openFileToEdit(this.getShell(), file);
        // save properties
        String location = file.getLocation().toString();
        String pathToProperties = location.substring(0, location.lastIndexOf('/') + 1) + file.getName() + ".properties";
        try {
            OutputStream output = new FileOutputStream(pathToProperties);
            properties.store(output, "Only edit this if you know what you are doing!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private HashMap<DB, Container> removeMongoCredentialsIfReplicated(HashMap<DB, Container> result) {
        for (DB db : result.keySet()) {
            if (db.getType().getName().equalsIgnoreCase("mongo")) {
                if (result.get(db) != null && result.get(db).getReplication() != null) {
                    db.setCredentials(null);
                    MessageDialog.openInformation(this.getShell(), "Wizard",
                            "In this version replication of mongo does not allow authentication");
                }
            }
        }
        return result;
    }

    private HashMap<DB, Container> getDabasesAndContainers() {
        HashMap<DB, Container> result = new HashMap<>();
        for (IWizardPage page : this.getPages()) {
            if (page.getName().contains(PAGENAME_DATABASE)) {
                Pair<DB, Container> fromDatabasePage = ((CreationDatabasePage) page).getResultPair();
                result.put(fromDatabasePage.firstValue, fromDatabasePage.secondValue);
            }
        }
        return result;
    }

    /**
     * TODO Checks if the TyphonDL Creation Wizard can finish.
     * 
     * @return <code>false</code> if
     *         <li>the current page is the Main Page or</li>
     *         <li>the current page is the DBMSPage or</li>
     *         <li>the current page is a DatabasePage and the next page as well</li>
     *         <p>
     *         Otherwise {@link Wizard#canFinish()}.
     */
    @Override
    public boolean canFinish() {
        IWizardPage currentPage = this.getContainer().getCurrentPage();
        if (currentPage instanceof CreationMainPage || currentPage instanceof CreationDBMSPage
                || currentPage instanceof CreationAnalyticsPage) {
            return false;
        }
        if (currentPage instanceof CreationDatabasePage && currentPage.getNextPage() instanceof CreationDatabasePage) {
            return false;
        }
        return super.canFinish();
    }

    /**
     * Returns the next page of the TyphonDL Creation Wizard. The pages for each
     * databases and the container definition page are created. Since this method is
     * called whenever something changes on a page (e.g. entering text) the input
     * for the created database and container pages can change and thus has to be
     * updated
     * 
     * @return the next page
     */
    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        if (page instanceof CreationMainPage) {
            this.chosenTemplate = ((CreationMainPage) page).getChosenTemplate();
            this.properties = ((CreationMainPage) page).getProperties();
            if (properties.get("polystore.useAnalytics").equals("true")) {
                if (!analyticsPagesExist()) {
                    for (SupportedTechnologies value : SupportedTechnologies.values()) {
                        CreationAnalyticsPage newPage = new CreationAnalyticsPage(
                                PAGENAME_ANALYTICS + value.getClusterType(), properties, value.ordinal());
                        newPage.setWizard(this);
                        addPage(newPage);
                    }
                } else {
                    CreationAnalyticsPage creationAnalyticsPage = (CreationAnalyticsPage) this
                            .getPage(getAnalyticsPageName(this.chosenTemplate));
                    if (creationAnalyticsPage.getControl() != null) {
                        creationAnalyticsPage.updateData(properties);
                    }
                }
                return this.getPage(getAnalyticsPageName(this.chosenTemplate));
            } else {
                return this.getPage(getDBMSPageName(this.chosenTemplate));
            }
        }
        if (page instanceof CreationAnalyticsPage) {
            this.properties = ((CreationAnalyticsPage) page).getProperties();
            return this.getPage(getDBMSPageName(this.chosenTemplate));
        }
        if (page instanceof CreationDBMSPage) {
            ArrayList<DB> result = ((CreationDBMSPage) page).getResult();
            for (DB db : result) {
                String pageName = PAGENAME_DATABASE + db.getName();
                if (!pageExists(pageName)) {
                    CreationDatabasePage databasePage = new CreationDatabasePage(pageName, db, chosenTemplate,
                            properties, pageWidth);
                    databasePage.setWizard(this);
                    addPage(databasePage);
                } else {
                    CreationDatabasePage databasePage = (CreationDatabasePage) this.getPage(pageName);
                    if (((CreationDBMSPage) page).hasFieldChanged(db.getName())
                            || databasePage.getChosenTechnology() != this.chosenTemplate) {
                        databasePage.setChosenTechnology(this.chosenTemplate);
                        databasePage.setDB(db);
                        ((CreationDBMSPage) page).setFieldChanged(db.getName(), false);
                        if (databasePage.getControl() != null) {
                            databasePage.updateAllAreas();
                        }
                    }
                }
            }
            // skip other DBMS pages
            IWizardPage nextPage = super.getNextPage(page);
            while (nextPage instanceof CreationDBMSPage || nextPage instanceof CreationAnalyticsPage) {
                nextPage = super.getNextPage(nextPage);
            }
            return nextPage;
        }
        return super.getNextPage(page);

    }

    /**
     * Checks if a page already exists
     * 
     * @param pageName The name of the page to check
     * @return true if page exists, false otherwise
     */
    private boolean pageExists(String pageName) {
        return Arrays.asList(this.getPages()).stream().anyMatch(page -> page.getName().equalsIgnoreCase(pageName));
    }

    private boolean analyticsPagesExist() {
        return pageExists(getAnalyticsPageName(0));
    }

    private String getAnalyticsPageName(int templateOrdinal) {
        return PAGENAME_ANALYTICS + SupportedTechnologies.values()[templateOrdinal].getClusterType();
    }

    private String getDBMSPageName(int templateOrdinal) {
        return PAGENAME_DBMS + SupportedTechnologies.values()[templateOrdinal].getClusterType();
    }

    public int getPageWidth() {
        return pageWidth;
    }

}
