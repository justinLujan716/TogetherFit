package com.example.heem.togetherfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class ClassesRegisteredForUser extends AppCompatActivity {

    //Variables
    ArrayAdapter<String> adapter;
    ListView list;
    DatabaseReference classes;
    ArrayList<String> className=new ArrayList<>();
    ArrayList<String> classLocaiton = new ArrayList<>();
    ArrayList<String> classImage = new ArrayList<>();
    ArrayList<String> classTime = new ArrayList<>();
    ArrayList<String> classDays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes_registered_for_user);

        //List View
        list = (ListView) findViewById(R.id.listAll);
        list.setAdapter(null); //To make sure is null at the beginning

        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        classes = FirebaseDatabase.getInstance().getReference().child("CreatedClass");
        classes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Make sure lists are empty to avoid duplicate
                className.clear();
                classLocaiton.clear();
                classImage.clear();
                classTime.clear();
                classDays.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot data: ds.child("List").getChildren())
                    {
                        String holdId = data.getValue().toString();
                        if (holdId.equalsIgnoreCase(currentUser))
                        {
                            //iterate to the class once oyu find the user is registered in
                            String classNamelocal = ds.child("ClassName").getValue().toString();
                            String classLocationlocal = ds.child("TrainPlace").getValue().toString();
                            String classTimelocal = ds.child("Time").getValue().toString();
                            String classImagelocal = ds.child("ImageURL").getValue().toString();
                            String classDaylocal = ds.child("WeekDate").getValue().toString();
                            className.add(classNamelocal);
                            classLocaiton.add(classLocationlocal);
                            classTime.add(classTimelocal);
                            classImage.add(classImagelocal);
                            classDays.add(classDaylocal);

                        }

                    }

                    if( className.size() > 0 ) {
                        //student name greater than 0 t least one user to be showed
                        CustomListForShowClassesRegisteredForUser adapter = new
                                CustomListForShowClassesRegisteredForUser(ClassesRegisteredForUser.this, className, classImage, classLocaiton, classTime, classDays);
                        list.setAdapter(adapter);
                    }
                    else //when the user is not register in any class
                    {
                        Toast.makeText(getApplicationContext(),"You are not registered in any class",Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Back button takes back to sign in activity
        //This is the way to refer to outside button from another laytout back button is in header.xml
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
                Intent back = new Intent(ClassesRegisteredForUser.this, MainActivity.class);
                //start the activity
                startActivity(back);
            }
        });

    }
}