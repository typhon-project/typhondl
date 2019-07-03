package de.atb.typhondl.xtext.ui.wizard;

import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.IMAGE;

public class Database {

	private String name;
	private DBTypeForWizard type;
	private DBType dbms;
	private String pathToDBModelFile;
	private IMAGE image;

	public Database(String name, DBTypeForWizard type) {
		this.name = name;
		this.type = type;
	}

	public DBType getDbms() {
		return dbms;
	}

	public void setDbms(DBType dbms) {
		this.dbms = dbms;
	}

	public String getName() {
		return name;
	}

	public DBTypeForWizard getType() {
		return type;
	}
	
	public String getPathToDBModelFile() {
		return pathToDBModelFile;
	}

	public void setaPathToDBModelFile(String pathToDBModelFile) {
		this.pathToDBModelFile = pathToDBModelFile;
	}

	public IMAGE getImage() {
		return image;
	}

	public void setImage(IMAGE image) {
		this.image = image;
	}

}
