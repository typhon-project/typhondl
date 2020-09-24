package de.atb.typhondl.xtext.ui.modelUtils;

import java.util.ArrayList;

import org.eclipse.xtext.EcoreUtil2;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Key_ValueArray;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class ModelService {

    public static Application getFirstApplication(DeploymentModel model) {
        return EcoreUtil2.getAllContentsOfType(model, Application.class).get(0);
    }

    public static Container getContainer(DeploymentModel model, String containerName) {
        return EcoreUtil2.getAllContentsOfType(model, Container.class).stream()
                .filter(container -> container.getName().equalsIgnoreCase(containerName)).findFirst().orElse(null);
    }

    public static Platform getPlatform(DeploymentModel model) {
        return EcoreUtil2.getAllContentsOfType(model, Platform.class).get(0);
    }

    public static SupportedTechnologies getSupportedTechnology(ClusterType clusterType) {
        return SupportedTechnologies.valueOf(clusterType.getName());
    }

    public static ClusterType getClusterType(DeploymentModel model) {
        return EcoreUtil2.getAllContentsOfType(model, ClusterType.class).get(0);
    }

    public static String getKafkaInternalURI(SupportedTechnologies tech) {
        switch (tech) {
        case DockerCompose:
            return "localhost:29092";
        case Kubernetes:
            return "typhon-cluster-kafka-bootstrap:9092";
        default:
            return "";
        }
    }

    public static Key_Values createKey_Values(String name, String value, String[] values) {
        Key_Values key_Values = TyphonDLFactory.eINSTANCE.createKey_Values();
        key_Values.setName(name);
        key_Values.setValue(value);
        if (values != null) {
            for (String string : values) {
                key_Values.getValues().add(string);
            }
        }
        return key_Values;
    }

    public static Key_ValueArray createKeyValuesArray(String name, String[] values) {
        Key_ValueArray array = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
        array.setName(name);
        for (int i = 0; i < values.length; i++) {
            array.getValues().add(values[i]);
        }
        return array;
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

    public static Key_Values createKubeconfig(String property) {
        Key_Values kubeconfig = TyphonDLFactory.eINSTANCE.createKey_Values();
        kubeconfig.setName("kubeconfig");
        kubeconfig.setValue(addQuotes(property));
        return kubeconfig;
    }

    private static String addQuotes(String property) {
        return property.contains("\"") ? property : "\"" + property + "\"";
    }
}
