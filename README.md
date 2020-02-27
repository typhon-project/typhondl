[![Build Status](http://typhon.clmsuk.com:8080/buildStatus/icon?job=TyphonDL)](http://typhon.clmsuk.com:8080/job/TyphonDL/)

# TyphonDL

## Functions
- Create and edit database templates
- Create a TyphonDL model from a TyphonML model and the database queries
- Edit TyphonDL model in textual editor
- Generate docker-compose.yaml including
  1. A container for each database
  2. Two containers and additional scripts for the use of the analytics component (optional)
  3. A container for the Typhon UI
  4. A container for the Typhon API
  5. A container for metadata (mongodb)
- Generate polystore.yaml to run on a local minicube cluster (just a testbed) including deployment and service definitions for 
  1. A pod for each database
  2. A pod for the Typhon UI
  3. A pod for the Typhon API
  4. A pod for metadata (mongodb)
- Supported databases (depending on ML and QL):
  1. MariaDB
  2. MySQL
  3. MongoDB

## Features
- Database configuration templates in persistend preference storage of eclipse. There is a default configuration (compare to [wiki](https://github.com/typhon-project/typhondl/wiki/Guide)) that can be altered before generating the DL model. So company specific database properties can be configured and stored without much effort.
- TyphonDL Creation Wizard to create a TyphonDL model from a ML model. Files created:
  1. mainModelFile.tdl (named on the first page of the wizard)
  2. dbTypes.tdl (containes all used dbTypes, here the **image** has to be given)
  3. One databaseName.tdl model file for each database (here the environment and other database specific configurations are added)
  4. polystore.properties (input given while using the wizard is stored here, only alter if you know what you are doing)
- Generator for Docker Compose and minikube scripts

## In Developement
- Support volumes both in TyphonDL grammar and generator
- Allow nested `Key_KeyValueList`

## Future Functions and Features
- Put first version of ML and DL model in metadata container
- Support for Kubernetes
- Scaling possibilities
- Directly start Docker containers out of eclipse
- Include TyphonMLs [requirements annotations](https://github.com/typhon-project/internal-material/blob/master/Contract/Submitted%20Deliverables/D3.3%20TyphonML%20to%20TyphonDL%20Model%20Transformation%20Tools.pdf) in model generation process
- Smoke tests after build process

## Example
This example is very similar to the one given [here](https://github.com/typhon-project/typhondl/wiki/Guide).


```
import Test.tmlx
import DocumentDatabase.tdl
import dbTypes.tdl
containertype Docker
clustertype DockerCompose
platformtype localhost
platform platformName : localhost {
	cluster clusterName : DockerCompose {
		application Polystore {
			container DocumentDatabase : Docker {
				deploys DocumentDatabase
				ports {
					target = 27017 ;
					published = 27018 ;
				}
				hostname = localhost ;
                restart = always;
                stop_grace_period: 1m30s;
			}
		}
	}
}

dbtype mongo {
	default image = mongo:latest ;
}

database DocumentDatabase : mongo {
	environment {
		MONGO_INITDB_ROOT_USERNAME = admin ;
		MONGO_INITDB_ROOT_PASSWORD = admin ;
	}
    command = --serviceExecutor adaptive;
}
```