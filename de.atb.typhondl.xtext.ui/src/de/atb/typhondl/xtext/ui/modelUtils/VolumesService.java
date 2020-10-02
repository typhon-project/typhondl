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
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class VolumesService {

    public static Volume_Properties createTestVolumeProperties() {
        return createVolume_Properties(new String[] { "./test/volume/" }, "testType", "testName");
    }

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

    private static ValueArray createPathArray(String[] volumePath) {
        ValueArray volumePathArray = TyphonDLFactory.eINSTANCE.createValueArray();
        volumePathArray.setValue(volumePath[0]);
        for (int i = 1; i < volumePath.length; i++) {
            volumePathArray.getValues().add(volumePath[i]);
        }
        return volumePathArray;
    }

    public static String getVolumesPath(Volume_Properties property) {
        return property.getVolumePath() != null ? property.getVolumePath().getValue() : ""; // TODO;
    }

    public static String getVolumesName(Volume_Properties property) {
        return property.getVolumeName() != null ? property.getVolumeName() : "";
    }

    public static String getVolumesType(Volume_Properties property) {
        return property.getVolumeType() != null ? property.getVolumeType() : "";
    }

    public static ValueArray createPath(String string) {
        ValueArray array = TyphonDLFactory.eINSTANCE.createValueArray();
        array.setValue(string);
        return array;
    }

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

    public static String getDefaultVolumesType(SupportedTechnologies chosenTechnology) {
        if (chosenTechnology.equals(SupportedTechnologies.Kubernetes)) {
            return "persistentVolumeClaim";
        }
        if (chosenTechnology.equals(SupportedTechnologies.DockerCompose)) {
            return "volume";
        }

        return "";
    }

    public static Volumes create(String[] path, String type, String name) {
        Volumes volumes = TyphonDLFactory.eINSTANCE.createVolumes();
//        if (type == null) {
//            type = getDefaultVolumesType(chosenTechnology);
//        }
        volumes.getDecls().add(createVolume_Properties(path, type, name));
        return volumes;
    }
}
