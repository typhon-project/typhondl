package de.atb.typhondl.acceleo.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.common.util.BasicMonitor;

import de.atb.typhondl.acceleo.main.Generate;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;

/**
 * Utility class for the script generation process
 * 
 * @author flug
 *
 */
public class Services {

    public static void generateDeployment(DeploymentModel model, File file) throws IOException {
        new Generate(model, file, new ArrayList<String>()).doGenerate(new BasicMonitor());
    }
}
