package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.core.runtime.IPath;

import de.atb.typhondl.xtext.typhonDL.*;
import de.atb.typhondl.xtext.ui.creationWizard.CreationAnalyticsPage.InputField;
import de.atb.typhondl.xtext.ui.utilities.DLmodelReader;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;
import de.atb.typhondl.xtext.ui.wizard.Database;

public class ModelCreator {

	public static DeploymentModel createDLmodel(HashMap<String, InputField> analyticsSettings,
			Set<Database> databaseInfo, IPath MLmodelPath, int chosenTemplate) {

		DeploymentModel DLmodel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
		Import MLmodel = TyphonDLFactory.eINSTANCE.createImport();
		MLmodel.setRelativePath(getModelName(MLmodelPath));
		DLmodel.getGuiMetaInformation().add(MLmodel);
		ArrayList<Database> databases = new ArrayList<Database>(databaseInfo);
		for (Database database : databases) {
			Import importedDB = TyphonDLFactory.eINSTANCE.createImport();
			importedDB.setRelativePath(database.getPathToDBModelFile());
			DLmodel.getGuiMetaInformation().add(importedDB);
			// TODO create containers and references and put them in list
			if (database.getPathToDBModelFile() != null) { // use existing .tdl file
				DeploymentModel existing = DLmodelReader
						.readDLmodel(MLmodelPath.removeLastSegments(1).append(database.getPathToDBModelFile()));
				DB db = DLmodelReader.getDBs(existing).get(0);
				DLmodel.getElements().add(db); // cross refence between files does not work yet
			}
			if (database.getDbms() != null) {
				DB db = TyphonDLFactory.eINSTANCE.createDB();
				db.setName(database.getName());
				db.setType(database.getDbms());
				DLmodel.getElements().add(db); // cross refence between files does not work yet
			}
		}
		
		Reference polystore_api = null;

		PlatformType platformType = TyphonDLFactory.eINSTANCE.createPlatformType();
		platformType.setName("default");
		DLmodel.getElements().add(platformType);

		ContainerType containerType = TyphonDLFactory.eINSTANCE.createContainerType();
		containerType.setName(SupportedTechnologies.values()[chosenTemplate].getContainerType());
		DLmodel.getElements().add(containerType);

		Deployment deployment = TyphonDLFactory.eINSTANCE.createDeployment();
		deployment.setName("platformName");
		deployment.setType(platformType);
		DLmodel.getElements().add(deployment);
		
		Cluster cluster = TyphonDLFactory.eINSTANCE.createCluster();
		cluster.setName("clusterName");
		deployment.getClusters().add(cluster);
		
		Application application = TyphonDLFactory.eINSTANCE.createApplication();
		application.setName("Polystore");
		cluster.getApplications().add(application);
		
		Container container = TyphonDLFactory.eINSTANCE.createContainer();
		container.setName("polystore");
		container.setType(containerType);
		container.getDeploys().add(polystore_api );

		return DLmodel;
	}

	private static String getModelName(IPath path) {
		String string = path.toString();
		return string.substring(string.lastIndexOf('/'), string.length() - 1);
	}

}
