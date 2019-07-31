package de.atb.typhondl.xtext.ui.updateWizard;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.Wizard;

import de.atb.typhondl.xtext.ui.creationWizard.CreationDBMSPage;
import de.atb.typhondl.xtext.ui.utilities.Database;

public class UpdateModelWizard extends Wizard {

	private ArrayList<Database> MLmodel;
	private CreationDBMSPage mainPage;
	private IFile file;

	public UpdateModelWizard(ArrayList<Database> MLmodel, IFile file) {
		this.MLmodel = MLmodel;
		this.file = file;
	}
	
	@Override
	public void addPages() {
		mainPage = new CreationDBMSPage("Choose DBMS for new databases", file, MLmodel);
		addPage(mainPage);
	}
	
	@Override
	public boolean performFinish() {
		this.MLmodel = mainPage.getDatabases();
		return true;
	}

	public ArrayList<Database> getUpdatedMLmodel() {
		return this.MLmodel;
	}
}
