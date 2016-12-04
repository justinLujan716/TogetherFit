package com.example.heem.togetherfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReadReviews extends AppCompatActivity {


    //Variables
    ArrayList<String> UserName;
    ArrayList<String> Comment;
    ArrayList<String> Rate;
    ArrayList<String> Date;
    ArrayList<String> UserId; //Get first user id to find their names
    //Variables
    String email = ""; //Trainer email from previous activity
    String id = ""; //To get Trainer id
    //Database
    DatabaseReference user;
    DatabaseReference review;
    ListView list;
    TextView showEmail;
    float keeptrackofrating;
    int i; //To count how many user have rated

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_reviews);
        //listview
        list = (ListView) findViewById(R.id.listAll);
        list.setAdapter(null); //to make sure the list view is empty

        //Initialize
        UserName = new ArrayList<>();
        Comment = new ArrayList<>();
        Rate = new ArrayList<>();
        Date = new ArrayList<>();
        UserId = new ArrayList<>();
        keeptrackofrating = 0;
        i = 0;
        //To receive a value from Show Trainer
        Intent intent = getIntent();
        email = (String) intent.getSerializableExtra("traineremail");

        //Access User table in database to get traine rid
        user = FirebaseDatabase.getInstance().getReference().child("User");
        // Attach a listener to read the data at our posts reference
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String localEmail = ds.child("Email").getValue().toString();
                    if (localEmail.equalsIgnoreCase(email)) {
                        //If you find the user give his id
                        id = ds.getKey();
                        //Once find the user
                        break;
                    }
                }

            }
                @Override
                public void onCancelled (DatabaseError databaseError){

                }
            });


        //Access Review Table Under This Trainer
        review = FirebaseDatabase.getInstance().getReference().child("Review").child(id);
        // Attach a listener to read the data at our posts reference
        review.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                //Access each child under this user who has wrriten a review
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (id.equalsIgnoreCase(ds.getKey())) {
                        for (DataSnapshot single : ds.getChildren()) {
                            String localid = single.getKey();
                            String localcomment = (String) single.child("Comment").getValue();
                            String localrate = (String) single.child("Rating").getValue();
                            String localdate = (String) single.child("Date").getValue();
                            //Add them to the lists
                            UserId.add(localid);
                            Comment.add(localcomment);
                            Rate.add(localrate);
                            Date.add(localdate);
                            //convert the rating to number
                            float num = Float.parseFloat(localrate);
                            keeptrackofrating = keeptrackofrating + num;
                            i++;
                        }
                    }
                }

                //Now get user name form User table
                // Attach a listener to read the data at our posts reference
                user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (String uid : UserId) { //For each id in list find the user name
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String localid = ds.getKey();
                                if (localid.equalsIgnoreCase(uid)) {
                                    String name = (String) ds.child("Name").getValue();
                                    UserName.add(name);
                                }
                            }
                        }


                        //Just check one of them
                        if (UserName.size() > 0) {
                            CustomListForReadReviews adapter = new
                                    CustomListForReadReviews(ReadReviews.this, UserName, Comment, Rate, Date);
                            //set the adapter
                            list.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void onCancelled (DatabaseError databaseError){

                    }
                });

                float findfinalrate = keeptrackofrating/i; //Devide the total rating by number of reviews
                //Rating bar
                RatingBar rate = (RatingBar) findViewById(R.id.rating);
                rate.setRating(findfinalrate);

            }
            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });

        showEmail = (TextView) findViewById(R.id.tranineremail) ;
        showEmail.setText("Reviews About Trainer (Email): " + email);
        //Back button takes back to sign in activity
        //This is the way to refer to outside button from another layout back button is in header.xml
        View myLayout = findViewById(R.id.backbtnlayout); // root View id from that link
        Button backbutton = (Button) myLayout.findViewById(R.id.backbtn); // id of a view contained in the included file
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // the name of the receiving activity is declared in the Intent Constructor, go back to log in page
                Intent back = new Intent(ReadReviews.this, FindTrainer.class);
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
                FirebaseAuth.getInstance().signOut();
                Intent back = new Intent(ReadReviews.this, MainActivity.class);
                //start the activity
                startActivity(back);
            }
        });
    }
}
