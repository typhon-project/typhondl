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
	        stream.forEach((String line) -> dbsMap
	        		.put(line.substring(0, line.indexOf(":")-1), new Database(line.substring(0, line.indexOf(":")-1), //name
	        								  convertStringToDBType(line.substring(line.indexOf(":")+2)), //type
	        								  "")) //dbms
	        		);
		}
	}
	
	private DBType convertStringToDBType (String toConvert) {
		return DBType.valueOf(toConvert);
	}
	
	public HashMap<String, Database> getData(){
		return dbsMap;
	}
}
