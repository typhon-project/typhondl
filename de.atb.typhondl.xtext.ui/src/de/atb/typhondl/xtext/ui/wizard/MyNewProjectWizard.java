/**
 * 
 */
package de.atb.typhondl.xtext.ui.wizard;

import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.xtext.ui.wizard.IExtendedProjectInfo;
import org.eclipse.xtext.ui.wizard.template.AbstractProjectTemplate;
import org.eclipse.xtext.ui.wizard.template.NewProjectWizardTemplateSelectionPage;
import org.eclipse.xtext.ui.wizard.template.TemplateNewProjectWizard;
import org.eclipse.xtext.ui.wizard.template.TemplateParameterPage;
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
		
		private MyWizardDBMSSelectionPage createSelectionPage(String pageName, String modelPath) {
			return new MyWizardDBMSSelectionPage(pageName, modelPath);
		}
		
		private String getModelPath() {
			//printModelPath();
			return mainPage == null? "test" : mainPage.getModelPath();
		}
		
		private void printModelPath() {
			System.out.println("modelPath: " + mainPage.getModelPath());
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

				parameterPage.setWizard(this);
				templateParameterPage = parameterPage;
				parameterPage.setTitle(shortName(getGrammarName()) + Messages.TemplateNewProjectWizard_title_suffix);
				parameterPage.setDescription(Messages.TemplateNewProjectWizard_create_new_prefix + shortName(getGrammarName())
						+ Messages.TemplateNewProjectWizard_create_new_suffix);
				return parameterPage;
			}
			
			if (page instanceof MyWizardNewProjectCreationPage  && ((MyWizardNewProjectCreationPage) page).useModel()) {
				MyWizardDBMSSelectionPage dbmsSelectionPage = createSelectionPage("DBMSSelectionNewProjectPage", getModelPath());
				dbmsSelectionPage.setWizard(this);
				selectionPage = dbmsSelectionPage;
				selectionPage.setTitle(Messages.WizardSelectionPage_title_suffix);
				selectionPage.setDescription(Messages.WizardSelectionPage_description);
				return selectionPage;
			}
			
			if (page instanceof MyWizardDBMSSelectionPage) {
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
