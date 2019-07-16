package de.atb.typhondl.xtext.ui.creationWizard;

import java.net.URI;

import org.eclipse.jface.wizard.Wizard;

public class CreateModelWizard extends Wizard {

	private URI modelPath;
	
	@Override
	public void addPages() {
		
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setModelPath(URI modelPath) {
		this.modelPath = modelPath;
	}

}
