package com.example.heem.togetherfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClassDetail extends AppCompatActivity {

    //Firebase variable
    DatabaseReference database;
    DatabaseReference databaseTrainer;
    //Variables
    TextView ClassName;
    TextView TrainerName;
    TextView TrainerEmail;
    TextView ClassLocation;
    TextView ClassDate;
    TextView ClassDay;
    TextView ClassTime;
    TextView ClassType;
    TextView Capacity;
    TextView AgeRange;
    TextView Price;
    CheckBox Agree;
    Button RegBtn;
    String ClassId;
    String trainerId;
    //To manage the registeration
    String NumReg;
    String cap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);
        //To receive a value from Add Class
        Intent intent = getIntent();
        ClassId = (String) intent.getSerializableExtra("Value");
        //Variables
        ClassName = (TextView) findViewById(R.id.ClassName);
        TrainerName = (TextView) findViewById(R.id.TrainerName);
        TrainerEmail = (TextView) findViewById(R.id.TrainerEmail);
        ClassLocation = (TextView) findViewById(R.id.ClassLocation);
        ClassDate = (TextView) findViewById(R.id.ClassDate);
        ClassDay = (TextView) findViewById(R.id.ClassDay);
        ClassType = (TextView) findViewById(R.id.ClassType);
        ClassTime = (TextView) findViewById(R.id.ClassTime);
        Capacity = (TextView) findViewById(R.id.Capacity);
        AgeRange = (TextView) findViewById(R.id.AgeRange);
        Price = (TextView) findViewById(R.id.Price);
        Agree = (CheckBox) findViewById(R.id.Agree);
        RegBtn = (Button) findViewById(R.id.RegBtn);
        RegBtn.setEnabled(false); //To start with disabled until user check the agree checkbox
        //Enable Button if and only if the checkbox is checked
        Agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    RegBtn.setEnabled(true);
                } else {
                    RegBtn.setEnabled(false);
                }
            }
        });

        // Connect to the Firebase database and access CreatedClass database
        database = FirebaseDatabase.getInstance().getReference().child("CreatedClass").child(ClassId);
        databaseTrainer = FirebaseDatabase.getInstance().getReference().child("User");
        // Attach a listener to read the data at Created Class
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getValues(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //Second listener to get the trainer name and email from user table
        databaseTrainer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String TrainerN = dataSnapshot.child(trainerId).child("Name").getValue().toString();
                String TrainerE = dataSnapshot.child(trainerId).child("Email").getValue().toString();
                TrainerName.setText(" " + TrainerN);
                TrainerEmail.setText(" " + TrainerE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
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
                Intent back = new Intent(ClassDetail.this, AddClass.class);
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

        //Click on the reigster button
        RegBtn.setOnClickListener(new View.OnClickListener () {

            @Override
            public void onClick(View v) {

                int reg = Integer.parseInt(NumReg);
                int max = Integer.parseInt(cap);
                if (reg < max)
                {
                    Intent intent = new Intent(ClassDetail.this, ConfirmRegistration.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Class is Full, Try later !", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /*
     * This method to get the values and send them to the fields
     */

    private void getValues(DataSnapshot ds)
    {

        //Values to set
        NumReg = ds.child("RegisterNum").getValue().toString();
        String name = ds.child("ClassName").getValue().toString();
        trainerId = ds.child("Trainer").getValue().toString(); //Just to get the name and email
        String location = ds.child("TrainPlace").getValue().toString();
        //String date = ds.child("").getValue().toString();
        String day = ds.child("WeekDate").getValue().toString();
        String time = ds.child("Time").getValue().toString();
        String fitnessType = ds.child("Type").getValue().toString();
        cap = ds.child("Capacity").getValue().toString();
        String age = ds.child("AgeRange").getValue().toString();
        String price = ds.child("Price").getValue().toString();
        //Send values to text views
        ClassName.setText(" " + name);
        ClassLocation.setText(" " + location);
        ClassDate.setText(" " + "");
        ClassDay.setText(" " + day);
        ClassTime.setText(" " + time);
        ClassType.setText(" " + fitnessType);
        Capacity.setText(" " + cap);
        AgeRange.setText(" " + age);
        Price.setText(" " + price + "$");
    }


}