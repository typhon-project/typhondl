package de.atb.typhondl.acceleo.services;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.xmi.XMLResource;

public class Options {

	private static String getXMIencoding() {
		return "UTF-8";
	}

	public static Map<Object, Object> getXMIoptions() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(XMLResource.OPTION_ENCODING, getXMIencoding());
		return options;
	}

}
