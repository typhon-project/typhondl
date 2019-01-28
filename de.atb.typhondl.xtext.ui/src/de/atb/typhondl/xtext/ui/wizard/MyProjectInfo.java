package de.atb.typhondl.xtext.ui.wizard;

import java.util.HashMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.xtext.ui.wizard.IExtendedProjectInfo;
import org.eclipse.xtext.ui.wizard.template.AbstractProjectTemplate;

@SuppressWarnings("restriction")
public class MyProjectInfo implements IExtendedProjectInfo {
	
	private final AbstractProjectTemplate projectTemplate;
	private final HashMap<String, Database> data;
	
	private String projectName;

	private IPath locationPath;

	public MyProjectInfo(AbstractProjectTemplate projectTemplate, HashMap<String, Database> data) {
		this.data = data;
		this.projectTemplate = projectTemplate;
	}
	
	public HashMap<String, Database> getData(){
		return data;
	}

	public AbstractProjectTemplate getProjectTemplate() {
		return projectTemplate;
	}


	@Override
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @since 2.10
	 */
	@Override
	public IPath getLocationPath() {
		return locationPath;
	}

	/**
	 * @since 2.10
	 */
	@Override
	public void setLocationPath(IPath locationPath) {
		this.locationPath = locationPath;
	}
}
