package de.atb.typhondl.xtext.ui.editor;

import org.eclipse.jface.preference.PreferenceNode;

public class PreferenceNodeFactory {
	public static PreferenceNode createPreferenceNode(EditorPage page) {
		return new PreferenceNode(page.getTitle(), page);		
	}
}
