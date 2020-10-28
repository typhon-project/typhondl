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
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.Pair;
import de.atb.typhondl.xtext.ui.utilities.PropertiesLoader;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

/**
 * The TyphonDL Creation Wizard consists of:
 * <li>A {@link CreationMainPage} to enter the DL model name, select a
 * technology (i.e. Docker Compose or Kubernetes), and specify API IP and
 * port.</li>
 * <li>A {@link CreationPolystorePage} to configure polystore settings</li>
 * <li>An optional {@link CreationAnalyticsPage} to enter Kafka and Zookeeper
 * configuration if the Typhon Analytics component is used.</li>
 * <li>An optional {@link CreationNLAEPage} to enter NLP and Flink
 * configuration</li>
 * <li>A {@link CreationDBMSPage} to select the DBMS (i.e. database template)
 * for each database extracted from the ML model.</li>
 * <li>A {@link CreationDatabasePage} for each database to enter database
 * specific configurations</li>
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

    private final String PAGENAME_DBMS = "DBMS";
    private final String PAGENAME_DATABASE = "Database"; // + DatabaseName
    private final String PAGENAME_ANALYTICS = "Analytics";
    private final String PAGENAME_NLAE = "NLAE";
    private final String PAGENAME_POLYSTORE = "Polystore";

    /**
     * To have a Scrollbar, a minSize has to be set. Somehow the page's width is
     * always 607, independent of what's given here //TODO wtf?
     */
    private final int pageWidth = 607;

    /**
     * The chosen technology template from {@link SupportedTechnologies}
     */
    private SupportedTechnologies chosenTechnology;

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
            this.properties = PropertiesLoader.loadPropertiesFromClasspath();
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

        CreationDBMSPage creationDBMSPage = new CreationDBMSPage(PAGENAME_DBMS, MLmodel, this.chosenTechnology);
        creationDBMSPage.setWizard(this);
        this.addPage(creationDBMSPage);
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
        String message = this.getPage(PAGENAME_DBMS).getMessage();
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
        IFile file = modelCreator.createDLmodel(result, chosenTechnology, properties);
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

    @Override
    public boolean canFinish() {
        IWizardPage currentPage = this.getContainer().getCurrentPage();
        if (currentPage instanceof CreationMainPage || currentPage instanceof CreationPolystorePage
                || currentPage instanceof CreationDBMSPage || currentPage instanceof CreationAnalyticsPage
                || currentPage instanceof CreationNLAEPage) {
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
            // next page is CreationPolystorePage
            this.chosenTechnology = ((CreationMainPage) page).getChosenTemplate();
            this.properties = ((CreationMainPage) page).getProperties();
            if (!pageExists(PAGENAME_POLYSTORE)) {
                CreationPolystorePage polystorePage = new CreationPolystorePage(PAGENAME_POLYSTORE, this.properties,
                        chosenTechnology);
                polystorePage.setWizard(this);
                addPage(polystorePage);
            } else {
                CreationPolystorePage creationPolystorePage = (CreationPolystorePage) this.getPage(PAGENAME_POLYSTORE);
                if (creationPolystorePage.getControl() != null) {
                    creationPolystorePage.updateData(properties, chosenTechnology);
                }
            }
            return this.getPage(PAGENAME_POLYSTORE);
        }

        if (page instanceof CreationPolystorePage) {
            // next page can be CreationAnalyticsPage, CreationNLAEPage or CreationDBMSPage
            if (properties.get(PropertiesService.POLYSTORE_USEANALYTICS).equals("true")
                    && properties.getProperty(PropertiesService.ANALYTICS_DEPLOYMENT_CREATE).equals("true")) {
                if (!analyticsPagesExist()) {
                    for (SupportedTechnologies value : SupportedTechnologies.values()) {
                        CreationAnalyticsPage newPage = new CreationAnalyticsPage(PAGENAME_ANALYTICS + value.name(),
                                properties, value);
                        newPage.setWizard(this);
                        addPage(newPage);
                    }
                } else {
                    CreationAnalyticsPage creationAnalyticsPage = (CreationAnalyticsPage) this
                            .getPage(getAnalyticsPageName());
                    if (creationAnalyticsPage.getControl() != null) {
                        creationAnalyticsPage.updateData(properties);
                    }
                }
                return this.getPage(getAnalyticsPageName());
            } else if (properties.getProperty(PropertiesService.POLYSTORE_USENLAE).equals("true")) {
                return getNLAEPage();
            }
            if (pageExists(PAGENAME_DBMS)) {
                CreationDBMSPage creationDBMSPage = (CreationDBMSPage) this.getPage(PAGENAME_DBMS);
                if (creationDBMSPage.getControl() != null) {
                    creationDBMSPage.updateData(this.chosenTechnology);
                }
            }
            return this.getPage(PAGENAME_DBMS);
        }

        if (page instanceof CreationAnalyticsPage) {
            // next page can be CreationNLAEPage or CreationDBMSPage
            this.properties = ((CreationAnalyticsPage) page).getProperties();
            if (properties.getProperty(PropertiesService.POLYSTORE_USENLAE).equals("true")) {
                return getNLAEPage();
            }
            return this.getPage(PAGENAME_DBMS);
        }
        if (page instanceof CreationNLAEPage) {
            // next page is CreationDBMSPage
            this.properties = ((CreationNLAEPage) page).getProperties();
            return this.getPage(PAGENAME_DBMS);
        }
        if (page instanceof CreationDBMSPage) {
            // next pages are the CreationDatabasePage(s)
            ArrayList<DB> result = ((CreationDBMSPage) page).getResult();
            for (DB db : result) {
                String pageName = PAGENAME_DATABASE + db.getName();
                if (!pageExists(pageName)) {
                    CreationDatabasePage databasePage = new CreationDatabasePage(pageName, db, chosenTechnology,
                            properties, pageWidth);
                    databasePage.setWizard(this);
                    addPage(databasePage);
                } else {
                    CreationDatabasePage databasePage = (CreationDatabasePage) this.getPage(pageName);
                    if (((CreationDBMSPage) page).hasFieldChanged(db.getName())
                            || databasePage.getChosenTechnology() != this.chosenTechnology) {
                        databasePage.setChosenTechnology(this.chosenTechnology);
                        databasePage.setDB(db);
                        ((CreationDBMSPage) page).setFieldChanged(db.getName(), false);
                        if (databasePage.getControl() != null) {
                            databasePage.updateAllAreas();
                        }
                    }
                }
            }
            // skip other pages to get to database pages
            IWizardPage nextPage = super.getNextPage(page);
            while (nextPage instanceof CreationDBMSPage || nextPage instanceof CreationAnalyticsPage
                    || nextPage instanceof CreationNLAEPage || nextPage instanceof CreationPolystorePage) {
                nextPage = super.getNextPage(nextPage);
            }
            return nextPage;
        }
        return super.getNextPage(page);

    }

    private CreationNLAEPage getNLAEPage() {
        if (!nlaePageExists()) {
            CreationNLAEPage newPage = new CreationNLAEPage(PAGENAME_NLAE, properties);
            newPage.setWizard(this);
            addPage(newPage);
        } else {
            CreationNLAEPage nlaePage = (CreationNLAEPage) this.getPage(PAGENAME_NLAE);
            if (nlaePage.getControl() != null) {
                nlaePage.updateData(properties);
            }
        }
        return (CreationNLAEPage) this.getPage(PAGENAME_NLAE);
    }

    private boolean nlaePageExists() {
        return pageExists(PAGENAME_NLAE);
    }

    private boolean pageExists(String pageName) {
        return Arrays.asList(this.getPages()).stream().anyMatch(page -> page.getName().equalsIgnoreCase(pageName));
    }

    private boolean analyticsPagesExist() {
        return pageExists(getAnalyticsPageName());
    }

    private String getAnalyticsPageName() {
        return PAGENAME_ANALYTICS + chosenTechnology.name();
    }

    public int getPageWidth() {
        return pageWidth;
    }

}
