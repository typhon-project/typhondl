Typhon Analytics
---
0. Deploy Polystore
1. Place the generated typhonAnalyticsConfig.properties into resource directory of Analytics Component.
2. Launch `AnalyticsRunner.java` as Java application to observe calls to the Typhon API (e.g. database reset, insert, and select issued with Insomnia). If installation was successful, the `AnalyticsRunner` will print statements for Typhon API calls.

Useful commands (optional)
---
Replace `HOST:PORT` with the value in analytics-container.uri, i.e. the external cluster node IP and the defined NodePort: `<ClusterIP>:32100`.
  
Run a Kafka command-line producer:
```
    cd /usr/local/Cellar/kafka/2.4.1/bin
    ./kafka-console-producer --broker-list HOST:PORT --topic fruits-topic
```

Run a Kafka command-line consumer:
 ```   
    cd /usr/local/Cellar/kafka/2.4.1/bin
    ./kafka-console-consumer --bootstrap-server HOST:PORT --topic fruits-topic --from-beginning
```

Create Kafka topic from command-line:
```    
    cd /usr/local/Cellar/kafka/2.4.1/bin
    ./kafka-topics --create --bootstrap-server HOST:PORT --replication-factor 1 --partitions 1 --topic my-new-topic
```

List existing Kafka topics in command-line:
 ```   
    cd /usr/local/Cellar/kafka/2.4.1/bin
    ./kafka-topics --list --bootstrap-server HOST:PORT
```

Execute Flink job by uploading from command-line. This requires Flink binaries to be available and Flink port-forward to be setup as indicated in section 'Setup Flink port-forwarding (...)')
```
    flink run -m localhost:8081 /usr/local/Cellar/apache-flink/1.10.0/libexec/examples/streaming/WordCount.jar
```