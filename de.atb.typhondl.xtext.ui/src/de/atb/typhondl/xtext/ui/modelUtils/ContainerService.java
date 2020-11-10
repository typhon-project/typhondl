package de.atb.typhondl.xtext.ui.modelUtils;

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
import de.atb.typhondl.xtext.typhonDL.Resources;
import de.atb.typhondl.xtext.typhonDL.Services;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.typhonDL.URI;
import de.atb.typhondl.xtext.typhonDL.Volumes;
import de.atb.typhondl.xtext.ui.technologies.ITechnology;

/**
 * Utility class for easier {@link Container} model object handling
 * 
 * @author flug
 *
 */
public class ContainerService {

    /**
     * Creates {@link Container} object
     * 
     * @param name             name of the container
     * @param containerType    e.g. Docker
     * @param deploys          the {@link Services} this container shall run
     * @param uri              the URI of the container for the API to parse
     * @param volumeTarget     database specific target entries for default named
     *                         volumes
     * @param chosenTechnology the clusterType
     * @return a new Container object
     */
    public static Container create(String name, ContainerType containerType, Services deploys, String uri,
            String volumeTarget, ITechnology chosenTechnology) {
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
        if (volumeTarget != null) {
            Volumes volumes = VolumesService.create(new String[] { volumeTarget },
                    chosenTechnology.defaultVolumesType(), name + "volume");
            container.setVolumes(volumes);
        }
        return container;
    }

    /**
     * Creates {@link Container} object
     * 
     * @param name          name of the container
     * @param containerType e.g. Docker
     * @param deploys       the {@link Services} this container shall run
     * @param uri           the URI of the container for the API to parse
     * @return a new Container object
     */
    public static Container create(String name, ContainerType containerType, Services deploys, String uri) {
        return create(name, containerType, deploys, uri, null, null);
    }

    /**
     * Creates {@link Container} object
     * 
     * @param name          name of the container
     * @param containerType e.g. Docker
     * @param deploys       the {@link Services} this container shall run
     * @return a new Container object
     */
    public static Container create(String name, ContainerType containerType, Services deploys) {
        return create(name, containerType, deploys, null, null, null);
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

    /**
     * Creates {@link Key_Values} for {@link Ports} from the input String array.
     * Example for input String array: ["target", 123, "published", 123]
     * 
     * @param strings array in the form of [name, value, name, value, ... ]
     * @return a new Ports object
     */
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

    /**
     * Checks if the given String is an Integer and in Kubernetes Range (between
     * 30000 and 32767)
     * 
     * @param port             String to check
     * @param chosenTechnology
     * @return true if port is an Integer in Kubernetes range
     */
    public static boolean isPortValidRange(String port, ITechnology chosenTechnology) {
        int portInt = 0;
        try {
            portInt = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            return false;
        }
        return portInt <= chosenTechnology.maxPort() && Integer.parseInt(port) >= chosenTechnology.minPort();
    }

    /**
     * Creates a random port as String in chosenTechnology's range
     * 
     * @param chosenTechnology
     * 
     * @return a random port in chosenTechnology's range
     */
    public static String createRandomPort(ITechnology chosenTechnology) {
        return Integer
                .toString(ThreadLocalRandom.current().nextInt(chosenTechnology.minPort(), chosenTechnology.maxPort()));
    }

    /**
     * Creates a {@link Replication} object with stateless mode
     * 
     * @param replicas the number of replicas
     * @return a new Replication object with stateless mode
     */
    public static Replication createStatelessReplication(int replicas) {
        Replication replication = TyphonDLFactory.eINSTANCE.createReplication();
        replication.setMode(Modes.STATELESS);
        replication.setReplicas(replicas);
        return replication;
    }

    /**
     * Adds wait-for-it functionality to the API container
     * 
     * @param entrypoint the entrypoint to run the API
     * @return the entrypoint as {@link Property} enriched with wait-for-it
     *         functionality
     */
    public static Property addAPIEntrypoint(String entrypoint) {
        ArrayList<String> list = new ArrayList<>(
                Arrays.asList(new String[] { "wait-for-it", "polystore-mongo:27017", "-t", "'60'", "--" }));
        list.addAll(new ArrayList<>(Arrays.asList(entrypoint.split(","))));
        return ModelService.createKeyValuesArray("entrypoint", list.toArray(new String[0]));
    }

    /**
     * Create intercontainer dependency model object
     * 
     * @param dependsOn the container to depend on
     * @return a new Dependency
     */
    public static Dependency createDependsOn(Container dependsOn) {
        Dependency dependency = TyphonDLFactory.eINSTANCE.createDependency();
        dependency.setReference(dependsOn);
        return dependency;
    }

    /**
     * Create intercontainer dependencies
     * 
     * @param containers the containers to depend on
     * @return a list of new Dependencies
     */
    public static ArrayList<Dependency> createDependencies(Container[] containers) {
        ArrayList<Dependency> dependencyList = new ArrayList<>();
        for (Container container : containers) {
            if (container != null) {
                dependencyList.add(createDependsOn(container));
            }
        }
        return dependencyList;
    }

    /**
     * Create Resources model object. Does not check for validity
     * 
     * @param limitCpu          the CPU limit to set (0.x+)
     * @param limitMemory       the memory limit to set (x+M)
     * @param reservationCpu    the CPU reservations to set (0.x+)
     * @param reservationMemory the memory reservations to set (x+M)
     * @return a new Resource object
     */
    public static Resources createResources(String limitCpu, String limitMemory, String reservationCpu,
            String reservationMemory) {
        if (!(limitCpu.isEmpty() && limitMemory.isEmpty() && reservationCpu.isEmpty() && reservationMemory.isEmpty())) {
            Resources resources = TyphonDLFactory.eINSTANCE.createResources();
            if (!limitCpu.isEmpty()) {
                resources.setLimitCPU(limitCpu);
            }
            if (!limitMemory.isEmpty()) {
                resources.setLimitMemory(limitMemory);
            }
            if (!reservationCpu.isEmpty()) {
                resources.setReservationCPU(reservationCpu);
            }
            if (!reservationMemory.isEmpty()) {
                resources.setReservationMemory(reservationMemory);
            }
            return resources;
        } else {
            return null;
        }
    }
}
