package de.atb.typhondl.xtext.ui.wizard;

public class Database {

	private String name;
	private DBType type;
	private String dbms;
	private String pathToImage;

	public Database(String name, DBType type) {
		this.name = name;
		this.type = type;
	}

	public String getDbms() {
		return dbms;
	}

	public void setDbms(String dbms) {
		this.dbms = dbms;
	}

	public String getName() {
		return name;
	}

	public DBType getType() {
		return type;
	}
	
	public String getPathToImage() {
		return pathToImage;
	}

	public void setPathToImage(String pathToImage) {
		this.pathToImage = pathToImage;
	}
}
