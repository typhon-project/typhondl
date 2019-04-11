package de.atb.typhondl.acceleo.services;

import com.google.inject.Injector;
import de.atb.typhondl.xtext.TyphonDLStandaloneSetup;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;

public class Services {

	public static DeploymentModel loadXtextModel(String pathToXTextModel) {
		Injector injector = new TyphonDLStandaloneSetup().createInjector();
		
		
		
		return null;
	}
}