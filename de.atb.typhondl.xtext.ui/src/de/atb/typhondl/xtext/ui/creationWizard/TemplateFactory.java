package de.atb.typhondl.xtext.ui.creationWizard;

import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class TemplateFactory {
	public static Template createTemplate(int technology, DeploymentModel DLmodel) {
		switch (SupportedTechnologies.values()[technology].name()) {
		case "DockerCompose":
			return new DockerComposeTemplate(DLmodel);
		default:
			break;
		}
		return null;
	}
}
