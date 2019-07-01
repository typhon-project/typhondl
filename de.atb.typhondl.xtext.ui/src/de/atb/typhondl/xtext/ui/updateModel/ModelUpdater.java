package de.atb.typhondl.xtext.ui.updateModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
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
import de.atb.typhondl.xtext.ui.wizard.Database;
import de.atb.typhondl.xtext.ui.wizard.ModelReader;

public class ModelUpdater {

	public static void updateModel(IPath fullPath) {
		DeploymentModel DLmodel = Service.readDLmodel(fullPath);
		ArrayList<Database> MLmodel = null;
		File file = new File(getMLURI(DLmodel, fullPath));
		try {
			MLmodel = ModelReader.readXMIFile(file.toURI());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		compareDLandML(DLmodel, MLmodel);
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
	 */
	private static void compareDLandML(DeploymentModel DLmodel, ArrayList<Database> MLmodel) {
		ArrayList<DB> DLdbs = Service.getDBs(DLmodel);
		if (MLmodel.size() > DLdbs.size()) { // case 2.1
			for (DB db : DLdbs) {
				MLmodel.removeIf(databse -> databse.getName().equals(db.getName()));
			}
			addDBsToDLmodel(DLmodel, MLmodel);
		} else {
			System.out.println("nothing to be done"); //TODO
		}
		
	}

	private static void addDBsToDLmodel(DeploymentModel DLmodel, ArrayList<Database> MLmodel) {
		Resource DLmodelResource = DLmodel.eResource();
		XtextResourceSet DLmodelResourceSet = (XtextResourceSet) DLmodelResource.getResourceSet();
		DLmodelResourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		Database newDatabase = MLmodel.get(0);
		DB newDB = TyphonDLFactory.eINSTANCE.createDB();
		newDB.setName(newDatabase.getName());
		DBType dbType = TyphonDLFactory.eINSTANCE.createDBType();
		dbType.setName("mongo");
		newDB.setType(dbType);
		IMAGE image = TyphonDLFactory.eINSTANCE.createIMAGE();
		image.setValue("mongo:test");
		newDB.setImage(image );
		DeploymentModel container = TyphonDLFactory.eINSTANCE.createDeploymentModel();
		container.getElements().add(dbType);
		container.getElements().add(newDB);
		URI dbURI = DLmodelResource.getURI().trimSegments(1).appendSegment(newDB.getName()+".tdl");
		Resource dbResource = DLmodelResourceSet.createResource(dbURI);
		dbResource.getContents().add(container);
		try {
			dbResource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
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
