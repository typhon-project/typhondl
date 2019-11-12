package de.atb.typhondl.xtext.ui.utilities;

public enum SupportedDBMS {
	relationaldb(){
		@Override
		public String[] getPossibleDBMSs(){
			return new String[] {"MariaDB", "MySQL"};
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
			return new String[] {"Mongo"};
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