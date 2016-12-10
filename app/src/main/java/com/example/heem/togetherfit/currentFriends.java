package com.example.heem.togetherfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class currentFriends extends AppCompatActivity {

    //Database
    DatabaseReference getInfo;
    DatabaseReference allusers;

    //To save info
    ArrayList<String> emails = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    ArrayList<String> imageURLs = new ArrayList<>();
    ArrayList<String> fitnesstypes = new ArrayList<>();
    ArrayList<String> usertypes = new ArrayList<>();


    //Fields
    ListView list;
    Button add;
    CustomListForCurrentFriends adapter; //ArrayAdapter


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_friends);

        list = (ListView) findViewById(R.id.listAll);
        list.setAdapter(null); //Make sure the list is null at the beginning before any adapter
        //Access Friend table under current user
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        allusers = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUser);
        getInfo = FirebaseDatabase.getInstance().getReference().child("User");

        allusers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.getKey().toString();
                    ids.add(id);//Add Id to the list
                    String email = ds.getValue().toString();
                    emails.add(email);//Add email to the list
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Now get the full info about the users
        getInfo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (String findUser:ids)
                {
                    String fitnessType = dataSnapshot.child(findUser).child("FitnessType").getValue().toString();
                    fitnesstypes.add(fitnessType);
                    String imageURL = dataSnapshot.child(findUser).child("imageURL").getValue().toString();
                    imageURLs.add(imageURL);
                    String type = dataSnapshot.child(findUser).child("Type").getValue().toString();
                    usertypes.add(type);
                    if(ids.size()>0) //That means user has at least one friend
                    {
                        //Pass Values to the list
                        adapter = new
                                CustomListForCurrentFriends(currentFriends.this, emails, imageURLs, fitnesstypes, usertypes);
                        list.setAdapter(adapter);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"You do not have any friend, click to add friend",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //Back button takes back to sign in activity
        //This is the way to refer to outside button from another layout back button is in header.xml
        View myLayout = findViewById(R.id.backbtnlayout); // root View id from that link
        Button backbutton = (Button) myLayout.findViewById(R.id.backbtn); // id of a view contained in the included file
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        //Log out button at the tool bar to take the user to main page
        View myLayout2 = findViewById(R.id.signOut); // root View id from that link
        Button signOut = (Button) myLayout2.findViewById(R.id.signOutbtn); // id of a view contained in the included file
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent back = new Intent(currentFriends.this, MainActivity.class);
                //start the activity
                startActivity(back);
            }
        });

        add = (Button) findViewById(R.id.addFriends);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addpage = new Intent(currentFriends.this, AddFriends.class);
                //start the activity
                startActivity(addpage);
            }
        });
    }
}
