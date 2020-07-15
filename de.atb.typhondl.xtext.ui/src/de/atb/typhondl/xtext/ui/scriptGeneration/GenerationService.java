package de.atb.typhondl.xtext.ui.scriptGeneration;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;

import de.atb.typhondl.acceleo.services.Services;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;

public class GenerationService {

    private IFile file;
    private XtextLiveScopeResourceSetProvider provider;
    private File outputFolder;
    private DeploymentModel model;

    public GenerationService(IFile file, XtextLiveScopeResourceSetProvider provider) {
        this.file = file;
        this.provider = provider;

        this.outputFolder = new File(file.getLocation().toOSString().replace("." + file.getFileExtension(), ""));
        deleteOldGeneratedFiles(outputFolder);
        this.model = loadXtextModel(file, provider);
    }

    public void generateDeployment() {
        try {
            Services.generateDeployment(model, outputFolder);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private DeploymentModel loadXtextModel(IFile file, XtextLiveScopeResourceSetProvider provider) {
        // TODO Auto-generated method stub
        return null;
    }

    private void deleteOldGeneratedFiles(File outputFolder) {
        // TODO Auto-generated method stub

    }

}
