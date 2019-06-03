package de.atb.typhondl.xtext.ui.editor;

import org.eclipse.jface.preference.PreferenceNode;

import de.atb.typhondl.xtext.ui.editor.pages.EditorPage;

public class PreferenceNodeFactory {
	public static PreferenceNode createPreferenceNode(EditorPage page) {
		return new PreferenceNode(page.getTitle(), page);		
	}
}
