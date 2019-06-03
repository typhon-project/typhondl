package de.atb.typhondl.xtext.ui.wizard;

public class Database {

	private String name;
	private DBType type;
	private String dbms;
	private String pathToDBModelFile;

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
	
	public String getPathToDBModelFile() {
		return pathToDBModelFile;
	}

	public void setaPathToDBModelFile(String pathToDBModelFile) {
		this.pathToDBModelFile = pathToDBModelFile;
	}
}
