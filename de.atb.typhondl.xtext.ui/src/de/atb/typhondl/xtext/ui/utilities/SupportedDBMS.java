package de.atb.typhondl.xtext.ui.utilities;

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
	
	public String[] getDBMStypes() {
		DBMS[] possibleDBMSs = getPossibleDBMSs();
		String[] types = new String[possibleDBMSs.length];
		for (int i = 0; i < possibleDBMSs.length; i++) {
			types[i] = possibleDBMSs[i].getName();
		}
		return types;
	}	
}