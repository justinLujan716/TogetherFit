package com.example.heem.togetherfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class UserRegInAClass extends AppCompatActivity {

    //Variables
    String ClassId;
    ListView listUsers;
    String userid;
    ArrayAdapter<String> adapter;
    ArrayList<String> userInfo=new ArrayList<String>();
    DatabaseReference getUsers;
    DatabaseReference getInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg_in_aclass);
        //List View
        listUsers = (ListView) findViewById(R.id.userList);
        listUsers.setAdapter(null); //To make sure is null at the beginning
        //To receive a value from Show Classes Class
        Intent intent = getIntent();
        ClassId = (String) intent.getSerializableExtra("Value");
        getUsers = FirebaseDatabase.getInstance().getReference().child("CreatedClass").child(ClassId).child("List");
        // Attach a listener to read the data at Created Class , List of Users
        getUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    userid = ds.getValue().toString();
                    getInfo = FirebaseDatabase.getInstance().getReference().child("User").child(userid);
                    // Attach a listener to read the data at User , to get user email and name by passing the id
                    getInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot ds) {

                            String name = ds.child("Name").getValue().toString();
                            String email = ds.child("Email").getValue().toString();
                            userInfo.add(name + "\t" + email);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        adapter = (new ArrayAdapter<String>(UserRegInAClass.this, android.R.layout.simple_list_item_1, userInfo));
        listUsers.setAdapter(adapter);
    }
}
