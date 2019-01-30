package de.atb.typhondl.xtext.ui.wizard;

public class Database {

	private String name;
	private DBType type;
	private String dbms;
	
	public Database(String name, DBType type, String dbms) {
		this.name = name;
		this.type = type;
		this.dbms = dbms;
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
}
