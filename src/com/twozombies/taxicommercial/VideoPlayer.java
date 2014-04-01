package com.twozombies.taxicommercial;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.VideoView;

public class VideoPlayer {

	private final static int COMMERCIAL_BLOCK_SIZE = 3;
	private int mVideoCounter = 0;
	
    private VideoView mVideoView;
    private Playlist mPlaylist;
    
    private Context mAppContext;
    
    public VideoPlayer(Context appContext) {
        mPlaylist = Playlist.get();
        mAppContext = appContext;
    }
    
    /** Connects to VideoView widget */
    public void connectToView(VideoView videoView) {
        mVideoView = videoView;
        
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
            }
        });
        
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            	mVideoCounter = (mVideoCounter + 1) % COMMERCIAL_BLOCK_SIZE;
            	
                if (mVideoCounter != 0) {
                    mp.reset();
                    mVideoView.setVideoPath(mPlaylist.getNextClip().getAbsolutePath());
                }
                else {
                    mp.reset();
                    Intent intent = new Intent(mAppContext, MapActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mAppContext.startActivity(intent);
                }          
            }
        });
    }
    
    /** Starts playing video from the beginning */
    public void play() {
        if (mPlaylist.isEmpty()) {
            return;
        }
        if (!mVideoView.isPlaying()) {
        	File nextClip = null;
        	while ((nextClip == null) && !mPlaylist.isEmpty()) {
        		nextClip = mPlaylist.getNextClip();
        		if (nextClip != null && !nextClip.exists())
        			nextClip = null;
        	}
        	if (nextClip != null) {
        		mVideoView.setVideoPath(nextClip.getAbsolutePath());
                mVideoView.requestFocus();
                mVideoView.start();
        	}
            
        }
    }
    
    /** Stops playing */
    public void stop() {
        if (mVideoView.isPlaying())
            mVideoView.stopPlayback();    
    }
    
}