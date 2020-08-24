package de.atb.typhondl.xtext.ui.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class for creating input fields.
 * 
 * @author flug
 *
 */
public class KafkaConfigEditor {
    public List<InputField> dockerComposeFields = Arrays.asList(
            new InputField("Kafka version: ", "analytics.kafka.version"),
            new InputField("Replicas: ", "analytics.kafka.replicas"));
    public List<InputField> kubernetesFields = Arrays.asList(
            new InputField("Flink jobmanager heap size: ", "analytics.flink.jobmanager.heap.size"),
            new InputField("Flink taskmanager memory process size: ",
                    "analytics.flink.taskmanager.memory.process.size"),
            new InputField("Logglevel rootlogger: ", "analytics.logging.rootlogger"),
            new InputField("Logglevel akka: ", "analytics.logging.akka"),
            new InputField("Logglevel kafka: ", "analytics.logging.kafka"),
            new InputField("Logglevel hadoop: ", "analytics.logging.hadoop"),
            new InputField("Logglevel zookeeper: ", "analytics.logging.zookeeper"),
            new InputField("Logglevel flink: ", "analytics.logging.flink"),
            new InputField("Flink jobmanager rest nodeport: ", "analytics.flink.rest.port"),
            new InputField("Flink taskmanager replicas: ", "analytics.flink.taskmanager.replicas"),
            new InputField("Kafka replicas: ", "analytics.kafka.cluster.replicas"),
            new InputField("Kafka version: ", "analytics.kafka.version"),
            new InputField("Kafka storage claim: ", "analytics.kafka.storageclaim"),
            new InputField("zookeeper storage claim: ", "analytics.zookeeper.storageclaim"));

    public List<InputField> getInputFields(SupportedTechnologies chosenTemplate) {
        switch (chosenTemplate) {
        case DockerCompose:
            return dockerComposeFields;
        case Kubernetes:
            return kubernetesFields;
        default:
            return new ArrayList<>();
        }
    }
}