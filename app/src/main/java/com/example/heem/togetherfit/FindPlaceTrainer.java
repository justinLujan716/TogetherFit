package com.example.heem.togetherfit;

import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.location.Address;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindPlaceTrainer extends FragmentActivity implements OnMapReadyCallback {
    //Map
    private GoogleMap mMap;
    // GPSTracker class object, gecocode object and a list to get addresses
    GPSTracker gps;
    Geocoder geocoder;
    List<Address> addresses;
    //Firebase variable
    DatabaseReference database;
    //locations variable
    CameraUpdate cameraUpdate;
    double latitude = 0.0;
    double longitude = 0.0;
    String address = "";
    String city = "";
    String state = "";
    String country = "";
    String postalCode = "";
    String knownName = "";
    ArrayList<String> toPrint = new ArrayList<>();
    LatLng location;
    String newLocation;
    //Fields
    Button searchbtn;
    Button updateLoc;
    EditText txtUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_place_trainer);
        // Connect to the Firebase database and access TrainerPlaces database
        database = FirebaseDatabase.getInstance().getReference().child("TrainerPlaces");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*
         * Show cloests locations
         * Search button, once the user click on this button. To Show the user all closest places to train students at
         */

        searchbtn = (Button) findViewById(R.id.search);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Attach a listener to read the data at our posts reference
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        toPrint = new ArrayList<String>(); //To update array list
                        for (DataSnapshot chunk : snapshot.getChildren()) {
                            //get value from the database. If it is equal to the postalcode from current location add the location to the user
                            String zipCode = (String) chunk.child("zip").getValue();
                            String name = (String) chunk.child("name").getValue();
                            if (zipCode.equals(postalCode))
                                toPrint.add(name);
                        }

                        //To print out each location close to the current location
                        for (String s : toPrint) {
                            //Find closets place to the trainer
                            LatLng closeLoc = getLocationFromAddress(s);
                            latitude = closeLoc.latitude;
                            longitude = closeLoc.longitude;
                            address(latitude,longitude,s);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseErr) {
                        System.out.println("The read failed: " + databaseErr.getCode());
                    }
                });
            }
        });


        /*
         * Update current location
         * to update current location when user click on the search button and typing a valid location
         */

        updateLoc = (Button) findViewById(R.id.updateLocation);
        txtUpdate = (EditText) findViewById(R.id.txtupdateLocation);
        updateLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newLocation = String.valueOf(txtUpdate.getText());
                if (newLocation.length()<= 5)
                {
                    Toast.makeText(getApplicationContext(), "You must enter a valid location", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //get the string entered by user
                    location = getLocationFromAddress(newLocation);
                    mMap.clear();//clear all the map
                    //update the current location
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15);
                    mMap.animateCamera(cameraUpdate);
                    latitude = location.latitude;
                    longitude = location.longitude;
                    address(latitude,longitude,newLocation);
                }

            }
        });

    }

    /**
     * Once it is ready show the current location
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // create class object from GPSTracker which will find current location if it is enabled or ask the user to enable locaiton service
        gps = new GPSTracker(FindPlaceTrainer.this,FindPlaceTrainer.this);
        geocoder = new Geocoder(this, Locale.getDefault());
        Log.e("latitude", "latitude--" + latitude);
        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }
        else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        address(latitude,longitude,"");

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    /*
     * Method to convert the String address to lng an latitude
     */

    public LatLng getLocationFromAddress(String strAddress) {

        List<Address> address;
        LatLng p1 = null;

        try {
            address = geocoder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    /*
     * Method to update complete address from lng and lat to string address and also add a marker
     */
    public void address(double lat, double lng, String name)
    {

        //These variables to get the complete address
        location = new LatLng(lat,lng);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15);
        mMap.animateCamera(cameraUpdate);
        //This part to retrive the actual address
        try {
            Log.e("latitude", "inside latitude--" + latitude);
            addresses = geocoder.getFromLocation(lat, lng, 1);

            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();
                knownName = addresses.get(0).getFeatureName(); //We might use it in future just keep it
                //This will appare when the user click on the marker
                mMap.addMarker(new MarkerOptions().position(location).title(name + " , " + address + " " + city + " " + state + " , " + country + " " + postalCode));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
