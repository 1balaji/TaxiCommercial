package com.twozombies.taxicommercial;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.VideoView;

public class VideoPlayer {

    private VideoView mVideoView;
    private Playlist mPlaylist;
    
    private static VideoPlayer mVideoPlayer;
    private Context mAppContext;
    
    public VideoPlayer(Context appContext, VideoView videoView) {
        mVideoView = videoView;
        mPlaylist = Playlist.get(appContext);
        mAppContext = appContext;
        
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
    
    /** Gets VideoPlayer instance if it is exists, or creates it otherwise */
    public static VideoPlayer get(Context c, VideoView videoView) {
        if (mVideoPlayer == null) {
            mVideoPlayer = new VideoPlayer(c.getApplicationContext(), videoView);
        }
        return mVideoPlayer;
    }
    
    /** Starts playing video from the beginning */
    public void play() {
        mVideoView.setVideoPath(mPlaylist.getFirstVideo());
        mVideoView.requestFocus();
        mVideoView.start();
    }
    
    /** Stops playing */
    public void stop() {
        mVideoView.stopPlayback();    
    }
    
}
