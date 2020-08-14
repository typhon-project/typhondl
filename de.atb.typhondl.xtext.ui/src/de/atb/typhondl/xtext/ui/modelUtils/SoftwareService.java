package de.atb.typhondl.xtext.ui.modelUtils;

import java.util.ArrayList;
import java.util.Properties;

import de.atb.typhondl.xtext.typhonDL.Environment;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

public class SoftwareService {

    public static Software create(String name, String imageValue) {
        Software software = TyphonDLFactory.eINSTANCE.createSoftware();
        software.setName(name);
        IMAGE image = TyphonDLFactory.eINSTANCE.createIMAGE();
        image.setValue(imageValue);
        software.setImage(image);
        return software;
    }

    public static Environment createEnvironment(String[] strings) {
        Environment environment = TyphonDLFactory.eINSTANCE.createEnvironment();
        for (int i = 0; i < strings.length; i = i + 2) {
            Key_Values keyValues = TyphonDLFactory.eINSTANCE.createKey_Values();
            keyValues.setName(strings[i]);
            keyValues.setValue(strings[i + 1]);
            environment.getParameters().add(keyValues);
        }
        return environment;
    }

    public static String getEnvironmentName(String propertyName) {
        String substring = propertyName.substring(propertyName.indexOf("environment"));
        return substring.substring(substring.indexOf('.') + 1);
    }

    public static String[] getEnvironmentFromProperties(Properties properties, String componentName) {
        ArrayList<String> list = new ArrayList<>();
        properties.stringPropertyNames().stream().filter(name -> name.contains(componentName))
                .filter(name -> name.contains("environment")).forEach(name -> {
                    list.add(getEnvironmentName(name));
                    list.add(properties.getProperty(name));
                });
        return list.toArray(new String[0]);
    }

}
