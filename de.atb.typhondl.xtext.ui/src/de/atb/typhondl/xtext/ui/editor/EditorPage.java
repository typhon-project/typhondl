package de.atb.typhondl.xtext.ui.editor;

import java.awt.Label;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferencePage;

public class EditorPage extends FieldEditorPreferencePage implements IPreferencePage{

	public EditorPage(String title) {
		super();
		this.setTitle(title);
	}
	
	@Override
	protected void createFieldEditors() {
		Label label = new Label("Please create subclass of this Page");
		label.setVisible(true);
	}

}
