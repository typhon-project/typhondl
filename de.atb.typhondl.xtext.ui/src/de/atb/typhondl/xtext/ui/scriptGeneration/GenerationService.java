package de.atb.typhondl.xtext.ui.scriptGeneration;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;
import org.xml.sax.SAXException;

import de.atb.typhondl.acceleo.services.Options;
import de.atb.typhondl.acceleo.services.Services;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.ui.modelUtils.ModelService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.PropertiesLoader;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class GenerationService {

    private IFile file;
    private XtextLiveScopeResourceSetProvider provider;
    private String outputFolder;
    private Properties properties;
    private DeploymentModel model;

    public GenerationService(IFile file, XtextLiveScopeResourceSetProvider provider) throws IOException {
        this.file = file;
        this.provider = provider;
        this.properties = PropertiesLoader.loadPropertiesFromModelFile(file);
        String fileLocation = file.getLocation().toOSString();
        this.outputFolder = fileLocation.substring(0, fileLocation.lastIndexOf("." + file.getFileExtension()));
    }

    public void startGeneration() throws ParserConfigurationException, IOException, SAXException {
        this.model = DeploymentModelService.createModel(file, provider, properties, outputFolder);
        String dlXMIPath = saveModelAsXMI();
        this.model = DeploymentModelService.addToMetadata(outputFolder, getMLmodelPath(), dlXMIPath, file, properties,
                model);
        generateDeployment();
    }

    /**
     * Reads the relative MLmodel path from the Import section of the DL model
     * 
     * @param model the DL model
     * @return The relative MLmodel path as a String
     */
    private String getMLmodelPath() {
        return EcoreUtil2.getAllContentsOfType(this.model, Import.class).stream()
                .filter(info -> (info.getRelativePath().endsWith("xmi") || info.getRelativePath().endsWith("tmlx")))
                .map(info -> info.getRelativePath()).findFirst().orElse("");
    }

    /**
     * Creates a .xmi file representing the given DL model
     * 
     * @param DLmodelResource The given DL model
     * @return URI to the saved DL model
     */
    public String saveModelAsXMI() {
        Resource DLmodelResource = model.eResource();
        XtextResourceSet resourceSet = (XtextResourceSet) DLmodelResource.getResourceSet();
        URI fileName = URI
                .createPlatformResourceURI(
                        DLmodelResource.getURI().trimFileExtension().toPlatformString(true).concat("_DL"), true)
                .appendFileExtension("xmi");
        Resource xmiResource = resourceSet.createResource(fileName);

        // add model
        xmiResource.getContents().add(this.model);

        EcoreUtil.resolveAll(resourceSet);
        try {
            xmiResource.save(Options.getXMIoptions());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName.toString();
    }

    public void generateDeployment() {
        if (properties.get(PropertiesService.POLYSTORE_USEANALYTICS).equals("true") && ModelService
                .getSupportedTechnology(ModelService.getClusterType(this.model)) == SupportedTechnologies.Kubernetes) {
            AnalyticsKubernetesService.addAnalyticsFiles(this.model, outputFolder, properties);
        }
        if (properties.get(PropertiesService.POLYSTORE_USENLAE).equals("true")) {
            NLAEService.addNLAEFiles(this.model, outputFolder, properties);
        }
        try {
            Services.generateDeployment(this.model, new File(outputFolder));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public Properties getProperties() {
        return this.properties;
    }

}
