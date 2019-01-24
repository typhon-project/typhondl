package de.atb.typhondl.xtext.ui.wizard;

public class Database {

	private String name;
	private DBType type;
	
	public Database(String name, DBType type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

//	public void setName(String name) { // QUESTION Not allowed to change name?
//		this.name = name;
//	}

	public DBType getType() {
		return type;
	}

//	public void setType(DBType type) { // QUESTION Not allowed to change type?
//		this.type = type;
//	}
}
