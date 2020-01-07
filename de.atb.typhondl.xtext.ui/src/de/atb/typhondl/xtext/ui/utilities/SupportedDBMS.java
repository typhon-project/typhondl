package de.atb.typhondl.xtext.ui.utilities;

public enum SupportedDBMS {
	relationaldb(){
		@Override
		public DBMS[] getPossibleDBMSs(){
			return new DBMS[] {new DBMS("MariaDB"), new DBMS("MySQL")};
		}
	},
	graphdb(){
		@Override
		public DBMS[] getPossibleDBMSs(){
			return new DBMS[] {null};
		}
	},
	documentdb(){
		@Override
		public DBMS[] getPossibleDBMSs(){
			return new DBMS[] {new DBMS("Mongo")};
		}
	},
	keyvaluedb(){
		@Override
		public DBMS[] getPossibleDBMSs(){
			return new DBMS[] {null};
		}
	};
	
	public abstract DBMS[] getPossibleDBMSs();
	
	public String[] getDBMSnames() {
		DBMS[] possibleDBMSs = getPossibleDBMSs();
		String[] names = new String[possibleDBMSs.length];
		for (int i = 0; i < possibleDBMSs.length; i++) {
			names[i] = possibleDBMSs[i].getName();
		}
		return names;
	}
	
	public DBMS getDBMS(String dbName) {
		for (DBMS db : getPossibleDBMSs()) {
			if (db.getName().equalsIgnoreCase(dbName))
				return db;
		}
		throw new RuntimeException("Database " + dbName + " not found");
	}
	
}