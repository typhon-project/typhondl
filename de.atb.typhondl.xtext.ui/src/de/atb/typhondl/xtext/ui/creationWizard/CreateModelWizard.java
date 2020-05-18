package de.atb.typhondl.xtext.ui.creationWizard;

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
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.xtext.ui.util.FileOpener;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.ui.activator.Activator;
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
    private final String PAGENAME_CONTAINER = "Container";
    private final String PAGENAME_ANALYTICS = "Analytics";

    /**
     * The chosen technology template from {@link SupportedTechnologies}
     */
    private int chosenTemplate;

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
    }

    /**
     * Two pages are initially added:
     * <li>Main Page {@link CreationMainPage}</li>
     * <li>DBMS Page {@link CreationDBMSPage}</li>
     */
    @Override
    public void addPages() {
        mainPage = new CreationMainPage("Create new DL model", MLmodel.getLocationURI());
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
        Properties properties;
        properties = this.mainPage.getProperties();
        HashMap<DB, ArrayList<Container>> result = ((CreationContainerPage) this.getPage(PAGENAME_CONTAINER))
                .getResult();
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

    /**
     * Checks if the TyphonDL Creation Wizard can finish.
     * 
     * @return <code>false</code> if
     *         <li>the current page is the Main Page or</li>
     *         <li>the current page is the DBMSPage or</li>
     *         <li>the current page is the TemplateVariablePage or</li>
     *         <li>the current page is the ContainerPage and the useAnalytics
     *         checkbox is checked</li>
     *         <p>
     *         Otherwise {@link Wizard#canFinish()}.
     */
    @Override
    public boolean canFinish() {
        IWizardPage currentPage = this.getContainer().getCurrentPage();
        if (currentPage instanceof CreationMainPage || currentPage instanceof CreationDBMSPage
                || currentPage instanceof CreationDatabasePage) {
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
            return this.getPage(getDBMSPageName(this.chosenTemplate));
        }
        if (page instanceof CreationDBMSPage) {
            HashMap<DB, TemplateBuffer> result = ((CreationDBMSPage) page).getResult();
            for (DB db : result.keySet()) {
                String pageName = PAGENAME_DATABASE + db.getName();
                if (!pageExists(pageName)) {
                    CreationDatabasePage databasePage = new CreationDatabasePage(pageName, db, result.get(db));
                    databasePage.setWizard(this);
                    addPage(databasePage);
                } else {
                    CreationDatabasePage databasePage = (CreationDatabasePage) this.getPage(pageName);
                    if (databasePage.getControl() != null) {
                        databasePage.updateParameterArea();
                        databasePage.updateTemplateVariablesArea();
                    }
                }
            }
            if (!pageExists(PAGENAME_CONTAINER)) {
                CreationContainerPage newPage = new CreationContainerPage(PAGENAME_CONTAINER,
                        new ArrayList<>(result.keySet()), this.chosenTemplate);
                newPage.setWizard(this);
                addPage(newPage);
            } else {
                ((CreationContainerPage) this.getPage(PAGENAME_CONTAINER)).setDBs(new ArrayList<>(result.keySet()));
            }
            // skip other DBMS pages
            IWizardPage nextPage = super.getNextPage(page);
            while (nextPage instanceof CreationDBMSPage) {
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

    private String getDBMSPageName(int templateOrdinal) {
        return PAGENAME_DBMS + SupportedTechnologies.values()[templateOrdinal].getClusterType();
    }

}
