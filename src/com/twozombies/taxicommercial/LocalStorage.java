package com.twozombies.taxicommercial;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

public class LocalStorage {
    
    private final static String ROOT = "com.twozombies.taxicommercial";
    private final static String TEMPFILE_EXTENSION = ".tmp";
    
    private File mRootFolder = null;
    
    /** Constructor, creates root directory if it not exists */
    public LocalStorage() {
        mRootFolder = getRootFolder();
        
        if (!mRootFolder.exists() || !mRootFolder.isDirectory()) {
            mRootFolder.mkdir();
        }
    }
    
    /** Gets root folder, e.g. "/mnt/sdcard/com.twozombies.taxicommercial" */
    public File getRootFolder() {
        if (mRootFolder != null) {
            return mRootFolder;
        }
        String rootFolderPath = new StringBuilder()
            	.append(Environment.getExternalStorageDirectory())
            	.append(File.separator)
            	.append(ROOT)
            	.toString();
        return new File(rootFolderPath);
    }
    
    /** Gets file by his name from the local storage */
    public File getFileByName(String fileName, boolean createIfNotExists) throws IOException {
        File file = new File(getRootFolder(), fileName);
        if (!file.exists() && createIfNotExists) {
            file.createNewFile();
        }
        return file;
    }
    
    /** Gets tempfile by his name from the local storage */
    public File getTempFileByName(String fileName, boolean createIfNotExists) throws IOException {
    	return getFileByName(
    			new StringBuilder(fileName).append(TEMPFILE_EXTENSION).toString(), 
    			createIfNotExists);
    }
}
