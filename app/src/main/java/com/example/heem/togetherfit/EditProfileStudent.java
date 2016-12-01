package com.example.heem.togetherfit;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//allows the Trainer to edit the info in their profile

public class EditProfileStudent extends AppCompatActivity {

    TextView title;
    TextView info;
    EditText newName;
    EditText newLocation;
    Spinner WorkoutDropdown;
    Button backbtn;
    Button submit;
    EditText newage;

    //private DatabaseReference mRef;
    //DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("User");
    //private FirebaseAuth auth;


    //Variables
    String username = "";
    String email = "";
    String age = "";
    String fitnesstype = "";
    String zipcode = "";
    String currentUId =  "";
    //Firebase reference
    DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_student);

        title = (TextView) findViewById(R.id.profile);
        title.setPaintFlags(title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        newName = (EditText) findViewById(R.id.newname);
        newLocation = (EditText) findViewById(R.id.newlocation);
        info = (TextView) findViewById(R.id.InfoView);
        backbtn = (Button) findViewById(R.id.back);
        submit = (Button) findViewById(R.id.submit);
        newage = (EditText) findViewById(R.id.newage);

        WorkoutDropdown = (Spinner) findViewById(R.id.workoutDropdownBtn);
        List<String> list = new ArrayList<String>();
        list.add("Yoga");
        list.add("Flexibilitiy");
        list.add("Strength");
        list.add("Balance");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);// Create an ArrayAdapter using the string array and a default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// Specify the layout to use when the list of choices appears
        WorkoutDropdown.setAdapter(adapter);// Applying the adapter to the spinner

        //inside onClick()
        currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Once you got the current user id , you access User table in database to get the user info and send them to the fields
        database = FirebaseDatabase.getInstance().getReference().child("User"); //This is how we access the table
        //Now implement these methods
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child(currentUId).child("Name").getValue().toString();
                email = dataSnapshot.child(currentUId).child("Email").getValue().toString();
                age = dataSnapshot.child(currentUId).child("Age").getValue().toString();
                fitnesstype = dataSnapshot.child(currentUId).child("FitnessType").getValue().toString();
                zipcode = dataSnapshot.child(currentUId).child("ZipCode").getValue().toString();

                String userinfo = "Student" + "\n" + "Name: " + username + "\n" + "Email: " + email + "\n" + "Age: " + age + "\n" + "Fitness Type: " + fitnesstype + "\n" + "Location: " + zipcode + "\n";
                info.setText(userinfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileStudent.this, StudentDashboard.class);
                //start the activity
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = newLocation.getText().toString();
                String newUserName = newName.getText().toString();
                String dropdownValue = String.valueOf(WorkoutDropdown.getSelectedItem());
                String newAge = newage.getText().toString();

                if(!newUserName.equals("")) {
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("User").child(currentUId).child("Name");
                    database.setValue(newUserName);
                }
                if( (!TextUtils.isEmpty(location)) && (location.length()==5)){
                    FirebaseDatabase.getInstance().getReference().child("User").child(currentUId).child("ZipCode").setValue(location);
                }
                FirebaseDatabase.getInstance().getReference().child("User").child(currentUId).child("FitnessType").setValue(dropdownValue);

                if( (!TextUtils.isEmpty(newAge) && (newAge.length()<3))){
                    FirebaseDatabase.getInstance().getReference().child("User").child(currentUId).child("Age").setValue(newAge);
                }


            }
        });

    }
}
