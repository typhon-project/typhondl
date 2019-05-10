package de.atb.typhondl.xtext.ui.wizard;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.ui.wizard.template.AbstractFileTemplate;
import org.eclipse.xtext.ui.wizard.template.BooleanTemplateVariable;
import org.eclipse.xtext.ui.wizard.template.FileTemplate;
import org.eclipse.xtext.ui.wizard.template.GroupTemplateVariable;
import org.eclipse.xtext.ui.wizard.template.IFileGenerator;
import org.eclipse.xtext.ui.wizard.template.StringSelectionTemplateVariable;
import org.eclipse.xtext.ui.wizard.template.StringTemplateVariable;
import org.xml.sax.SAXException;

@FileTemplate(label = "Docker-Compose", icon = "docker.png", description = "<p><b>Docker-Compose</b></p>\r\n<p>Descriptive text about using Docker-Compose</p>")
@SuppressWarnings("all")
public final class DockerComposeFile extends AbstractFileTemplate {

	private ArrayList<String> dbTypes;
	private final GroupTemplateVariable dbmsGroup = this.group("Choose DBMS");

	private final GroupTemplateVariable imageGroup = this.group("Select Image configuration");

	private final GroupTemplateVariable containerGroup = this.group("Containers will be created for:");
	private final BooleanTemplateVariable isQL = this.check("QL", false, containerGroup);
	private final BooleanTemplateVariable isTextSearch = this.check("Text Search", false, containerGroup);
	private final BooleanTemplateVariable isDataAnalytics = this.check("Data Analytics", false, containerGroup);

	// ParameterList:
	private HashMap<Database, Tuple> data;
	private URI modelPath;

	public DockerComposeFile(URI modelPath) {
		this.modelPath = modelPath;
		this.data = new HashMap<Database, Tuple>();
		readModel(modelPath).forEach(db -> this.data.put(db, null));
		this.dbTypes = getTypes();
		createParameter();
	}

	private void createParameter() {

		for (Database database : data.keySet()) {
			StringSelectionTemplateVariable combo = this.combo("DBMS for " + database.getName() + " : ",
					database.getType().getPossibleDBMSs(),
					"Choose specific DBMS for " + database.getName() + " of type " + database.getType().name(),
					dbmsGroup);
			BooleanTemplateVariable bool = this.check("Use template for " + database.getName(), false,
					"If you already have a database.tdl template for " + database.getName()
							+ " enter relative path here",
					imageGroup);
			StringTemplateVariable text = this.text("Image for " + database.getName() + " : ", "default",
					"Give the path to your image configuration file", imageGroup);
			data.put(database, new Tuple(combo, text, bool));

		}
	}

	@Override
	protected void updateVariables() {
		for (Database database : data.keySet()) {
			Tuple fields = data.get(database);
			// read chosen dbms and add info to database-object
			String dbms = fields.dbms.getValue().toLowerCase();
			database.setDbms(dbms);
			// enable textfields to enter path to database.tdl
			fields.image.setEnabled(fields.useTemplateImage.getValue());
			if (fields.useTemplateImage.getValue()) {
				String image = fields.image.getValue();
				database.setPathToImage(image);
			} else {
				database.setPathToImage("");
			}
		}
		this.dbTypes = getTypes();
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

	// dbtype is the dbms and not {relational, document etc}
	public ArrayList<String> getTypes() {
		dbTypes = new ArrayList<String>();
		for (final Database database : data.keySet()) {
			boolean _contains = dbTypes.contains(database.getDbms());
			boolean _not = (!_contains);
			if (_not) {
				dbTypes.add(database.getDbms());
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
		{
			for (final Database db : this.data.keySet()) {
				if (db.getPathToImage().isEmpty()) {
					String dbms = db.getDbms();
					String name = db.getName();
					StringConcatenation _builderdb = new StringConcatenation();
					_builderdb.append(_folder);
					_builderdb.append("/");
					_builderdb.append(name);
					_builderdb.append(".tdl");
					StringConcatenation _builder_1 = new StringConcatenation();
					_builder_1.append("database ");
					_builder_1.append(name);
					_builder_1.append(" : ");
					_builder_1.append(dbms);
					_builder_1.append(" {");
					_builder_1.newLine();
					_builder_1.append("\t");
					_builder_1.append("image = //TODO");
					// _builder_1.append(tag);
					_builder_1.newLine();
					_builder_1.append("\t");
					_builder_1.append("// TODO: evironment etc.");
					_builder_1.newLine();
					_builder_1.append("}");
					_builder_1.newLine();
					deleteExistingDatabaseFile(_builderdb.toString());
					generator.generate(_builderdb, _builder_1);
				}
			}
		}
		StringConcatenation _builder_2 = new StringConcatenation();
		_builder_2.append("platformtype default //TODO (e.g. AWS)");
		_builder_2.newLine();
		_builder_2.append("containertype Docker");
		_builder_2.newLine();
		{
			for (final String type : this.dbTypes) {
				_builder_2.append("dbtype ");
				_builder_2.append(type);
				_builder_2.newLineIfNotEmpty();
			}
		}
		_builder_2.newLine();
		_builder_2.append("platform platformname : default { //TODO");
		_builder_2.newLine();
		_builder_2.append("\t");
		_builder_2.append("cluster clustername { //TODO");
		_builder_2.newLine();
		_builder_2.append("\t\t");
		_builder_2.append("application name { //TODO");
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
				_builder_2.append("use : ");
				String name = db.getName();
				_builder_2.append(name);
				_builder_2.newLineIfNotEmpty();
				_builder_2.append("\t\t\t\t");
				_builder_2.append("dbType : ");
				_builder_2.append(db.getDbms());
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
	}

	private void deleteExistingDatabaseFile(String _builderdb) {
		// TODO Auto-generated method stub
		String pathWithFolder = modelPath.toString().substring(0, modelPath.toString().lastIndexOf('/'));
		String pathWithoutFolder = pathWithFolder.substring(0, pathWithFolder.lastIndexOf('/') + 1);
		String path = pathWithoutFolder + _builderdb;
		File file = new File(URI.create(path));
		System.out.println(file.getAbsolutePath().toString() + ", exists:" + file.exists());
		if (file.exists()) {
			System.out.println("Try to delete file");
			file.delete();
			for (IProject iproject : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
				try {
					iproject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
