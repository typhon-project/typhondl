package de.atb.typhondl.xtext.ui.editor;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferencePage;

import de.atb.typhondl.xtext.typhonDL.SupportedDBMS;

public class DBPage extends FieldEditorPreferencePage implements IPreferencePage {
	
	private SupportedDBMS supportedDBMS;

	public DBPage(SupportedDBMS supportedDBMS) {
		super();
		this.supportedDBMS = supportedDBMS;
	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		
	}
}
