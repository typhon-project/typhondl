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
			DeploymentModel model = loadXtextModel(pathToXTextModel, folder);
			new Generate(model, new File(folder), new ArrayList<String>()).doGenerate(new BasicMonitor());
			System.out.println("Generated!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DeploymentModel loadXtextModel(String pathToXTextModel, String folder) {
		/*
		 * TODO TYP-45
		 * https://stackoverflow.com/questions/35839786/xtext-export-model-as-xmi-xml#
		 * 35885943
		 *
		 * Inside Eclipse IDE never use MyLanguageStandaloneSetup, the instance of an
		 * injector MUST be accessed via an Activator of a UI plugin:
		 * MyLanguageActivator.getInstance().getInjector(MyLanguageActivator.
		 * COM_MYCOMPANY_MYLANGUAGE).
		 * 
		 * Calling of MyLanguageStandaloneSetup.createInjectorAndDoEMFRegistration will
		 * create a new instance of an Injector that is different from used by Eclipse.
		 * Also it can break the state of EMF registries.
		 * 
		 * flug: when doing so with de.atb.typhondl.xtext.ui.activator.Activator, theres
		 * a loop in the manifest because acceleo.service package requires xtext.ui
		 * package and vice versa
		 */
		XtextResourceSet resourceSet = new TyphonDLStandaloneSetup().createInjector()
				.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);

		URI modelURI = URI.createURI(pathToXTextModel);
		Resource xtextResource = resourceSet.getResource(modelURI, true);
		EcoreUtil.resolveAll(xtextResource);
		DeploymentModel model = (DeploymentModel) resourceSet.getResource(modelURI, true).getContents().get(0);
		saveModelAsXMI(model, folder, resourceSet);
		// has to be in this order because xmiResource.getContents().add(db); removes db
		// from its original container (DB). So in code generation the DBs are also
		// missing //TODO
		// saveDBsAsXMI(model, folder, resourceSet);
		return model;
	}

	/*
	 * TODO maybe put this in "onSave()" in Xtext package
	 */
	public static void saveModelAsXMI(DeploymentModel model, String pathToTargetFolder, XtextResourceSet resourceSet) {
		Resource xmiResource = resourceSet.createResource(URI.createFileURI(pathToTargetFolder + "/model.xmi"));
		xmiResource.getContents().add(model);
		try {
			xmiResource.save(Options.getXMIoptions());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
