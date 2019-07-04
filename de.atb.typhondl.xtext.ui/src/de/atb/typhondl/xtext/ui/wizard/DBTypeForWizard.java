package de.atb.typhondl.xtext.ui.wizard;

public enum DBTypeForWizard {
	relationaldb(){
		@Override
		public String[] getPossibleDBMSs(){
			return new String[] {"MariaDB", "MySQL"};
		}
	},
	graphdb(){
		@Override
		public String[] getPossibleDBMSs(){
			return new String[] {"ArangoDB", "Neo4j"};
		}
	},
	documentdb(){
		@Override
		public String[] getPossibleDBMSs(){
			return new String[] {"Mongo", "CouchDB"};
		}
	},
	keyvaluedb(){
		@Override
		public String[] getPossibleDBMSs(){
			return new String[] {"ArangoDB", "Redis"};
		}
	};
	
	public abstract String[] getPossibleDBMSs(); 
}