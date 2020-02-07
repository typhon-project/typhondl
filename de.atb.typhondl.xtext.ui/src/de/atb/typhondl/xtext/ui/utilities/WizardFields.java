package de.atb.typhondl.xtext.ui.utilities;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

import de.atb.typhondl.xtext.typhonDL.DB;

public class WizardFields {

	private Button checkbox;
	private Combo combo;
	private DB[] dbTemplates;

	public WizardFields(Button checkbox, Combo combo, DB[] dbTemplates) {
		this.checkbox = checkbox;
		this.combo = combo;
		this.dbTemplates = dbTemplates;
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
//
//	public DB getChosenTemplate() {
//		return dbTemplates;
//	}
//
//	public void setChosenTemplate(DB chosenTemplate) {
//		this.dbTemplates = chosenTemplate;
//	}

	public DB[] getDbTemplates() {
		return dbTemplates;
	}
}
