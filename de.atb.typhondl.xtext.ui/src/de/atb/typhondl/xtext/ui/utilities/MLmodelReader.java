package de.atb.typhondl.xtext.ui.utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Utility class for parsinf the selected ML model
 * 
 * @author flug
 *
 */
public class MLmodelReader {

	/**
	 * Reads the selected ML model file
	 * 
	 * @param uri The URI to the selected ML model file
	 * @return A List of String {@link Pairs}s containing the database name and
	 *         abstract type (i.e. relationaldb, documentdb, keyvaluedb or graphdb)
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static ArrayList<Pair<String, String>> readXMIFile(URI uri)
			throws ParserConfigurationException, SAXException, IOException {
		ArrayList<Pair<String, String>> dbsMap = new ArrayList<>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		byte[] encoded = Files.readAllBytes(Paths.get(uri));
		ByteArrayInputStream input = new ByteArrayInputStream(encoded);
		Document doc = builder.parse(input);
		NodeList nList = doc.getElementsByTagName("databases");
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				String name = eElement.getAttribute("name");
				String abstractType = eElement.getAttribute("xsi:type").split(":")[1].toLowerCase();
				dbsMap.add(new Pair<String, String>(name, abstractType));
			}
		}
		return dbsMap;
	}
}
