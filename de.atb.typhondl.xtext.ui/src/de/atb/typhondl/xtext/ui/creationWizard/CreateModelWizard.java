package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import de.atb.typhondl.xtext.ui.creationWizard.CreationAnalyticsPage.InputField;

public class CreateModelWizard extends Wizard {

	private IFile MLmodel;
	private CreationMainPage mainPage;
	private CreationDBMSPage dbmsPage;
	private CreationAnalyticsPage analyticsPage;
	private int chosenTemplate;
	private HashMap<String, InputField> analyticsSettings;

	public CreateModelWizard(IFile MLmodel) {
		super();
		this.MLmodel = MLmodel;
	}

	@Override
	public void addPages() {
		mainPage = new CreationMainPage("Create new DL model", MLmodel.getLocationURI());
		addPage(mainPage);

		dbmsPage = new CreationDBMSPage("Choose DBMS", MLmodel.getLocationURI());
		addPage(dbmsPage);

		// analyticsPage = new CreationAnalyticsPage("Analytics Configuration");
		// addPage(analyticsPage);
	}

	@Override
	public boolean performFinish() {
		if (dbmsPage.getMessage() != null) {
			return MessageDialog.openConfirm(this.getShell(), "Wizard", dbmsPage.getMessage());
		}
		if (mainPage.getUseAnalytics()) {
			this.analyticsSettings = this.analyticsPage.getAnalyticsSettings();
		} else {
			this.analyticsSettings = null;
		}
		ModelCreator modelCreator = new ModelCreator(MLmodel);
		modelCreator.createDLmodel(analyticsSettings, dbmsPage.getDatabases(), chosenTemplate);
		return true;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page instanceof CreationMainPage) {
			this.chosenTemplate = ((CreationMainPage) page).getChosenTemplate();
		}
		if (page instanceof CreationDBMSPage && mainPage.getUseAnalytics()) {
			this.analyticsPage = createAnalyticsPage("Analytics Page");
			this.analyticsPage.setWizard(this);
			return analyticsPage;
		}
		return super.getNextPage(page);
	}

	private CreationAnalyticsPage createAnalyticsPage(String string) {
		return new CreationAnalyticsPage(string);
	}
}
