/**
 * 
 */
package de.atb.typhondl.xtext.ui.wizard;

import org.eclipse.xtext.ui.wizard.IExtendedProjectInfo;
import org.eclipse.xtext.ui.wizard.template.Messages;
import org.eclipse.xtext.ui.wizard.template.NewProjectWizardTemplateSelectionPage;
import org.eclipse.xtext.ui.wizard.template.TemplateNewProjectWizard;
import org.eclipse.xtext.ui.wizard.template.TemplateParameterPage;

/**
 * @author flug
 *
 */
@SuppressWarnings("restriction")
public class MyNewProjectWizard extends TemplateNewProjectWizard {

	protected MyWizardNewProjectCreationPage mainPage;
	protected NewProjectWizardTemplateSelectionPage templatePage;
	protected TemplateParameterPage templateParameterPage;
	//protected MyWizardChoosingDBMSPage choosingPage;
	
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
			
//			choosingPage = createChoosingPage("choosingNewProjectPage", mainPage.getModelPath());
//			templatePage = createTemplatePage("templateNewProjectPage"); //$NON-NLS-1$
//			System.out.println("TemplatePage created: " + templatePage == null);
//			templatePage.setTitle(shortName(getGrammarName()) + Messages.TemplateNewProjectWizard_title_suffix);
//			templatePage.setDescription(Messages.TemplateNewProjectWizard_create_new_prefix + shortName(getGrammarName())
//					+ Messages.TemplateNewProjectWizard_create_new_suffix);
//			addPage(templatePage);
		}
		
		private MyWizardChoosingDBMSPage createChoosingPage(String pageName, String modelPath) {
			return new MyWizardChoosingDBMSPage(pageName, modelPath);
		}

		@Override
		protected IExtendedProjectInfo createProjectInfo() {
			// TODO Auto-generated method stub
			return super.createProjectInfo();
		}
		
		private String shortName(String fullName) {
			return fullName.contains(".") ? fullName.substring(fullName.lastIndexOf('.') + 1) : fullName; //$NON-NLS-1$
		}
}
