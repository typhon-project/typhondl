package de.atb.typhondl.xtext.ui.utilities;

import java.util.ArrayList;

import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

public class WizardFields {

	private Button checkbox;
	private Combo combo;
	private ArrayList<Pair<String, TemplateBuffer>> templateBuffers;

	public WizardFields(Button checkbox, Combo combo, ArrayList<Pair<String, TemplateBuffer>> buffers) {
		this.checkbox = checkbox;
		this.combo = combo;
		this.templateBuffers = buffers;
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

	public ArrayList<Pair<String, TemplateBuffer>> getTemplateBuffers() {
		return templateBuffers;
	}
}
