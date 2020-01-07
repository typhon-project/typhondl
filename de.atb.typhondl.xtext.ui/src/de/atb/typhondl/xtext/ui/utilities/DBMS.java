package de.atb.typhondl.xtext.ui.utilities;

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

	public DBMS(String name) {
		this.name = name;
		setDBType();
	}

	private void setDBType() {
		type = TyphonDLFactory.eINSTANCE.createDBType();
		type.setName(name);
	}

	public DBType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
