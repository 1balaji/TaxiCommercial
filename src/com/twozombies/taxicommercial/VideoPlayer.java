package com.twozombies.taxicommercial;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.VideoView;

public class VideoPlayer {

    private VideoView mVideoView;
    private Playlist mPlaylist;
    
    private Context mAppContext;
    
    public VideoPlayer(Context appContext) {
        mPlaylist = Playlist.get(appContext);
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
                if (!mPlaylist.isSwitching()) {
                    mp.reset();
                    mVideoView.setVideoPath(mPlaylist.getNextVideo());
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
            mVideoView.setVideoPath(mPlaylist.getNextVideo());
            mVideoView.requestFocus();
            mVideoView.start();
        }
    }
    
    /** Stops playing */
    public void stop() {
        if (mVideoView.isPlaying())
            mVideoView.stopPlayback();    
    }
    
}
