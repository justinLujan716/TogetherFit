package com.example.heem.togetherfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;

public class AddClass extends AppCompatActivity {

    //Variables
    ListView list;
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> type = new ArrayList<String>();
    ArrayList<String> location = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<>();
    View mylayout;
    TextView ClassName;
    //Firebase variable
    DatabaseReference database;
    //Class Id
    ArrayList<String> ClassId = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        // Connect to the Firebase database and access CreatedClass database
        database = FirebaseDatabase.getInstance().getReference().child("CreatedClass");
        mylayout = findViewById(R.id.details);
        ClassName = (TextView) findViewById(R.id.ClassName);
        // Attach a listener to read the data at our posts reference
        database.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                ClassId.add(dataSnapshot.getRef().getKey());
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
                Intent back = new Intent(AddClass.this, StudentDashboard.class);
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
                finish();
            }
        });

    }

    private void getUpdate(DataSnapshot ds)
    {
        //Get the class name and send that to the adapter
        String titleValue = (ds.child("ClassName").getValue()).toString();
        title.add(titleValue);
        //Get teh class type and send that to the adapter
        String typeValue = (ds.child("Type").getValue()).toString();
        type.add(typeValue);
        //Get teh class location and send that to the adapter
        String locationValue = (ds.child("TrainPlace").getValue()).toString();
        //Get the image URL
        String imageURL = (ds.child("ImageURL").getValue()).toString();
        image.add(imageURL);
        location.add(locationValue);
        if (title.size() >0)
        {

            CustomList adapter = new
                    CustomList(AddClass.this, title, image, type,location);
            list=(ListView)findViewById(R.id.listAll);
            list.setAdapter(adapter);
            //perform a specific action when the user click on a class redirect the user to class's detiales
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                //When they click on an Item
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(AddClass.this, "You Clicked at " + title.get(+position) + "\nI took you to class's details", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddClass.this,ClassDetail.class);
                    intent.putExtra("Value", ClassId.get(+position));
                    startActivity(intent);

                }
            });
        }
        else
        {
            Toast.makeText(AddClass.this, "No data to view", Toast.LENGTH_SHORT).show();

        }
    }
}
