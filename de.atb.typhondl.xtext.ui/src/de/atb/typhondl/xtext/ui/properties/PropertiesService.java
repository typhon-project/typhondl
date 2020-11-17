package de.atb.typhondl.xtext.ui.properties;

/*-
 * #%L
 * de.atb.typhondl.xtext.ui
 * %%
 * Copyright (C) 2018 - 2020 ATB
 * %%
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * #L%
 */

/**
 * Service for finding property names
 * 
 * @author flug
 *
 */
public class PropertiesService {

    public static final String POLYSTORE_USEANALYTICS = "polystore.useAnalytics";
    public static final String POLYSTORE_INAPPLICATION = "polystore.inApplication";
    public static final String POLYSTORE_USEEVOLUTION = "polystore.useEvolution";
    public static final String POLYSTORE_USENLAE = "polystore.useNLAE";
    public static final String POLYSTORE_USENLAEDEV = "polystore.useNLAEDev";
    public static final String POLYSTORE_KUBECONFIG = "polystore.kubeconfig";
    public static final String POLYSTORE_LOGGING = "polystore.logging";
    public static final String LOGGING_ELASTICSEARCH_HOST = "logging.elasticsearch.host";
    public static final String API_NAME = "api.name";
    public static final String API_CONTAINERNAME = "api.containername";
    public static final String API_PORT = "api.port";
    public static final String API_PUBLISHEDPORT = "api.publishedPort";
    public static final String API_IMAGE = "api.image";
    public static final String API_ENTRYPOINT = "api.entrypoint";
    public static final String API_REPLICAS = "api.replicas";
    public static final String API_LIMIT_MEMORY = "api.limit.memory";
    public static final String API_LIMIT_CPU = "api.limit.cpu";
    public static final String API_RESERVATION_MEMORY = "api.reservation.memory";
    public static final String API_RESERVATION_CPU = "api.reservation.cpu";
    public static final String UI_NAME = "ui.name";
    public static final String UI_CONTAINERNAME = "ui.containername";
    public static final String UI_IMAGE = "ui.image";
    public static final String UI_PUBLISHEDPORT = "ui.publishedPort";
    public static final String UI_PORT = "ui.port";
    public static final String DB_NAME = "db.name";
    public static final String DB_CONTAINERNAME = "db.containername";
    public static final String DB_PORT = "db.port";
    public static final String DB_ENVIRONMENT_MONGO_INITDB_ROOT_USERNAME = "db.environment.MONGO_INITDB_ROOT_USERNAME";
    public static final String DB_ENVIRONMENT_MONGO_INITDB_ROOT_PASSWORD = "db.environment.MONGO_INITDB_ROOT_PASSWORD";
    public static final String DB_ENVIRONMENT_MONGO_INITDB_DATABASE = "db.environment.MONGO_INITDB_DATABASE";
    public static final String DB_VOLUME = "db.volume";
    public static final String DB_IMAGE = "db.image";
    public static final String QLSERVER_NAME = "qlserver.name";
    public static final String QLSERVER_CONTAINERNAME = "qlserver.containername";
    public static final String QLSERVER_IMAGE = "qlserver.image";
    public static final String QLSERVER_PORT = "qlserver.port";
    public static final String QLSERVER_REPLICAS = "qlserver.replicas";
    public static final String QLSERVER_LIMIT_MEMORY = "qlserver.limit.memory";
    public static final String QLSERVER_LIMIT_CPU = "qlserver.limit.cpu";
    public static final String QLSERVER_RESERVATION_MEMORY = "qlserver.reservation.memory";
    public static final String QLSERVER_RESERVATION_CPU = "qlserver.reservation.cpu";
    public static final String QLSERVER_TIMEZONE = "qlserver.timezone";
    public static final String ANALYTICS_DEPLOYMENT_CREATE = "analytics.deployment.create";
    public static final String ANALYTICS_DEPLOYMENT_CONTAINED = "analytics.deployment.contained";
    public static final String ANALYTICS_KUBECONFIG = "analytics.kubeconfig";
    public static final String ANALYTICS_AUTHALL_IMAGE = "analytics.authAll.image";
    public static final String ANALYTICS_ZOOKEEPER_CONTAINERNAME = "analytics.zookeeper.containername";
    public static final String ANALYTICS_ZOOKEEPER_PORT = "analytics.zookeeper.port";
    public static final String ANALYTICS_ZOOKEEPER_PUBLISHEDPORT = "analytics.zookeeper.publishedPort";
    public static final String ANALYTICS_ZOOKEEPER_IMAGE = "analytics.zookeeper.image";
    public static final String ANALYTICS_KAFKA_CONTAINERNAME = "analytics.kafka.containername";
    public static final String ANALYTICS_KAFKA_URI = "analytics.kafka.uri";
    public static final String ANALYTICS_KAFKA_VERSION = "analytics.kafka.version";
    public static final String ANALYTICS_KAFKA_SCALA_VERSION = "analytics.kafka.scala.version";
    public static final String ANALYTICS_KAFKA_LISTENERS = "analytics.kafka.listeners";
    public static final String ANALYTICS_KAFKA_INSIDEPORT = "analytics.kafka.insidePort";
    public static final String ANALYTICS_KAFKA_LISTENERNAME_IN = "analytics.kafka.listenerName.in";
    public static final String ANALYTICS_KAFKA_LISTENERNAME_OUT = "analytics.kafka.listenerName.out";
    public static final String ANALYTICS_KAFKA_CLUSTER_REPLICAS = "analytics.kafka.cluster.replicas";
    public static final String ANALYTICS_KAFKA_REPLICAS = "analytics.kafka.replicas";
    public static final String ANALYTICS_KAFKA_PORT = "analytics.kafka.port";
    public static final String ANALYTICS_KAFKA_STORAGECLAIM = "analytics.kafka.storageclaim";
    public static final String ANALYTICS_ZOOKEEPER_STORAGECLAIM = "analytics.zookeeper.storageclaim";
    public static final String ANALYTICS_FLINK_JOBMANAGER_HEAP_SIZE = "analytics.flink.jobmanager.heap.size";
    public static final String ANALYTICS_FLINK_TASKMANAGER_MEMORY_PROCESS_SIZE = "analytics.flink.taskmanager.memory.process.size";
    public static final String ANALYTICS_LOGGING_ROOTLOGGER = "analytics.logging.rootlogger";
    public static final String ANALYTICS_LOGGING_AKKA = "analytics.logging.akka";
    public static final String ANALYTICS_LOGGING_KAFKA = "analytics.logging.kafka";
    public static final String ANALYTICS_LOGGING_HADOOP = "analytics.logging.hadoop";
    public static final String ANALYTICS_LOGGING_ZOOKEEPER = "analytics.logging.zookeeper";
    public static final String ANALYTICS_LOGGING_FLINK = "analytics.logging.flink";
    public static final String ANALYTICS_FLINK_REST_PORT = "analytics.flink.rest.port";
    public static final String ANALYTICS_FLINK_TASKMANAGER_REPLICAS = "analytics.flink.taskmanager.replicas";
    public static final String EVOLUTION_DB_CONTAINERNAME = "evolution.db.containername";
    public static final String EVOLUTION_DB_ENVIRONMENT_MONGO_INITDB_DATABASE = "evolution.db.environment.MONGO_INITDB_DATABASE";
    public static final String EVOLUTION_DB_USERNAME = "evolution.db.username";
    public static final String EVOLUTION_DB_PASSWORD = "evolution.db.password";
    public static final String EVOLUTION_DB_IMAGE = "evolution.db.image";
    public static final String EVOLUTION_DB_PORT = "evolution.db.port";
    public static final String EVOLUTION_JAVA_CONTAINERNAME = "evolution.java.containername";
    public static final String EVOLUTION_JAVA_IMAGE = "evolution.java.image";
    public static final String EVOLUTION_JAVA_ENVIRONMENT_WAKEUP_TIME_MS_FREQUENCY = "evolution.java.environment.WAKEUP_TIME_MS_FREQUENCY";
    public static final String EVOLUTION_JAVA_ENVIRONMENT_KAFKA_CHANNEL_IP = "evolution.java.environment.KAFKA_CHANNEL_IP";
    public static final String EVOLUTION_JAVA_ENVIRONMENT_KAFKA_CHANNEL_PORT = "evolution.java.environment.KAFKA_CHANNEL_PORT";
    public static final String EVOLUTION_JAVA_ENVIRONMENT_WEBSERVICE_URL = "evolution.java.environment.WEBSERVICE_URL";
    public static final String EVOLUTION_JAVA_ENVIRONMENT_WEBSERVICE_USERNAME = "evolution.java.environment.WEBSERVICE_USERNAME";
    public static final String EVOLUTION_JAVA_ENVIRONMENT_WEBSERVICE_PASSWORD = "evolution.java.environment.WEBSERVICE_PASSWORD";
    public static final String EVOLUTION_JAVA_ENVIRONMENT_ANALYTICS_DB_IP = "evolution.java.environment.ANALYTICS_DB_IP";
    public static final String EVOLUTION_JAVA_ENVIRONMENT_ANALYTICS_DB_PORT = "evolution.java.environment.ANALYTICS_DB_PORT";
    public static final String EVOLUTION_JAVA_ENVIRONMENT_ANALYTICS_DB_USER = "evolution.java.environment.ANALYTICS_DB_USER";
    public static final String EVOLUTION_JAVA_ENVIRONMENT_ANALYTICS_DB_PWD = "evolution.java.environment.ANALYTICS_DB_PWD";
    public static final String EVOLUTION_JAVA_ENVIRONMENT_ANALYTICS_DB_NAME = "evolution.java.environment.ANALYTICS_DB_NAME";
    public static final String EVOLUTION_BACKEND_CONTAINERNAME = "evolution.backend.containername";
    public static final String EVOLUTION_BACKEND_IMAGE = "evolution.backend.image";
    public static final String EVOLUTION_BACKEND_PORT = "evolution.backend.port";
    public static final String EVOLUTION_BACKEND_ENVIRONMENT_BACKEND_DEPLOYMENT_PORT = "evolution.backend.environment.BACKEND_DEPLOYMENT_PORT";
    public static final String EVOLUTION_BACKEND_ENVIRONMENT_ANALYTICS_DB_URL = "evolution.backend.environment.ANALYTICS_DB_URL";
    public static final String EVOLUTION_BACKEND_ENVIRONMENT_ANALYTICS_DB_NAME = "evolution.backend.environment.ANALYTICS_DB_NAME";
    public static final String EVOLUTION_BACKEND_ENVIRONMENT_ANALYTICS_DB_USER = "evolution.backend.environment.ANALYTICS_DB_USER";
    public static final String EVOLUTION_BACKEND_ENVIRONMENT_ANALYTICS_DB_PWD = "evolution.backend.environment.ANALYTICS_DB_PWD";
    public static final String EVOLUTION_BACKEND_ENVIRONMENT_WEBSERVICE_URL = "evolution.backend.environment.WEBSERVICE_URL";
    public static final String EVOLUTION_BACKEND_ENVIRONMENT_WEBSERVICE_USERNAME = "evolution.backend.environment.WEBSERVICE_USERNAME";
    public static final String EVOLUTION_BACKEND_ENVIRONMENT_WEBSERVICE_PASSWORD = "evolution.backend.environment.WEBSERVICE_PASSWORD";
    public static final String EVOLUTION_BACKEND_PUBLISHEDPORT = "evolution.backend.publishedPort";
    public static final String EVOLUTION_FRONTEND_CONTAINERNAME = "evolution.frontend.containername";
    public static final String EVOLUTION_FRONTEND_IMAGE = "evolution.frontend.image";
    public static final String EVOLUTION_FRONTEND_ENVIRONMENT_BACKEND_ENDPOINT = "evolution.frontend.environment.BACKEND_ENDPOINT";
    public static final String EVOLUTION_FRONTEND_PORT = "evolution.frontend.port";
    public static final String EVOLUTION_FRONTEND_PUBLISHEDPORT = "evolution.frontend.publishedPort";
    public static final String NLAE_NAME = "nlae.name";
    public static final String NLAE_API_HOST = "nlae.api.host";
    public static final String NLAE_API_PORT = "nlae.api.port";
    public static final String NLAE_TASKMANAGER_REPLICAS = "nlae.taskmanager.replicas";
    public static final String NLAE_JOBMANAGER_HEAPSIZE = "nlae.jobmanager.heapsize";
    public static final String NLAE_TASKMANAGER_HEAPSIZE = "nlae.taskmanager.heapsize";
    public static final String NLAE_TASKMANAGER_SLOTS = "nlae.taskmanager.numberOfTaskSlots";
    public static final String NLAE_PARALLELISM = "nlae.flink.parallelism.default";
    public static final String NLAE_SHAREDVOLUME = "nlae.sharedvolume";
    public static final String NLAEDEV_IMAGE = "nlaedev.image";
    public static final String NLAEDEV_PUBLISHEDPORT = "nlaedev.publishedport";
    public static final String MARIADB_PORT = "mariadb.port";
    public static final String MYSQL_PORT = "mysql.port";
    public static final String MONGO_PORT = "mongo.port";
    public static final String NEO4J_PORT = "neo4j.port";
    public static final String CASSANDRA_PORT = "cassandra.port";
    public static final String MARIADBGALERA_PORT = "mariadbgalera.port";
    public static final String MONGOSHARDED_PORT = "mongosharded.port";
//    public static final String MARIADB_REPLICATION_DOCKERCOMPOSE= "#mariadb.replication.dockercompose";
    public static final String MONGO_REPLICATION_DOCKERCOMPOSE = "mongo.replication.dockercompose";
//    public static final String MARIADB_REPLICATION_KUBERNETES= "#mariadb.replication.kubernetes";
//    public static final String MONGO_REPLICATION_KUBERNETES= "#mongo.replication.kubernetes";
//    public static final String MARIADBGALERA_REPLICATION_KUBERNETES= "#mariadbgalera.replication.kubernetes";
//    public static final String MONGOSHARDED_REPLICATION_KUBERNETES= "#mongosharded.replication.kubernetes";
    public static final String MARIADB_CREDENTIALS = "mariadb.credentials";
    public static final String MONGO_CREDENTIALS = "mongo.credentials";
    public static final String NEO4J_CREDENTIALS = "neo4j.credentials";
    public static final String CASSANDRA_CREDENTIALS = "cassandra.credentials";
    public static final String MARIADBGALERA_CREDENTIALS = "mariadbgalera.credentials";
    public static final String MONGOSHARDED_CREDENTIALS = "mongosharded.credentials";
    public static final String RELATIONALDB = "relationaldb";
    public static final String DOCUMENTDB = "documentdb";
    public static final String GRAPHDB = "graphdb";
    public static final String KEYVALUEDB = "keyvaluedb";

}
