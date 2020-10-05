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

import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.typhonDL.ValueArray;
import de.atb.typhondl.xtext.typhonDL.Volume_Properties;
import de.atb.typhondl.xtext.typhonDL.Volumes;
import de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas.VolumesDialog;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

/**
 * Utility class for easier {@link Volumes} model object handling
 * 
 * @author flug
 *
 */
public class VolumesService {

    /**
     * Create a Volumes model object
     * 
     * @param path the targetpath (for a named volume), the complete path (e.g.
     *             my/local/:target/container/) if not named
     * @param type the type (e.g. "volume", "persitendVolumeClaim")
     * @param name name of volume if named
     * @return a new Volumes object
     */
    public static Volumes create(String[] path, String type, String name) {
        Volumes volumes = TyphonDLFactory.eINSTANCE.createVolumes();
        volumes.getDecls().add(createVolume_Properties(path, type, name));
        return volumes;
    }

    /**
     * Creates a {@link Volume_Properties} object
     * 
     * @param volumePath the targetpath (for a named volume), the complete path
     *                   (e.g. my/local/:target/container/) if not named
     * @param volumeType the type (e.g. "volume", "persitendVolumeClaim")
     * @param volumeName name of volume if named
     * @return a new Volume_Properties object
     */
    public static Volume_Properties createVolume_Properties(String[] volumePath, String volumeType, String volumeName) {
        Volume_Properties properties = TyphonDLFactory.eINSTANCE.createVolume_Properties();
        ValueArray volumePathArray = null;
        if (volumePath != null) {
            volumePathArray = createPathArray(volumePath);
        }
        properties.setVolumePath(volumePathArray);
        properties.setVolumeType(volumeType);
        properties.setVolumeName(volumeName);
        return properties;
    }

    /**
     * Creates a {@link ValueArray} object from a String array
     * 
     * @param volumePath array containing path Strings
     * @return a new ValueArray to be used in a {@link Volume_Properties}
     */
    private static ValueArray createPathArray(String[] volumePath) {
        ValueArray volumePathArray = TyphonDLFactory.eINSTANCE.createValueArray();
        volumePathArray.setValue(volumePath[0]);
        for (int i = 1; i < volumePath.length; i++) {
            volumePathArray.getValues().add(volumePath[i]);
        }
        return volumePathArray;
    }

    /**
     * Helper for {@link VolumesDialog}
     */
    public static String getVolumesPath(Volume_Properties property) {
        return property.getVolumePath() != null ? property.getVolumePath().getValue() : ""; // TODO;
    }

    /**
     * Helper for {@link VolumesDialog}
     */
    public static String getVolumesName(Volume_Properties property) {
        return property.getVolumeName() != null ? property.getVolumeName() : "";
    }

    /**
     * Helper for {@link VolumesDialog}
     */
    public static String getVolumesType(Volume_Properties property) {
        return property.getVolumeType() != null ? property.getVolumeType() : "";
    }

    /**
     * Helper for {@link VolumesDialog}
     */
    public static ValueArray createPath(String string) {
        ValueArray array = TyphonDLFactory.eINSTANCE.createValueArray();
        array.setValue(string);
        return array;
    }

    /**
     * Helper for {@link VolumesDialog}
     */
    public static Volume_Properties setProperty(Text now, Volume_Properties properties) {
        final String text = now.getText();
        switch (now.getData("type").toString()) {
        case "name":
            properties.setVolumeName(text.isEmpty() ? null : text);
            break;
        case "type":
            properties.setVolumeType(text.isEmpty() ? null : text);
            break;
        case "mountPath":
            properties.setVolumePath(text.isEmpty() ? null : createPathArray(new String[] { text }));
        default:
            break;
        }
        return properties;
    }

    /**
     * Returns technology dependent default volume type
     * 
     * @param chosenTechnology the clusterType
     * @return technology dependent default volume type
     */
    public static String getDefaultVolumesType(SupportedTechnologies chosenTechnology) {
        if (chosenTechnology.equals(SupportedTechnologies.Kubernetes)) {
            return "persistentVolumeClaim";
        }
        if (chosenTechnology.equals(SupportedTechnologies.DockerCompose)) {
            return "volume";
        }

        return "";
    }

}
