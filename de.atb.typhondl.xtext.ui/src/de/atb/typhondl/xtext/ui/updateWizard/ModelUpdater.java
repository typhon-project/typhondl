package de.atb.typhondl.xtext.ui.updateWizard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;
import org.xml.sax.SAXException;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.typhonDL.Reference;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.utilities.DLmodelReader;
import de.atb.typhondl.xtext.ui.utilities.Database;
import de.atb.typhondl.xtext.ui.utilities.MLmodelReader;
import de.atb.typhondl.xtext.ui.utilities.SavingOptions;

public class ModelUpdater {

	// the main DL model file to be updated
	private IFile file;
	// URI of main DL model file
	private URI DLmodelURI;
	// the DL model
	private DeploymentModel DLmodel;
	// the active WorkbenchWindow to open the wizard in
	private IWorkbenchWindow window;
	// The resourceSet containing all DL resources in project folder
	private XtextResourceSet resourceSet;
	// The updated MLmodel
	private ArrayList<Database> MLmodel;

	public ModelUpdater(IFile file, IWorkbenchWindow window) {
		this.file = file;
		this.window = window;
		this.DLmodelURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		addResources();
	}

	/**
	 * Gets the provided ResourceSet and adds all .tdl files in the project folder
	 * to the ResourceSet
	 */
	private void addResources() {
		this.resourceSet = (XtextResourceSet) Activator.getInstance()
				.getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL)
				.getInstance(XtextLiveScopeResourceSetProvider.class).get(this.file.getProject());
		this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		IResource members[] = null;
		try {
			members = this.file.getProject().members();
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
		this.DLmodel = (DeploymentModel) resourceSet.getResource(DLmodelURI, true).getContents().get(0);
	}

