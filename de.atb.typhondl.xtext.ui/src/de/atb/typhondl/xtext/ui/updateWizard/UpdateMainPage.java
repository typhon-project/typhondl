package de.atb.typhondl.xtext.ui.updateWizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import de.atb.typhondl.xtext.ui.wizard.Database;

public class UpdateMainPage extends WizardPage {

	private ArrayList<Database> MLmodel;

	protected UpdateMainPage(String pageName, ArrayList<Database> MLmodel) {
		super(pageName);
		this.MLmodel = MLmodel;
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub

	}

	public ArrayList<Database> getMLmodel() {
		return MLmodel;
	}

}
