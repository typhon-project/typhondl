package de.atb.typhondl.xtext.ui.creationWizard;

import de.atb.typhondl.xtext.typhonDL.DeploymentModel;

public class DockerComposeTemplate extends Template {

	private DeploymentModel DLmodel;

	public DockerComposeTemplate(DeploymentModel DLmodel) {
		this.DLmodel = DLmodel;
	}

}
