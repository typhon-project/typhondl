package de.atb.typhondl.xtext.ui.wizard;

public enum DBTypeForWizard {
	relationaldb(){
		@Override String[] getPossibleDBMSs(){
			return new String[] {"MariaDB", "MySQL"};
		}
	},
	graphdb(){
		@Override String[] getPossibleDBMSs(){
			return new String[] {"ArangoDB", "Neo4j"};
		}
	},
	documentdb(){
		@Override String[] getPossibleDBMSs(){
			return new String[] {"Mongo", "CouchDB"};
		}
	},
	keyvaluedb(){
		@Override String[] getPossibleDBMSs(){
			return new String[] {"ArangoDB", "Redis"};
		}
	};
	
	abstract String[] getPossibleDBMSs(); 
}