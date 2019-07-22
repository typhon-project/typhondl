package de.atb.typhondl.xtext.ui.creationWizard;

import java.net.URI;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.ui.creationWizard.CreationAnalyticsPage.InputField;
import de.atb.typhondl.xtext.ui.wizard.Database;

public class CreateModelWizard extends Wizard {

	private IPath MLmodelPath;
	private CreationMainPage mainPage;
	private CreationDBMSPage dbmsPage;
	private CreationAnalyticsPage analyticsPage;
	private int chosenTemplate;
	private HashMap<String, InputField> analyticsSettings;
	private URI MLmodelURI;
	

	@Override
	public void addPages() {
		mainPage = new CreationMainPage("Create new DL model", MLmodelURI);
		addPage(mainPage);

		dbmsPage = new CreationDBMSPage("Choose DBMS", MLmodelURI);
		addPage(dbmsPage);
		
		//analyticsPage = new CreationAnalyticsPage("Analytics Configuration");
		//addPage(analyticsPage);
	}

	@Override
	public boolean performFinish() {
		if (dbmsPage.getMessage() != null) {
			return MessageDialog.openConfirm(this.getShell(), "Wizard", dbmsPage.getMessage());
		}
		if (mainPage.getUseAnalytics()) {
			this.analyticsSettings = this.analyticsPage.getAnalyticsSettings();
		}
		DeploymentModel DLmodel = ModelCreator.createDLmodel(analyticsSettings, dbmsPage.getDatabases(),
				MLmodelPath, chosenTemplate);
		//createTemplate(DLmodel);
		return true;
	}

	private void createTemplate(DeploymentModel DLmodel) {
		Template template = TemplateFactory.createTemplate(chosenTemplate, DLmodel);
	}

	public void setModelPath(IPath path) {
		this.MLmodelPath = path;
		this.MLmodelURI = path.toFile().toURI();
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		System.out.println(page.getName());
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
