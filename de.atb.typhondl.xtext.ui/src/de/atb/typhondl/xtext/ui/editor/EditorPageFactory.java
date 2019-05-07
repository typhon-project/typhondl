package de.atb.typhondl.xtext.ui.editor;

import org.eclipse.emf.ecore.EObject;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Deployment;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.SupportedDBMS;
import de.atb.typhondl.xtext.ui.editor.pages.ApplicationPage;
import de.atb.typhondl.xtext.ui.editor.pages.ClusterPage;
import de.atb.typhondl.xtext.ui.editor.pages.ContainerPage;
import de.atb.typhondl.xtext.ui.editor.pages.DBOverview;
import de.atb.typhondl.xtext.ui.editor.pages.DBPage;
import de.atb.typhondl.xtext.ui.editor.pages.DeploymentOverview;
import de.atb.typhondl.xtext.ui.editor.pages.MyOverview;

public class EditorPageFactory {
    public static EditorPage createEditorPage(EObject modelObject) {
    	switch (modelObject.eClass().getName()) {
		case "Cluster":
			return new ClusterPage((Cluster) modelObject);
		case "SupportedDBMS"://TODO
		case "MariaDB":
		case "Mongo":
			return new DBPage((SupportedDBMS) modelObject);
		case "DB":
			return new DBOverview((DB) modelObject);
		case "Deployment":
			return new DeploymentOverview((Deployment) modelObject);
		case "DeploymentModel": 
			return new MyOverview((DeploymentModel) modelObject);
		case "Container":
			return new ContainerPage((Container) modelObject);
		case "Application":
			return new ApplicationPage((Application) modelObject);
		default:;
			return new EditorPage("Error");
		}
    }
}