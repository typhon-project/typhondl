package de.atb.typhondl.xtext.ui.refactoring;

import java.io.IOException;
import java.util.Properties;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.xtext.resource.XtextResource;

import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.ui.utilities.PropertiesLoader;

public class ClusterTypeRefactor {

    public static void refactor(EObject selectedElement, FileEditorInput fileEditorInput, XtextResource resource) {
        if (ClusterType.class.isInstance(selectedElement)) {
            DeploymentModel model = (DeploymentModel) resource.getContents().get(0);
            Properties properties = null;
            try {
                properties = PropertiesLoader.loadPropertiesFromModelFile(fileEditorInput.getFile().getLocationURI()
                        .toString().replace("file:", "").concat(".properties"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
