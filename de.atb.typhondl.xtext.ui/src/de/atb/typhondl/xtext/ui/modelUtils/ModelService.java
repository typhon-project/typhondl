package de.atb.typhondl.xtext.ui.modelUtils;

import org.eclipse.xtext.EcoreUtil2;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Platform;

public class ModelService {

    public static Application getFirstApplication(DeploymentModel model) {
        return EcoreUtil2.getAllContentsOfType(model, Application.class).get(0);
    }

    public static Container getContainer(DeploymentModel model, String containerName) {
        return EcoreUtil2.getAllContentsOfType(model, Container.class).stream()
                .filter(container -> container.getName().equalsIgnoreCase(containerName)).findFirst().orElse(null);
    }

    public static Platform getPlatform(DeploymentModel model) {
        return EcoreUtil2.getAllContentsOfType(model, Platform.class).get(0);
    }

}
