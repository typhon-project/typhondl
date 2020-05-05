db.models.insert([{"_id":UUID(), "version":1, "initializedDatabases":false, "initializedConnections":true, "contents":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><typhonDL:DeploymentModel xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:typhonDL=\"http://www.typhon.org/dsls/xtext/TyphonDL\">  <guiMetaInformation xsi:type=\"typhonDL:Import\" relativePath=\"weatherModel.xmi\"/>  <guiMetaInformation xsi:type=\"typhonDL:Import\" relativePath=\"VehicleMetadataDB.tdl\"/>  <guiMetaInformation xsi:type=\"typhonDL:Import\" relativePath=\"TextWarningData.tdl\"/>  <guiMetaInformation xsi:type=\"typhonDL:Import\" relativePath=\"VehicleDataDB.tdl\"/>  <guiMetaInformation xsi:type=\"typhonDL:Import\" relativePath=\"AppData.tdl\"/>  <guiMetaInformation xsi:type=\"typhonDL:Import\" relativePath=\"dbTypes.tdl\"/>  <elements xsi:type=\"typhonDL:ContainerType\" name=\"Docker\"/>  <elements xsi:type=\"typhonDL:ClusterType\" name=\"DockerCompose\"/>  <elements xsi:type=\"typhonDL:PlatformType\" name=\"localhost\"/>  <elements xsi:type=\"typhonDL:Platform\" name=\"platformName\" type=\"//@elements.2\">    <clusters name=\"clusterName\" type=\"//@elements.1\">      <applications name=\"Polystore\">        <containers name=\"VehicleMetadataDB\" type=\"//@elements.0\">          <deploys reference=\"//@elements.4\"/>          <ports>            <key_values name=\"target\" value=\"3306\"/>          </ports>        </containers>        <containers name=\"TextWarningData\" type=\"//@elements.0\">          <deploys reference=\"//@elements.5\"/>          <ports>            <key_values name=\"target\" value=\"27017\"/>          </ports>        </containers>        <containers name=\"VehicleDataDB\" type=\"//@elements.0\">          <deploys reference=\"//@elements.6\"/>          <ports>            <key_values name=\"target\" value=\"27017\"/>          </ports>        </containers>        <containers name=\"AppData\" type=\"//@elements.0\">          <deploys reference=\"//@elements.7\"/>          <ports>            <key_values name=\"target\" value=\"3306\"/>          </ports>        </containers>        <containers name=\"polystore-mongo\" type=\"//@elements.0\">          <deploys reference=\"//@elements.10\"/>          <ports>            <key_values name=\"target\" value=\"27017\"/>          </ports>          <properties xsi:type=\"typhonDL:Key_ValueArray\" name=\"volumes\">            <values>./models/:/docker-entrypoint-initdb.d</values>          </properties>        </containers>        <containers name=\"typhon-polystore-service\" type=\"//@elements.0\">          <deploys reference=\"//@elements.11\"/>          <ports>            <key_values name=\"target\" value=\"8080\"/>            <key_values name=\"published\" value=\"8080\"/>          </ports>        </containers>        <containers name=\"polystore-ui\" type=\"//@elements.0\">          <deploys reference=\"//@elements.12\"/>          <depends_on reference=\"//@elements.3/@clusters.0/@applications.0/@containers.5\"/>          <ports>            <key_values name=\"target\" value=\"4200\"/>            <key_values name=\"published\" value=\"4200\"/>          </ports>        </containers>        <containers name=\"typhonql-server\">          <deploys reference=\"//@elements.13\"/>          <ports>            <key_values name=\"target\" value=\"7000\"/>          </ports>        </containers>      </applications>    </clusters>  </elements>  <elements xsi:type=\"typhonDL:DB\" name=\"VehicleMetadataDB\" type=\"//@elements.8\">    <parameters xsi:type=\"typhonDL:Key_KeyValueList\" name=\"environment\">      <properties xsi:type=\"typhonDL:Key_Values\" name=\"MYSQL_ROOT_PASSWORD\" value=\"password\"/>    </parameters>  </elements>  <elements xsi:type=\"typhonDL:DB\" name=\"TextWarningData\" type=\"//@elements.9\">    <parameters xsi:type=\"typhonDL:Key_KeyValueList\" name=\"environment\">      <properties xsi:type=\"typhonDL:Key_Values\" name=\"MONGO_INITDB_ROOT_USERNAME\" value=\"username\"/>      <properties xsi:type=\"typhonDL:Key_Values\" name=\"MONGO_INITDB_ROOT_PASSWORD\" value=\"password\"/>    </parameters>  </elements>  <elements xsi:type=\"typhonDL:DB\" name=\"VehicleDataDB\" type=\"//@elements.9\">    <parameters xsi:type=\"typhonDL:Key_KeyValueList\" name=\"environment\">      <properties xsi:type=\"typhonDL:Key_Values\" name=\"MONGO_INITDB_ROOT_USERNAME\" value=\"username\"/>      <properties xsi:type=\"typhonDL:Key_Values\" name=\"MONGO_INITDB_ROOT_PASSWORD\" value=\"password\"/>    </parameters>  </elements>  <elements xsi:type=\"typhonDL:DB\" name=\"AppData\" type=\"//@elements.8\">    <parameters xsi:type=\"typhonDL:Key_KeyValueList\" name=\"environment\">      <properties xsi:type=\"typhonDL:Key_Values\" name=\"MYSQL_ROOT_PASSWORD\" value=\"password\"/>    </parameters>  </elements>  <elements xsi:type=\"typhonDL:DBType\" name=\"MariaDB\">    <image value=\"mariadb:latest\"/>  </elements>  <elements xsi:type=\"typhonDL:DBType\" name=\"Mongo\">    <image value=\"mongo:latest\"/>  </elements>  <elements xsi:type=\"typhonDL:DB\" name=\"polystore_db\" type=\"//@elements.9\">    <parameters xsi:type=\"typhonDL:Key_KeyValueList\" name=\"environment\">      <properties xsi:type=\"typhonDL:Key_Values\" name=\"MONGO_INITDB_ROOT_USERNAME\" value=\"admin\"/>      <properties xsi:type=\"typhonDL:Key_Values\" name=\"MONGO_INITDB_ROOT_PASSWORD\" value=\"admin\"/>      <properties xsi:type=\"typhonDL:Key_Values\" name=\"MONGO_INITDB_DATABASE\" value=\"admin\"/>    </parameters>  </elements>  <elements xsi:type=\"typhonDL:Software\" name=\"polystore_api\">    <image value=\"clms/typhon-polystore-api:latest\"/>  </elements>  <elements xsi:type=\"typhonDL:Software\" name=\"polystore_ui\">    <image value=\"clms/typhon-polystore-ui:latest\"/>    <parameters xsi:type=\"typhonDL:Key_KeyValueList\" name=\"environment\">      <properties xsi:type=\"typhonDL:Key_Values\" name=\"API_PORT\" value=\"8080\"/>      <properties xsi:type=\"typhonDL:Key_Values\" name=\"API_HOST\" value=\"localhost\"/>    </parameters>  </elements>  <elements xsi:type=\"typhonDL:Software\" name=\"polystore_ql\">    <image value=\"swatengineering/typhonql-server\"/>  </elements></typhonDL:DeploymentModel>", "type":"DL", "dateReceived":ISODate(), "_class":"com.clms.typhonapi.models.Model" }, {"_id":UUID(), "version":1, "initializedDatabases":false, "initializedConnections":false, "contents":"<?xml version=\"1.0\" encoding=\"ASCII\"?><typhonml:Model xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:typhonml=\"http://org.typhon.dsls.typhonml.sirius\">  <databases xsi:type=\"typhonml:RelationalDB\" name=\"VehicleMetadataDB\">    <tables name=\"VehicleMetadataDB\" entity=\"//@dataTypes.4\">      <indexSpec name=\"metadataIndex\" attributes=\"//@dataTypes.4/@attributes.0\"/>      <idSpec attributes=\"//@dataTypes.4/@attributes.0\"/>    </tables>  </databases>  <databases xsi:type=\"typhonml:DocumentDB\" name=\"VehicleDataDB\">    <collections name=\"VehicleWeatherDataDB\" entity=\"//@dataTypes.3\"/>    <collections name=\"ESPDB\" entity=\"//@dataTypes.5\"/>  </databases>  <databases xsi:type=\"typhonml:RelationalDB\" name=\"AppData\">    <tables name=\"AppDB\" entity=\"//@dataTypes.6\">      <indexSpec name=\"appIndex\" attributes=\"//@dataTypes.6/@attributes.0\"/>    </tables>    <tables name=\"AnalyticsDB\" entity=\"//@dataTypes.9\">      <indexSpec name=\"analyticsIndex\" attributes=\"//@dataTypes.9/@attributes.14\"/>    </tables>  </databases>  <databases xsi:type=\"typhonml:DocumentDB\" name=\"TextWarningData\">    <collections name=\"TextWarningDB\" entity=\"//@dataTypes.8\"/>    <collections name=\"WarningWeatherDB\" entity=\"//@dataTypes.7\"/>  </databases>  <dataTypes xsi:type=\"typhonml:PrimitiveDataType\" name=\"Int\"/>  <dataTypes xsi:type=\"typhonml:PrimitiveDataType\" name=\"String\"/>  <dataTypes xsi:type=\"typhonml:PrimitiveDataType\" name=\"Bool\"/>  <dataTypes xsi:type=\"typhonml:Entity\" name=\"VehicleWeatherData\">    <attributes name=\"VIN\" type=\"//@dataTypes.0\"/>    <attributes name=\"timeStamp\" type=\"//@dataTypes.1\"/>    <attributes name=\"vehicle_position\" type=\"//@dataTypes.1\"/>    <attributes name=\"temperature\" type=\"//@dataTypes.0\"/>    <attributes name=\"rain_Intensity\" type=\"//@dataTypes.0\"/>    <attributes name=\"solar_Intensity\" type=\"//@dataTypes.0\"/>    <relations name=\"metadata\" type=\"//@dataTypes.4\" cardinality=\"one\" isContainment=\"true\"/>  </dataTypes>  <dataTypes xsi:type=\"typhonml:Entity\" name=\"VehicleMetadata\">    <attributes name=\"VIN\" type=\"//@dataTypes.0\"/>    <attributes name=\"brand\" type=\"//@dataTypes.1\"/>    <attributes name=\"model\" type=\"//@dataTypes.1\"/>    <attributes name=\"constr_year\" type=\"//@dataTypes.0\"/>    <attributes name=\"color\" type=\"//@dataTypes.1\"/>    <attributes name=\"t_sensor_h\" type=\"//@dataTypes.0\"/>    <attributes name=\"engine_type\" type=\"//@dataTypes.1\"/>  </dataTypes>  <dataTypes xsi:type=\"typhonml:Entity\" name=\"ESP\">    <attributes name=\"VIN\" type=\"//@dataTypes.0\"/>    <attributes name=\"timeStamp\" type=\"//@dataTypes.1\"/>    <attributes name=\"vehicle_position\" type=\"//@dataTypes.1\"/>    <attributes name=\"esp_edl\" type=\"//@dataTypes.2\"/>    <attributes name=\"esp_idd\" type=\"//@dataTypes.2\"/>    <attributes name=\"esp_abs\" type=\"//@dataTypes.2\"/>    <relations name=\"vweather\" type=\"//@dataTypes.3\" cardinality=\"one\" isContainment=\"true\"/>  </dataTypes>  <dataTypes xsi:type=\"typhonml:Entity\" name=\"App\">    <attributes name=\"VIN\" type=\"//@dataTypes.0\"/>    <attributes name=\"timeStamp\" type=\"//@dataTypes.1\"/>    <attributes name=\"vehicle_position\" type=\"//@dataTypes.1\"/>    <relations name=\"esp\" type=\"//@dataTypes.5\" cardinality=\"zero_many\" isContainment=\"true\"/>    <relations name=\"warnings\" type=\"//@dataTypes.7\" cardinality=\"zero_many\" isContainment=\"true\"/>  </dataTypes>  <dataTypes xsi:type=\"typhonml:Entity\" name=\"WarningWeatherData\">    <attributes name=\"time_start\" type=\"//@dataTypes.1\"/>    <attributes name=\"time_end\" type=\"//@dataTypes.1\"/>    <attributes name=\"warningType\" type=\"//@dataTypes.1\"/>    <attributes name=\"severity\" type=\"//@dataTypes.0\"/>    <attributes name=\"area\" type=\"//@dataTypes.1\"/>  </dataTypes>  <dataTypes xsi:type=\"typhonml:Entity\" name=\"TextWarning\">    <attributes name=\"timeStamp\" type=\"//@dataTypes.1\"/>    <attributes name=\"text\" type=\"//@dataTypes.1\"/>  </dataTypes>  <dataTypes xsi:type=\"typhonml:Entity\" name=\"ExtractedWarning\">    <attributes name=\"ORGANIZATION\" type=\"//@dataTypes.1\"/>    <attributes name=\"WEATHER_EVENT\" type=\"//@dataTypes.1\"/>    <attributes name=\"WEATHER_EVENT_INTENSITY\" type=\"//@dataTypes.1\"/>    <attributes name=\"WEATHER_EVENT_COUNT\" type=\"//@dataTypes.0\"/>    <attributes name=\"LOCATION\" type=\"//@dataTypes.1\"/>    <attributes name=\"WARNING_LEVEL\" type=\"//@dataTypes.0\"/>    <attributes name=\"DAY\" type=\"//@dataTypes.1\"/>    <attributes name=\"DATE\" type=\"//@dataTypes.1\"/>    <attributes name=\"TIME\" type=\"//@dataTypes.1\"/>    <attributes name=\"DISTANCE\" type=\"//@dataTypes.1\"/>    <attributes name=\"DIRECTION\" type=\"//@dataTypes.1\"/>    <attributes name=\"SPEED\" type=\"//@dataTypes.1\"/>    <attributes name=\"SIZE\" type=\"//@dataTypes.1\"/>    <attributes name=\"TEMPERATURE\" type=\"//@dataTypes.0\"/>    <attributes name=\"NUMBER\" type=\"//@dataTypes.0\"/>  </dataTypes></typhonml:Model>", "type":"ML", "dateReceived":ISODate(), "_class":"com.clms.typhonapi.models.Model" }]);