package de.atb.typhondl.xtext.ui.utilities;

import java.util.ArrayList;

import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

import de.atb.typhondl.xtext.typhonDL.DB;

public class WizardFields {

	private Button checkbox;
	private Combo combo;
	private ArrayList<Pair<DB, TemplateBuffer>> templateBuffers;

	public WizardFields(Button checkbox, Combo combo, ArrayList<Pair<DB, TemplateBuffer>> templates) {
		this.checkbox = checkbox;
		this.combo = combo;
		this.templateBuffers = templates;
	}

	public void setCheckbox(Button checkbox) {
		this.checkbox = checkbox;
	}

	public void setCombo(Combo combo) {
		this.combo = combo;
	}

	public Button getCheckbox() {
		return checkbox;
	}

	public Combo getCombo() {
		return combo;
	}

	public ArrayList<Pair<DB, TemplateBuffer>> getTemplateBuffers() {
		return templateBuffers;
	}
}
