package com.twozombies.taxicommercial;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements LocationListener {
    private final static float ZOOM = 17.0f;
    
    private GoogleMap mGoogleMap = null;
    private Marker mMyLocation = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        setupMap();  
        return view;
    }
    
    /** Sets up GoogleMap and founds my location on it */
    private void setupMap() {
        if (mGoogleMap == null) {
            SupportMapFragment mf = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
            mGoogleMap = mf.getMap();
            if (mGoogleMap != null) {
                mGoogleMap.setMyLocationEnabled(true);

                LocationManager lm = (LocationManager) getActivity()
                    .getSystemService(FragmentActivity.LOCATION_SERVICE);
                
                String provider = lm.getBestProvider(new Criteria(), true);
                
                if (provider == null) {
                    provider = LocationManager.NETWORK_PROVIDER;
                }
                
                if (provider != null) {
                    onProviderDisabled(provider);
                }
                
                Location location = lm.getLastKnownLocation(provider);
                
                if (location != null) {
                    onLocationChanged(location);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        
        if (mMyLocation != null) {
            mMyLocation.setPosition(latlng);
        } else {
            mMyLocation = mGoogleMap.addMarker(new MarkerOptions().position(latlng));
        }
        
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}