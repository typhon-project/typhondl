package de.atb.typhondl.xtext.ui.utilities;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

/**
 * 
 * @author flug
 *
 */
public class DBMS {
	
	private String name;
	private DBType type;
	private String abstractType;
	private String pathToDBModelFile;
	private DB databaseModel;

	public DBMS(String name) {
		this.name = name;
	}
	
	public DBMS(String name, String abstractType) {
		this.name = name;
		this.abstractType = abstractType;
	}
	
	public DBMS(String name, DBType type, String abstractType) {
		this.name = name;
		this.type = type;
		this.abstractType = abstractType;
	}
	
	public void removeDBType() {
		this.type = null;
	}

	public void setDBType(DBType type) {
		this.type = type;
	}
	
	public void setDBType(String type) {
		this.type = TyphonDLFactory.eINSTANCE.createDBType();
		this.type.setName(type);
	}

	public DBType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getPathToDBModelFile() {
		return pathToDBModelFile;
	}

	public void setPathToDBModelFile(String pathToDBModelFile) {
		this.pathToDBModelFile = pathToDBModelFile;
	}

	public String getAbstractType() {
		return abstractType;
	}

}
