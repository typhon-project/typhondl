package de.atb.typhondl.xtext.ui.creationWizard;

import java.net.URI;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import de.atb.typhondl.xtext.ui.wizard.Database;

public class CreateModelWizard extends Wizard {

	private URI modelPath;
	private CreationMainPage mainPage;
	private CreationDBMSPage dbmsPage;
	private String chosenTemplate;

	@Override
	public void addPages() {
		mainPage = new CreationMainPage("Create new DL model", modelPath);
		addPage(mainPage);

		dbmsPage = new CreationDBMSPage("Choose DBMS", modelPath);
		addPage(dbmsPage);
	}

	@Override
	public boolean performFinish() {
		Set<Database> databases = dbmsPage.getDatabases();
		databases.forEach(database -> System.out.println(database.getName() + ": " + database.getType() + ", "
				+ database.getPathToDBModelFile() + ", " + database.getDbms()));
		if (!dbmsPage.getMessage().isEmpty()) {
			return MessageDialog.openConfirm(this.getShell(), "Wizard", dbmsPage.getMessage());
			//MessageDialog.openWarning(this.getShell(), "Wizard", dbmsPage.getMessage());
		}
		return true;
	}

	public void setModelPath(URI modelPath) {
		this.modelPath = modelPath;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page instanceof CreationMainPage) {
			this.chosenTemplate = ((CreationMainPage) page).getChosenTemplate();
		}
		return super.getNextPage(page);
	}
}
