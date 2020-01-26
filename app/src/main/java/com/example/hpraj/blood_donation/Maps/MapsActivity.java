package com.example.hpraj.blood_donation.Maps;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hpraj.blood_donation.Holder.LocationHolder;
import com.example.hpraj.blood_donation.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String current_latitude, current_longtude;
    private float smallestDistance = -1;
    private DatabaseReference databaseReference;
    private ArrayList<Location> locations;
    private Location current_location;
    private Location closestLocation;
    private String loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Blood_Banks");
        locations = new ArrayList<>();

        loc = getIntent().getExtras().getString("location");
        if  (!loc.equals("")){
            current_latitude = loc.substring(0, loc.indexOf(","));
            current_longtude = loc.substring(loc.indexOf(",") + 1);
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        current_location = new Location("Your Location");
        current_location.setLatitude(Double.parseDouble(current_latitude));
        current_location.setLongitude(Double.parseDouble(current_longtude));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String loc = postSnapshot.child("location").getValue(String.class);
                    String loc_name = postSnapshot.child("hos_name").getValue(String.class);

                    String latitude = loc.substring(0, loc.indexOf(","));
                    String longitude = loc.substring(loc.indexOf(",") + 1);

                    Location location = new Location(loc_name);
                    location.setLatitude(Double.parseDouble(latitude));
                    location.setLongitude(Double.parseDouble(longitude));

                    locations.add(location);
                }
                if (locations.size() > 0) {
                    for (Location location : locations) {
                        float distance = current_location.distanceTo(location);

                        if (smallestDistance == -1 || distance < smallestDistance) {
                            closestLocation = location;
                            smallestDistance = distance;
                        }
                    }
                    LatLng lat = null;
                    MarkerOptions markerOptions = new MarkerOptions();
                    Marker locatoinMarcker = null;

                    lat = new LatLng(current_location.getLatitude(), current_location.getLongitude());
                    markerOptions.position(lat).title("Your Location");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    locatoinMarcker = mMap.addMarker(markerOptions);
                    locatoinMarcker.showInfoWindow();

                    lat = new LatLng(closestLocation.getLatitude(), closestLocation.getLongitude());
                    markerOptions.position(lat).title(closestLocation.getProvider());
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    locatoinMarcker = mMap.addMarker(markerOptions);
                    locatoinMarcker.showInfoWindow();

                    float zoomLevel = 14.0f;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, zoomLevel));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
