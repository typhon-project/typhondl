package de.atb.typhondl.xtext.ui.wizard;

import org.eclipse.xtext.ui.wizard.template.BooleanTemplateVariable;
import org.eclipse.xtext.ui.wizard.template.StringSelectionTemplateVariable;
import org.eclipse.xtext.ui.wizard.template.StringTemplateVariable;

@SuppressWarnings("restriction")
public class Tuple {

	public StringSelectionTemplateVariable dbms;
	public StringTemplateVariable databaseFile;
	public BooleanTemplateVariable useTemplateImage;

	public Tuple(StringSelectionTemplateVariable dbms, StringTemplateVariable databaseFile,
			BooleanTemplateVariable useTemplateImage) {
		this.dbms = dbms;
		this.databaseFile = databaseFile;
		this.useTemplateImage = useTemplateImage;
	}

}
