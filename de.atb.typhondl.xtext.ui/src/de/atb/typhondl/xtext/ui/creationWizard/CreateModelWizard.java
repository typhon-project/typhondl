package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.xtext.ui.util.FileOpener;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.ui.activator.Activator;

public class CreateModelWizard extends Wizard {

	private IFile MLmodel;
	private CreationMainPage mainPage;
	private CreationDBMSPage dbmsPage;
	private CreationAnalyticsPage analyticsPage;
	private CreationTemplateVariablePage variablePage;
	private int chosenTemplate;

	public CreateModelWizard(IFile MLmodel) {
		super();
		this.MLmodel = MLmodel;
	}

	@Override
	public void addPages() {
		mainPage = new CreationMainPage("Create new DL model", MLmodel.getLocationURI());
		addPage(mainPage);

		dbmsPage = new CreationDBMSPage("Choose DBMS", MLmodel);
		addPage(dbmsPage);
	}

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
		if (dbmsPage.isHasTemplateVariables()) {
			// TODO
		}
		ModelCreator modelCreator = new ModelCreator(MLmodel, mainPage.getDLmodelName());
		// create DL model
		HashMap<DB, TemplateVariable[]> result = dbmsPage.getResult();
		IFile file = modelCreator.createDLmodel(new ArrayList<DB>(result.keySet()), chosenTemplate, properties);
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

	@Override
	public boolean canFinish() {
		IWizardPage currentPage = this.getContainer().getCurrentPage();
		if (currentPage instanceof CreationMainPage) {
			return false;
		}
		if (currentPage instanceof CreationDBMSPage
				&& (mainPage.getUseAnalytics() || ((CreationDBMSPage) currentPage).isHasTemplateVariables())) {
			return false;
		}
		if (currentPage instanceof CreationTemplateVariablePage && mainPage.getUseAnalytics()) {
			return false;
		}
		return super.canFinish();
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page instanceof CreationMainPage) {
			this.chosenTemplate = ((CreationMainPage) page).getChosenTemplate();
		}
		if (page instanceof CreationDBMSPage && ((CreationDBMSPage) page).isHasTemplateVariables()) {
			this.variablePage = createVariablesPage("Template Variables Page", ((CreationDBMSPage) page).getResult());
			this.variablePage.setWizard(this);
			return variablePage;
		}
		if (mainPage.getUseAnalytics()) {
			if ((page instanceof CreationDBMSPage && !((CreationDBMSPage) page).isHasTemplateVariables())
					|| page instanceof CreationTemplateVariablePage) {
				this.analyticsPage = createAnalyticsPage("Analytics Page", this.mainPage.getProperties());
				this.analyticsPage.setWizard(this);
				return analyticsPage;
			}
		}
		return super.getNextPage(page);
	}

	private CreationTemplateVariablePage createVariablesPage(String string, HashMap<DB, TemplateVariable[]> result) {
		return new CreationTemplateVariablePage(string, result);
	}

	private CreationAnalyticsPage createAnalyticsPage(String string, Properties properties) {
		return new CreationAnalyticsPage(string, properties);
	}
}
