package com.twozombies.taxicommercial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;

public class Playlist {
    private final static String ROOT = "com.twozombies.taxicommercial";
    private final static String PLAYLIST_FILENAME = "playlist.txt";
    private final int SWITCHING_INTERVAL = 1;
    
    private BufferedReader reader = null;
    private ArrayList<String> mVideos;
    private int mIterator;
    private boolean mSwitching = false;
    
    private static Playlist sPlaylist;
    private Context mAppContext;
    
    public Playlist(Context appContext) {
        mAppContext = appContext;
        mVideos = new ArrayList<String>();
        mIterator = 0;
        
        update();
    }
    
    public static Playlist get(Context c) {
        if (sPlaylist == null) {
            sPlaylist = new Playlist(c.getApplicationContext());
        }
        return sPlaylist;
    }
    
    /** Updates the playlist from a playlist file */
    public void update() {

        try {
            File root = new File(Environment.getExternalStorageDirectory(), ROOT);
            File playlistFile = new File(root, PLAYLIST_FILENAME);
            reader = new BufferedReader(new FileReader(playlistFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                File videoFile = new File(root, line);
                if (videoFile.isFile()) {
                    mVideos.add(videoFile.getAbsolutePath());
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

    }
    
    /** Gets first video from the playlist */
    public String getFirstVideo() {
        return mVideos.get(0);
    }
    
    /** Gets next video from the playlist */
    public String getNextVideo() {
        String nextVideo = mVideos.get(mIterator % mVideos.size());
        mIterator++;
        mSwitching = ((mIterator % SWITCHING_INTERVAL) == 0)? true : false;

        return nextVideo;
    }
    
    /** Checks switching state, true if a commercial block ended */
    public boolean isSwitching() {
        return mSwitching;
    }
}
