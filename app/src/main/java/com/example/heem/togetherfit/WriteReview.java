package com.example.heem.togetherfit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class WriteReview extends AppCompatActivity {

    //Variables
    TextView traineremail;
    RatingBar rate;
    EditText comment;
    Button submitbtn;
    Button backbtn;
    //Database
    DatabaseReference user;
    DatabaseReference review;
    String currentUserId;
    //TO get value from previous activity
    String email;
    String id;
    String currentDate;
    //Progressdialog to show while sending review
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        //To receive a value from Show Trainer
        Intent intent = getIntent();
        email = (String) intent.getSerializableExtra("TrainerEmail");
        //Declare the fields
        traineremail = (TextView) findViewById(R.id.traineremail);
        rate = (RatingBar) findViewById(R.id.ratebar);
        comment = (EditText) findViewById(R.id.review);
        submitbtn = (Button) findViewById(R.id.submitbutton);
        backbtn = (Button) findViewById(R.id.backbutton);

        //Set the trainer email in the field
        traineremail.setText("Trainer Email : " + email);
        currentDate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Acces User table in database
                user = FirebaseDatabase.getInstance().getReference().child("User");
                //Get current user id
                currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // Attach a listener to read the data at our posts reference
                user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren())
                        {
                            String localEmail = ds.child("Email").getValue().toString();
                            if (localEmail.equalsIgnoreCase(email))
                            {
                                //If you find the user give his id
                                id = ds.getKey();
                                //Once find the user
                                break;
                            }
                        }

                        review = FirebaseDatabase.getInstance().getReference().child("Review").child(id).child(currentUserId); //add ore create to the database
                        String message = comment.getText().toString();
                        review.child("Comment").setValue(message); //Add the message under the user name under this trainer
                        float rateNum = rate.getRating();
                        String convertRateNum = Float.toString(rateNum);
                        review.child("Rating").setValue(convertRateNum); //Add the rating under the table
                        review.child("Date").setValue(currentDate); //Add the rating under the table


                        //Show loading dialog
                        progressDialog = ProgressDialog.show(WriteReview.this, "Submitting Review",
                                "Wait until storing your review", true);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                // Actions to do after seconds
                                onBackPressed(); //Go back once it submitted
                            }
                        }, 5000);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
