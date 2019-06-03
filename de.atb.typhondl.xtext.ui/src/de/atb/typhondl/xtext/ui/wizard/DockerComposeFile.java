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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.internal.core.ModelUpdater;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
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
	// private final GroupTemplateVariable dbmsGroup = this.group("Choose DBMS");

	private final GroupTemplateVariable databaseGroup = this.group("Select Database configuration");

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
			GroupTemplateVariable subGroup = this.group(database.getName(), databaseGroup);
			BooleanTemplateVariable bool = this.check("Use existing file", true,
					"If you already have a " + database.getName() + ".tdl template, enter relative path here",
					subGroup);
			StringTemplateVariable text = this.text("Database file: ", database.getName() + ".tdl",
					"Give the path to your database configuration file", subGroup);
			StringSelectionTemplateVariable combo = this.combo("Chose DBMS: ", database.getType().getPossibleDBMSs(),
					"Choose specific DBMS for " + database.getName() + " of type " + database.getType().name(),
					subGroup);
			data.put(database, new Tuple(combo, text, bool));

		}
	}

	@Override
	protected void updateVariables() {
		for (Database database : data.keySet()) {
			Tuple fields = data.get(database);
			fields.databaseFile.setEnabled(fields.useTemplateImage.getValue());
			fields.dbms.setEnabled(!fields.useTemplateImage.getValue());
			if (fields.dbms.isEnabled()) {
				database.setDbms(fields.dbms.getValue().toLowerCase());
			}
			if (fields.databaseFile.isEnabled()) {
				String image = fields.databaseFile.getValue();
				database.setaPathToDBModelFile(image);
			} else {
				database.setaPathToDBModelFile("");
			}
		}
		this.dbTypes = getTypes();
	}

	public DockerComposeFile() {
		// mandatory
	}

	private ArrayList<Database> readModel(URI modelPath) {
		try {
			return ModelReader.readXMIFile(modelPath);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected IStatus validate() {
		Status status = null;
		for (Database database : data.keySet()) {
			Tuple fields = data.get(database);
			if (fields.databaseFile.isEnabled()) {
				String pathToDatabaseFile = fields.databaseFile.getValue();
				if (!pathToDatabaseFile.endsWith(".tdl")) {
					status = new Status(IStatus.ERROR, "Wizard",
							"Database file (" + pathToDatabaseFile + ") has to end with .tdl");
				}
			} else {
				String pathToDatabaseFile = database.getName() + ".tdl";
				String pathWithFolder = modelPath.toString().substring(0, modelPath.toString().lastIndexOf('/') + 1);
				String path = pathWithFolder + pathToDatabaseFile;
				File file = new File(URI.create(path));
				if (file.exists()) {
					status = new Status(IStatus.WARNING, "Wizard", "Database file " + pathToDatabaseFile
							+ " already exists and will be overwritten if you continue");
					// IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
					MessageDialog.openWarning(databaseGroup.getWidget().getShell(), "Wizard", "Database file "
							+ pathToDatabaseFile + " already exists and will be overwritten if you continue");
				}
			}
		}
		return status;
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
		StringConcatenation _builder_2 = new StringConcatenation();
		{
			for (final Database db : this.data.keySet()) {
				if (db.getPathToDBModelFile().isEmpty()) {
					String dbms = db.getDbms();
					String name = db.getName();
					StringConcatenation _builderdb = new StringConcatenation();
					_builderdb.append(_folder);
					_builderdb.append("/");
					_builderdb.append(name);
					_builderdb.append(".tdl");
					db.setaPathToDBModelFile(name + ".tdl");
					StringConcatenation _builder_1 = new StringConcatenation();
					_builder_1.append("dbtype ");
					_builder_1.append(dbms);
					_builder_1.newLine();
					_builder_1.newLine();
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
		IPath path = new Path(modelPath.getPath());
		_builder_2.append("import " + new Path(modelPath.getPath()).lastSegment());
		_builder_2.newLine();
		{
			for (final Database db : this.data.keySet()) {
				_builder_2.append("import ");
				_builder_2.append(db.getPathToDBModelFile());
				_builder_2.newLine();
			}
		}
		_builder_2.newLine();
		_builder_2.append("platformtype default //TODO (e.g. AWS)");
		_builder_2.newLine();
		_builder_2.append("containertype Docker");
		_builder_2.newLine();
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
				String name = db.getName();
				_builder_2.append("deploys " + name);
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
		String pathWithFolder = modelPath.toString().substring(0, modelPath.toString().lastIndexOf('/'));
		String pathWithoutFolder = pathWithFolder.substring(0, pathWithFolder.lastIndexOf('/') + 1);
		String path = pathWithoutFolder + _builderdb;
		File file = new File(URI.create(path));
		if (file.exists()) {
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
