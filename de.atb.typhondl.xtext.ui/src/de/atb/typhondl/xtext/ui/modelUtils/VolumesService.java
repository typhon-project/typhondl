package de.atb.typhondl.xtext.ui.modelUtils;

import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.typhonDL.ValueArray;
import de.atb.typhondl.xtext.typhonDL.Volume_Properties;

public class VolumesService {

    public static Volume_Properties createTestVolumeProperties() {
        return createVolume_Properties(new String[] { "./test/volume/" }, "testType", "testName");
    }

    public static Volume_Properties createVolume_Properties(String[] volumePath, String volumeType, String volumeName) {
        Volume_Properties properties = TyphonDLFactory.eINSTANCE.createVolume_Properties();
        ValueArray volumePathArray = null;
        if (volumePath != null) {
            volumePathArray = TyphonDLFactory.eINSTANCE.createValueArray();
            volumePathArray.setValue(volumePath[0]);
            for (int i = 1; i < volumePath.length; i++) {
                volumePathArray.getValues().add(volumePath[i]);
            }
        }
        properties.setVolumePath(volumePathArray);
        properties.setVolumeType(volumeType);
        properties.setVolumeName(volumeName);
        return properties;
    }

    public static String getVolumesPath(Volume_Properties property) {
        return property.getVolumePath().getValue(); // TODO;
    }

    public static String getVolumesName(Volume_Properties property) {
        return property.getVolumeName();
    }

    public static String getVolumesType(Volume_Properties property) {
        return property.getVolumeType();
    }

    public static ValueArray createPath(String string) {
        ValueArray array = TyphonDLFactory.eINSTANCE.createValueArray();
        array.setValue(string);
        return array;
    }
}
