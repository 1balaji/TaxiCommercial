package com.twozombies.taxicommercial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class Playlist {
    private final static String TAG = "Playlist";
    
    public final static String PLAYLIST_FILENAME = "playlist.txt";
    private final static int UPDATE_INTERVAL = 1000 * 60 * 60 * 1; // 1 hour
    
    private ArrayList<File> mClips;
    private int nextClipIndex = 0;
    private Handler mHandler;
    
    private static Playlist sPlaylist;
    
    /** Gets ArrayList of the clips from the file */
    public static ArrayList<File> getClips(File file) {
        LocalStorage localStorage = new LocalStorage();
        BufferedReader reader = null;
        ArrayList<File> clips = new ArrayList<File>();
        
        try {
            File playlistFile = localStorage.getFileByName(PLAYLIST_FILENAME, true);
            
            reader = new BufferedReader(new FileReader(playlistFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    File clip = localStorage.getFileByName(line, false);
                    clips.add(clip);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return clips;
    }
    
    /** Constructor */
    public Playlist() {
        mClips = new ArrayList<File>();
        try {
            update();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                Log.d(TAG, "update initiate");
                
                AsyncTask<Void, Integer, Void> task = new AsyncTask<Void, Integer, Void>() {

                    public Void doInBackground(Void... params) {
                        try {
                            
                            PlaylistDownloader downloader = new PlaylistDownloader();
                            
                            File playlist = downloader.loadPlaylistFile();
                            ArrayList<File> clips = Playlist.getClips(playlist);
                            
                            for (File clip : clips) {
                                if (!clip.exists())
                                    downloader.loadClipFile(clip.getName());
                            }
                            
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    }
                    
                    @Override
                    protected void onPostExecute(Void result) {
                        try {
                            update();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        super.onPostExecute(result);    
                    }

                };
                
                task.execute();
                
                mHandler.postDelayed(this, UPDATE_INTERVAL);
            }
         }, UPDATE_INTERVAL);
    }
    
    /** Gets instance */
    public static Playlist get() {
        if (sPlaylist == null) {
            sPlaylist = new Playlist();
        }
        return sPlaylist;
    }

    /** Updates playlist and validates all clips */
    public void update() throws IOException {
        LocalStorage localStorage = new LocalStorage();
        mClips.clear();
        mClips = getClips(localStorage.getFileByName(PLAYLIST_FILENAME, true));
        validateAll();
        
    }

    /** Checks clip by a index for the existence of */
    public boolean validate(int index) {
        if (mClips.size() <= index) {
            return false;
        }
        File clip = mClips.get(index);
        if (clip != null && !clip.exists() && !mClips.isEmpty()) {
            mClips.remove(clip);
            
            if ((nextClipIndex == (mClips.size() - 1))) // last element
                nextClipIndex = (nextClipIndex == 0) ? 0 : (nextClipIndex - 1);
            
            return false;
        }
        return true;
    }
    
    /** Checks clip for the existence of */ */
    public void validate(File clip) {
        validate(mClips.indexOf(clip));
    }
    
    /** Checks all clips for the existence of */
    public void validateAll() {
        int size = mClips.size();
        for (int i = 0; i < size; i++) {
            if (!validate(i)) {
                size -= 1;
                i -= 1;
            }
        }
    }

    /** Gets clip by a index */
    public File getClip(int index) {
        if (mClips.size() <= index) {
            return null;
        }
        File clip = mClips.get(index);
        if (validate(index)) {
            return clip;
        }
        else {
            return null;
        }
    }
    
    /** Gets next clip */
    public File getNextClip() {
        if (isEmpty())
            return null;
        
        File nextClip = mClips.get(nextClipIndex);
        nextClipIndex = (nextClipIndex + 1) % mClips.size();
        
        return nextClip;
    }
    
    /** Clears clip list */
    public void clear() {
        mClips.clear();
    }
    
    /** Checks for emptiness */
    public boolean isEmpty() {
        return mClips.isEmpty();
    }
    
}
