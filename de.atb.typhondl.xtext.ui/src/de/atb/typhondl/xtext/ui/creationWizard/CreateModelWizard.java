package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.xtext.ui.util.FileOpener;

import de.atb.typhondl.xtext.ui.activator.Activator;
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
		if (mainPage.getUseAnalytics()) {
			this.analyticsSettings = this.analyticsPage.getAnalyticsSettings();
		} else {
			this.analyticsSettings = null;
		}
		ModelCreator modelCreator = new ModelCreator(MLmodel, mainPage.getDLmodelName());
		// create DL model
		IFile file = modelCreator.createDLmodel(analyticsSettings, dbmsPage.getDatabases(), chosenTemplate);
		// get fileOpener
		FileOpener fileOpener = Activator.getInstance().getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL)
				.getInstance(FileOpener.class);
		// main DL model gets selected in Project Explorer
		fileOpener.selectAndReveal(file);
		// main DL model is opened in editor
		fileOpener.openFileToEdit(this.getShell(), file);		
		return true;
	}


	@Override
	public boolean canFinish() {
		if (this.getContainer().getCurrentPage() instanceof CreationMainPage) {
			return false;
		}
		if (this.getContainer().getCurrentPage() instanceof CreationDBMSPage && mainPage.getUseAnalytics()) {
			return false;
		}
		return super.canFinish();
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
