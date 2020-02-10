package de.atb.typhondl.xtext.ui.utilities;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

public class MLmodelReader {

	public static ArrayList<Pair<String, String>> readXMIFile(URI uri) throws ParserConfigurationException, SAXException, IOException {
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
