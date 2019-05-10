package de.atb.typhondl.xtext.ui.wizard;

import org.eclipse.xtext.ui.wizard.template.BooleanTemplateVariable;
import org.eclipse.xtext.ui.wizard.template.StringSelectionTemplateVariable;
import org.eclipse.xtext.ui.wizard.template.StringTemplateVariable;

@SuppressWarnings("restriction")
public class Tuple {

	public StringSelectionTemplateVariable dbms;
	public StringTemplateVariable image;
	public BooleanTemplateVariable useTemplateImage;

	public Tuple(StringSelectionTemplateVariable dbms, StringTemplateVariable image,
			BooleanTemplateVariable useTemplateImage) {
		this.dbms = dbms;
		this.image = image;
		this.useTemplateImage = useTemplateImage;
	}

}
