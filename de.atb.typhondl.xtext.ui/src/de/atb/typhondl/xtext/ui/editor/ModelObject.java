package de.atb.typhondl.xtext.ui.editor;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class ModelObject {

	private String name;

	public ModelObject(EObject object) {
		this.name = object.eClass().getName();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
