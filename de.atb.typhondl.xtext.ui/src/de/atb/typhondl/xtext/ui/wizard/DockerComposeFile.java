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
	
	private final GroupTemplateVariable containerGroup = this.group("Containers will be created for:");
	private final BooleanTemplateVariable isQL = this.check("QL", false, containerGroup);
	private final BooleanTemplateVariable isTextSearch = this.check("Text Search", false, containerGroup);
	private final BooleanTemplateVariable isDataAnalytics = this.check("Data Analytics", false, containerGroup);

	// ParameterList:
	private HashMap<Database, StringSelectionTemplateVariable> data;

	public DockerComposeFile(URI modelPath) {
		this.data = new HashMap<Database, StringSelectionTemplateVariable>();
		readModel(modelPath).forEach(db -> this.data.put(db, null));
		this.dbTypes = getTypes();
		createParameter();
	}

	private void createParameter() {
		for (Database database : data.keySet()) {
			data.put(database,
					this.combo("DBMS for " + database.getName() + " : ", database.getType().getPossibleDBMSs(),
							"Choose specific DBMS for " + database.getName() + " of type " + database.getType().name(),
							dbmsGroup));
		}

	}

	@Override
	protected void updateVariables() {
		for (Database database : data.keySet()) {
			database.setDbms(data.get(database).getValue());
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
		StringConcatenation _builder_1 = new StringConcatenation();
		_builder_1.append("platformtype default //TODO (e.g. AWS)");
		_builder_1.newLine();
		_builder_1.append("containertype Docker");
		_builder_1.newLine();
		{
			for (final DBType type : this.dbTypes) {
				_builder_1.append("dbtype ");
				_builder_1.append(type);
				_builder_1.newLineIfNotEmpty();
			}
		}
		_builder_1.newLine();
		_builder_1.append("platform platformname : default { //TODO");
		_builder_1.newLine();
		_builder_1.append("\t");
		_builder_1.append("cluster clustername { //TODO");
		_builder_1.newLine();
		_builder_1.append("\t\t");
		_builder_1.append("application name {");
		_builder_1.newLine();
		{
			for (final Database db : this.data.keySet()) {
				_builder_1.append("\t\t\t");
				_builder_1.append("container ");
				String _name_1 = db.getName();
				_builder_1.append(_name_1, "\t\t\t");
				_builder_1.append(" : Docker {");
				_builder_1.newLineIfNotEmpty();
				_builder_1.append("\t\t\t");
				_builder_1.append("\t");
				_builder_1.append("dbType : ");
				DBType _type = db.getType();
				_builder_1.append(_type, "\t\t\t\t");
				_builder_1.newLineIfNotEmpty();
				_builder_1.append("\t\t\t");
				_builder_1.append("\t");
				_builder_1.append("image = ");
				String _lowerCase = db.getDbms().toLowerCase();
				_builder_1.append(_lowerCase, "\t\t\t\t");
				_builder_1.append(":latest; // TODO");
				_builder_1.newLineIfNotEmpty();
				_builder_1.append("\t\t\t");
				_builder_1.append("\t");
				_builder_1.append("// TODO: evironment, volumes, networks, ports etc.");
				_builder_1.newLine();
				_builder_1.append("\t\t\t");
				_builder_1.append("}");
				_builder_1.newLine();
			}
		}
		_builder_1.append("\t\t");
		_builder_1.append("}");
		_builder_1.newLine();
		_builder_1.append("\t");
		_builder_1.append("}");
		_builder_1.newLine();
		_builder_1.append("}\t\t\t\t\t");
		_builder_1.newLine();
		generator.generate(_builder, _builder_1);
	}
}
