package com.twozombies.taxicommercial;

import android.support.v4.app.Fragment;

public class CommercialActivity extends SingleFragmentActivity {
	
	@Override
	protected Fragment createFragment() {
		return new CommercialFragment();
	}
	
}
