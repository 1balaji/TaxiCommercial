package com.twozombies.taxicommercial;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Environment;

public class FileManager {
    private final static String EXTERNAL_DIR = "https://dl.dropboxusercontent.com/u/95289152";
    private final static String ROOT = "com.twozombies.taxicommercial";
    private final static int MAX_BUFFER = 1024;
    
    /** Download file from external source and save it to the sdcard */
    public static long downloadFile(String externalFile, String localFile) throws IOException {
        String external = getExternalFilePath(externalFile);
        File tempLocal = getLocalFile(localFile);
        long skipBytes = tempLocal.length();
        
        URL url = new URL(external);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        if (skipBytes > 0) {
            connection.setRequestProperty("Range", "bytes="+(skipBytes)+"-");
        }
        
        try {
            FileOutputStream outFile = new FileOutputStream(tempLocal, true);
            InputStream in = connection.getInputStream();
            
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return 0;
            }
             
            int bytesRead = 0;
            long total = 0;
            byte[] buffer = new byte[MAX_BUFFER];
            while ((bytesRead = in.read(buffer)) > 0) {
                outFile.write(buffer, 0, bytesRead);
                total += bytesRead;
            }
            outFile.close();
                        
            return total;
        } finally {
            connection.disconnect();
        }
    }
    
    /** Gets external file content */
    public static byte[] getExternalFileContent(String externalFile) throws IOException {
        String external = getExternalFilePath(externalFile);
        
        URL url = new URL(external);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            ByteArrayOutputStream outFile = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            
            int bytesRead = 0;
            byte[] buffer = new byte[MAX_BUFFER];
            while ((bytesRead = in.read(buffer)) > 0) {
                outFile.write(buffer, 0, bytesRead);
            }
            outFile.close();
                        
            return outFile.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    
    /** Gets File by filename, e.g. "playlist.txt" */
    public static File getLocalFile(String fileName) throws IOException {
        String localFileName = getLocalFilePath(fileName);
        File file = new File(localFileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
    
    /** Saves bytes array to a local file by filename, e.g. "playlist.txt" */
    public static File saveToLocalFile(String fileName, byte[] content) throws IOException {
        File local = getLocalFile(fileName);
        FileOutputStream outFile = new FileOutputStream(local);
        outFile.write(content, 0, content.length);
        outFile.close();
        return local;
    }
    
    /** Gets absolute path to the local file by filename, e.g. "platlist.txt" */
    public static String getLocalFilePath(String fileName) {
        StringBuilder local = new StringBuilder()
            .append(Environment.getExternalStorageDirectory())
            .append(File.separator)
            .append(ROOT)
            .append(File.separator)
            .append(fileName);
        return local.toString();
    }
    
    /** Gets absolute path to the file from the external storage by filename, e.g. "platlist.txt" */
    public static String getExternalFilePath(String fileName) {
        StringBuilder external = new StringBuilder()
            .append(EXTERNAL_DIR)
            .append("/")
            .append(fileName);
        return external.toString();
    }
    
    /** Creates app root folder, if not exists */
    public static String createRootFolder() {
        StringBuilder local = new StringBuilder()
            .append(Environment.getExternalStorageDirectory())
            .append(File.separator)
            .append(ROOT);
        File root = new File(local.toString());
        if (!root.exists())
            root.mkdir();
        return local.toString();
    }
    
}
