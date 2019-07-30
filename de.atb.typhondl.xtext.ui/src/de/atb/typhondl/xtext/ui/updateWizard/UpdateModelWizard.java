package de.atb.typhondl.xtext.ui.updateWizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.Wizard;

import de.atb.typhondl.xtext.ui.utilities.Database;

public class UpdateModelWizard extends Wizard {

	private ArrayList<Database> MLmodel;
	private UpdateMainPage mainPage;

	public UpdateModelWizard(ArrayList<Database> MLmodel) {
		this.MLmodel = MLmodel;
	}
	
	@Override
	public void addPages() {
		mainPage = new UpdateMainPage("Choose DBMS for new databases", MLmodel);
		addPage(mainPage);
	}
	
	@Override
	public boolean performFinish() {
		this.MLmodel = mainPage.getMLmodel();
		return true;
	}

	public ArrayList<Database> getUpdatedMLmodel() {
		return this.MLmodel;
	}
}
