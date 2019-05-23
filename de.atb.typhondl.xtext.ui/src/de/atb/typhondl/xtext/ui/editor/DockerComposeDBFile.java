package de.atb.typhondl.xtext.ui.editor;

import org.eclipse.xtend2.lib.StringConcatenation;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Key_Value;
import de.atb.typhondl.xtext.typhonDL.Key_ValueArray;
import de.atb.typhondl.xtext.typhonDL.Key_ValueList;
import de.atb.typhondl.xtext.typhonDL.Property;

public class DockerComposeDBFile {

	public static String createDBcontent(DB db) {
		String name = db.getName();
		String dbms = db.getType().getName();
		StringConcatenation builder = new StringConcatenation();
		builder.append("database ");
		builder.append(name);
		builder.append(" : ");
		builder.append(dbms);
		builder.append(" {");
		builder.newLine();
		builder.append("\t");
		builder.append("image = ");
		builder.append(db.getImage().getValue());
		builder.append(";");
		builder.newLine();
		{
			for (Property property : db.getParameters()) {
				resolveProperties(property, builder);
			}
		}
		builder.append("}");
		builder.newLine();
		return builder.toString();

	}

	private static void resolveProperties(Property property, StringConcatenation builder) {
		switch (property.eClass().getInstanceClassName()) {
		case "de.atb.typhondl.xtext.typhonDL.Key_Value":
			Key_Value key_Value = (Key_Value) property;
			builder.append("\t");
			builder.append(key_Value.getName());
			builder.append(" = ");
			builder.append(key_Value.getValue());
			builder.append(";");
			builder.newLineIfNotEmpty();
			break;
		case "de.atb.typhondl.xtext.typhonDL.Key_ValueList":
			Key_ValueList key_ValueList = (Key_ValueList) property;
			builder.append("\t");
			builder.append(key_ValueList.getName());
			builder.append(" {");
			builder.newLine();
			for (String environmentVar : key_ValueList.getEnvironmentVars()) {
				builder.append("\t\t");
				builder.append(environmentVar);
				builder.newLine();
			}
			builder.append("\t");
			builder.append("}");
			builder.newLine();
			break;
		case "de.atb.typhondl.xtext.typhonDL.Key_ValueArray":
			Key_ValueArray key_ValueArray = (Key_ValueArray) property;
			builder.append("\t");
			builder.append(key_ValueArray.getName());
			builder.append(" [");
			builder.newLine();
			builder.append("\t\t");
			builder.append(key_ValueArray.getValue());
			for (String value : key_ValueArray.getValues()) {
				builder.append(", ");
				builder.append(value);
			}
			builder.newLine();
			builder.append("\t");
			builder.append("]");
			builder.newLine();
			break;
		default:
			break;
		}
	}

}
