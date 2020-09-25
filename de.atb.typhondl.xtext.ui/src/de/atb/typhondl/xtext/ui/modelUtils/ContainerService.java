package de.atb.typhondl.xtext.ui.modelUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.Dependency;
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

    /**
     * Names of container have to be DNS subdomain names (see DL #31)
     * 
     * @param name
     * @return
     */
    public static String createContainerName(String name) {
        name = Pattern.compile("^[^a-zA-Z]*").matcher(name).replaceFirst("");
        name = Pattern.compile("[^a-zA-Z]*$").matcher(name).replaceFirst("");
        name = name.toLowerCase().replaceAll("[^a-z0-9.]", "-");
        return Pattern.compile("-{2,}").matcher(name).replaceAll("-");
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

    public static boolean isPortInKubernetesRange(String port) {
        int portInt = 0;
        try {
            portInt = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            return false;
        }
        return portInt <= 32767 && Integer.parseInt(port) >= 30000;
    }

    public static String createRandomPort() {
        return Integer.toString(ThreadLocalRandom.current().nextInt(30000, 32767));
    }

    public static Replication createStatelessReplication(int replicas) {
        Replication replication = TyphonDLFactory.eINSTANCE.createReplication();
        replication.setMode(Modes.STATELESS);
        replication.setReplicas(replicas);
        return replication;
    }

    public static Property addAPIEntrypoint(String entrypoint) {
        ArrayList<String> list = new ArrayList<>(
                Arrays.asList(new String[] { "wait-for-it", "polystore-mongo:27017", "-t", "'60'", "--" }));
        list.addAll(new ArrayList<>(Arrays.asList(entrypoint.split(","))));
        return ModelService.createKeyValuesArray("entrypoint", list.toArray(new String[0]));
    }

    public static Dependency createDependsOn(Container dependsOn) {
        Dependency dependency = TyphonDLFactory.eINSTANCE.createDependency();
        dependency.setReference(dependsOn);
        return dependency;
    }

    public static ArrayList<Dependency> createDependencies(Container[] containers) {
        ArrayList<Dependency> dependencyList = new ArrayList<>();
        for (Container container : containers) {
            if (container != null) {
                Dependency dependency = TyphonDLFactory.eINSTANCE.createDependency();
                dependency.setReference(container);
                dependencyList.add(dependency);
            }
        }
        return dependencyList;
    }

}