	public String updateModel() {
		// read ML model
		MLmodel = new ArrayList<Database>();
		try {
			MLmodel = getMLmodel();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		// compare Databases of ML and DL models
		ArrayList<DB> DLdbs = DLmodelReader.getDBs(DLmodel);
		if (MLmodel.size() > DLdbs.size()) { // There are more DBs in the ML model
			// delete all DBs that are both in the ML and DL (only the new ones remain in
			// the ML model)
			for (DB db : DLdbs) {
				MLmodel.removeIf(databse -> databse.getName().equals(db.getName()));
			}
			// ask the user for additional information (DBMS or path to existing model file)
			MLmodel = openUpdateWizard();
			// Add the new DBs to the old DL model
			addDBsToDLmodel();
			// Create output String
			String updatedDBS = MLmodel.get(0).getName();
			String dbModelFiles = MLmodel.get(0).getPathToDBModelFile();
			for (int i = 1; i < MLmodel.size(); i++) {
				updatedDBS += ", " + MLmodel.get(i).getName();
				dbModelFiles += ", " + MLmodel.get(i).getPathToDBModelFile();
			}
			return "New container(s) created for " + updatedDBS + " in Application " + getFirstApplication().getName()
					+ ". Please add additional configuration details and check the database model file(s) "
					+ dbModelFiles + ".";
		} else {
			return "All ML databases match the DL databases. The DL model does not have to be updated via the Updater. "
					+ "Please add additional model content through the editor.";
		}
	}

	private ArrayList<Database> getMLmodel() throws ParserConfigurationException, SAXException, IOException {
		String MLmodelName = DLmodel.getGuiMetaInformation().stream()
				.filter(metaModel -> Import.class.isInstance(metaModel)).map(metaModel -> (Import) metaModel)
				.filter(info -> info.getRelativePath().endsWith(".xmi")).findFirst().map(info -> info.getRelativePath())
				.orElse("");
		File MLfile = new File(
				file.getLocation().toString().substring(0, file.getLocation().toString().lastIndexOf("/")) + "/"
						+ MLmodelName);
		return MLmodelReader.readXMIFile(MLfile.toURI());
	}

	private ArrayList<Database> openUpdateWizard() {
		UpdateModelWizard updateWizard = new UpdateModelWizard(MLmodel, file);
		WizardDialog dialog = new WizardDialog(window.getShell(), updateWizard);
		dialog.open();
		return updateWizard.getUpdatedMLmodel();
	}

	private void addDBsToDLmodel() {
		Resource DLmodelResource = DLmodel.eResource();
		for (Database newDatabase : MLmodel) {
			String relativePath;
			DB newDB;
			// 1. create new Resource if no DB model file exists
			if (newDatabase.getPathToDBModelFile() == null) {
				newDB = TyphonDLFactory.eINSTANCE.createDB();
				newDB.setName(newDatabase.getName());
				newDB.setType(newDatabase.getDbms());
				IMAGE image = TyphonDLFactory.eINSTANCE.createIMAGE();
				image.setValue(newDB.getType().getName() + ":latest");
				newDB.setImage(image);
				DeploymentModel dbModel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
				dbModel.getElements().add(newDB.getType());
				dbModel.getElements().add(newDB);
				relativePath = newDatabase.getName() + ".tdl";
				newDatabase.setPathToDBModelFile(relativePath);
				URI dbURI = DLmodel.eResource().getURI().trimSegments(1).appendSegment(relativePath);
				// delete resource in case it exists
				if (resourceSet.getResource(dbURI, false) != null) {
					try {
						resourceSet.getResource(dbURI, true).delete(Collections.EMPTY_MAP);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				Resource dbResource = resourceSet.createResource(dbURI);
				dbResource.getContents().add(dbModel);
				try {
					dbResource.save(SavingOptions.getTDLoptions());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else { // otherwise read provided DB model
				URI dbURI = DLmodel.eResource().getURI().trimSegments(1)
						.appendSegment(newDatabase.getPathToDBModelFile());
				newDB = getDB(resourceSet.getResource(dbURI, true));
				relativePath = newDatabase.getPathToDBModelFile();
			}

			// 2. add new database file to imports
			Import newImport = TyphonDLFactory.eINSTANCE.createImport();
			newImport.setRelativePath(relativePath);
			DLmodel.getGuiMetaInformation().add(newImport);

			// 3. craeate new dummy-container
			Container newContainer = TyphonDLFactory.eINSTANCE.createContainer();
			newContainer.setName(newDB.getName());
			newContainer.setType(getContainerType());
			Reference reference = TyphonDLFactory.eINSTANCE.createReference();
			reference.setReference(newDB);
			newContainer.setDeploys(reference);
			getFirstApplication().getContainers().add(newContainer);

			// 4. save updated model
			try {
				DLmodelResource.save(SavingOptions.getTDLoptions());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * This only works if there is only one DB in each DB model file
	 * 
	 * @param dbResource
	 * @return
	 */
	private DB getDB(Resource dbResource) {
		ArrayList<DB> dbs = new ArrayList<DB>();
		dbs.addAll(((DeploymentModel) dbResource.getContents().get(0)).getElements().stream()
				.filter(element -> DB.class.isInstance(element)).map(element -> (DB) element)
				.collect(Collectors.toList()));
		return dbs.get(0);
	}

	/*
	 * TODO only gets the fist app in the first cluster, maybe ask in the wizard
	 * where to put this,
	 * /de.atb.typhondl.xtext.ui/src/de/atb/typhondl/xtext/ui/utilities/Database.
	 * java has to get an attribute "application"
	 */
	private Application getFirstApplication() {
		return this.DLmodel.getElements().stream().filter(element -> Platform.class.isInstance(element))
				.map(element -> (Platform) element).collect(Collectors.toList()).get(0).getClusters().get(0)
				.getApplications().get(0);
	}

	private ContainerType getContainerType() {
		List<ContainerType> types = this.DLmodel.getElements().stream()
				.filter(element -> ContainerType.class.isInstance(element)).map(element -> (ContainerType) element)
				.collect(Collectors.toList());
		return types.get(0);
	}

	public XtextResourceSet getResourceSet() {
		return resourceSet;
	}
}
