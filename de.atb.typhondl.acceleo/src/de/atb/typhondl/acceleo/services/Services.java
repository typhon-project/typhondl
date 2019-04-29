package de.atb.typhondl.acceleo.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import de.atb.typhondl.acceleo.main.Generate;
import de.atb.typhondl.xtext.TyphonDLStandaloneSetup;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;

public class Services {

	public static void generateDeployment(String pathToXTextModel, String folder) {
		System.out.println("Generate from template...");
		try {
			new Generate(loadXtextModel(pathToXTextModel, folder), new File(folder), new ArrayList<String>())
					.doGenerate(new BasicMonitor());
			System.out.println("Generated!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DeploymentModel loadXtextModel(String pathToXTextModel, String folder) {
		/* TODO TYP-45
		 * https://stackoverflow.com/questions/35839786/xtext-export-model-as-xmi-xml#35885943
		 *
		 * Inside Eclipse IDE never use MyLanguageStandaloneSetup, the instance of an
		 * injector MUST be accessed via an Activator of a UI plugin:
		 * MyLanguageActivator.getInstance().getInjector(MyLanguageActivator.
		 * COM_MYCOMPANY_MYLANGUAGE).
		 * 
		 * Calling of MyLanguageStandaloneSetup.createInjectorAndDoEMFRegistration will
		 * create a new instance of an Injector that is different from used by Eclipse.
		 * Also it can break the state of EMF registries.
		 */
		XtextResourceSet resourceSet = new TyphonDLStandaloneSetup().createInjector()
				.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		URI xtextModelURI = URI.createURI(pathToXTextModel);
		saveModelAsXMI(xtextModelURI, folder, resourceSet);
		return (DeploymentModel) resourceSet.getResource(xtextModelURI, true).getContents().get(0);
	}
	
	public static void saveModelAsXMI(URI xtextModelURI, String pathToTargetFolder, XtextResourceSet resourceSet) {
		Resource xtextResource = resourceSet.getResource(xtextModelURI, true);
		EcoreUtil.resolveAll(xtextResource);
		Resource xmiResource = resourceSet.createResource(URI.createURI(pathToTargetFolder));
	    xmiResource.getContents().add(xtextResource.getContents().get(0));
	    try {
	        xmiResource.save(null);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
