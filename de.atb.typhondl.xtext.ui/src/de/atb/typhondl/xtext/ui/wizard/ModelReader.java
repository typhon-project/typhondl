package de.atb.typhondl.xtext.ui.wizard;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

public class ModelReader {

	private HashMap<String, Database> dbsMap;
	private DocumentBuilder builder;
	
	public ModelReader() {
		dbsMap = new HashMap<String, Database>();
	}
	
	public HashMap<String, Database> readXMIFile(URI uri) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		byte[] encoded = Files.readAllBytes(Paths.get(uri));
		ByteArrayInputStream input = new ByteArrayInputStream(encoded);
		Document doc = builder.parse(input);
		NodeList nList = doc.getElementsByTagName("databases");
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) node;
	               String name = eElement.getAttribute("name");
	               String dbType = eElement.getAttribute("xsi:type").split(":")[1].toLowerCase();
	               dbsMap.put(name, new Database(name, convertStringToDBType(dbType), ""));
			}
		}
		return dbsMap;
	}
	
	private DBType convertStringToDBType (String toConvert) {
		return DBType.valueOf(toConvert);
	}

	public HashMap<String, Database> readTMLFile(URI modelPath) throws IOException {
		try (Stream<String> stream = Files.lines(Paths.get(modelPath))) {
	        stream.forEach((String line) -> dbsMap
	        		.put(line.substring(0, line.indexOf(":")-1), new Database(line.substring(0, line.indexOf(":")-1), //name
	        								  convertStringToDBType(line.substring(line.indexOf(":")+2)), //type
	        								  "")) // TODO linesplit(":")
	        		);
		}
		return dbsMap;
	}
	
}
