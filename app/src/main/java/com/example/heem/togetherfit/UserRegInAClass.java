package com.example.heem.togetherfit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
    ArrayList<String> userName=new ArrayList<String>();
    ArrayList<String> userEmail = new ArrayList<>();
    ArrayList<String> userImage = new ArrayList<>();
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
                //Make sure lists are empty to avoid duplicate
                userName.clear();
                userEmail.clear();
                userImage.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    userid = ds.getValue().toString();
                    getInfo = FirebaseDatabase.getInstance().getReference().child("User").child(userid);
                    getInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("Name").getValue().toString();
                            String email = dataSnapshot.child("Email").getValue().toString();
                            String image = dataSnapshot.child("imageURL").getValue().toString();
                            userName.add(name);
                            userEmail.add(email);
                            userImage.add(image);



                            if( userName.size() > 0 ){
                                //studentname greater than 0 t least one user to be showed
                                CustomListUsers adapter = new
                                        CustomListUsers(UserRegInAClass.this, userName, userEmail, userImage);
                                listUsers.setAdapter(adapter);
                                //perform a specific action when the user click on a class redirect the user to class's detiales
                                listUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    //When they click on an Item
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        final String studentEmail = userEmail.get(+position);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(UserRegInAClass.this);
                                        builder.setMessage("Do you want to contact the student: " + userName.get(+position))
                                                .setCancelable(true)
                                                .setPositiveButton("Send Email", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Intent intent = new Intent(UserRegInAClass.this, SendEmailThroughAndroid.class);
                                                        intent.putExtra("emailTo", studentEmail);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "No data to show", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }


                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //This is the way to refer to outside button from another laytout back button is in header.xml
        View myLayout = findViewById( R.id.backbtnlayout ); // root View id from that link
        Button backbutton = (Button) myLayout.findViewById( R.id.backbtn ); // id of a view contained in the included file
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // the name of the receiving activity is declared in the Intent Constructor, go back to log in page
                Intent back = new Intent(UserRegInAClass.this, ShowClasses.class);
                //start the activity
                startActivity(back);
            }
        });
        //Log out button at the tool bar to take the user to main page
        View myLayout2 = findViewById( R.id.signOut); // root View id from that link
        Button signOut = (Button) myLayout2.findViewById( R.id.signOutbtn ); // id of a view contained in the included file
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent back = new Intent(UserRegInAClass.this, MainActivity.class);
                //start the activity
                startActivity(back);
            }
        });

    }

}
