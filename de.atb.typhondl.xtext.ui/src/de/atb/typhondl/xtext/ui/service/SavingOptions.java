package de.atb.typhondl.xtext.ui.service;

import java.util.Map;

import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.resource.SaveOptions.Builder;

public class SavingOptions {
	
	public static Map<Object, Object> getTDLoptions() {
		Builder builder = SaveOptions.newBuilder();
		builder.format();
		return builder.getOptions().toOptionsMap();
	}
}
