package com.example.heem.togetherfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Administrator on 11/16/16.
 */


public class TrainerCreateClass extends AppCompatActivity {

    private EditText className, weekDate, timeFrom, timeTo, price, capacity;
    private Button btn;
    private FirebaseAuth auth;
    private DatabaseReference mRef;
    //private ArrayAdapter<String> adapter;
    //private Spinner FitnessType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        //load data
        btn = (Button) findViewById(R.id.button2);
        className = (EditText) findViewById(R.id.creatClassContent);
        weekDate = (EditText) findViewById(R.id.editTextDate);
        timeFrom = (EditText) findViewById(R.id.editTextFromTime);
        timeTo = (EditText) findViewById(R.id.editTextToTime);
        price = (EditText) findViewById(R.id.editTextDes);
        capacity = (EditText) findViewById(R.id.editText6);
        //FitnessType = (Spinner) findViewById(R.id.Type_spinner);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = auth.getCurrentUser().getUid();
                try {
                    mRef = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://togetherfit-148901.firebaseio.com/CreatedClass");
                    DatabaseReference mRefChild = mRef.child(className.getText().toString().trim());
                    DatabaseReference mRefChildEmail1 = mRefChild.child("Trainer");
                    mRefChildEmail1.setValue(uid);
                    DatabaseReference mRefChildEmail2 = mRefChild.child("WeekDate");
                    mRefChildEmail2.setValue(weekDate.getText().toString().trim());
                    DatabaseReference mRefChildEmail3 = mRefChild.child("Time");
                    mRefChildEmail3.setValue((timeFrom.getText().toString().trim() + " to " + timeTo.getText().toString().trim()));
                    DatabaseReference mRefChildEmail4 = mRefChild.child("Price");
                    mRefChildEmail4.setValue(price.getText().toString().trim());
                    DatabaseReference mRefChildEmail5 = mRefChild.child("Capacity");
                    mRefChildEmail5.setValue(capacity.getText().toString().trim());
                }catch (Exception e)
                {
                    Toast.makeText(TrainerCreateClass.this, "Failed to create a class", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(TrainerCreateClass.this, TrainerDashboard.class));
            }
        });
    }
}
