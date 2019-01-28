/**
 * 
 */
package de.atb.typhondl.xtext.ui.wizard;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.xtext.ui.wizard.IExtendedProjectInfo;
import org.eclipse.xtext.ui.wizard.template.AbstractProjectTemplate;
import org.eclipse.xtext.ui.wizard.template.NewProjectWizardTemplateSelectionPage;
import org.eclipse.xtext.ui.wizard.template.TemplateNewProjectWizard;
import org.eclipse.xtext.ui.wizard.template.TemplateParameterPage;
import org.eclipse.xtext.ui.wizard.template.TemplateProjectInfo;
import org.eclipse.xtext.ui.wizard.template.TemplateVariable;

/**
 * @author flug
 *
 */
@SuppressWarnings("restriction")
public class MyNewProjectWizard extends TemplateNewProjectWizard {

	protected MyWizardNewProjectCreationPage mainPage;
	protected NewProjectWizardTemplateSelectionPage templatePage;
	protected TemplateParameterPage templateParameterPage;
	protected MyWizardDBMSSelectionPage selectionPage;

	/**
	 * 
	 */
	public MyNewProjectWizard() {
		super();
	}

	protected MyWizardNewProjectCreationPage createMyMainPage(String pageName) {
		return new MyWizardNewProjectCreationPage(pageName);
	}

	/* 1.5. Working with data in the wizard
	 * To use the same data in different pages of your wizard, pass them to the wizard pages via their constructor parameters.
	 * The isVisible() method is called whenever the WizardPage either gets visible or invisible. Call the super.isVisible() 
	 * method and check the current status of the page. If the page is visible, assign the data of your object to the user interface components.
	 */
	@Override
	public void addPages() {
		mainPage = createMyMainPage("basicNewProjectPage"); //$NON-NLS-1$
		mainPage.setTitle(shortName(getGrammarName()) + Messages.TemplateNewProjectWizard_title_suffix);
		mainPage.setDescription(Messages.TemplateNewProjectWizard_create_new_prefix + shortName(getGrammarName())
		+ Messages.TemplateNewProjectWizard_create_new_suffix);
		addPage(mainPage);
	}

	private MyWizardDBMSSelectionPage createSelectionPage(String pageName, URI modelPath) {
		return new MyWizardDBMSSelectionPage(pageName, modelPath);
	}

	@Override
	public IExtendedProjectInfo createProjectInfo() {
		// TODO not pretty
		boolean test = selectionPage == null;
		System.out.println("createProjectInfo() : " + test);
		HashMap<String, Database> data = new HashMap<String, Database>();
		if (!test) {
			data = selectionPage.getData();	
		}
		if (templatePage == null) {
			return null;
		}
		MyProjectInfo info = new MyProjectInfo(templatePage.getSelectedTemplate(), data);
		return info;
	}
	
	private URI getModelPath() {
		return mainPage == null? null : mainPage.getModelPath();
	}

	private String shortName(String fullName) {
		return fullName.contains(".") ? fullName.substring(fullName.lastIndexOf('.') + 1) : fullName; //$NON-NLS-1$
	}

	protected IExtendedProjectInfo getProjectInfo() {
		IExtendedProjectInfo projectInfo = createProjectInfo();
		projectInfo.setProjectName(mainPage.getProjectName());
		if (!mainPage.useDefaults()) {
			projectInfo.setLocationPath(mainPage.getLocationPath());
		}
		return projectInfo;
	}

	@Override
	public boolean canFinish() {
		if (mainPage.useModel()) {
			return (templateParameterPage != null)? templateParameterPage.isPageComplete() : false;
		}
		return super.canFinish();
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page instanceof NewProjectWizardTemplateSelectionPage) {
			AbstractProjectTemplate selectedTemplate = templatePage.getSelectedTemplate();
			if (selectedTemplate == null)
				return null;
			List<TemplateVariable> variables = selectedTemplate.getVariables();
			if (variables.isEmpty())
				return null;
			//selectedTemplate.setProjectInfo(getProjectInfo()); // doens't work because setProjectInfo is not public
			TemplateParameterPage parameterPage = new TemplateParameterPage(selectedTemplate);
			parameterPage.setPageComplete(false); // TODO put this in own TemplateParameterPage
			parameterPage.setWizard(this);
			templateParameterPage = parameterPage;
			parameterPage.setTitle(shortName(getGrammarName()) + Messages.TemplateNewProjectWizard_title_suffix);
			parameterPage.setDescription(Messages.TemplateNewProjectWizard_create_new_prefix + shortName(getGrammarName())
			+ Messages.TemplateNewProjectWizard_create_new_suffix);
			return parameterPage;
		}

		if (page instanceof MyWizardNewProjectCreationPage  && ((MyWizardNewProjectCreationPage) page).useModel()) {
			boolean test = selectionPage==null;
			if (test) {
				selectionPage = createSelectionPage("DBMSSelectionNewProjectPage", getModelPath());
				selectionPage.setWizard(this);
				selectionPage.setTitle(Messages.WizardSelectionPage_title_suffix);
				selectionPage.setDescription(Messages.WizardSelectionPage_description);
			}
			return selectionPage;
		}

		if (page instanceof MyWizardDBMSSelectionPage && page.isPageComplete()) {
			NewProjectWizardTemplateSelectionPage templateSelection = createTemplatePage("templateNewProjectPage"); //$NON-NLS-1$
			templateSelection.setWizard(this);
			templatePage =  templateSelection;
			super.templatePage = templatePage;
			templatePage.setTitle(shortName(getGrammarName()) + Messages.TemplateNewProjectWizard_title_suffix);
			templatePage.setDescription(Messages.TemplateNewProjectWizard_create_new_prefix + shortName(getGrammarName())
			+ Messages.TemplateNewProjectWizard_create_new_suffix);
			return templatePage;
		}

		return super.getNextPage(page);
	}
}
