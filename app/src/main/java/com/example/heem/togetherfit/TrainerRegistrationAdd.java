package com.example.heem.togetherfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TrainerRegistrationAdd extends AppCompatActivity {

    private EditText name, age, zipcode;
    private Spinner FitnessType;
    private Button btnSSignUp;
    private FirebaseAuth auth;
    private DatabaseReference mRef;
    private ArrayAdapter<String> adapter;
    String sType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_registration_additional);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        //load data
        btnSSignUp = (Button) findViewById(R.id.add_button_Trainer);

        name = (EditText) findViewById(R.id.Trainer_Name);
        age = (EditText) findViewById(R.id.Trainer_Age);
        zipcode = (EditText) findViewById(R.id.Trainer_Zipcode);
        FitnessType = (Spinner) findViewById(R.id.Type_spinner);

        //---------------------------------------------
        //for the spinner part
        String FType[] = {"Yoga", "Flexibility", "Strength", "Balance"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FitnessType.setAdapter(adapter);
        FitnessType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                sType = adapter.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(getApplicationContext(),
                        "Selected Country : " + sType, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        //---------------------------------------------
        btnSSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                String uid = auth.getCurrentUser().getUid();
                mRef = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl("https://togetherfit-148901.firebaseio.com/User");
                DatabaseReference mRefChild = mRef.child(uid);
                DatabaseReference mRefChildEmail2 = mRefChild.child("Name");mRefChildEmail2.setValue(name.getText().toString().trim());
                DatabaseReference mRefChildEmail3 = mRefChild.child("Age");mRefChildEmail3.setValue(age.getText().toString().trim());
                DatabaseReference mRefChildEmail4 = mRefChild.child("ZipCode");mRefChildEmail4.setValue(zipcode.getText().toString().trim());
                DatabaseReference mRefChildEmail5 = mRefChild.child("FitnessType");mRefChildEmail5.setValue(sType);
                startActivity(new Intent(TrainerRegistrationAdd.this, TrainerDashboard.class));
            }
        });
    }
}
