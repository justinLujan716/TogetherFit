package com.example.heem.togetherfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ShowTrainer extends AppCompatActivity {

    private DatabaseReference mRef;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("User");
    ListView list;
    //Variables
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> email = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> city = new ArrayList<>();
    ArrayList<String> state = new ArrayList<>();
    ArrayList<String> zip = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();
    ArrayList<String> fitnesstype = new ArrayList<>();
    ArrayList<String> image = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trainer);


        list = (ListView) findViewById(R.id.listAllForTrainer);
        list.setAdapter(null); //to make sure the list view is empty

        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://togetherfit-148901.firebaseio.com/User");
        final String WorkoutType = getIntent().getStringExtra("WorkoutType"); //input from previous page

            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot chunk : snapshot.getChildren()) {
                        //get value from the database. If it is equal to the postalcode from current location add the location to the user
                         String localname = (String) chunk.child("Name").getValue();
                         String localemail = (String) chunk.child("Email").getValue();
                         String localAddress = (String) chunk.child("Address").getValue();
                         String localCity = (String) chunk.child("City").getValue();
                         String localState = (String) chunk.child("State").getValue();
                         String localZip = (String) chunk.child("ZipCode").getValue();
                         String locallocation = localAddress + " " + localCity + " " + localState + " " + localZip;
                        //get the user type and check if it is trainer or not
                         String userType = (String) chunk.child("Type").getValue();
                        String localfitnesstype = (String) chunk.child("FitnessType").getValue();
                        if( WorkoutType.equals(localfitnesstype) && userType.equalsIgnoreCase("trainer")){
                            name.add(localname);
                            email.add(localemail);
                            address.add(locallocation);
                            fitnesstype.add(localfitnesstype);
                            image.add("https://goo.gl/images/kq302a");
                        }
                    }

                    if( name.size() > 0 ){
                        //name has the name of the trianer that match the data type
                        //Find closets place to the trainer
                        CustomListForShowTrainer adapter = new
                                CustomListForShowTrainer(ShowTrainer.this, name, image, email, address,fitnesstype);
                        list.setAdapter(adapter);
                        //perform a specific action when the user click on a class redirect the user to class's detiales
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            //When they click on an Item
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String currentEmail = email.get(+position);
                                    Toast.makeText(ShowTrainer.this, "You Clicked at " + name.get(+position) + "\nI took you to send email page", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ShowTrainer.this, SendEmailThroughAndroid.class);
                                    intent.putExtra("emailTo", currentEmail);
                                    startActivity(intent);
                                }
                        });

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No Trainers Found, Please Search Again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseErr) {
                    System.out.println("The read failed: " + databaseErr.getCode());
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
                Intent back = new Intent(ShowTrainer.this, FindTrainer.class);
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
                Intent back = new Intent(ShowTrainer.this, MainActivity.class);
                //start the activity
                startActivity(back);
            }
        });

        }
}
