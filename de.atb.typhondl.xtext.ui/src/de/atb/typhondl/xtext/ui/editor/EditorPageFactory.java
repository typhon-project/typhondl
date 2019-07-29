package de.atb.typhondl.xtext.ui.editor;

import org.eclipse.emf.ecore.EObject;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.ui.editor.pages.ApplicationPage;
import de.atb.typhondl.xtext.ui.editor.pages.ClusterPage;
import de.atb.typhondl.xtext.ui.editor.pages.ContainerPage;
import de.atb.typhondl.xtext.ui.editor.pages.DBPage;
import de.atb.typhondl.xtext.ui.editor.pages.EditorPage;
import de.atb.typhondl.xtext.ui.editor.pages.PlatformOverview;

public class EditorPageFactory {
    public static EditorPage createEditorPage(EObject modelObject) {
    	if (modelObject == null) return new EditorPage("Error");
    	switch (modelObject.eClass().getName()) {
		case "Cluster":
			return new ClusterPage((Cluster) modelObject);
		case "DB":
			return new DBPage((DB) modelObject);
		case "Platform":
			return new PlatformOverview((Platform) modelObject);
		case "Container":
			return new ContainerPage((Container) modelObject);
		case "Application":
			return new ApplicationPage((Application) modelObject);
		default:
			return new EditorPage("Error");
		}
    }
}
