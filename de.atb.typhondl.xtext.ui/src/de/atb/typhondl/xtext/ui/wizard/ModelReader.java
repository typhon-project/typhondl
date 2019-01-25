package de.atb.typhondl.xtext.ui.wizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;


public class ModelReader {

	private HashMap<String, Database> dbsMap;
	
	public ModelReader(URI uri) {
		this.dbsMap = new HashMap<String, Database>();
		try {
			readFile(uri);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readFile(URI uri) throws IOException {
		try (Stream<String> stream = Files.lines(Paths.get(uri))) {
	        stream.forEach(System.out::println);
		}
	}
}
