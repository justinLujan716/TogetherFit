package com.example.heem.togetherfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShowClasses extends AppCompatActivity {

    //Variables
    ListView list;
    ArrayList<String> classTitle = new ArrayList<String>();
    ArrayList<String> classID = new ArrayList<String>();
    ArrayList<String> location = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<>();
    ArrayList<String> cap = new ArrayList<>();
    ArrayList<String> regNum = new ArrayList<>();
    View mylayout;
    TextView ClassName;
    //Firebase variable
    DatabaseReference database;
    //To get cureent User Id
    String UserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        classTitle.clear();
        classID.clear();
        location.clear();
        image.clear();
        cap.clear();
        regNum.clear();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_classes);
        //listview
        list = (ListView) findViewById(R.id.listAllForTrainer);
        list.setAdapter(null); //to make sure the list view is empty
        //get teh current user id
        UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Connect to the Firebase database and access CreatedClass database
        database = FirebaseDatabase.getInstance().getReference().child("CreatedClass");
        // Attach a listener to read the data at our posts reference
        database.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                getUpdate(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getUpdate(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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
                Intent back = new Intent(ShowClasses.this, TrainerDashboard.class);
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
                Intent back = new Intent(ShowClasses.this, MainActivity.class);
                //start the activity
                startActivity(back);
            }
        });

    }

    private void getUpdate(DataSnapshot ds)
    {
        String dsId = (ds.child("Trainer").getValue()).toString();
        if (UserId.equalsIgnoreCase(dsId)) {
            //Get the class name and send that to the adapter
            String titleValue = (ds.child("ClassName").getValue()).toString();
            classTitle.add(titleValue);
            //Get teh class ID and send that to the adapter
            String classId = ds.getRef().getKey();
            classID.add(classId);
            //Get the class location and send that to the adapter
            String locationValue = (ds.child("TrainPlace").getValue()).toString();
            location.add(locationValue);
            //Get the image URL
            String imageURL = (ds.child("ImageURL").getValue()).toString();
            image.add(imageURL);
            //Get the Capacity
            String capacity = (ds.child("Capacity").getValue()).toString();
            cap.add(capacity);
            //Get the Number of Registration
            String num = (ds.child("RegisterNum").getValue()).toString();
            regNum.add(num);

            if (classTitle.size() > 0) {

                CustomListForShowClasses adapter = new
                        CustomListForShowClasses(ShowClasses.this, classTitle, image, classID, location,cap,regNum);
                list.setAdapter(adapter);
                //perform a specific action when the user click on a class redirect the user to class's detiales
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    //When they click on an Item
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int number = Integer.parseInt(regNum.get(+position));
                        if ( number == 0) {
                            Toast.makeText(getApplicationContext(), "No user is registered in your class", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ShowClasses.this, "You Clicked at " + classTitle.get(+position) + "\nI took you to View Users", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ShowClasses.this, UserRegInAClass.class);
                            intent.putExtra("Value", classID.get(+position)); //Send the Class Id to the second activity to show the users who are registering in this class
                            startActivity(intent);
                        }
                    }
                });

            }
            else {
                Toast.makeText(ShowClasses.this, "No data to view", Toast.LENGTH_SHORT).show();

            }
        }

    }
}
