package de.atb.typhondl.xtext.ui.updateModel;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IPath;
import org.xml.sax.SAXException;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.ui.service.Service;
import de.atb.typhondl.xtext.ui.wizard.Database;
import de.atb.typhondl.xtext.ui.wizard.ModelReader;

public class ModelUpdater {

	public static void updateModel(IPath fullPath) {
		DeploymentModel DLmodel = Service.readDLmodel(fullPath);
		ArrayList<Database> MLmodel = null;
		System.out.println(getMLURI(DLmodel, fullPath));
		java.net.URI.create(getMLURI(DLmodel, fullPath));
		try {
			MLmodel = ModelReader.readXMIFile(java.net.URI.create(getMLURI(DLmodel, fullPath)));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		compareDLandML(DLmodel, MLmodel);
	}

	private static void compareDLandML(DeploymentModel DLmodel, ArrayList<Database> MLmodel) {
		ArrayList<DB> DLdbs = Service.getDBs(DLmodel);
		for (Database database : MLmodel) {
			DLdbs.removeIf(db -> db.getName().equals(database.getName()));
			System.out.println(DLdbs.size());
		}
	}

	private static String getMLURI(DeploymentModel DLmodel, IPath fullPath) {
		String DLmodelName = DLmodel.getGuiMetaInformation().stream().filter(metaModel -> Import.class.isInstance(metaModel))
				.map(metaModel -> (Import) metaModel).filter(info -> info.getRelativePath().endsWith(".xmi"))
				.findFirst().map(info ->info.getRelativePath()).orElse("");		
		return fullPath.toString().substring(0, fullPath.toString().lastIndexOf("/")) + "/" + DLmodelName;
	}
}
