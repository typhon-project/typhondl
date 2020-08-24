package de.atb.typhondl.xtext.ui.refactoring;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;

import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.ui.utilities.PropertiesLoader;
import de.atb.typhondl.xtext.ui.utilities.SavingOptions;

public class ClusterTypeRefactor {

    public static void refactor(EObject selectedElement, XtextEditor editor, XtextResource resource) {
        if (ClusterType.class.isInstance(selectedElement)) {
            DeploymentModel model = (DeploymentModel) resource.getContents().get(0);
            Properties properties = null;
            final String pathToProperties = ((FileEditorInput) editor.getEditorInput()).getFile().getLocationURI()
                    .toString().replace("file:", "").concat(".properties");
            try {
                properties = PropertiesLoader.loadPropertiesFromModelFile(pathToProperties);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ClusterTypeDialog dialog = new ClusterTypeDialog(editor.getShell(), properties, model,
                    (ClusterType) selectedElement);
            if (dialog.open() == Window.OK) {
                Properties newProperties = dialog.getProperties();
                try {
                    resource.save(SavingOptions.getTDLoptions());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    OutputStream output = new FileOutputStream(pathToProperties);
                    newProperties.store(output, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}
