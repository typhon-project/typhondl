package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import de.atb.typhondl.xtext.typhonDL.DB;

public class CreationContainerPage extends MyWizardPage {

	private ArrayList<DB> dbs;

	protected CreationContainerPage(String pageName, ArrayList<DB> dbs) {
		super(pageName);
		this.dbs = dbs;
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub

	}

}
