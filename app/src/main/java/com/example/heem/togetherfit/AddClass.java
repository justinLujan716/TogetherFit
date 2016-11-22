package com.example.heem.togetherfit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
    //Firebase variable
    DatabaseReference database;


    Integer[] imageId = {
            R.drawable.gym,
            R.drawable.gym,
            R.drawable.gym,
            R.drawable.gym,
            R.drawable.gym,
            R.drawable.gym,
            R.drawable.gym
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
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
        location.add(locationValue);
        if (title.size() >0)
        {
            CustomList adapter = new
                    CustomList(AddClass.this, title, imageId, type,location);
            list=(ListView)findViewById(R.id.listAll);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                //When they click on an Item
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(AddClass.this, "You Clicked at " + title.get(+position) + "\nI am taking you to the details page", Toast.LENGTH_SHORT).show();


                }
            });

        }
        else
        {
            Toast.makeText(AddClass.this, "No data to view", Toast.LENGTH_SHORT).show();
        }
    }


}
