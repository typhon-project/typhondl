package de.atb.typhondl.xtext.ui.utilities;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;

import de.atb.typhondl.acceleo.services.Services;

public abstract class GenerationService {

    public static void generateDeployment(IFile file, XtextLiveScopeResourceSetProvider provider) {

        File outputFolder = new File(file.getLocation().toOSString().replace("." + file.getFileExtension(), ""));
        Services.oldGenerateDeployment(file, provider);
        deleteOldGeneratedFiles(outputFolder);
    }

    private static void deleteOldGeneratedFiles(File outputFolder) {
        // TODO Auto-generated method stub

    }

}
