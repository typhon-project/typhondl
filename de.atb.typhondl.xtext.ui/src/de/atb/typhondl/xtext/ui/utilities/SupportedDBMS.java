package de.atb.typhondl.xtext.ui.utilities;

import de.atb.typhondl.xtext.typhonDL.DBType;

public enum SupportedDBMS {
	relationaldb(){
		@Override
		public DBMS[] getPossibleDBMSs(){
			return PreferenceReader.readDBs("relationaldb");
		}
	},
	graphdb(){
		@Override
		public DBMS[] getPossibleDBMSs(){
			return PreferenceReader.readDBs("graphdb");
		}
	},
	documentdb(){
		@Override
		public DBMS[] getPossibleDBMSs(){
			return PreferenceReader.readDBs("documentdb");
		}
	},
	keyvaluedb(){
		@Override
		public DBMS[] getPossibleDBMSs(){
			return PreferenceReader.readDBs("keyvaluedb");
		}
	};
	
	public abstract DBMS[] getPossibleDBMSs();
	
	public DBType[] getDBMStypes() {
		DBMS[] possibleDBMSs = getPossibleDBMSs();
		DBType[] types = new DBType[possibleDBMSs.length];
		for (int i = 0; i < possibleDBMSs.length; i++) {
			types[i] = possibleDBMSs[i].getType();
		}
		return types;
	}	
	
	public DBType getTypeByDBMSName(String name) {
		DBMS[] possibleDBMSs = getPossibleDBMSs();
		for (int i = 0; i < possibleDBMSs.length; i++) {
			if (name.equalsIgnoreCase(possibleDBMSs[i].getName())) {
				return possibleDBMSs[i].getType();
			}
		}
		return null;
	}
	
	public String[] getDBMSnames() {
		DBMS[] possibleDBMSs = getPossibleDBMSs();
		String[] names = new String[possibleDBMSs.length];
		for (int i = 0; i < possibleDBMSs.length; i++) {
			names[i] = possibleDBMSs[i].getName();
		}
		return names;
	}
}