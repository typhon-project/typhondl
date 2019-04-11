package de.atb.typhondl.acceleo.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import de.atb.typhondl.acceleo.main.Generate;
import de.atb.typhondl.xtext.TyphonDLStandaloneSetup;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;

public class Services {

	public static void generateDeployment(String pathToXTextModel, String folder) {
			System.out.println("Generate from template...");
			try {
				Generate generator = new Generate(loadXtextModel(pathToXTextModel), new File(folder), new ArrayList<String>());
				generator.doGenerate(new BasicMonitor());
				System.out.println("Generated!");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static DeploymentModel loadXtextModel(String pathToXTextModel) {
		Injector injector = new TyphonDLStandaloneSetup().createInjector();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		URI uri = URI.createURI(pathToXTextModel);
		Resource resource = resourceSet.getResource(uri, true);
		DeploymentModel deploymentModel = (DeploymentModel) resource.getContents().get(0);
		
		
		return deploymentModel;
	}
}