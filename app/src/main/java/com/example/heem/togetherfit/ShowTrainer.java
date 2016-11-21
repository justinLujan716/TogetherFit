package com.example.heem.togetherfit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//V1 Output Very Rough
public class ShowTrainer extends AppCompatActivity {

    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trainer);

        //final ListView list = (ListView) findViewById(R.id.list);

        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://togetherfit-148901.firebaseio.com/User");
        final TextView header = (TextView) findViewById(R.id.Header);
        final TextView output = (TextView) findViewById(R.id.output);

        String zip = getIntent().getStringExtra("Zip"); //input from previous page
        String WorkoutType = getIntent().getStringExtra("WorkoutType"); //input from previous page

        if (zip == null) {
            //Way to give an exception if it isnt found?
            mRef.orderByChild("FitnessType").equalTo(WorkoutType).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getKey();
                    if(dataSnapshot.getValue() != null) {
                        String Result = dataSnapshot.getValue().toString();
                        output.setText(Result);
                    }
                    else{
                        output.setText("Search Criteria Yielded No Results, Please Search Again...");
                    }
                    //Log.d("Trainer", dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("user", databaseError.getMessage());
                }
            });
        }

        if (WorkoutType == null){
            mRef.orderByChild("ZipCode").equalTo(zip).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getKey();
                    if(dataSnapshot.getValue() != null) {
                        String Result = dataSnapshot.getValue().toString();
                        output.setText(Result);
                    }
                    else{
                        output.setText("Search Criteria Yielded No Results, Please Search Again...");
                    }
                    //Log.d("Trainer", dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("user", databaseError.getMessage());
                }
            });
        }

    }
}
