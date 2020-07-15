db.models.insert([{"_id":UUID(), "version":1, "initializedDatabases":false, "initializedConnections":true, "contents":"import weatherModel.xmiimport TextWarningData.tdlimport VehicleMetadataDB.tdlimport AppData.tdlimport VehicleDataDB.tdlimport dbTypes.tdlcontainertype Dockerclustertype DockerComposeplatformtype localhostplatform platformName : localhost {	cluster clusterName : DockerCompose {		application Polystore {			container textwarningdata : Docker {				deploys TextWarningData				uri = textwarningdata:27017 ;			}			container vehiclemetadatadb : Docker {				deploys VehicleMetadataDB				uri = vehiclemetadatadb:3306 ;			}			container appdata : Docker {				deploys AppData				uri = appdata:3306 ;			}			container vehicledatadb : Docker {				deploys VehicleDataDB				uri = vehicledatadb:27017 ;			}		}	}}", "type":"DL", "dateReceived":ISODate(), "_class":"com.clms.typhonapi.models.Model" }, {"_id":UUID(), "version":1, "initializedDatabases":false, "initializedConnections":false, "contents":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><typhonDL:DeploymentModel xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:typhonDL=\"http://www.typhon.org/dsls/xtext/TyphonDL\">  <guiMetaInformation xsi:type=\"typhonDL:Import\" relativePath=\"weatherModel.xmi\"/>  <guiMetaInformation xsi:type=\"typhonDL:Import\" relativePath=\"TextWarningData.tdl\"/>  <guiMetaInformation xsi:type=\"typhonDL:Import\" relativePath=\"VehicleMetadataDB.tdl\"/>  <guiMetaInformation xsi:type=\"typhonDL:Import\" relativePath=\"AppData.tdl\"/>  <guiMetaInformation xsi:type=\"typhonDL:Import\" relativePath=\"VehicleDataDB.tdl\"/>  <guiMetaInformation xsi:type=\"typhonDL:Import\" relativePath=\"dbTypes.tdl\"/>  <elements xsi:type=\"typhonDL:ContainerType\" name=\"Docker\"/>  <elements xsi:type=\"typhonDL:ClusterType\" name=\"DockerCompose\"/>  <elements xsi:type=\"typhonDL:PlatformType\" name=\"localhost\"/>  <elements xsi:type=\"typhonDL:Platform\" name=\"platformName\" type=\"//@elements.2\">    <clusters name=\"clusterName\" type=\"//@elements.1\">      <applications name=\"Polystore\">        <containers name=\"textwarningdata\" type=\"//@elements.0\">          <deploys reference=\"//@elements.4\"/>          <uri value=\"textwarningdata:27017\"/>        </containers>        <containers name=\"vehiclemetadatadb\" type=\"//@elements.0\">          <deploys reference=\"//@elements.5\"/>          <uri value=\"vehiclemetadatadb:3306\"/>        </containers>        <containers name=\"appdata\" type=\"//@elements.0\">          <deploys reference=\"//@elements.6\"/>          <uri value=\"appdata:3306\"/>        </containers>        <containers name=\"vehicledatadb\" type=\"//@elements.0\">          <deploys reference=\"//@elements.7\"/>          <uri value=\"vehicledatadb:27017\"/>        </containers>        <containers name=\"polystore-mongo\" type=\"//@elements.0\">          <deploys reference=\"//@elements.10\"/>          <uri value=\"polystore-mongo:27017\"/>          <properties xsi:type=\"typhonDL:Key_ValueArray\" name=\"volumes\">            <values>./models/:/docker-entrypoint-initdb.d</values>          </properties>        </containers>        <containers name=\"typhon-polystore-service\" type=\"//@elements.0\">          <deploys reference=\"//@elements.11\"/>          <ports>            <key_values name=\"published\" value=\"8080\"/>            <key_values name=\"target\" value=\"8080\"/>          </ports>          <uri value=\"typhon-polystore-service:8080\"/>          <properties xsi:type=\"typhonDL:Key_ValueArray\" name=\"entrypoint\">            <values>wait-for-it</values>            <values>polystore-mongo:27017</values>            <values>-t</values>            <values>'60'</values>            <values>--</values>            <values>java</values>            <values>-cp</values>            <values>/app/resources:/app/classes:/app/libs/*</values>            <values>com.clms.typhonapi.Application</values>          </properties>          <properties xsi:type=\"typhonDL:Key_Values\" name=\"restart\" value=\"always\"/>        </containers>        <containers name=\"polystore-ui\" type=\"//@elements.0\">          <deploys reference=\"//@elements.12\"/>          <depends_on reference=\"//@elements.3/@clusters.0/@applications.0/@containers.5\"/>          <ports>            <key_values name=\"published\" value=\"4200\"/>            <key_values name=\"target\" value=\"4200\"/>          </ports>          <uri value=\"polystore-ui:4200\"/>        </containers>        <containers name=\"typhonql-server\" type=\"//@elements.0\">          <deploys reference=\"//@elements.13\"/>          <uri value=\"typhonql-server:7000\"/>          <properties xsi:type=\"typhonDL:Key_Values\" name=\"restart\" value=\"always\"/>        </containers>        <containers name=\"kafka\" type=\"//@elements.0\">          <deploys reference=\"//@elements.15\"/>          <depends_on reference=\"//@elements.3/@clusters.0/@applications.0/@containers.10\"/>          <ports>            <key_values name=\"published\" value=\"29092\"/>            <key_values name=\"target\" value=\"29092\"/>          </ports>          <uri value=\"localhost:29092\"/>          <properties xsi:type=\"typhonDL:Key_ValueArray\" name=\"volumes\">            <values>/var/run/docker.sock:/var/run/docker.sock</values>          </properties>        </containers>        <containers name=\"authAll\" type=\"//@elements.0\">          <deploys reference=\"//@elements.18\"/>        </containers>        <containers name=\"zookeeper\" type=\"//@elements.0\">          <deploys reference=\"//@elements.14\"/>          <ports>            <key_values name=\"published\" value=\"2181\"/>            <key_values name=\"target\" value=\"2181\"/>          </ports>          <uri value=\"localhost:2181\"/>        </containers>        <containers name=\"taskmanager\" type=\"//@elements.0\">          <deploys reference=\"//@elements.17\"/>          <depends_on reference=\"//@elements.3/@clusters.0/@applications.0/@containers.12\"/>          <properties xsi:type=\"typhonDL:Key_Values\" name=\"command\" value=\"taskmanager\"/>          <properties xsi:type=\"typhonDL:Key_ValueArray\" name=\"expose\">            <values>6121</values>            <values>6122</values>          </properties>        </containers>        <containers name=\"jobmanager\" type=\"//@elements.0\">          <deploys reference=\"//@elements.16\"/>          <ports>            <key_values name=\"published\" value=\"8081\"/>            <key_values name=\"target\" value=\"8081\"/>          </ports>          <properties xsi:type=\"typhonDL:Key_Values\" name=\"command\" value=\"jobmanager\"/>          <properties xsi:type=\"typhonDL:Key_ValueArray\" name=\"expose\">            <values>6123</values>          </properties>        </containers>      </applications>    </clusters>  </elements>  <elements xsi:type=\"typhonDL:DB\" name=\"TextWarningData\" type=\"//@elements.9\">    <credentials username=\"chooseUsername\" password=\"choosePassword\"/>  </elements>  <elements xsi:type=\"typhonDL:DB\" name=\"VehicleMetadataDB\" type=\"//@elements.8\">    <credentials username=\"root\" password=\"choosePassword\"/>  </elements>  <elements xsi:type=\"typhonDL:DB\" name=\"AppData\" type=\"//@elements.8\">    <credentials username=\"root\" password=\"choosePassword\"/>  </elements>  <elements xsi:type=\"typhonDL:DB\" name=\"VehicleDataDB\" type=\"//@elements.9\">    <credentials username=\"chooseUsername\" password=\"choosePassword\"/>  </elements>  <elements xsi:type=\"typhonDL:DBType\" name=\"MariaDB\">    <image value=\"mariadb:latest\"/>  </elements>  <elements xsi:type=\"typhonDL:DBType\" name=\"Mongo\">    <image value=\"mongo:latest\"/>  </elements>  <elements xsi:type=\"typhonDL:DB\" name=\"polystore_db\" type=\"//@elements.9\">    <environment>      <parameters name=\"MONGO_INITDB_DATABASE\" value=\"admin\"/>    </environment>    <credentials username=\"admin\" password=\"admin\"/>  </elements>  <elements xsi:type=\"typhonDL:Software\" name=\"polystore_api\">    <image value=\"clms/typhon-polystore-api:latest\"/>  </elements>  <elements xsi:type=\"typhonDL:Software\" name=\"polystore_ui\">    <image value=\"clms/typhon-polystore-ui:latest\"/>    <environment>      <parameters name=\"API_PORT\" value=\"8080\"/>      <parameters name=\"API_HOST\" value=\"localhost\"/>    </environment>  </elements>  <elements xsi:type=\"typhonDL:Software\" name=\"polystore_ql\">    <image value=\"swatengineering/typhonql-server\"/>  </elements>  <elements xsi:type=\"typhonDL:Software\" name=\"zookeeper\">    <image value=\"wurstmeister/zookeeper\"/>  </elements>  <elements xsi:type=\"typhonDL:Software\" name=\"Kafka\">    <image value=\"wurstmeister/kafka:2.12-2.4.0\"/>    <environment>      <parameters name=\"KAFKA_ZOOKEEPER_CONNECT\" value=\"zookeeper:2181\"/>      <parameters name=\"KAFKA_ADVERTISED_HOST_NAME\" value=\"localhost\"/>      <parameters name=\"KAFKA_LISTENERS\" value=\"OUTSIDE://:29092, INSIDE://:9092\"/>      <parameters name=\"KAFKA_LISTENER_SECURITY_PROTOCOL_MAP\" value=\"INSIDE:PLAINTEXT, OUTSIDE:PLAINTEXT\"/>      <parameters name=\"KAFKA_INTER_BROKER_LISTENER_NAME\" value=\"INSIDE\"/>      <parameters name=\"KAFKA_ADVERTISED_LISTENERS\" value=\"OUTSIDE://localhost:29092, INSIDE://:9092\"/>      <parameters name=\"KAFKA_AUTO_CREATE_TOPICS_ENABLE\" value=\"&quot;true&quot;\"/>    </environment>  </elements>  <elements xsi:type=\"typhonDL:Software\" name=\"FlinkJobmanager\">    <image value=\"flink:latest\"/>    <environment>      <parameters name=\"JOB_MANAGER_RPC_ADDRESS\" value=\"jobmanager\"/>    </environment>  </elements>  <elements xsi:type=\"typhonDL:Software\" name=\"FlinkTaskmanager\">    <image value=\"flink:latest\"/>    <environment>      <parameters name=\"JOB_MANAGER_RPC_ADDRESS\" value=\"jobmanager\"/>    </environment>  </elements>  <elements xsi:type=\"typhonDL:Software\" name=\"authAll\">    <image value=\"zolotas4/typhon-analytics-auth-all\"/>  </elements></typhonDL:DeploymentModel>", "type":"ML", "dateReceived":ISODate(), "_class":"com.clms.typhonapi.models.Model" }]);