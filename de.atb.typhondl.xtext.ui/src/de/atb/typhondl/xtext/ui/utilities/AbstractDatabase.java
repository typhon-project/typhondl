package de.atb.typhondl.xtext.ui.utilities;

/**
 * This class is for internal data transfer between plugin parts, can be replaced by DBMS (WIP)
 * @author flug
 *
 */
public class AbstractDatabase {

	private String name;
	private SupportedDBMS type;
//	private DBType dbms;
//	private String pathToDBModelFile;
//	private IMAGE image;

	public AbstractDatabase(String name, SupportedDBMS type) {
		this.name = name;
		this.type = type;
	}
//
//	public DBType getDbms() {
//		return dbms;
//	}
//
//	public void setDbms(DBType dbms) {
//		this.dbms = dbms;
//	}

	public String getName() {
		return name;
	}

	public SupportedDBMS getType() {
		return type;
	}
	
//	public String getPathToDBModelFile() {
//		return pathToDBModelFile;
//	}
//
//	public void setPathToDBModelFile(String pathToDBModelFile) {
//		this.pathToDBModelFile = pathToDBModelFile;
//	}
//
//	public IMAGE getImage() {
//		return image;
//	}
//
//	public void setImage(IMAGE image) {
//		this.image = image;
//	}

}
