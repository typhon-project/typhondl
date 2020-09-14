[![Build Status](http://typhon.clmsuk.com:8080/buildStatus/icon?job=TyphonDL)](http://typhon.clmsuk.com:8080/job/TyphonDL/)

# TyphonDL

The Polystore - and therefore the TyphonDL model - consists of the following components:

* Typhon API
* Typhon UI
* Typhon Metadata Database
* Typhon QL
* Optional Typhon Analytics
* The User Databases
  
The user is only able to edit the DL model for the user databases and the analytics component, the other configuration parameters are provided by the respective components and are not editable.
To create a Polystore, the TyphonDL Tools generate a TyphonDL model from a given TyphonML model. After completing the TyphonDL model, scripts are generated and the Polystore is started. When the Polystore is started, the ML and DL model are uploaded to the Typhon Metadata Database automatically. The Typhon API parses the DL model and provides the other components with connection information about all Polystore components. This way the DL model contains “addresses” to all the Polystore components.

A TyphonDL model can be created with the help of the [TyphonDL Creation Wizard](#typhondl-creation-wizard) from the ML model. The wizard uses the previously defined (default or use-case specific) [Templates](#templates) and creates a TyphonDL model file and additional model files for every database that can be edited with the [textual editor](#typhondl-editor). When the DL model is ready, the [TyphonDL Script Generator](#typhondl-script-generation) can be used to generate technology dependent deployment scripts. For further usage details consult the [wiki](https://github.com/typhon-project/typhondl/wiki/User-Guide).

## Installation

There are two possible ways to add the TyphonDL plugin to Eclipse:

1. Using the Typhon Update Site in _Eclipse -> Help -> Install New Software... -> <http://typhon.clmsuk.com:8082/>_
2. Cloning this repository and building the update site with `mvn clean package`, then installing with _Eclipse -> Help -> Install New Software... -> Add... -> Archive... -> .../typhondl/typhondl-update-site/target/typhondl-update-site-1.0.0-SNAPSHOT.zip_

## Usage

Please consult the [wiki](https://github.com/typhon-project/typhondl/wiki/User-Guide).

## Polystore Deployment

After generating the Scripts the Polystore can be deployed

### Docker Compose
Start the polystore:
```bash
$ docker-compose up -d
```
Show all running containers:
```bash
$ docker-compose ps
```
Show logs of a specific service (e.g. the API):
```bash
$ docker-compose logs typhon-polystore-service
``` 
Stop and remove the polystore:
```bash
$ docker-compose down
```

### Docker Swarm

If the deployment scripts contain a Resource definition or the replication of stateless polystore parts (i.e. API, QL server and Analytics.Kafka), Docker has to be run in [Swarm Mode](https://docs.docker.com/engine/swarm/swarm-tutorial/create-swarm/).
Start the polystore:
```bash
$ docker stack deploy --compose-file docker-compose.yaml typhon
```
Show all running containers:
```bash
$ docker stack services typhon
```
Stop and delete all Polystore containers:
```bash
$ docker stack rm typhon
```

### Kubernetes
Start the polystore:
```bash
$ sh deploy.sh
```
Stop and delete the Polystore deplyoment:
```bash
$ kubectl delete namespaces typhon
```
If the analytics component was started and also should be stopped and removed:
```bash
$ kubectl delete namespaces kafka
```
