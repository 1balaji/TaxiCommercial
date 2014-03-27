package com.twozombies.taxicommercial;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MapActivity extends SingleFragmentActivity {

    private final static int DELAY = 2000;
    
    @Override
    protected Fragment createFragment() {
        return new MapFragment();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        new Timer().schedule(new TimerTask(){
            public void run() { 
                startActivity(new Intent(MapActivity.this, CommercialActivity.class));
            }
        }, DELAY);
    }

}
