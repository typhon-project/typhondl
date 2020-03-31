package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.PlatformType;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.utilities.SavingOptions;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

/**
 * This class creates the new TyphonDL model from the selected ML model and the
 * given input from the {@link CreateModelWizard}. The following files are
 * created:
 * <li>The main model file</li>
 * <li>A dbtypes.tdl containing all used {@link DBType}s</li>
 * <li>A model file for each {@link DB} extracted from the ML model</li>
 * 
 * @author flug
 *
 */
public class ModelCreator {

	/**
	 * The selected source ML model from which a DL model is created
	 */
	private IFile MLmodel;

	/**
	 * The resourceSet containing all tdl resources
	 */
	private XtextResourceSet resourceSet;

	/**
	 * The path to folder in which to save all model files
	 */
	private IPath folder;

	/**
	 * The DL model name entered on the {@link CreationMainPage}
	 */
	private String DLmodelName;

	/**
	 * Creates an instance of the {@link ModelCreator}
	 * 
	 * @param MLmodel     The selected ML model
	 * @param DLmodelName The entered name for the DL model
	 */
	public ModelCreator(IFile MLmodel, String DLmodelName) {
		this.MLmodel = MLmodel;
		this.folder = this.MLmodel.getFullPath().removeLastSegments(1);
		this.DLmodelName = DLmodelName;
		addResources();
	}

	/**
	 * Gets the provided ResourceSet and adds all .tdl files to the ResourceSet
	 */
	private void addResources() {
		this.resourceSet = (XtextResourceSet) Activator.getInstance()
				.getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL)
				.getInstance(XtextLiveScopeResourceSetProvider.class).get(this.MLmodel.getProject());
		this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		IResource members[] = null;
		try {
			members = this.MLmodel.getProject().members();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		for (IResource member : members) {
			if (member instanceof IFile) {
				if (((IFile) member).getFileExtension().equals("tdl")) {
					resourceSet.getResource(URI.createPlatformResourceURI(member.getFullPath().toString(), true), true);
				}
			}
		}
	}

	/**
	 * Creates the new DL model
	 * 
	 * @param result         The DBs and Containers to add to the new model
	 * @param chosenTemplate The int representation of the chosen technology
	 *                       Template from {@link SupportedTechnologies}
	 * @param properties     The polystore.properties
	 * @return The main model file to be opened by the Xtext editor after creation
	 */
	public IFile createDLmodel(HashMap<DB, Container> result, int chosenTemplate, Properties properties) {

		// create main model
		DeploymentModel DLmodel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
		// add reference to ML model
		Import MLmodelImport = TyphonDLFactory.eINSTANCE.createImport();
		MLmodelImport.setRelativePath(this.MLmodel.getName());
		DLmodel.getGuiMetaInformation().add(MLmodelImport);

		// Add selected container type (chosen template in wizard)
		ContainerType containerType = TyphonDLFactory.eINSTANCE.createContainerType();
		containerType.setName(SupportedTechnologies.values()[chosenTemplate].getContainerType());
		DLmodel.getElements().add(containerType);

		// Add selected cluster type (chosen template in wizard)
		ClusterType clusterType = TyphonDLFactory.eINSTANCE.createClusterType();
		clusterType.setName(SupportedTechnologies.values()[chosenTemplate].getClusterType());
		DLmodel.getElements().add(clusterType);

		// create platform type from API HOST
		PlatformType platformType = TyphonDLFactory.eINSTANCE.createPlatformType();
		switch (SupportedTechnologies.values()[chosenTemplate].getClusterType()) {
		case "DockerCompose":
			platformType.setName("localhost");
			break;
		case "Kubernetes":
			platformType.setName("minikube");
			break;
		default:
			platformType.setName("localhost");
			break;
		}
		DLmodel.getElements().add(platformType);

		ArrayList<DBType> dbTypes = new ArrayList<DBType>();
		// create import for each db
		for (DB db : result.keySet()) {
			Import importedDB = TyphonDLFactory.eINSTANCE.createImport();
			importedDB.setRelativePath(db.getName() + ".tdl");
			DLmodel.getGuiMetaInformation().add(importedDB);

			// types need to be the same instance
			boolean containsType = false;
			for (DBType dbType : dbTypes) {
				if (dbType.getName().equals(db.getType().getName())) {
					containsType = true;
					db.setType(dbType);
				}
			}
			if (!containsType) {
				dbTypes.add(db.getType());
			}
		}

		// save dbTypes in file
		DeploymentModel dbTypesModel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
		for (DBType dbType : dbTypes) {
			dbTypesModel.getElements().add(dbType);
		}
		save(dbTypesModel, "dbTypes.tdl");
		Import dbTypesImport = TyphonDLFactory.eINSTANCE.createImport();
		dbTypesImport.setRelativePath("dbTypes.tdl");
		DLmodel.getGuiMetaInformation().add(dbTypesImport);

		// save DBs in file
		for (DB db : result.keySet()) {
			DeploymentModel dbModel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
			dbModel.getElements().add(db);
			save(dbModel, db.getName() + ".tdl");
		}

		/*
		 * start container structure
		 */
		Platform deployment = TyphonDLFactory.eINSTANCE.createPlatform();
		deployment.setName("platformName");
		deployment.setType(platformType);
		DLmodel.getElements().add(deployment);

		Cluster cluster = TyphonDLFactory.eINSTANCE.createCluster();
		cluster.setName("clusterName");
		cluster.setType(clusterType);
		deployment.getClusters().add(cluster);

		Application application = TyphonDLFactory.eINSTANCE.createApplication();
		application.setName("Polystore");
		cluster.getApplications().add(application);

		for (DB db : result.keySet()) {
			Container containerToAdd = result.get(db);
			containerToAdd.setType(containerType);
			application.getContainers().add(containerToAdd);
		}

		/*
		 * save main model file
		 */
		String filename = DLmodelName + ".tdl";
		save(DLmodel, filename);
		URI DLmodelURI = URI.createPlatformResourceURI(this.folder.append(filename).toString(), true);
		// return main model file to be opened in editor
		return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(DLmodelURI.toPlatformString(true)));
	}

	/**
	 * Save the given model as {@link Resource}
	 * 
	 * @param model    The model to save
	 * @param filename The name of the file to create
	 */
	private void save(DeploymentModel model, String filename) {
		URI uri = URI.createPlatformResourceURI(this.folder.append(filename).toString(), true);
		// delete resource in case it already exists
		if (resourceSet.getResource(uri, false) != null) {
			try {
				resourceSet.getResource(uri, true).delete(Collections.EMPTY_MAP);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Resource resource = resourceSet.createResource(uri);
		resource.getContents().add(model);
		try {
			resource.save(SavingOptions.getTDLoptions());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
