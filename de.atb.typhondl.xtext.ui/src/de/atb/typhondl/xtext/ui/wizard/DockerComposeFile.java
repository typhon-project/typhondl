package de.atb.typhondl.xtext.ui.wizard;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.ui.wizard.template.AbstractFileTemplate;
import org.eclipse.xtext.ui.wizard.template.BooleanTemplateVariable;
import org.eclipse.xtext.ui.wizard.template.FileTemplate;
import org.eclipse.xtext.ui.wizard.template.GroupTemplateVariable;
import org.eclipse.xtext.ui.wizard.template.IFileGenerator;
import org.eclipse.xtext.ui.wizard.template.StringSelectionTemplateVariable;
import org.xml.sax.SAXException;

@FileTemplate(label = "Docker-Compose", icon = "docker.png", description = "<p><b>Docker-Compose</b></p>\r\n<p>Descriptive text about using Docker-Compose</p>")
@SuppressWarnings("all")
public final class DockerComposeFile extends AbstractFileTemplate {

	private ArrayList<DBType> dbTypes;
	private final GroupTemplateVariable dbmsGroup = this.group("Choose DBMS");

	private final GroupTemplateVariable imageGroup = this.group("Select Image configuration");

	private final GroupTemplateVariable containerGroup = this.group("Containers will be created for:");
	private final BooleanTemplateVariable isQL = this.check("QL", false, containerGroup);
	private final BooleanTemplateVariable isTextSearch = this.check("Text Search", false, containerGroup);
	private final BooleanTemplateVariable isDataAnalytics = this.check("Data Analytics", false, containerGroup);

	// ParameterList:
	private HashMap<Database, Tuple> data;

	public DockerComposeFile(URI modelPath) {
		this.data = new HashMap<Database, Tuple>();
		readModel(modelPath).forEach(db -> this.data.put(db, null));
		this.dbTypes = getTypes();
		createParameter();
	}

	private void createParameter() {
		for (Database database : data.keySet()) {
			data.put(database,
					new Tuple(
							this.combo("DBMS for " + database.getName() + " : ", database.getType().getPossibleDBMSs(),
									"Choose specific DBMS for " + database.getName() + " of type "
											+ database.getType().name(),
									dbmsGroup),
							this.text("Image for " + database.getName() + " : ", "path to config file",
									"Give the path to your image configuration file", imageGroup)));
		}
	}

	@Override
	protected void updateVariables() {
		for (Database database : data.keySet()) {
			database.setDbms(data.get(database).dbms.getValue().toLowerCase());
			database.setPathToImage(data.get(database).image.getValue());
		}
	}

	public DockerComposeFile() {
		// TODO Auto-generated constructor stub
	}

	private ArrayList<Database> readModel(URI modelPath) {
		try {
			return ModelReader.readXMIFile(modelPath);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//TODO
	private String readImageConfig(String imagePath) {
		return "testImage: " + imagePath;
	}

	public ArrayList<DBType> getTypes() {
		dbTypes = new ArrayList<DBType>();
		for (final Database database : data.keySet()) {
			boolean _contains = dbTypes.contains(database.getType());
			boolean _not = (!_contains);
			if (_not) {
				dbTypes.add(database.getType());
			}
		}
		return dbTypes;
	}

	@Override
	public void generateFiles(final IFileGenerator generator) {
		StringConcatenation _builder = new StringConcatenation();
		String _folder = this.getFolder();
		_builder.append(_folder);
		_builder.append("/");
		String _name = this.getName();
		_builder.append(_name);
		_builder.append(".tdl");
		StringConcatenation _builderdb = new StringConcatenation();
		_builderdb.append(_folder);
		_builderdb.append("/");
		_builder.append("databases");
		_builder.append(".tdl");
		StringConcatenation _builder_1 = new StringConcatenation();
		// DB:
		// 'database' name=ID ':' type=[DBType] '{'
		// (image+=IMAGE)+
		// (parameters+=Key_Value)*
		// '}'
		// ;
		{
			for (final Database db : this.data.keySet()) {
				String dbms = db.getDbms();
				String name = db.getName();
				String image = readImageConfig(db.getPathToImage());
				_builder_1.append("database ");
				_builder_1.append(name);
				_builder_1.append(" : ");
				_builder_1.append(dbms);
				_builder_1.append(" {");
				_builder_1.newLine();
				_builder_1.append("\t");
				_builder_1.append("image = \"");
				_builder_1.append(image);
				//_builder_1.append(tag);
				_builder_1.newLine();
				_builder_1.append("\t");
				_builder_1.append("// TODO: evironment etc.");
				_builder_1.newLine();
				_builder_1.append("}");
				_builder_1.newLine();
			}
		}
		StringConcatenation _builder_2 = new StringConcatenation();
		_builder_2.append("platformtype default //TODO (e.g. AWS)");
		_builder_2.newLine();
		_builder_2.append("containertype Docker");
		_builder_2.newLine();
		{
			for (final DBType type : this.dbTypes) {
				_builder_2.append("dbtype ");
				_builder_2.append(type);
				_builder_2.newLineIfNotEmpty();
			}
		}
		_builder_2.newLine();
		_builder_2.append("}");
		_builder_2.newLine();
		_builder_2.newLine();
		_builder_2.append("platform platformname : default { //TODO");
		_builder_2.newLine();
		_builder_2.append("\t");
		_builder_2.append("cluster clustername { //TODO");
		_builder_2.newLine();
		_builder_2.append("\t\t");
		_builder_2.append("application name {");
		_builder_2.newLine();
		{
			for (final Database db : this.data.keySet()) {
				_builder_2.append("\t\t\t");
				_builder_2.append("container ");
				String _name_1 = db.getName();
				_builder_2.append(_name_1);
				_builder_2.append(" : Docker {");
				_builder_2.newLineIfNotEmpty();
				_builder_2.append("\t\t\t\t");
				_builder_2.append("database : ");
				String name = db.getName();
				_builder_2.append(name);
				_builder_2.newLineIfNotEmpty();
				_builder_2.append("\t\t\t\t");
				_builder_2.append("dbType : ");
				DBType _type = db.getType();
				_builder_2.append(_type);
				_builder_2.newLineIfNotEmpty();
				_builder_2.append("\t\t\t\t");
				_builder_2.append("// TODO: volumes, networks, ports etc.");
				_builder_2.newLine();
				_builder_2.append("\t\t\t");
				_builder_2.append("}");
				_builder_2.newLine();

			}
		}
		_builder_2.append("\t\t");
		_builder_2.append("}");
		_builder_2.newLine();
		_builder_2.append("\t");
		_builder_2.append("}");
		_builder_2.newLine();
		_builder_2.append("}");
		_builder_2.newLine();
		generator.generate(_builder, _builder_2);
		generator.generate(_builderdb, _builder_1);
	}
}
