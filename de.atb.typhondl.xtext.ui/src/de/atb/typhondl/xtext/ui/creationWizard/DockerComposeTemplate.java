package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.HashMap;

import de.atb.typhondl.xtext.ui.utilities.WizardFields;
import de.atb.typhondl.xtext.ui.wizard.Database;

public class DockerComposeTemplate {

	private HashMap<String, String> analyticsSettings;
	private HashMap<Database, WizardFields> databaseSettings;

	public DockerComposeTemplate(HashMap<String, String> analyticsSettings,
			HashMap<Database, WizardFields> databaseSettings) {
		this.analyticsSettings = analyticsSettings;
		this.databaseSettings = databaseSettings;
	}

}
