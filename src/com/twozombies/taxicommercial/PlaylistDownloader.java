package com.twozombies.taxicommercial;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PlaylistDownloader {
	
	private final static String EXTERNAL_FOLDER = "https://dl.dropboxusercontent.com/u/95289152";
	private final static int MAX_BUFFER = 1024;
	
	LocalStorage mLocalStorage;
	
    /** Constructor */
	public PlaylistDownloader() {
		mLocalStorage = new LocalStorage();
	}
	
    /** Download playlist file */
	public File loadPlaylistFile() throws IOException {
		return loadFile(Playlist.PLAYLIST_FILENAME);
	}
	
    /** Download clip */
	public File loadClipFile(String clipName) throws IOException {
		return loadFile(clipName);
	}
	
    /** Gets URL by a file name */
	private URL getUrlbyName(String fileName) throws MalformedURLException {
		String uri = new StringBuilder(EXTERNAL_FOLDER)
				.append("/")
				.append(fileName)
				.toString();
		return new URL(uri);
	}
	
    /** Download file */
	private File loadFile(String fileName) throws IOException {
		File tempFile = mLocalStorage.getTempFileByName(fileName, true);
		long skipBytes = (tempFile.length() > 0) ? tempFile.length() : 0;
		
		HttpURLConnection connection = (HttpURLConnection) getUrlbyName(fileName).openConnection();
		if (skipBytes > 0) {
            connection.setRequestProperty("Range", "bytes="+(skipBytes)+"-");
        }
		
        FileOutputStream outFile = null;
		try {
			outFile = new FileOutputStream(tempFile, true);
            InputStream in = connection.getInputStream();
            
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
             
            int bytesRead = 0;
            byte[] buffer = new byte[MAX_BUFFER];
            while ((bytesRead = in.read(buffer)) > 0) {
                outFile.write(buffer, 0, bytesRead);
            }
            
            File file = mLocalStorage.getFileByName(fileName, false);
            if (file.exists())
            	file.delete();
            tempFile.renameTo(file);
            
            return tempFile;
            
		} finally {
			connection.disconnect();
            outFile.close();
		}
	}
}
