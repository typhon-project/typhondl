package de.atb.typhondl.acceleo.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;

import de.atb.typhondl.acceleo.main.Generate;
import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.Credentials;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.Dependency;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Environment;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.typhonDL.Key_KeyValueList;
import de.atb.typhondl.xtext.typhonDL.Key_ValueArray;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Ports;
import de.atb.typhondl.xtext.typhonDL.Reference;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

/**
 * Utility class for the script generation process
 * 
 * @author flug
 *
 */
public class Services {

    private static final String ANALYTICS_KUBERNETES_ZIP_FILENAME = "analyticsKubernetes.zip";
    private static final String ANALYTICS_ZIP_ADDRESS = "http://typhon.clmsuk.com/static/"
            + ANALYTICS_KUBERNETES_ZIP_FILENAME;
    private static final int BUFFER_SIZE = 4096;

    /**
     * Starts the script generation process, deletes old files
     * 
     * @param file     The main DL model from which to generate the scripts
     * @param provider The injected resourceSetProvider
     * @return A result String informing the user whether the generation succeeded
     */
    public static String generateDeployment(IFile file, XtextLiveScopeResourceSetProvider provider) {
        String result = "";
        try {
            String outputFolder = file.getLocation().toOSString().replace("." + file.getFileExtension(), "");
            deleteOldGeneratedFiles(new File(outputFolder));
            DeploymentModel model = loadXtextModel(file, provider);
            if (model == null) {
                result = "Please select the main model file containing the Platform definition and make sure there is a <mainModelName>.properties file.";
            } else {
                new Generate(model, new File(outputFolder), new ArrayList<String>()).doGenerate(new BasicMonitor());
                result = getResult(new File(outputFolder));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Checks if a yml file exists in projects subfolders TODO do better
     * 
     * @param folder the folder to check for yml files
     * @return Informative String for the user.
     */
    private static String getResult(File folder) {
        if (!folder.exists()) {
            return "Something went wrong";
        } else {
            for (File subFile : folder.listFiles()) {
                if (subFile.getName().contains("yml") || subFile.getName().contains("yaml")) {
                    return "Deployment script generated";
                }
                if (subFile.getName().endsWith("xmi")) {
                    return "Only the model to export was generated";
                }
            }
        }
        return "";
    }

    /**
     * Deletes all files and folders in given folder
     * 
     * @param folder All files an folders in this folder are deleted
     */
    private static void deleteOldGeneratedFiles(File folder) {
        if (folder.exists()) {
            for (File subFile : folder.listFiles()) {
                if (subFile.isDirectory()) {
                    deleteOldGeneratedFiles(subFile);
                } else {
                    subFile.delete();
                }
            }
        }
    }

    /**
     * Reads the TyphonDL model from the given main model, using the
     * {@link XtextLiveScopeResourceSetProvider} to find other model files in the
     * folder. Also saves the model in a .xmi file.
     * 
     * @param file     The main TyphonDL model
     * @param provider Provides the correct resourceSet for finding other model
     *                 files
     * @return The TyphonDL model including all information taken from other related
     *         model files
     */
    public static DeploymentModel loadXtextModel(IFile file, XtextLiveScopeResourceSetProvider provider) {

        XtextResourceSet resourceSet = (XtextResourceSet) provider.get(file.getProject());
        resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
        // adds all .tdl files in project folder to resourceSet
        IResource members[] = null;
        try {
            members = file.getProject().members();
        } catch (CoreException e) {
            e.printStackTrace();
        }
        for (IResource member : members) {
            if (member instanceof IFile) {
                if (((IFile) member).getFileExtension().equals("tdl")) {
                    resourceSet.getResource(URI.createPlatformResourceURI(member.getFullPath().toString(), true), true);
                }
            }
        }
        // read DL model and properties
        URI modelURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
        Resource DLmodelResource = resourceSet.getResource(modelURI, true);
        DeploymentModel model = (DeploymentModel) DLmodelResource.getContents().get(0);
        String path = Paths.get(file.getLocationURI()).getParent().resolve(file.getName() + ".properties").toString();
        model = addDBsToModel(model);
        Properties properties = new Properties();
        try {
            InputStream input = new FileInputStream(path);
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        model = addPolystoreToModel(path, model, properties);
        Container polystoreMongoContainer = getPolystoreMongoContainer(model, properties);
        String clusterType = getClusterTypeOfPolystore(polystoreMongoContainer);
        URI DLmodelXMI = saveModelAsXMI(DLmodelResource);
        String folder = file.getLocation().toOSString().replace(file.getName(),
                DLmodelXMI.segment(DLmodelXMI.segmentCount() - 2));
        Path DLPath = Paths.get(file.getLocation().toOSString().replace(file.getName(),
                DLmodelXMI.segment(DLmodelXMI.segmentCount() - 2) + File.separator + DLmodelXMI.lastSegment()));
        if (properties.get("polystore.useAnalytics").equals("true") && clusterType.equalsIgnoreCase("Kubernetes")) {
            try {
                downloadKafkaFiles(folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String mongoInsertStatement = createMongoCommands(DLPath,
                Paths.get(file.getLocation().toOSString().replace(file.getName(), getMLmodelPath(model))));
        switch (clusterType) {
        case "Kubernetes":
            // to be able to add the models to the kubernetes job, the
            // mongo.insert(DLxmi,MLxmi) has to be added to the model here, so that acceleo
            // can access it. It's not nice, maybe we should think about a different plugin
            // to generate our scripts
            addInsertStatementToPolystoreMongoContainer(polystoreMongoContainer, mongoInsertStatement, properties);
            break;
        case "DockerCompose":
            writeInsertStatementToJavaScriptFile(DLPath, mongoInsertStatement, properties);
            break;
        default:
            break;
        }
        return model;
    }

    private static void downloadKafkaFiles(String folder) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String analyticsZipPath = folder + File.separator + ANALYTICS_KUBERNETES_ZIP_FILENAME;
        IWorkbench wb = PlatformUI.getWorkbench();
        IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
        try {
            URL url = new URL(ANALYTICS_ZIP_ADDRESS);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                MessageDialog.openError(win.getShell(), "Scripts", "Analytics files could not be downloaded at "
                        + ANALYTICS_ZIP_ADDRESS + ", please check your internet connection and try again");
                System.out.println(
                        "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(analyticsZipPath);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
                System.out.println("EXCEPTION!");
            }
            if (connection != null)
                connection.disconnect();
        }

        if (input != null) {
            unzip(analyticsZipPath, folder);
        }
    }

    private static void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        // delete zip
        new File(zipFilePath).delete();
    }

    /**
     * Extracts a zip entry (file entry)
     * 
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    /**
     * Creates a "addModels.js" file inside the db.volume directory. This script is
     * executed the first time the polystore-mongo container is started and adds
     * both the ML and DL model to the database db.environment.MONGO_INITDB_DATABASE
     * 
     * @param DLPath
     * @param mongoInsertStatement
     * @param properties
     */
    private static void writeInsertStatementToJavaScriptFile(Path DLPath, String mongoInsertStatement,
            Properties properties) {
        String folder = DLPath.toString().replace(DLPath.getFileName().toString(), properties.getProperty("db.volume"));
        String path = folder + File.separator + "addModels.js";
        if (!Files.exists(Paths.get(folder))) {
            try {
                Files.createDirectory(Paths.get(folder));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Files.write(Paths.get(path), mongoInsertStatement.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the {@link ClusterType} of the given Container
     * 
     * @param polystoreMongoContaier
     * @return The ClusterType of the containing Cluster
     */
    private static String getClusterTypeOfPolystore(Container polystoreMongoContaier) {
        return ((Cluster) ((Application) polystoreMongoContaier.eContainer()).eContainer()).getType().getName();
    }

    /**
     * Searches for the polystore-mongo container in the given model
     * 
     * @param model
     * @param properties
     * @return A container named "polystore-mongo"
     */
    private static Container getPolystoreMongoContainer(DeploymentModel model, Properties properties) {
        List<Container> mongo = EcoreUtil2.getAllContentsOfType(model, Container.class).stream()
                .filter(container -> container.getName().equalsIgnoreCase(properties.getProperty("db.containername")))
                .collect(Collectors.toList());
        return mongo.isEmpty() ? null : mongo.get(0);
    }

    /**
     * Adds the mongo insert statement to the polystoreMongoContainer
     * 
     * @param polystoreMongoContainer The polystore-mongo container
     * @param addToMongoContainer     The String containing the
     *                                mongo.insert(DLmodel,MLmodel) statement
     * @param properties              The polystore.properties saved in the project
     *                                folder
     */
    private static void addInsertStatementToPolystoreMongoContainer(Container polystoreMongoContainer,
            String addToMongoContainer, Properties properties) {
        Key_Values print = TyphonDLFactory.eINSTANCE.createKey_Values();
        print.setName("print");
        print.setValue(addToMongoContainer);
        polystoreMongoContainer.getProperties().add(print);
    }

    /**
     * Creates a mongo insert statement containing the DL and ML model in a JSON
     * field fitting the API
     * 
     * @param DLmodel The path to the newly created DLmodel.xmi
     * @param MLmodel The path to the source MLmodel.xmi
     * @return The mongo insert statement containing both models
     */
    private static String createMongoCommands(Path DLmodel, Path MLmodel) {
        String DLmodelContent = "";
        String MLmodelContent = "";
        try {
            DLmodelContent = String.join("", Files.readAllLines(DLmodel));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            MLmodelContent = String.join("", Files.readAllLines(MLmodel));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "db.models.insert([{\"_id\":UUID(), \"version\":1, \"initializedDatabases\":"
                + "false, \"initializedConnections\":true, \"contents\":\"" + DLmodelContent.replaceAll("\"", "\\\\\"")
                + "\", \"type\":\"DL\", \"dateReceived\":ISODate(), "
                + "\"_class\":\"com.clms.typhonapi.models.Model\" }, {\"_id\":UUID(), \"version\":1, \"initializedDatabases\":"
                + "false, \"initializedConnections\":false, \"contents\":\"" + MLmodelContent.replaceAll("\"", "\\\\\"")
                + "\", \"type\":\"ML\", \"dateReceived\":ISODate(), "
                + "\"_class\":\"com.clms.typhonapi.models.Model\" }]);";
    }

    /**
     * Reads the relative MLmodel path from the Import section of the DL model
     * 
     * @param model the DL model
     * @return The relative MLmodel path as a String
     */
    private static String getMLmodelPath(DeploymentModel model) {
        return EcoreUtil2.getAllContentsOfType(model, Import.class).stream()
                .filter(info -> (info.getRelativePath().endsWith("xmi") || info.getRelativePath().endsWith("tmlx")))
                .map(info -> info.getRelativePath()).collect(Collectors.toList()).get(0);
    }

    /**
     * Finds and adds the DB models from additional files if given as {@link Import}
     * in the main model file
     * 
     * @param model The model to add the DBs to
     * @return The model with the DB models added
     */
    private static DeploymentModel addDBsToModel(DeploymentModel model) {
        Resource resource = model.eResource();
        URI uri = resource.getURI().trimSegments(1);

        EcoreUtil2.getAllContentsOfType(model, Import.class).stream()
                .filter(info -> info.getRelativePath().endsWith("tdl")).forEach(info -> {
                    model.getElements()
                            .addAll(((DeploymentModel) resource.getResourceSet()
                                    .getResource(uri.appendSegment(info.getRelativePath()), true).getContents().get(0))
                                            .getElements());
                });
        ;
        return model;
    }

    /**
     * Creates a .xmi file representing the given DL model
     * 
     * @param DLmodelResource The given DL model
     * @return URI to the saved DL model
     */
    private static URI saveModelAsXMI(Resource DLmodelResource) {
        XtextResourceSet resourceSet = (XtextResourceSet) DLmodelResource.getResourceSet();
        DeploymentModel DLmodel = (DeploymentModel) DLmodelResource.getContents().get(0);
        URI folder = DLmodelResource.getURI().trimFileExtension();
        // creates a xmi resource with the same name as the model in a folder named like
        // the model, so example/test.tdl -> example/test/test.xmi
        Resource xmiResource = resourceSet.createResource(folder.appendSegment(folder.lastSegment() + ".xmi"));

        // add model
        xmiResource.getContents().add(DLmodel);

        EcoreUtil.resolveAll(resourceSet);
        try {
            xmiResource.save(Options.getXMIoptions());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xmiResource.getURI();
    }

    /**
     * Adds the three polystore containers and corresponding {@link Services} to the
     * TyphonDL model
     * 
     * @param path       The path to the polystore.properties
     * @param model      The given TyphonDL model
     * @param properties The polystore.properties saved in the project folder
     * @return The given TyphonDL model plus the polystore component
     */
    private static DeploymentModel addPolystoreToModel(String path, DeploymentModel model, Properties properties) {

        List<DBType> dbTypes = EcoreUtil2.getAllContentsOfType(model, DBType.class);
        // if Mongo is not allready a DBType, add Mongo
        DBType mongo = dbTypes.stream().filter(dbType -> dbType.getName().equalsIgnoreCase("mongo")).findFirst()
                .orElse(null);
        if (mongo == null) {
            mongo = TyphonDLFactory.eINSTANCE.createDBType();
            mongo.setName("Mongo");
            IMAGE mongoImage = TyphonDLFactory.eINSTANCE.createIMAGE();
            mongoImage.setValue("mongo:latest");
            mongo.setImage(mongoImage);
            model.getElements().add(mongo);
        }

        // will be removed -----------------------------------------v
        // get Application for polystore containers
        Application application = null;
        if (!properties.get("polystore.inApplication").equals("default")) {
            application = getApplication(model, (String) properties.get("polystore.inApplication"));
        } else if (properties.get("polystore.inApplication").equals("default") || application == null) {
            // get first application
            application = EcoreUtil2.getAllContentsOfType(model, Application.class).get(0);
        }
        // will be removed -----------------------------------------^

        // get containertype
        ContainerType containerType = application.getContainers().get(0).getType();

        // get clustertype
        String clusterType = ((Cluster) application.eContainer()).getType().getName();

        // polystore_db
        DB polystoredb = TyphonDLFactory.eINSTANCE.createDB();
        polystoredb.setName(properties.getProperty("db.name"));
        polystoredb.setType(mongo);
        if (clusterType.equalsIgnoreCase("DockerCompose")) {
            Environment polystoredb_environment = TyphonDLFactory.eINSTANCE.createEnvironment();
            Key_Values polystoredb_environment_1 = TyphonDLFactory.eINSTANCE.createKey_Values();
            polystoredb_environment_1.setName("MONGO_INITDB_DATABASE");
            polystoredb_environment_1.setValue(properties.getProperty("db.environment.MONGO_INITDB_DATABASE"));
            polystoredb_environment.getParameters().add(polystoredb_environment_1);
            polystoredb.setEnvironment(polystoredb_environment);
        }
        Credentials credentials = TyphonDLFactory.eINSTANCE.createCredentials();
        credentials.setUsername("admin");
        credentials.setPassword("admin");
        polystoredb.setCredentials(credentials);
        model.getElements().add(polystoredb);
        Reference poystoredbReference = TyphonDLFactory.eINSTANCE.createReference();
        poystoredbReference.setReference(polystoredb);

        Container polystoredb_container = TyphonDLFactory.eINSTANCE.createContainer();
        polystoredb_container.setName(properties.getProperty("db.containername"));
        polystoredb_container.setType(containerType);
        polystoredb_container.setDeploys(poystoredbReference);
        if (clusterType.equalsIgnoreCase("DockerCompose")) {
            Key_ValueArray polystoredb_container_volume = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
            polystoredb_container_volume.setName("volumes");
            polystoredb_container_volume.getValues()
                    .add("./" + properties.getProperty("db.volume") + "/:/docker-entrypoint-initdb.d");
            polystoredb_container.getProperties().add(polystoredb_container_volume);
        }

        de.atb.typhondl.xtext.typhonDL.URI polystoredb_container_uri = TyphonDLFactory.eINSTANCE.createURI();
        polystoredb_container_uri
                .setValue(properties.getProperty("db.containername") + ":" + properties.getProperty("db.port"));
        polystoredb_container.setUri(polystoredb_container_uri);

        // polystore_api
        Software polystore_api;
        polystore_api = TyphonDLFactory.eINSTANCE.createSoftware();
        polystore_api.setName(properties.getProperty("api.name"));
        IMAGE polystore_api_image = TyphonDLFactory.eINSTANCE.createIMAGE();
        polystore_api_image.setValue(properties.getProperty("api.image"));
        polystore_api.setImage(polystore_api_image);
        model.getElements().add(polystore_api);
        Reference polystore_api_reference = TyphonDLFactory.eINSTANCE.createReference();
        polystore_api_reference.setReference(polystore_api);

        Container polystore_api_container = TyphonDLFactory.eINSTANCE.createContainer();
        polystore_api_container.setName(properties.getProperty("api.containername"));
        polystore_api_container.setType(containerType);
        polystore_api_container.setDeploys(polystore_api_reference);

        de.atb.typhondl.xtext.typhonDL.URI polystore_api_container_uri = TyphonDLFactory.eINSTANCE.createURI();
        polystore_api_container_uri
                .setValue(properties.getProperty("api.containername") + ":" + properties.getProperty("api.port"));
        polystore_api_container.setUri(polystore_api_container_uri);

        Key_Values polystore_api_container_port = TyphonDLFactory.eINSTANCE.createKey_Values();
        polystore_api_container_port.setName("target");
        polystore_api_container_port.setValue(properties.getProperty("api.port"));
        Key_Values polystore_api_container_publishedPort = TyphonDLFactory.eINSTANCE.createKey_Values();
        polystore_api_container_publishedPort.setName("published");
        polystore_api_container_publishedPort.setValue(properties.getProperty("api.publishedPort"));
        Ports polystore_api_container_ports = TyphonDLFactory.eINSTANCE.createPorts();
        polystore_api_container_ports.getKey_values().add(polystore_api_container_port);
        polystore_api_container_ports.getKey_values().add(polystore_api_container_publishedPort);
        polystore_api_container.setPorts(polystore_api_container_ports);

        if (clusterType.equalsIgnoreCase("DockerCompose")) {
            Key_ValueArray polystore_api_entrypoint = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
            polystore_api_entrypoint.setName("entrypoint");
            polystore_api_entrypoint.getValues().add("wait-for-it");
            polystore_api_entrypoint.getValues().add("polystore-mongo:27017");
            polystore_api_entrypoint.getValues().add("-t");
            polystore_api_entrypoint.getValues().add("'60'");
            polystore_api_entrypoint.getValues().add("--");
            polystore_api_entrypoint.getValues()
                    .addAll(Arrays.asList(properties.getProperty("api.entrypoint").split(",")));
            polystore_api_container.getProperties().add(polystore_api_entrypoint);
            Key_Values polystore_api_container_restart = TyphonDLFactory.eINSTANCE.createKey_Values();
            polystore_api_container_restart.setName("restart");
            polystore_api_container_restart.setValue("always");
            polystore_api_container.getProperties().add(polystore_api_container_restart);
        }

        Dependency polystore_api_dependency = TyphonDLFactory.eINSTANCE.createDependency();
        polystore_api_dependency.setReference(polystore_api_container);

        // polystore ui
        Software polystore_ui = TyphonDLFactory.eINSTANCE.createSoftware();
        polystore_ui.setName(properties.getProperty("ui.name"));
        IMAGE polystore_ui_image = TyphonDLFactory.eINSTANCE.createIMAGE();
        polystore_ui_image.setValue(properties.getProperty("ui.image"));
        polystore_ui.setImage(polystore_ui_image);
        Key_KeyValueList polystore_ui_environment = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
        polystore_ui_environment.setName("environment");
        Key_Values polystore_ui_environment1 = TyphonDLFactory.eINSTANCE.createKey_Values();
        polystore_ui_environment1.setName("API_PORT");
        polystore_ui_environment1.setValue(properties.getProperty("ui.environment.API_PORT"));
        polystore_ui_environment.getProperties().add(polystore_ui_environment1);
        Key_Values polystore_ui_environment2 = TyphonDLFactory.eINSTANCE.createKey_Values();
        polystore_ui_environment2.setName("API_HOST");
        polystore_ui_environment2.setValue(properties.getProperty("ui.environment.API_HOST"));
        polystore_ui_environment.getProperties().add(polystore_ui_environment2);
        polystore_ui.getParameters().add(polystore_ui_environment);
        model.getElements().add(polystore_ui);
        Reference polystore_ui_reference = TyphonDLFactory.eINSTANCE.createReference();
        polystore_ui_reference.setReference(polystore_ui);

        Container polystore_ui_container = TyphonDLFactory.eINSTANCE.createContainer();
        polystore_ui_container.setName(properties.getProperty("ui.containername"));
        polystore_ui_container.setType(containerType);
        polystore_ui_container.setDeploys(polystore_ui_reference);
        polystore_ui_container.getDepends_on().add(polystore_api_dependency);
        de.atb.typhondl.xtext.typhonDL.URI polystore_ui_container_uri = TyphonDLFactory.eINSTANCE.createURI();
        polystore_ui_container_uri
                .setValue(properties.getProperty("ui.containername") + ":" + properties.getProperty("ui.port"));
        polystore_ui_container.setUri(polystore_ui_container_uri);
        Key_Values polystore_ui_container_port = TyphonDLFactory.eINSTANCE.createKey_Values();
        polystore_ui_container_port.setName("target");
        polystore_ui_container_port.setValue(properties.getProperty("ui.port"));
        Ports polystore_ui_container_ports = TyphonDLFactory.eINSTANCE.createPorts();
        polystore_ui_container_ports.getKey_values().add(polystore_ui_container_port);
        Key_Values polystore_ui_published_port = TyphonDLFactory.eINSTANCE.createKey_Values();
        polystore_ui_published_port.setName("published");
        polystore_ui_published_port.setValue(properties.getProperty("ui.publishedPort"));
        polystore_ui_container_ports.getKey_values().add(polystore_ui_published_port);
        polystore_ui_container.setPorts(polystore_ui_container_ports);
        Key_KeyValueList polystore_ui_container_build = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
        polystore_ui_container_build.setName("build");
        Key_Values polystore_ui_container_build_context = TyphonDLFactory.eINSTANCE.createKey_Values();
        polystore_ui_container_build_context.setName("context");
        polystore_ui_container_build_context.setValue("Typhon Service UI");
        polystore_ui_container_build.getProperties().add(polystore_ui_container_build_context);

        application.getContainers().add(polystoredb_container);
        application.getContainers().add(polystore_api_container);
        application.getContainers().add(polystore_ui_container);

        // QL server
        Software qlserver = TyphonDLFactory.eINSTANCE.createSoftware();
        qlserver.setName(properties.getProperty("qlserver.name"));
        IMAGE qlserver_image = TyphonDLFactory.eINSTANCE.createIMAGE();
        qlserver_image.setValue(properties.getProperty("qlserver.image"));
        qlserver.setImage(qlserver_image);
        Reference qlserver_reference = TyphonDLFactory.eINSTANCE.createReference();
        qlserver_reference.setReference(qlserver);
        model.getElements().add(qlserver);
        Container qlserver_container = TyphonDLFactory.eINSTANCE.createContainer();
        qlserver_container.setName(properties.getProperty("qlserver.containername"));
        de.atb.typhondl.xtext.typhonDL.URI qlserver_container_uri = TyphonDLFactory.eINSTANCE.createURI();
        qlserver_container_uri.setValue(
                properties.getProperty("qlserver.containername") + ":" + properties.getProperty("qlserver.port"));
        qlserver_container.setUri(qlserver_container_uri);
        qlserver_container.setDeploys(qlserver_reference);
        if (clusterType.equalsIgnoreCase("DockerCompose")) {
            Key_Values qlserver_container_restart = TyphonDLFactory.eINSTANCE.createKey_Values();
            qlserver_container_restart.setName("restart");
            qlserver_container_restart.setValue("always");
            qlserver_container.getProperties().add(qlserver_container_restart);
        }

        application.getContainers().add(qlserver_container);

        // Analytics, see https://github.com/typhon-project/typhondl/issues/6

        String kafkaURI = properties.getProperty("analytics.kafka.uri");
        String kafkaPort = kafkaURI.substring(kafkaURI.indexOf(':') + 1);
        String kafkaHost = kafkaURI.substring(0, kafkaURI.indexOf(':'));
        if (properties.get("polystore.useAnalytics").equals("true") && clusterType.equalsIgnoreCase("DockerCompose")) {
            String zookeeperPort = properties.getProperty("analytics.zookeeper.publishedPort");
            String zookeeperTargetPort = properties.getProperty("analytics.zookeeper.port");
            String kafkaInsidePort = properties.getProperty("analytics.kafka.insidePort");
            String kafkaAdvertisedHost = kafkaHost;
            String[] kafkaListeners = properties.getProperty("analytics.kafka.listeners").split("\\s*,\\s*");
            String kafkaListenerNameIn = properties.getProperty("analytics.kafka.listenerName.in");
            String kafkaListenerNameOut = properties.getProperty("analytics.kafka.listenerName.out");
            String kafkaListenersString = "";
            String kafkaAdvertisedListenerString = "";
            for (int i = 0; i < kafkaListeners.length; i++) {
                kafkaListenersString += kafkaListenerNameOut + "://:" + kafkaListeners[i] + ", ";
                kafkaAdvertisedListenerString += kafkaListenerNameOut + "://" + kafkaAdvertisedHost + ":"
                        + kafkaListeners[i] + ", ";
            }
            kafkaListenersString += kafkaListenerNameIn + "://:" + kafkaInsidePort;
            kafkaAdvertisedListenerString += kafkaListenerNameIn + "://:" + kafkaInsidePort;

            if (properties.get("analytics.deployment.create").equals("true")) {
                Software zookeeper = TyphonDLFactory.eINSTANCE.createSoftware();
                zookeeper.setName("zookeeper");
                IMAGE zookeeper_image = TyphonDLFactory.eINSTANCE.createIMAGE();
                zookeeper_image.setValue(properties.getProperty("analytics.zookeeper.image"));
                zookeeper.setImage(zookeeper_image);
                model.getElements().add(zookeeper);
                Reference zookeeper_reference = TyphonDLFactory.eINSTANCE.createReference();
                zookeeper_reference.setReference(zookeeper);

                Container zookeeper_container = TyphonDLFactory.eINSTANCE.createContainer();
                zookeeper_container.setName(properties.getProperty("analytics.zookeeper.containername"));
                zookeeper_container.setType(containerType);
                zookeeper_container.setDeploys(zookeeper_reference);
                Key_Values zookeeper_container_ports1 = TyphonDLFactory.eINSTANCE.createKey_Values();
                zookeeper_container_ports1.setName("published");
                zookeeper_container_ports1.setValue(zookeeperPort);
                Key_Values zookeeper_container_ports2 = TyphonDLFactory.eINSTANCE.createKey_Values();
                zookeeper_container_ports2.setName("target");
                zookeeper_container_ports2.setValue(zookeeperTargetPort);
                Ports zookeeper_container_port = TyphonDLFactory.eINSTANCE.createPorts();
                zookeeper_container_port.getKey_values().add(zookeeper_container_ports1);
                zookeeper_container_port.getKey_values().add(zookeeper_container_ports2);
                zookeeper_container.setPorts(zookeeper_container_port);

                Dependency zookeeper_dependency = TyphonDLFactory.eINSTANCE.createDependency();
                zookeeper_dependency.setReference(zookeeper_container);

                application.getContainers().add(zookeeper_container);
                de.atb.typhondl.xtext.typhonDL.URI kafkaURIObject = TyphonDLFactory.eINSTANCE.createURI();
                kafkaURIObject.setValue(kafkaURI);

                Software kafka = TyphonDLFactory.eINSTANCE.createSoftware();
                kafka.setName("Kafka");
                model.getElements().add(kafka);
                Environment kafka_environment = TyphonDLFactory.eINSTANCE.createEnvironment();
                Key_Values KAFKA_ZOOKEEPER_CONNECT = TyphonDLFactory.eINSTANCE.createKey_Values();
                KAFKA_ZOOKEEPER_CONNECT.setName("KAFKA_ZOOKEEPER_CONNECT");
                KAFKA_ZOOKEEPER_CONNECT.setValue("zookeeper:" + zookeeperTargetPort);
                kafka_environment.getParameters().add(KAFKA_ZOOKEEPER_CONNECT);
                Key_Values KAFKA_ADVERTISED_HOST_NAME = TyphonDLFactory.eINSTANCE.createKey_Values();
                KAFKA_ADVERTISED_HOST_NAME.setName("KAFKA_ADVERTISED_HOST_NAME");
                KAFKA_ADVERTISED_HOST_NAME.setValue(kafkaAdvertisedHost);
                kafka_environment.getParameters().add(KAFKA_ADVERTISED_HOST_NAME);
                Key_Values KAFKA_LISTENERS = TyphonDLFactory.eINSTANCE.createKey_Values();
                KAFKA_LISTENERS.setName("KAFKA_LISTENERS");
                KAFKA_LISTENERS.setValue(kafkaListenersString);
                kafka_environment.getParameters().add(KAFKA_LISTENERS);
                Key_Values KAFKA_LISTENER_SECURITY_PROTOCOL_MAP = TyphonDLFactory.eINSTANCE.createKey_Values();
                KAFKA_LISTENER_SECURITY_PROTOCOL_MAP.setName("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP");
                KAFKA_LISTENER_SECURITY_PROTOCOL_MAP
                        .setValue(kafkaListenerNameIn + ":PLAINTEXT, " + kafkaListenerNameOut + ":PLAINTEXT");
                kafka_environment.getParameters().add(KAFKA_LISTENER_SECURITY_PROTOCOL_MAP);
                Key_Values KAFKA_INTER_BROKER_LISTENER_NAME = TyphonDLFactory.eINSTANCE.createKey_Values();
                KAFKA_INTER_BROKER_LISTENER_NAME.setName("KAFKA_INTER_BROKER_LISTENER_NAME");
                KAFKA_INTER_BROKER_LISTENER_NAME.setValue(kafkaListenerNameIn);
                kafka_environment.getParameters().add(KAFKA_INTER_BROKER_LISTENER_NAME);
                Key_Values KAFKA_ADVERTISED_LISTENERS = TyphonDLFactory.eINSTANCE.createKey_Values();
                KAFKA_ADVERTISED_LISTENERS.setName("KAFKA_ADVERTISED_LISTENERS");
                KAFKA_ADVERTISED_LISTENERS.setValue(kafkaAdvertisedListenerString);
                kafka_environment.getParameters().add(KAFKA_ADVERTISED_LISTENERS);
                Key_Values KAFKA_AUTO_CREATE_TOPICS_ENABLE = TyphonDLFactory.eINSTANCE.createKey_Values();
                KAFKA_AUTO_CREATE_TOPICS_ENABLE.setName("KAFKA_AUTO_CREATE_TOPICS_ENABLE");
                KAFKA_AUTO_CREATE_TOPICS_ENABLE.setValue("\"true\"");
                kafka_environment.getParameters().add(KAFKA_AUTO_CREATE_TOPICS_ENABLE);
                Reference kafka_reference = TyphonDLFactory.eINSTANCE.createReference();
                kafka.setEnvironment(kafka_environment);
                kafka_reference.setReference(kafka);

                Container kafka_container = TyphonDLFactory.eINSTANCE.createContainer();
                kafka_container.setName(properties.getProperty("analytics.kafka.containername"));
                kafka_container.setType(containerType);
                kafka_container.setDeploys(kafka_reference);
                Key_Values kafka_container_build = TyphonDLFactory.eINSTANCE.createKey_Values();
                kafka_container_build.setName("build");
                kafka_container_build.setValue(".");
                kafka_container.getProperties().add(kafka_container_build);
                kafka_container.getDepends_on().add(zookeeper_dependency);
                Key_Values kafka_container_ports1 = TyphonDLFactory.eINSTANCE.createKey_Values();
                kafka_container_ports1.setName("published");
                kafka_container_ports1.setValue(kafkaPort);
                Key_Values kafka_container_ports2 = TyphonDLFactory.eINSTANCE.createKey_Values();
                kafka_container_ports2.setName("target");
                kafka_container_ports2.setValue(properties.getProperty("analytics.kafka.port"));
                Ports kafka_container_ports = TyphonDLFactory.eINSTANCE.createPorts();
                kafka_container_ports.getKey_values().add(kafka_container_ports1);
                kafka_container_ports.getKey_values().add(kafka_container_ports2);
                kafka_container.setPorts(kafka_container_ports);
                Key_ValueArray kafka_container_volumes = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
                kafka_container_volumes.setName("volumes");
                kafka_container_volumes.getValues().add("/var/run/docker.sock:/var/run/docker.sock");
                kafka_container.getProperties().add(kafka_container_volumes);
                application.getContainers().add(kafka_container);
                kafka_container.setUri(kafkaURIObject);

                Software flink_jobmanager = TyphonDLFactory.eINSTANCE.createSoftware();
                flink_jobmanager.setName("FlinkTJobmanager");
                IMAGE flink_image = TyphonDLFactory.eINSTANCE.createIMAGE();
                flink_image.setValue("flink:latest");
                flink_jobmanager.setImage(flink_image);
                Environment flink_environment = TyphonDLFactory.eINSTANCE.createEnvironment();
                Key_Values flink_rcp_address = TyphonDLFactory.eINSTANCE.createKey_Values();
                flink_rcp_address.setName("JOB_MANAGER_RPC_ADDRESS");
                flink_rcp_address.setValue("jobmanager");
                flink_environment.getParameters().add(flink_rcp_address);
                flink_jobmanager.setEnvironment(flink_environment);
                model.getElements().add(flink_jobmanager);
                Software flink_taskmanager = TyphonDLFactory.eINSTANCE.createSoftware();
                flink_taskmanager.setName("FlinkTaskmanager");
                flink_taskmanager.setImage(EcoreUtil.copy(flink_image));
                flink_taskmanager.setEnvironment(EcoreUtil.copy(flink_environment));
                model.getElements().add(flink_taskmanager);
                Container flink_jobmanager_container = TyphonDLFactory.eINSTANCE.createContainer();
                flink_jobmanager_container.setName("jobmanager");
                Reference flink_jobmanger_reference = TyphonDLFactory.eINSTANCE.createReference();
                flink_jobmanger_reference.setReference(flink_jobmanager);
                flink_jobmanager_container.setDeploys(flink_jobmanger_reference);
                Ports flink_jobmanager_ports = TyphonDLFactory.eINSTANCE.createPorts();
                Key_Values flink_jobmanager_published = TyphonDLFactory.eINSTANCE.createKey_Values();
                flink_jobmanager_published.setName("published");
                flink_jobmanager_published.setValue("8081");
                flink_jobmanager_ports.getKey_values().add(flink_jobmanager_published);
                Key_Values flink_jobmanager_target = TyphonDLFactory.eINSTANCE.createKey_Values();
                flink_jobmanager_target.setName("target");
                flink_jobmanager_target.setValue("8081");
                flink_jobmanager_ports.getKey_values().add(flink_jobmanager_target);
                flink_jobmanager_container.setPorts(flink_jobmanager_ports);
                Key_Values flink_jobmanager_command = TyphonDLFactory.eINSTANCE.createKey_Values();
                flink_jobmanager_command.setName("command");
                flink_jobmanager_command.setValue("jobmanager");
                flink_jobmanager_container.getProperties().add(flink_jobmanager_command);
                Key_ValueArray flink_jobmanager_expose = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
                flink_jobmanager_expose.setName("expose");
                flink_jobmanager_expose.getValues().add("6123");
                flink_jobmanager_container.getProperties().add(flink_jobmanager_expose);
                Container flink_taskmanager_container = TyphonDLFactory.eINSTANCE.createContainer();
                flink_taskmanager_container.setName("taskmanager");
                Reference flink_taskmanger_reference = TyphonDLFactory.eINSTANCE.createReference();
                flink_taskmanger_reference.setReference(flink_taskmanager);
                flink_taskmanager_container.setDeploys(flink_taskmanger_reference);
                Key_Values flink_taskmanager_command = TyphonDLFactory.eINSTANCE.createKey_Values();
                flink_taskmanager_command.setName("command");
                flink_taskmanager_command.setValue("taskmanager");
                flink_taskmanager_container.getProperties().add(flink_taskmanager_command);
                Key_ValueArray flink_taskmanager_expose = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
                flink_taskmanager_expose.setName("expose");
                flink_taskmanager_expose.getValues().addAll(Arrays.asList("6121", "6122"));
                flink_taskmanager_container.getProperties().add(flink_taskmanager_expose);
                Dependency flink_jobmanager_dependency = TyphonDLFactory.eINSTANCE.createDependency();
                flink_jobmanager_dependency.setReference(flink_jobmanager_container);
                flink_taskmanager_container.getDepends_on().add(flink_jobmanager_dependency);
                application.getContainers().add(flink_jobmanager_container);
                application.getContainers().add(flink_taskmanager_container);

                Container authAllContainer = TyphonDLFactory.eINSTANCE.createContainer();
                authAllContainer.setName("authAll");
                authAllContainer.setType(containerType);
                Software authAll = TyphonDLFactory.eINSTANCE.createSoftware();
                authAll.setName("authAll");
                IMAGE authAllImage = TyphonDLFactory.eINSTANCE.createIMAGE();
                authAllImage.setValue(properties.getProperty("analytics.authAll.image"));
                authAll.setImage(authAllImage);
                model.getElements().add(authAll);
                Reference authAllRef = TyphonDLFactory.eINSTANCE.createReference();
                authAllRef.setReference(authAll);
                authAllContainer.setDeploys(authAllRef);

                application.getContainers().add(authAllContainer);

                if (properties.get("analytics.deployment.contained").equals("false")) {
                    kafka.setExternal(true);
                }
            } else {
                Software kafka = TyphonDLFactory.eINSTANCE.createSoftware();
                kafka.setName("Kafka");
                kafka.setExternal(true);
                de.atb.typhondl.xtext.typhonDL.URI kafkaURIObject = TyphonDLFactory.eINSTANCE.createURI();
                kafkaURIObject.setValue(kafkaURI);
                kafka.setUri(kafkaURIObject);
                model.getElements().add(kafka);
            }

        } else if (properties.get("polystore.useAnalytics").equals("true")
                && clusterType.equalsIgnoreCase("Kubernetes")) {
            Software kafka = TyphonDLFactory.eINSTANCE.createSoftware();
            kafka.setName("Kafka");
            de.atb.typhondl.xtext.typhonDL.URI kafkaURIObject = TyphonDLFactory.eINSTANCE.createURI();
            kafkaURIObject.setValue(kafkaURI);
            model.getElements().add(kafka);
            if (properties.get("analytics.deployment.create").equals("true")) {
                Container kafkaContainer = TyphonDLFactory.eINSTANCE.createContainer();
                kafkaContainer.setName("kafka");
                Reference kafka_reference = TyphonDLFactory.eINSTANCE.createReference();
                kafka_reference.setReference(kafka);
                kafkaContainer.setDeploys(kafka_reference);
                kafkaContainer.setUri(kafkaURIObject);
                if (properties.get("analytics.deployment.contained").equals("false")) {
                    kafka.setExternal(true);
                    kafka.setUri(kafkaURIObject);
                }
            } else {
                kafka.setExternal(true);
                kafka.setUri(kafkaURIObject);
            }
        }
        return model;
    }

    private static String createPassword(int length) {
        String dic = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        String result = "";
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(dic.length());
            result += dic.charAt(index);
        }
        return result;
    }

    /**
     * Gets application TODO remove application
     * 
     * @param model   The TyphonDL model
     * @param appName The name of the Application to find
     * @return The Application to put the polystore in
     */
    private static Application getApplication(DeploymentModel model, String appName) {
        List<Application> list = new ArrayList<Application>();
        EcoreUtil2.getAllContentsOfType(model, Application.class).stream().filter(app -> app.getName().equals(appName))
                .map(app -> list.add(app));
        return (list.size() == 1) ? list.get(0) : null;
    }
}
