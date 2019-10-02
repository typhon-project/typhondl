package de.atb.typhondl.xtext.ui.utilities;

public enum SupportedDBMS {
	relationaldb(){
		@Override
		public String[] getPossibleDBMSs(){
			return new String[] {"MariaDB"};
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
			return new String[] {"Mongo"};
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