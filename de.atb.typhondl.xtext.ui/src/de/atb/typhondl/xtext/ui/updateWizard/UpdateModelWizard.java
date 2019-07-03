package de.atb.typhondl.xtext.ui.updateWizard;

import java.util.ArrayList;
import org.eclipse.jface.wizard.Wizard;
import de.atb.typhondl.xtext.ui.wizard.Database;

public class UpdateModelWizard extends Wizard {

	private ArrayList<Database> MLmodel;

	public UpdateModelWizard(ArrayList<Database> MLmodel) {
		this.MLmodel = MLmodel;
	}
	
	@Override
	public boolean performFinish() {
		// TODO save user input in this.MLmodel
		return false;
	}

	public ArrayList<Database> getUpdatedMLmodel() {
		return this.MLmodel;
	}

}
