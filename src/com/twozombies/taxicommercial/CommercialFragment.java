package com.twozombies.taxicommercial;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;

public class CommercialFragment extends Fragment {
    private static final String TAG = "CommercialFragment";

    private View mView;
    private Playlist mPlaylist;
    private VideoPlayer mVideoPlayer;
    private VideoView mVideoView;
    private ProgressDialog mProgressBar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mPlaylist = Playlist.get(getActivity());
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_commercial, container, false);
        
        Log.d(TAG, "onCreateView");
        
        updateVideoView();

        return mView;
    }
    
    /** Updates playlist file and download all videos from the Dropbox if playlist not found */
    private void updatePlaylist() {
        Log.d(TAG, "updatePlaylist");
        mPlaylist.update();
        if (mPlaylist.isEmpty()) {
            ConnectivityManager cm =
                    (ConnectivityManager) getActivity()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm.getActiveNetworkInfo() == null || 
               !cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
                Toast.makeText(
                    getActivity(), 
                    R.string.network_not_found, 
                    Toast.LENGTH_LONG
                ).show();
                
                return;
            }
            
            mProgressBar = new ProgressDialog(getActivity());
            mProgressBar.setCancelable(true);
            mProgressBar.setMessage(getActivity().getString(R.string.downloader));
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressBar.setProgress(0);
            mProgressBar.setMax(100);
            mProgressBar.show();
            
            AsyncTask<Void, Integer, Void> task = new AsyncTask<Void, Integer, Void>() {

                public Void doInBackground(Void... params) {
                    try {
                        
                        byte[] content = FileManager.getExternalFileContent(Playlist.PLAYLIST_FILENAME);
                        String externalPlaylist = new String(content);
                        ArrayList<String> files = new ArrayList<String>();
                        for (String fileName : externalPlaylist.split(System.getProperty("line.separator"))) {
                            if (fileName != "") {
                                files.add(fileName);
                            }
                        }
                        
                        int filesDownloaded = 0;
                        for (String fileName : files) {
                            FileManager.downloadFile(fileName, fileName);
                            filesDownloaded++;
                            publishProgress((int) (100 * filesDownloaded / files.size()));
                        }
                        
                        FileManager.saveToLocalFile(Playlist.PLAYLIST_FILENAME, content);
                        
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }
                
                @Override
                protected void onProgressUpdate(Integer... values) {
                    mProgressBar.setProgress(values[0]);
                    super.onProgressUpdate(values);
                }
                
                @Override
                protected void onPostExecute(Void result) {
                    mProgressBar.dismiss();
                    mPlaylist.update();
                    updateVideoView();
                    if (!mPlaylist.isEmpty())
                        mVideoPlayer.play();
                    super.onPostExecute(result);    
                }

            };
            
            task.execute();
                        
        }
    }
    
    private void updateVideoView() {
        mVideoView = (VideoView) mView.findViewById(R.id.commercial);
        
        mVideoPlayer = new VideoPlayer(this.getActivity());
        mVideoPlayer.connectToView(mVideoView);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        updatePlaylist();
        
        if (!mPlaylist.isEmpty())
            mVideoPlayer.play();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        mVideoPlayer.stop();
    }
    
}
