package de.atb.typhondl.xtext.ui.utilities;

public enum DBMSdefaultConfiguration {
	MariaDB("mariadb:latest","3306",new String[] {"MYSQL_ROOT_PASSWORD ","example"}, null),
	MySQL("mysql:latest","3306",new String[] {"MYSQL_ROOT_PASSWORD ","example"}, new String[] {"command","--default-authentication-plugin=mysql_native_password"}),
	Mongo("mongo:latest","27018",new String[] {"MONGO_INITDB_ROOT_USERNAME", "admin", "MONGO_INITDB_ROOT_PASSWORD", "admin"}, null), 
	ArangoDB("","",null, null),
	Neo4j("","", null, null),
	Redis("","", null, null);
	
	public String getImage() {
		return image;
	}

	public String getPort() {
		return port;
	}

	public String[] getEnvironment() {
		return environment;
	}

	public String[] getKeyValues() {
		return keyValues;
	}
	
	private String image;
	private String port;
	private String[] environment;
	private String[] keyValues;
	
	private DBMSdefaultConfiguration(String image, String port, String[] environment, String[] keyValues) {
		this.image = image;
		this.port = port;
		this.environment = environment;
		this.keyValues = keyValues;
	}
}

