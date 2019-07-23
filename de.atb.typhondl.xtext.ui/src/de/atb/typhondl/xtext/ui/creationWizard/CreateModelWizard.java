package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.creationWizard.CreationAnalyticsPage.InputField;

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
		DeploymentModel DLmodel = ModelCreator.createDLmodel(analyticsSettings, dbmsPage.getDatabases(), MLmodelPath,
				chosenTemplate);

		XtextResourceSet resourceSet = Activator.getInstance().getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL)
				.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		Resource resource = resourceSet.createResource(org.eclipse.emf.common.util.URI
				.createFileURI(MLmodelPath.removeLastSegments(1).append(mainPage.getModelName()).toString()));
		resource.getContents().add(DLmodel);
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public void setModelPath(IPath path) {
		this.MLmodelPath = path;
		this.MLmodelURI = path.toFile().toURI();
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
