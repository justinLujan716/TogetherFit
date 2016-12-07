package com.example.heem.togetherfit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//Show Trainer by location
public class showtrainermap extends FragmentActivity implements OnMapReadyCallback {

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
    Context mContext = showtrainermap.this;
    Button clearPath;
    //To find distance
    Location start;
    Location dis;
    LatLng holdCurrent;
    //To keep track of paths
    List<Polyline> polyPaths = new ArrayList<>();
    //To record trainer information
    List<String> trainername = new ArrayList<>();
    List<String> traineremail = new ArrayList<>();
    List<String> trainerfitnesstype = new ArrayList<>();
    List<String> trainerpicture = new ArrayList<>();
    TextView title;
    TextView snippet;
    ImageView image;
    Dialog settingsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtrainermap);
        // Connect to the Firebase database and access TrainerPlaces database
        database = FirebaseDatabase.getInstance().getReference().child("User");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Create a storage reference from our app
        //StorageReference storageRef = storage.getReferenceFromUrl("gs://<your-bucket-name>");

        /*
         * Show closets locations
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
                            //get value from the database. You the name to find closets locations.
                            String localAddress = (String) chunk.child("Address").getValue();
                            String localCity = (String) chunk.child("City").getValue();
                            String localState = (String) chunk.child("State").getValue();
                            String localZip = (String) chunk.child("ZipCode").getValue();
                            String locallocation = localAddress + " " + localCity + " " + localState + " " + localZip;
                            //get the user type and check if it is trainer or not
                            String userType = (String) chunk.child("Type").getValue();
                            //For the new location from the Database
                            //Find the lat and lon for the new location
                            if (userType.equalsIgnoreCase("trainer")) {
                                LatLng nearLoc = getLocationFromAddress(locallocation);
                                if (nearLoc != null) {  //To make sure that nearLoc is not null first
                                    double lat = nearLoc.latitude;
                                    double lon = nearLoc.longitude;
                                    dis = latlngToloc(lat, lon); //Update the distention
                                    float distance = start.distanceTo(dis) / 1000;
                                    //To find a distance in KM
                                    if (distance <= 5) { //Find places near the current location, at most 5 KM almost 2.5 Miles
                                        toPrint.add(locallocation); //Add them to the toPrint list
                                        String getTrainername = (String) chunk.child("Name").getValue();
                                        trainername.add(getTrainername);
                                        String getTraineremail = (String) chunk.child("Email").getValue();
                                        traineremail.add(getTraineremail);
                                        String getTrainerfitnesstype = (String) chunk.child("FitnessType").getValue();
                                        trainerfitnesstype.add(getTrainerfitnesstype);
                                        String getTrainerpicture = (String) chunk.child("imageURL").getValue();
                                        trainerpicture.add(getTrainerpicture);
                                    }
                                }
                            } else {
                                //Since it is not trainer do not check
                                continue;
                            }

                        }

                        //To print out each trainer location close to the current location
                        if (toPrint.size() > 0) {
                            int i = 0;
                            for (String s : toPrint) {
                                //Find closets trainer
                                LatLng closeLoc = getLocationFromAddress(s);
                                latitude = closeLoc.latitude;
                                longitude = closeLoc.longitude;
                                address(latitude, longitude, s, BitmapDescriptorFactory.HUE_AZURE, i);
                                i++;//To pass the index
                            }
                        } else
                            Toast.makeText(getApplicationContext(), "Can not find a close trainers", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseErr) {
                        System.out.println("The read failed: " + databaseErr.getCode());
                    }
                });

                Toast.makeText(getApplicationContext(), "Rose marker -- displays your current location \nBlue markers -- display trainers", Toast.LENGTH_LONG).show();

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
                if (newLocation.length() <= 5) {
                    Toast.makeText(getApplicationContext(), "You must enter a valid location", Toast.LENGTH_LONG).show();
                } else {
                    //get the string entered by user
                    location = getLocationFromAddress(newLocation);
                    holdCurrent = getLocationFromAddress(newLocation); //To hold
                    mMap.clear();//clear all the map
                    //update the current location
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15);
                    mMap.animateCamera(cameraUpdate);
                    latitude = location.latitude;
                    longitude = location.longitude;
                    start = latlngToloc(latitude, longitude);
                    //Rose to distinguish the current location marker
                    address(latitude, longitude, ("Your location: " + newLocation), BitmapDescriptorFactory.HUE_ROSE, -1);// -1 because we do not need to add trainer info
                }

            }
        });


        //Back button takes back to sign in activity
        //This is the way to refer to outside button from another laytout back button is in header.xml
        View myLayout = findViewById(R.id.backbtnlayout); // root View id from that link
        Button backbutton = (Button) myLayout.findViewById(R.id.backbtn); // id of a view contained in the included file
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // the name of the receiving activity is declared in the Intent Constructor, go back to log in page
                Intent back = new Intent(showtrainermap.this, FindTrainer.class);
                //start the activity
                startActivity(back);
            }
        });
        //Log out button at the tool bar to take the user to main page
        View myLayout2 = findViewById(R.id.signOut); // root View id from that link
        Button signOut = (Button) myLayout2.findViewById(R.id.signOutbtn); // id of a view contained in the included file
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent back = new Intent(showtrainermap.this, MainActivity.class);
                //start the activity
                startActivity(back);
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
        gps = new GPSTracker(showtrainermap.this, showtrainermap.this);
        geocoder = new Geocoder(this, Locale.getDefault());
        Log.e("latitude", "latitude--" + latitude);
        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        start = latlngToloc(latitude, longitude);
        holdCurrent = new LatLng(latitude, longitude); //To hold
        //Rose to distinguish the current location marker
        address(latitude, longitude, "Your location", BitmapDescriptorFactory.HUE_ROSE, -1);//-1 because we do not need to add trainer info to this marker
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
            Address locationGet = address.get(0);
            p1 = new LatLng(locationGet.getLatitude(), locationGet.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    /*
     * Method to update complete address from lng and lat to string address and also add a marker
     */
    public void address(double lat, double lng, String name, float f, int index) {

        //These variables to get the complete address
        location = new LatLng(lat, lng);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 10);
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
                if (index != -1) { //That's mean is not the user location
                    String tname = trainername.get(index);
                    String temail = traineremail.get(index);
                    String tfitness = trainerfitnesstype.get(index);
                    String tpicture = trainerpicture.get(index); //Here where we get the picture
                    //This will appear when the user click on the marker (marker color pass on when the method is called)
                    mMap.addMarker(new MarkerOptions().position(location).title(temail).snippet("Trainer Name: " + tname + "\nTrainer Fitness Type: " + tfitness + "\nStreet: " + address + "\n" + "City: " + city + "\n" + "State: " + state + "\n" + "Country: " + country + "\n" + "Postal Code: " + postalCode).icon(BitmapDescriptorFactory.defaultMarker(f)));

                } else {
                    //This will appear when the user click on the marker (marker color pass on when the method is called)
                    mMap.addMarker(new MarkerOptions().position(location).title(name).snippet("Street: " + address + "\n" + "City: " + city + "\n" + "State: " + state + "\n" + "Country: " + country + "\n" + "Postal Code: " + postalCode).icon(BitmapDescriptorFactory.defaultMarker(f)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*
           Multi line marker, this method just to design the marker when the user click on
         */
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() { //Here create a custom

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                if (polyPaths.size() > 0) //Delete path just to make sure there is no more than one paths there
                    deletePath();
                title = new TextView(mContext);
                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());
                snippet = new TextView(mContext);
                snippet.setTextColor(Color.BLACK);
                if (!marker.getTitle().equalsIgnoreCase("Your location") && !marker.getTitle().substring(0, 13).equalsIgnoreCase("Your location")) {
                    snippet.setText("Click for more info");
                } else {
                    snippet.setText(marker.getSnippet());
                }
                info.addView(title);
                info.addView(snippet);
                LatLng position = marker.getPosition();
                Direction(holdCurrent,position);
                return info;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                final String temail = title.getText().toString();
                if (!temail.equalsIgnoreCase("Your Location") && !temail.substring(0, 13).equalsIgnoreCase("Your Location")) { //To make sure when the user click on his locaiton nothing appear
                    settingsDialog = new Dialog(showtrainermap.this);
                    settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    View view = getLayoutInflater().inflate(R.layout.custom_in_click_info_windows, null);
                    view.invalidate();
                    settingsDialog.setContentView(view);
                    TextView trainerT = (TextView) settingsDialog.findViewById(R.id.trainerT);
                    trainerT.setText(marker.getTitle());
                    TextView trainerS = (TextView) settingsDialog.findViewById(R.id.trainerS);
                    trainerS.setText(marker.getSnippet());
                    String imageToShow = getImageURLFromtitle(temail);
                    ImageView trainerI = (ImageView) settingsDialog.findViewById(R.id.image);
                    Picasso.with(mContext).load(imageToShow).resize(120,120).error(R.drawable.personal).into(trainerI, new MarkerCallback(marker));
                    settingsDialog.show();

                    Button read = (Button) settingsDialog.findViewById(R.id.ReadReview);
                    read.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(showtrainermap.this, ReadReviews.class);
                            intent.putExtra("traineremail", temail);
                            startActivity(intent);
                        }
                    });
                    Button write = (Button) settingsDialog.findViewById(R.id.WriteReview);
                    write.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(showtrainermap.this, WriteReview.class);
                            intent.putExtra("TrainerEmail", temail);
                            startActivity(intent);
                        }
                    });
                    Button email = (Button) settingsDialog.findViewById(R.id.SendEmail);
                    email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(showtrainermap.this, SendEmailThroughAndroid.class);
                            intent.putExtra("emailTo", temail);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }



    /*
     * Mehtod to convert --> Latlng to Location
     */
    public Location latlngToloc(double lat, double lng) {
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(lat);
        temp.setLongitude(lng);
        return temp;
    }


    /*
     * To draw a path
     * To get the source and destenation address
     * Calling routePathTask
     */
    public void Direction(LatLng from, LatLng to) {
        String url = makeURL(from, to);
        showtrainermap.RoutePathTask routePathTask = new showtrainermap.RoutePathTask(url);
        routePathTask.execute();
    }

    //Route path task to help to draw the path between 2 points
    private class RoutePathTask extends AsyncTask<Void, Void, String> {

        private String url;

        public RoutePathTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected String doInBackground(Void... params) {
            DirectionsJSONParser jParser = new DirectionsJSONParser();
            String json = jParser.downloadUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                drawPath(result);
            }
        }
    }

    /*
     *Method to draw the path between 2 points
     */
    public void drawPath(String result) {
        try {
            //Transform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                        .width(6)
                        .color(Color.parseColor("#696969")).geodesic(true));
                polyPaths.add(line);//To keep track of paths
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     *Configure the path line
     */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    /*
     * Method to get the URL for the roting address
     */
    public String makeURL(LatLng sourceLoc, LatLng destLoc) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(sourceLoc.latitude);
        urlString.append(",");
        urlString.append(sourceLoc.longitude);
        urlString.append("&destination=");// to
        urlString.append(destLoc.latitude);
        urlString.append(",");
        urlString.append(destLoc.longitude);
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }

    /*
     * Method to clear the path
     */
    public void deletePath() {
        for (Polyline path : polyPaths) {
            path.remove();
        }

        polyPaths.clear();

    }

    /*
     * This method: to return url and disply that in info windows
     */

    public String getImageURLFromtitle(String email)
    {
        int index=0;
        for (String temail: traineremail) {
            if (temail.equalsIgnoreCase(email))
                return trainerpicture.get(index);
            ++index;
        }
        return "";
    }


    /*
     * To hold some exception in Picasa
     */
    static class MarkerCallback implements Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.showInfoWindow();
            }
        }
    }

}

