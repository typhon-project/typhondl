package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.xtext.ui.util.FileOpener;

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

	/**
	 * The second page of this wizard. A template can be chosen for each Database
	 */
	private CreationDBMSPage dbmsPage;

	/**
	 * An optional page of this wizard. Kafka and Zookeeper settings can be entered
	 */
	private CreationAnalyticsPage analyticsPage;

	/**
	 * An optional page. Values for extracted {@link TemplateVariable}s can be
	 * entered
	 */
	private CreationTemplateVariablePage variablePage;

	/**
	 * The chosen technology template from {@link SupportedTechnologies}
	 */
	private int chosenTemplate;

	/**
	 * A page to define container specifications
	 */
	private CreationContainerPage containerPage;

	/**
	 * Creates an instance of <code>CreateModelWizard</code>
	 * 
	 * @param MLmodel the ML model used as input
	 */
	public CreateModelWizard(IFile MLmodel) {
		super();
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
		addPage(mainPage);

		dbmsPage = new CreationDBMSPage("Choose DBMS", MLmodel);
		addPage(dbmsPage);
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
		if (dbmsPage.getMessage() != null) {
			if (!MessageDialog.openConfirm(this.getShell(), "Wizard", dbmsPage.getMessage())) {
				return false;
			}
		}
		if (mainPage.getMessage() != null) {
			if (!MessageDialog.openConfirm(this.getShell(), "Wizard", mainPage.getMessage())) {
				return false;
			}
		}
		Properties properties;
		if (mainPage.getUseAnalytics()) {
			properties = this.analyticsPage.getProperties();
		} else {
			properties = this.mainPage.getProperties();
		}
		ArrayList<DB> dbs = new ArrayList<>();
		if (dbmsPage.hasTemplateVariables()) {
			dbs = variablePage.getDBs();
		} else {
			dbs = new ArrayList<DB>(dbmsPage.getResult().keySet());
		}
		ModelCreator modelCreator = new ModelCreator(MLmodel, mainPage.getDLmodelName());
		// create DL model
		IFile file = modelCreator.createDLmodel(dbs, chosenTemplate, properties);
		// get fileOpener
		FileOpener fileOpener = Activator.getInstance().getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL)
				.getInstance(FileOpener.class);
		// main DL model gets selected in Project Explorer
		fileOpener.selectAndReveal(file);
		// main DL model is opened in editor
		fileOpener.openFileToEdit(this.getShell(), file);
		// save properties
		String location = file.getLocation().toString();
		String pathToProperties = location.substring(0, location.lastIndexOf('/') + 1) + "polystore.properties";
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
				|| currentPage instanceof CreationTemplateVariablePage) {
			return false;
		}
		if (currentPage instanceof CreationContainerPage && mainPage.getUseAnalytics()) {
			return false;
		}
		return super.canFinish();
	}

	/**
	 * Returns the next page of the TyphonDL Creation Wizard.
	 * 
	 * @return
	 *         <li>{@link Wizard#getNextPage(IWizardPage)}</li>
	 *         <li>if the current page is the {@link CreationDBMSPage} and the
	 *         useAnalytics checkbox is checked, Then a
	 *         {@link CreationAnalyticsPage} is created and returned.</li>
	 *         <li>if the current page is the {@link CreationDBMSPage} and a
	 *         template with {@link TemplateVariable}s is used, a
	 *         {@link CreationTemplateVariablePage} is created and returned,
	 *         otherwise a {@link CreationContainerPage} is returned.</li>
	 *         <li>if the current page is the {@link CreationTemplateVariablePage} a
	 *         {@link CreationContainerPage} is returned.</li>
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page instanceof CreationMainPage) {
			this.chosenTemplate = ((CreationMainPage) page).getChosenTemplate();
		}
		if (page instanceof CreationDBMSPage) {
			if (((CreationDBMSPage) page).hasTemplateVariables()) {
				this.variablePage = createVariablesPage("Template Variables Page",
						((CreationDBMSPage) page).getResult());
				this.variablePage.setWizard(this);
				return variablePage;
			} else {
				this.containerPage = createContainerPage("Container Definition Page",
						new ArrayList<>(((CreationDBMSPage) page).getResult().keySet()));
				this.containerPage.setWizard(this);
				return containerPage;
			}
		}
		if (page instanceof CreationTemplateVariablePage) {
			this.containerPage = createContainerPage("Container Definition Page",
					((CreationTemplateVariablePage) page).getDBs());
			this.containerPage.setWizard(this);
			return containerPage;
		}
		if (mainPage.getUseAnalytics()) {
			if ((page instanceof CreationDBMSPage && !((CreationDBMSPage) page).hasTemplateVariables())
					|| page instanceof CreationTemplateVariablePage) {
				this.analyticsPage = createAnalyticsPage("Analytics Page", this.mainPage.getProperties());
				this.analyticsPage.setWizard(this);
				return analyticsPage;
			}
		}
		return super.getNextPage(page);
	}

	private CreationContainerPage createContainerPage(String string, ArrayList<DB> dbs) {
		return new CreationContainerPage(string, dbs);
	}

	/**
	 * Creates a CreationTemplateVariablePage
	 * 
	 * @param string Name of the Page
	 * @param result Map of {@link DB}s and their {@link TemplateBuffer}
	 * @return a new {@link CreationTemplateVariablePage}
	 */
	private CreationTemplateVariablePage createVariablesPage(String string, HashMap<DB, TemplateBuffer> result) {
		return new CreationTemplateVariablePage(string, result);
	}

	/**
	 * Creates a CreationAnalyticsPage
	 * 
	 * @param string     Name of the Page
	 * @param properties default properties for the polystore
	 * @return a new {@link CreationAnalyticsPage}
	 */
	private CreationAnalyticsPage createAnalyticsPage(String string, Properties properties) {
		return new CreationAnalyticsPage(string, properties);
	}
}
