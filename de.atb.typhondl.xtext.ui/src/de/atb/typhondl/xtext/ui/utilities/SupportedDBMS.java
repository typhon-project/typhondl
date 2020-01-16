package de.atb.typhondl.xtext.ui.utilities;

public enum SupportedDBMS {
	relationaldb(){
		@Override
		public DBMS[] getPossibleDBMSs(){
			//return new DBMS[] {new DBMS("", "MariaDB"), new DBMS("", "MySQL")};
			return PreferenceReader.readDBs("relationaldb");
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
			return new DBMS[] {new DBMS("", "Mongo")};
		}
	},
	keyvaluedb(){
		@Override
		public DBMS[] getPossibleDBMSs(){
			return new DBMS[] {null};
		}
	};
	
	public abstract DBMS[] getPossibleDBMSs();
	
	public String[] getDBMStypes() {
		DBMS[] possibleDBMSs = getPossibleDBMSs();
		String[] types = new String[possibleDBMSs.length];
		for (int i = 0; i < possibleDBMSs.length; i++) {
			types[i] = possibleDBMSs[i].getType().getName();
		}
		return types;
	}	
}