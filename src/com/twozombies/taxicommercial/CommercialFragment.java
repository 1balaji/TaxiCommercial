package com.twozombies.taxicommercial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

public class CommercialFragment extends Fragment {
	
	private VideoPlayer mVideoPlayer;
	private VideoView mVideoView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_commercial, container, false);
		
		mVideoView = (VideoView) view.findViewById(R.id.commercial);
		mVideoPlayer = VideoPlayer.get(this.getActivity(), mVideoView);
		mVideoPlayer.play();

		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mVideoPlayer.play();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mVideoPlayer.stop();
	}
	
}
