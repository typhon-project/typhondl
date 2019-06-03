package de.atb.typhondl.xtext.ui.editor.pages;

import java.awt.Label;

import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class EditorPage extends PreferencePage implements IPreferencePage{

	public EditorPage(String title) {
		super(title);
		super.noDefaultButton();
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Label label = new Label("Please create subclass of this Page");
		label.setVisible(true);
		return parent;
	}

}
