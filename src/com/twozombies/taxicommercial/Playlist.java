package com.twozombies.taxicommercial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;

public class Playlist {
    public final static String PLAYLIST_FILENAME = "playlist.txt";
    
    private final int SWITCHING_INTERVAL = 3;
    
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
        mIterator = 0;
        mVideos.clear();
        
        try {
            File playlistFile = new File(FileManager.getLocalFilePath(PLAYLIST_FILENAME));
            
            if (!playlistFile.exists())
                playlistFile.createNewFile();
            
            reader = new BufferedReader(new FileReader(playlistFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                File videoFile = new File(FileManager.getLocalFilePath(line));
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
    
    /** Gets video list */
    public ArrayList<String> getVideoList() {
        return mVideos;
    }
    
    /** Gets first video from the playlist */
    public String getFirstVideo() {
        return getVideo(0);
    }
    
    /** Gets next video from the playlist */
    public String getNextVideo() {
        if (isEmpty()) {
            return null;
        }
        
        String nextVideo = getVideo(mIterator % mVideos.size());
        mIterator++;
        
        File nextVideoFile = new File(nextVideo);
        if (!nextVideoFile.exists()) {
            mVideos.remove(mIterator);
            return getNextVideo();
        }
        
        mSwitching = ((mIterator % SWITCHING_INTERVAL) == 0)? true : false;

        return nextVideo;
    }
    
    /** Checks switching state, true if a commercial block ended */
    public boolean isSwitching() {
        return mSwitching;
    }
    
    /** True if playlist is empty */
    public boolean isEmpty() {
        return mVideos.isEmpty();
    }
    
    /** Gets video by index */
    private String getVideo(int index) {
        return mVideos.get(index);
    }
}
