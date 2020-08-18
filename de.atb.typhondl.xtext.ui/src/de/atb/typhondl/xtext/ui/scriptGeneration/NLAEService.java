package de.atb.typhondl.xtext.ui.scriptGeneration;

import java.util.Properties;

import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.typhonDL.URI;
import de.atb.typhondl.xtext.ui.modelUtils.SoftwareService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;

public class NLAEService {

    public static DeploymentModel addNLAE(DeploymentModel model, Properties properties) {
        Software nlae = SoftwareService.create("nlae", null);
        nlae.setExternal(true);
        URI uri = TyphonDLFactory.eINSTANCE.createURI();
        uri.setValue(properties.getProperty(PropertiesService.NLAE_API_HOST) + ":"
                + properties.getProperty(PropertiesService.NLAE_API_PORT));
        nlae.setUri(uri);
        model.getElements().add(nlae);
        return model;
    }

    public static void addNLAEFiles(DeploymentModel model, String outputFolder, Properties properties) {
        // TODO Auto-generated method stub

    }

}
