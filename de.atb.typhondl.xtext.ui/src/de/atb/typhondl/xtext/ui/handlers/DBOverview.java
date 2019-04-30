package de.atb.typhondl.xtext.ui.handlers;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferencePage;

import de.atb.typhondl.xtext.typhonDL.DB;

public class DBOverview extends FieldEditorPreferencePage implements IPreferencePage {

	private DB db;
	
	public DBOverview(DB db) {
		super();
		this.db = db;
	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub

	}

}
