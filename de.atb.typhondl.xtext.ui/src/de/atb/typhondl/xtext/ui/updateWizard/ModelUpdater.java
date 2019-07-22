package de.atb.typhondl.xtext.ui.updateWizard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.xml.sax.SAXException;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.creationWizard.Database;
import de.atb.typhondl.xtext.ui.utilities.DLmodelReader;
import de.atb.typhondl.xtext.ui.utilities.MLmodelReader;

public class ModelUpdater {

	public static String updateModel(IPath fullPath, IWorkbenchWindow window) {
		DeploymentModel DLmodel = DLmodelReader.readDLmodel(fullPath);
		ArrayList<Database> MLmodel = null;
		File file = new File(getMLURI(DLmodel, fullPath));
		try {
			MLmodel = MLmodelReader.readXMIFile(file.toURI());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return compareDLandML(DLmodel, MLmodel, window, fullPath);
	}

	/**
	 * Possible Cases:
	 * <ol>
	 * <li>nothing is changed</li>
	 * <li>database(s) is/are added and</li>
	 * <ol>
	 * <li>none removed</li>
	 * <li>one/some removed</li>
	 * </ol>
	 * <li>database(s) is/are removed and</li>
	 * <ol>
	 * <li>none added</li>
	 * <li>== 2.2</li>
	 * </ol>
	 * </ol>
	 * TODO cases 2.2, 3.* may not be possible, discuss with WP2+WP6
	 * 
	 * @param DLmodel
	 * @param MLmodel
	 * @param window
	 * @param fullPath
	 */
	private static String compareDLandML(DeploymentModel DLmodel, ArrayList<Database> MLmodel, IWorkbenchWindow window,
			IPath fullPath) {
		ArrayList<DB> DLdbs = DLmodelReader.getDBs(DLmodel);
		if (MLmodel.size() > DLdbs.size()) { // case 2.1
			for (DB db : DLdbs) {
				MLmodel.removeIf(databse -> databse.getName().equals(db.getName()));
			}
			MLmodel = openUpdateWizard(DLmodel, MLmodel, window);
			addDBsToDLmodel(DLmodel, MLmodel, fullPath);
			String updatedDBS = MLmodel.get(0).getName();
			for (int i = 1; i < MLmodel.size(); i++) {
				updatedDBS += ", " + MLmodel.get(i).getName();
			}
			return "Created new file(s) for " + updatedDBS
					+ ". To add a database to the deployment, please create a new container in your model file "
					+ fullPath.lastSegment() + " and reference the database via the \"deploys\" key.";
		} else {
			return "All ML databases match the DL databases. The DL model does not have to be updated via the Updater. "
					+ "Please add additional model content through the editor.";
		}

	}

	private static ArrayList<Database> openUpdateWizard(DeploymentModel DLmodel, ArrayList<Database> MLmodel,
			IWorkbenchWindow window) {
		UpdateModelWizard updateWizard = new UpdateModelWizard(MLmodel);
		WizardDialog dialog = new WizardDialog(window.getShell(), updateWizard);
		dialog.open();
		return updateWizard.getUpdatedMLmodel();
	}

	private static void addDBsToDLmodel(DeploymentModel DLmodel, ArrayList<Database> MLmodel, IPath fullPath) {
		Resource DLmodelResource = DLmodel.eResource();
		XtextResourceSet DLmodelResourceSet = (XtextResourceSet) DLmodelResource.getResourceSet();
		DLmodelResourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		for (Database newDatabase : MLmodel) {

			// 1. create new file for the new database TODO useExistingFile checked
			DB newDB = TyphonDLFactory.eINSTANCE.createDB();
			newDB.setName(newDatabase.getName());
			newDB.setType(newDatabase.getDbms());
			IMAGE image = TyphonDLFactory.eINSTANCE.createIMAGE();
			image.setValue(newDB.getType().getName() + ":latest;");
			newDB.setImage(image);
			DeploymentModel container = TyphonDLFactory.eINSTANCE.createDeploymentModel();
			container.getElements().add(newDB.getType());
			container.getElements().add(newDB);
			String relativePath = newDatabase.getPathToDBModelFile();
			URI dbURI = DLmodelResource.getURI().trimSegments(1).appendSegment(relativePath);
			Resource dbResource = DLmodelResourceSet.createResource(dbURI);
			dbResource.getContents().add(container);
			try {
				dbResource.save(Collections.EMPTY_MAP);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 2. add new database file to imports
			Import newImport = TyphonDLFactory.eINSTANCE.createImport();
			newImport.setRelativePath(relativePath);
			DLmodel.getGuiMetaInformation().add(newImport);

			// 3. craeate new dummy-container
			/*
			 * TODO reference to DB in other file (see code below) throws a
			 * RuntimeException: No EObjectDescription could be found in Scope
			 * Reference.reference for DeploymentModel.elements[1]->DB'testdb' Semantic
			 * Object:
			 * DeploymentModel.elements[2]->Deployment'platformname'.clusters[0]->Cluster'
			 * clustername'.applications[0]->Application'name'.containers[6]->Container'
			 * testdb'.deploys[0]->Reference URI:
			 * file:/C:/Users/flug/runtime-EclipseXtext/example.typhondl/ECommerceExample.
			 * tdl EStructuralFeature: typhonDL::Reference.reference
			 */
//			Container newContainer = TyphonDLFactory.eINSTANCE.createContainer();
//			newContainer.setName(newDB.getName());
//			newContainer.setType(getContainerType(DLmodel));
//			Reference reference = TyphonDLFactory.eINSTANCE.createReference();
//			reference.setReference(newDB);
//			newContainer.getDeploys().add(reference);
//			getFirstApplication(DLmodel).getContainers().add(newContainer);

//			This adds the DB and the type to the main model file:
//			DLmodel.getElements().add(newDB);
//			DLmodel.getElements().add(newDB.getType());

			// 4. save updated model
			try {
				DLmodel.eResource().save(Collections.EMPTY_MAP);
				// TODO add formatting options to save()
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

// TODO only gets the fist app in the first cluster
//	private static Application getFirstApplication(DeploymentModel DLmodel) {
//		return DLmodel.getElements().stream().filter(element -> Deployment.class.isInstance(element))
//				.map(element -> (Deployment) element).collect(Collectors.toList()).get(0).getClusters().get(0)
//				.getApplications().get(0);
//	}
//
//	private static ContainerType getContainerType(DeploymentModel DLmodel) {
//		List<ContainerType> types = DLmodel.getElements().stream()
//				.filter(element -> ContainerType.class.isInstance(element)).map(element -> (ContainerType) element)
//				.collect(Collectors.toList());
//		return types.get(0);
//	}

	private static String getMLURI(DeploymentModel DLmodel, IPath fullPath) {
		String MLmodelName = DLmodel.getGuiMetaInformation().stream()
				.filter(metaModel -> Import.class.isInstance(metaModel)).map(metaModel -> (Import) metaModel)
				.filter(info -> info.getRelativePath().endsWith(".xmi")).findFirst().map(info -> info.getRelativePath())
				.orElse("");
		return fullPath.toString().substring(0, fullPath.toString().lastIndexOf("/")) + "/" + MLmodelName;
	}
}
