package de.atb.typhondl.xtext.ui.editor;

import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Deployment;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.editor.pages.DBOverview;
import de.atb.typhondl.xtext.ui.utilities.DLmodelReader;

public class TyphonEditorDialog extends PreferenceDialog {

	// path to model resource
	private IPath path;

	public TyphonEditorDialog(Shell parentShell, IPath path) {
		this(parentShell, createManager(parentShell, path));
		this.path = path;
	}

	public TyphonEditorDialog(Shell parentShell, PreferenceManager manager) {
		super(parentShell, manager);
	}

	private static PreferenceManager createManager(Shell parentShell, IPath path) {
		DeploymentModel model = DLmodelReader.readDLmodel(path); // adds all resources to ResourceSet
		PreferenceManager preferenceManager = createPages(model);
		return preferenceManager;
	}

	private static PreferenceManager createPages(DeploymentModel model) {
		PreferenceManager preferenceManager = new PreferenceManager();

		ArrayList<DB> dbs = DLmodelReader.getDBs(model);
		PreferenceNode databaseNode = PreferenceNodeFactory.createPreferenceNode(new DBOverview(dbs));
		preferenceManager.addToRoot(databaseNode);
		for (DB db : dbs) {
			databaseNode.add(PreferenceNodeFactory.createPreferenceNode(EditorPageFactory.createEditorPage(db)));
		}

		PreferenceNode deploymentNode = PreferenceNodeFactory
				.createPreferenceNode(EditorPageFactory.createEditorPage(getDeployment(model)));

		for (Cluster cluster : getClusters(model)) {
			PreferenceNode clusterNode = PreferenceNodeFactory
					.createPreferenceNode(EditorPageFactory.createEditorPage(cluster));
			deploymentNode.add(clusterNode);
			for (Application application : cluster.getApplications()) {
				PreferenceNode appNode = PreferenceNodeFactory
						.createPreferenceNode(EditorPageFactory.createEditorPage(application));
				clusterNode.add(appNode);
				for (Container container : application.getContainers()) {
					appNode.add(
							PreferenceNodeFactory.createPreferenceNode(EditorPageFactory.createEditorPage(container)));
				}
			}
		}

		preferenceManager.addToRoot(deploymentNode);
		return preferenceManager;
	}

	private static EList<Cluster> getClusters(DeploymentModel model) {
		return getDeployment(model).getClusters();
	}

	private static Deployment getDeployment(DeploymentModel model) {
		return model.getElements().stream().filter(element -> Deployment.class.isInstance(element)).findFirst()
				.map(deployment -> (Deployment) deployment).orElse(null);
	}

	public void start() {
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
		this.create();
		final Image image = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_PATH);
		this.getShell().setImage(image);
		String modelName = path.lastSegment().substring(0, path.lastSegment().lastIndexOf('.'));
		this.getShell().setText("Change TyphonDL Model \"" + modelName + "\"");
		this.open();
	}
}
