package de.atb.typhondl.xtext.ui.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to load properties from classpath
 * 
 * @author flug
 *
 */
public class PropertiesLoader {

	private static final String PROPERTIES_PATH = "de/atb/typhondl/xtext/ui/properties/polystore.properties";

	/**
	 * Loads polystore.properties from classpath
	 * 
	 * @return polystore.properties
	 * @throws IOException
	 */
	public static Properties loadProperties() throws IOException {

		Properties properties = new Properties();

		InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH);
		properties.load(input);
		return properties;
	}
}
