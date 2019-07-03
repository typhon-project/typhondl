package de.atb.typhondl.xtext.ui.updateModel;

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
import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.service.Service;
import de.atb.typhondl.xtext.ui.updateWizard.UpdateModelWizard;
import de.atb.typhondl.xtext.ui.wizard.Database;
import de.atb.typhondl.xtext.ui.wizard.ModelReader;

public class ModelUpdater {

	public static String updateModel(IPath fullPath, IWorkbenchWindow window) {
		DeploymentModel DLmodel = Service.readDLmodel(fullPath);
		ArrayList<Database> MLmodel = null;
		File file = new File(getMLURI(DLmodel, fullPath));
		try {
			MLmodel = ModelReader.readXMIFile(file.toURI());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return compareDLandML(DLmodel, MLmodel, window);
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
	 */
	private static String compareDLandML(DeploymentModel DLmodel, ArrayList<Database> MLmodel, IWorkbenchWindow window) {
		ArrayList<DB> DLdbs = Service.getDBs(DLmodel);
		if (MLmodel.size() > DLdbs.size()) { // case 2.1
			for (DB db : DLdbs) {
				MLmodel.removeIf(databse -> databse.getName().equals(db.getName()));
			}
			MLmodel = openUpdateWizard(DLmodel, MLmodel, window);
			addDBsToDLmodel(DLmodel, MLmodel);
			return "";
		} else {
			return "All ML databases match the DL databases. The DL model does not have to be updated via the Updater. "
					+ "Please add additional model content through the editor.";
		}

	}

	private static ArrayList<Database> openUpdateWizard(DeploymentModel DLmodel, ArrayList<Database> MLmodel, IWorkbenchWindow window) {
		UpdateModelWizard updateWizard = new UpdateModelWizard(MLmodel);
		WizardDialog dialog = new WizardDialog(window.getShell(), updateWizard);
		dialog.open();
		return updateWizard.getUpdatedMLmodel();
	}

	private static void addDBsToDLmodel(DeploymentModel DLmodel, ArrayList<Database> MLmodel) {
		Resource DLmodelResource = DLmodel.eResource();
		XtextResourceSet DLmodelResourceSet = (XtextResourceSet) DLmodelResource.getResourceSet();
		DLmodelResourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		for (Database newDatabase : MLmodel) {
			DB newDB = TyphonDLFactory.eINSTANCE.createDB();
			newDB.setName(newDatabase.getName());
			newDB.setType(newDatabase.getDbms());
			newDB.setImage(newDatabase.getImage());
			DeploymentModel container = TyphonDLFactory.eINSTANCE.createDeploymentModel();
			container.getElements().add(newDB.getType());
			container.getElements().add(newDB);
			URI dbURI = DLmodelResource.getURI().trimSegments(1).appendSegment(newDB.getName() + ".tdl");
			Resource dbResource = DLmodelResourceSet.createResource(dbURI);
			dbResource.getContents().add(container);
			try {
				dbResource.save(Collections.EMPTY_MAP);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static String getMLURI(DeploymentModel DLmodel, IPath fullPath) {
		String DLmodelName = DLmodel.getGuiMetaInformation().stream()
				.filter(metaModel -> Import.class.isInstance(metaModel)).map(metaModel -> (Import) metaModel)
				.filter(info -> info.getRelativePath().endsWith(".xmi")).findFirst().map(info -> info.getRelativePath())
				.orElse("");
		return fullPath.toString().substring(0, fullPath.toString().lastIndexOf("/")) + "/" + DLmodelName;
	}
}
