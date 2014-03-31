package com.twozombies.taxicommercial;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class CommercialActivity extends SingleFragmentActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileManager.createRootFolder();
    }
    
    @Override
    protected Fragment createFragment() {
        return new CommercialFragment();
    }
    
}
