package com.example.heem.togetherfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

//V1.2 Output Better - Nick
public class ShowTrainer extends AppCompatActivity {

    private DatabaseReference mRef;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("User");
    ArrayList<String> toPrint = new ArrayList<>();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trainer);

        Button Search = (Button) findViewById(R.id.SearchBTN); // id of a view contained in the included file
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(ShowTrainer.this, FindTrainer.class);
                finish();
                startActivity(back);
            }
        });

        list = (ListView) findViewById(R.id.list);

        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://togetherfit-148901.firebaseio.com/User");

        final String zip = getIntent().getStringExtra("Zip"); //input from previous page
        final String WorkoutType = getIntent().getStringExtra("WorkoutType"); //input from previous page

        if (WorkoutType == null) {
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    toPrint = new ArrayList<String>(); //To update array list
                    for (DataSnapshot chunk : snapshot.getChildren()) {
                        //get value from the database. If it is equal to the postalcode from current location add the location to the user
                        String name = (String) chunk.child("Name").getValue();
                        String email = (String) chunk.child("Email").getValue();
                        String location = (String) chunk.child("ZipCode").getValue();
                        String type = (String) chunk.child("FitnessType").getValue();
                        String output = "Name: "+ name +  "\n"+ "Email: " + email + "\n" + "Location/Zipcode: " + location + "\n" + "Workout Type: " + type + "\n" ;
                        if( zip.equals(location) ){
                            toPrint.add(output);
                        }
                    }

                    if( toPrint.size() != 0 ){
                        //To print out each location close to the current location
                        //Find closets place to the trainer
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShowTrainer.this, R.layout.show_trainer_custom_listview, toPrint);
                        list.setAdapter(adapter);
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

        }

        if (zip == null) {
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    toPrint = new ArrayList<String>(); //To update array list
                    for (DataSnapshot chunk : snapshot.getChildren()) {
                        //get value from the database. If it is equal to the postalcode from current location add the location to the user
                        String name = (String) chunk.child("Name").getValue();
                        String email = (String) chunk.child("Email").getValue();
                        String location = (String) chunk.child("ZipCode").getValue();
                        String type = (String) chunk.child("FitnessType").getValue();
                        String output = "Name: "+ name +  "\n"+ "Email: " + email + "\n" + "Location/Zipcode: " + location + "\n" + "Workout Type: " + type + "\n";
                        if( WorkoutType.equals(type) ){
                            toPrint.add(output);
                        }
                    }

                    if( toPrint.size() != 0 ){
                        //To print out each location close to the current location
                        //Find closets place to the trainer
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShowTrainer.this, R.layout.show_trainer_custom_listview, toPrint);
                        list.setAdapter(adapter);
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

        }



    }
}
