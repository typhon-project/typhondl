package de.atb.typhondl.xtext.ui.wizard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ImageReader {

	public static void readImageConfigFromXML(URI uri) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		byte[] encoded = Files.readAllBytes(Paths.get(uri));
		ByteArrayInputStream input = new ByteArrayInputStream(encoded);
		Document doc = builder.parse(input);
		NodeList nList = doc.getChildNodes();
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			NamedNodeMap attributes = node.getAttributes();
			System.out.println(attributes.toString());
		}
	}

}
