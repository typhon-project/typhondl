package de.atb.typhondl.xtext.ui.scriptGeneration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;

import de.atb.typhondl.acceleo.services.Options;
import de.atb.typhondl.acceleo.services.Services;
import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;

public class GenerationService {

    private IFile file;
    private XtextLiveScopeResourceSetProvider provider;
    private String outputFolder;
    private Properties properties;

    public GenerationService(IFile file, XtextLiveScopeResourceSetProvider provider) {
        this.file = file;
        this.provider = provider;
        this.properties = loadProperties();
        this.outputFolder = file.getLocation().toOSString().replace("." + file.getFileExtension(), "");
    }

    private Properties loadProperties() {
        String path = Paths.get(file.getLocationURI()).getParent().resolve(file.getName() + ".properties").toString();
        Properties properties = new Properties();
        try {
            InputStream input = new FileInputStream(path);
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

    public DeploymentModel buildModel() {
        DeploymentModelService deploymentModelService = new DeploymentModelService(file, provider, properties);
        deploymentModelService.readModel();
        deploymentModelService.addPolystore();
        deploymentModelService.addToMetadata();
        return deploymentModelService.getModel();
    }

    /**
     * Creates a .xmi file representing the given DL model
     * 
     * @param DLmodelResource The given DL model
     * @return URI to the saved DL model
     */
    public void saveModelAsXMI(DeploymentModel model) {
        Resource DLmodelResource = model.eResource();
        XtextResourceSet resourceSet = (XtextResourceSet) DLmodelResource.getResourceSet();
        URI fileName = DLmodelResource.getURI().trimFileExtension();
        Resource xmiResource = resourceSet.createResource(fileName.appendSegment(".xmi"));

        // delete resource in case it already exists
        if (resourceSet.getResource(fileName, false) != null) {
            try {
                resourceSet.getResource(fileName, true).delete(Collections.EMPTY_MAP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // add model
        xmiResource.getContents().add(model);

        EcoreUtil.resolveAll(resourceSet);
        try {
            xmiResource.save(Options.getXMIoptions());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateDeployment(DeploymentModel model) {
        if (properties.get("analytics.deployment.create").equals("true")
                && getClusterType(model).equalsIgnoreCase("Kubernetes")) {
            AnalyticsKubernetesService.addAnalyticsFiles(model, outputFolder, properties);
        }
        try {
            Services.generateDeployment(model, new File(outputFolder));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getClusterType(DeploymentModel model) {
        return EcoreUtil2.getAllContentsOfType(model, ClusterType.class).get(0).getName();
    }

    /**
     * Deletes all files and folders in given folder
     * 
     * @param folder All files an folders in this folder are deleted
     */
    public void deleteOldGeneratedFiles(File folder) {
        if (folder.exists()) {
            for (File subFile : folder.listFiles()) {
                if (subFile.isDirectory()) {
                    deleteOldGeneratedFiles(subFile);
                } else {
                    subFile.delete();
                }
            }
        }
    }

}
