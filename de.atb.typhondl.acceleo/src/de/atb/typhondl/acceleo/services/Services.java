package de.atb.typhondl.acceleo.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import de.atb.typhondl.acceleo.main.Generate;
import de.atb.typhondl.xtext.TyphonDLStandaloneSetup;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;

public class Services {

	public static void generateDeployment(String pathToXTextModel, String folder) {
			System.out.println("Generate from template...");
			try {
				new Generate(loadXtextModel(pathToXTextModel), new File(folder), new ArrayList<String>()).doGenerate(new BasicMonitor());
				System.out.println("Generated!");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static DeploymentModel loadXtextModel(String pathToXTextModel) {
		XtextResourceSet resourceSet = new TyphonDLStandaloneSetup().createInjector().getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		return (DeploymentModel) resourceSet.getResource(URI.createURI(pathToXTextModel), true).getContents().get(0);
	}
}
