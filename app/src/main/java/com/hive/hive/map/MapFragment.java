package com.hive.hive.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hive.hive.R;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public MapFragment() {
        // Required empty public constructor
    }


    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng parqueUnaLatLng = new LatLng(-31.7650966, -52.3161512);
        float initialZoom = 15f;

        Marker parqueUnaMarker = mMap.addMarker(
                new MarkerOptions()
                        .position(parqueUnaLatLng)
                        .title(getString(R.string.parque_una))
        );

        parqueUnaMarker.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parqueUnaLatLng, initialZoom));
    }
}
