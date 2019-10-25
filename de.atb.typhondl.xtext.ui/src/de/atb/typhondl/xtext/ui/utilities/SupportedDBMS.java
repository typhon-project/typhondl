package de.atb.typhondl.xtext.ui.utilities;

public enum SupportedDBMS {
	relationaldb(){
		@Override
		public String[] getPossibleDBMSs(){
			return new String[] {DBMSdefaultConfiguration.MariaDB.name(), DBMSdefaultConfiguration.MySQL.name()};
		}
	},
	graphdb(){
		@Override
		public String[] getPossibleDBMSs(){
			return new String[] {"not supported yet"};
		}
	},
	documentdb(){
		@Override
		public String[] getPossibleDBMSs(){
			return new String[] {DBMSdefaultConfiguration.Mongo.name()};
		}
	},
	keyvaluedb(){
		@Override
		public String[] getPossibleDBMSs(){
			return new String[] {"not supported yet"};
		}
	};
	
	public abstract String[] getPossibleDBMSs(); 
}