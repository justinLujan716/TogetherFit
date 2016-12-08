package com.example.heem.togetherfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

public class ChatRoomNew extends AppCompatActivity {

    //Database
    DatabaseReference getInfo;
    DatabaseReference allusers;

    //To save info
    ArrayList<String> emails = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();//friend's id
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
        setContentView(R.layout.activity_chat_room_new);

        list = (ListView) findViewById(R.id.listAll);
        list.setAdapter(null); //Make sure the list is null at the beginning before any adapter
        //Access Friend table under current user
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        allusers = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUser);
        getInfo = FirebaseDatabase.getInstance().getReference().child("User");

        //get all friends' id, store into <ArrayList>ids
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

        //Then get full info about the friends from table "User" using ids
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
                                CustomListForCurrentFriends(ChatRoomNew.this, emails, imageURLs, fitnesstypes, usertypes);
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

        // Add friend button
        add = (Button) findViewById(R.id.addFriends);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addpage = new Intent(ChatRoomNew.this, AddFriends.class);
                //start the activity
                startActivity(addpage);
            }
        });

        // click one friend to chat with, open a new chat page
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paret, View viewClicked, int position, long id) {
                Toast.makeText(ChatRoomNew.this, "You will chat with " + emails.get(+position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChatRoomNew.this,SelectChatPerson.class);
                intent.putExtra("One's ID", ids.get(+position));
                intent.putExtra("One's Pic", imageURLs.get(+position));
                startActivity(intent);
            }
        });
    }
}
