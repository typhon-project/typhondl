package de.atb.typhondl.xtext.ui.wizard;

import org.eclipse.xtext.ui.wizard.template.StringSelectionTemplateVariable;
import org.eclipse.xtext.ui.wizard.template.StringTemplateVariable;

@SuppressWarnings("restriction")
public class Tuple {

	public StringSelectionTemplateVariable dbms;
	public StringTemplateVariable image;
	
	public Tuple(StringSelectionTemplateVariable dbms, StringTemplateVariable image) {
		this.dbms = dbms;
		this.image = image;
	}

}
