package de.atb.typhondl.xtext.ui.utilities;

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
            new InputField("Flink taskmanager memory: ", "analytics.flink.taskmanager.memory.process.size"),
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
        // TODO TYP-186
        case DockerCompose:
            return dockerComposeFields;
        case Kubernetes:
            return kubernetesFields;
        default:
            return new ArrayList<>();
        }
    }
}
