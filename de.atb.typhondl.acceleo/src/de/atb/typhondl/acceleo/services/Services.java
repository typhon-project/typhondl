package de.atb.typhondl.acceleo.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import de.atb.typhondl.acceleo.main.Generate;
import de.atb.typhondl.xtext.TyphonDLStandaloneSetup;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Element;
import de.atb.typhondl.xtext.typhonDL.SupportedDBMS;

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
		 */
		XtextResourceSet resourceSet = new TyphonDLStandaloneSetup().createInjector()
				.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);

		URI modelURI = URI.createURI(pathToXTextModel);
		DeploymentModel model = (DeploymentModel) resourceSet.getResource(modelURI, true).getContents().get(0);
		saveDBs(model, folder, resourceSet);
		saveModelAsXMI(model, folder, resourceSet);
		EList<EObject> list = resourceSet.getResource(modelURI, true).getContents();
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + ":" + list.get(i).eClass().toString());
		}
		return model;
	}

	private static void saveDBs(DeploymentModel model, String pathToTargetFolder, XtextResourceSet resourceSet) {
		DB allDatabases = getDBs(model);
		for (SupportedDBMS db : allDatabases.getDbs()) {
			Resource xmiResource = resourceSet.createResource(URI.createFileURI(pathToTargetFolder + "databases/" + db.getName() + ".xmi"));
			xmiResource.getContents().add(db);
			try {
				xmiResource.save(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}

	private static DB getDBs(DeploymentModel model) {
		for (Element element : model.getElements()) {
			// TODO not nice
			if(element.eClass().getInstanceClassName().equals("de.atb.typhondl.xtext.typhonDL.DB")) {
				return (DB) element;
			}
		}
		return null;
	}

	/*
	 * TODO maybe put this in "onSave()" in Xtext package
	 */
	public static void saveModelAsXMI(DeploymentModel model, String pathToTargetFolder, XtextResourceSet resourceSet) {
		Resource xmiResource = resourceSet.createResource(URI.createFileURI(pathToTargetFolder + "/model.xmi"));
		xmiResource.getContents().add(model);
		try {
			xmiResource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
