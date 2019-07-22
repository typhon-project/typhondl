package de.atb.typhondl.xtext.ui.utilities;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

public class WizardFields {

	private Button checkbox;
	private Combo combo;
	private Text textField;
	
	public WizardFields(Button checkbox, Combo combo, Text textField) {
		this.checkbox = checkbox;
		this.combo = combo;
		this.textField = textField;
	}

	public Button getCheckbox() {
		return checkbox;
	}

	public Combo getCombo() {
		return combo;
	}

	public Text getTextField() {
		return textField;
	}
}
