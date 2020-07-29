package de.atb.typhondl.xtext.ui.modelUtils;

import java.util.ArrayList;
import java.util.Arrays;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.Dependency;
import de.atb.typhondl.xtext.typhonDL.Key_ValueArray;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Modes;
import de.atb.typhondl.xtext.typhonDL.Ports;
import de.atb.typhondl.xtext.typhonDL.Property;
import de.atb.typhondl.xtext.typhonDL.Reference;
import de.atb.typhondl.xtext.typhonDL.Replication;
import de.atb.typhondl.xtext.typhonDL.Services;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.typhonDL.URI;

public class ContainerService {

    public static Container create(String name, ContainerType containerType, Services deploys, String uri) {
        Container container = TyphonDLFactory.eINSTANCE.createContainer();
        container.setName(name);
        container.setType(containerType);
        if (deploys != null) {
            Reference reference = TyphonDLFactory.eINSTANCE.createReference();
            reference.setReference(deploys);
            container.setDeploys(reference);
        }
        if (uri != null) {
            URI uriObject = TyphonDLFactory.eINSTANCE.createURI();
            uriObject.setValue(uri);
            container.setUri(uriObject);
        }
        return container;
    }

    public static Ports createPorts(String[] strings) {
        Ports ports = TyphonDLFactory.eINSTANCE.createPorts();
        for (int i = 0; i < strings.length; i = i + 2) {
            Key_Values keyValues = TyphonDLFactory.eINSTANCE.createKey_Values();
            keyValues.setName(strings[i]);
            keyValues.setValue(strings[i + 1]);
            ports.getKey_values().add(keyValues);
        }
        return ports;
    }

    public static Replication createStatelessReplication(int replicas) {
        Replication replication = TyphonDLFactory.eINSTANCE.createReplication();
        replication.setMode(Modes.STATELESS);
        replication.setReplicas(replicas);
        return replication;
    }

    public static Property addAPIEntrypoint(String clusterType, String entrypoint) {
        ArrayList<String> list = new ArrayList<>(
                Arrays.asList(new String[] { "wait-for-it", "polystore-mongo:27017", "-t", "'60'", "--" }));
        list.addAll(new ArrayList<>(Arrays.asList(entrypoint.split(","))));
        return createKeyValuesArray("entrypoint", list.toArray(new String[0]));
    }

    public static Dependency createDependsOn(Container dependsOn) {
        Dependency dependency = TyphonDLFactory.eINSTANCE.createDependency();
        dependency.setReference(dependsOn);
        return dependency;
    }

    public static ArrayList<Key_Values> createKeyValues(String[] strings) {
        ArrayList<Key_Values> list = new ArrayList<>();
        for (int i = 0; i < strings.length; i = i + 2) {
            Key_Values keyValues = TyphonDLFactory.eINSTANCE.createKey_Values();
            keyValues.setName(strings[i]);
            keyValues.setValue(strings[i + 1]);
            list.add(keyValues);
        }
        return list;
    }

    public static Key_ValueArray createKeyValuesArray(String name, String[] values) {
        Key_ValueArray array = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
        array.setName(name);
        for (int i = 0; i < values.length; i++) {
            array.getValues().add(values[i]);
        }
        return array;
    }

}
