package de.atb.typhondl.xtext.ui.utilities;

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
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class FileService {

    private final static int BUFFER_SIZE = 4096;

    public static String replaceOldValueWithNewValue(String string, String propertyNameInFile, String property) {
        String separator = propertyNameInFile.substring(propertyNameInFile.length() - 1, propertyNameInFile.length());
        String target = string.substring(string.indexOf(separator) + 1);
        if (separator.equals(":")) {
            return string.replace(target, " " + property);
        } else if (separator.equals("=")) {
            return string.replace(target, property);
        }
        return "ERROR";
    }

    public static void applyMapToFile(Properties properties, Path path, HashMap<String, String> map)
            throws IOException {
        List<String> list = Files.readAllLines(path);
        for (int i = 0; i < list.size(); i++) {
            for (String propertyNameInFile : map.keySet()) {
                String string = list.get(i);
                if (string.contains(propertyNameInFile)) {
                    list.set(i, FileService.replaceOldValueWithNewValue(string, propertyNameInFile,
                            properties.getProperty(map.get(propertyNameInFile))));
                }
            }
        }
        Files.write(path, list, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static InputStream downloadFiles(String path, String address, String component) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        IWorkbench wb = PlatformUI.getWorkbench();
        IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                MessageDialog.openError(win.getShell(), "Scripts", component + " files could not be downloaded at "
                        + address + ", please check your internet connection and try again");
                System.out.println(
                        "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(path);

            byte data[] = new byte[4096];
            int count;
            while ((count = input.read(data)) != -1) {
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
        return input;
    }

    public static void unzip(String zipFilePath, String destDirectory) throws IOException {
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
    public static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public static void save(Path path, List<String> lines) throws IOException {
        Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
