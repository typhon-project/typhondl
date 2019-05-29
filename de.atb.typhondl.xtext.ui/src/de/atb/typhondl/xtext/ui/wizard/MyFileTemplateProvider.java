package de.atb.typhondl.xtext.ui.wizard;

import java.net.URI;

import org.eclipse.xtext.ui.wizard.template.AbstractFileTemplate;
import org.eclipse.xtext.ui.wizard.template.IFileTemplateProvider;

@SuppressWarnings("restriction")
public class MyFileTemplateProvider  implements IFileTemplateProvider {
	  @Override
	  public AbstractFileTemplate[] getFileTemplates() {
	    DockerComposeFile _dockerComposeFile = new DockerComposeFile();
	    //KubernetesFile _kubernetesFile = new KubernetesFile();
	    return new AbstractFileTemplate[] {_dockerComposeFile};
	  }

	public AbstractFileTemplate[] getFileTemplates(URI modelPath) {
	    DockerComposeFile _dockerComposeFile = new DockerComposeFile(modelPath);
	    //KubernetesFile _kubernetesFile = new KubernetesFile();
	    return new AbstractFileTemplate[] {_dockerComposeFile};
	}
}
