package de.atb.typhondl.xtext.ui.wizard;

import java.util.HashMap;

import org.eclipse.xtext.ui.wizard.template.AbstractProjectTemplate;
import org.eclipse.xtext.ui.wizard.template.TemplateProjectInfo;

@SuppressWarnings("restriction")
public class MyProjectInfo extends TemplateProjectInfo {
	
	private final HashMap<String, Database> data;

	public MyProjectInfo(AbstractProjectTemplate projectTemplate, HashMap<String, Database> data) {
		super(projectTemplate);
		this.data = data;
	}
	
	public HashMap<String, Database> getData(){
		return data;
	}
}
