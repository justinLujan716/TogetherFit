package com.example.heem.togetherfit;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TrainerLogin extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnSignup, btnLogin;
    private DatabaseReference data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_trainer_login);
        TextView welcome = (TextView) findViewById(R.id.welcomeTrainer);
        welcome.setTextColor(Color.parseColor("#ffffff"));

        inputEmail = (EditText) findViewById(R.id.userNameTrainer);
        inputPassword = (EditText) findViewById(R.id.passTrainer);
        btnLogin = (Button) findViewById(R.id.log_in_button);
        btnSignup = (Button) findViewById(R.id.Reg_button);

        btnLogin.setBackgroundColor(Color.parseColor("#AFD3DF"));
        btnSignup.setBackgroundColor(Color.parseColor("#AFD3DF"));

        ActivityManager activityManager;
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (!(activityManager.isUserAMonkey())) {
            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //declare our intent object which takes two parameters, the context and the new activity name
                    startActivity(new Intent(TrainerLogin.this, TrainerRegistration.class));
                }
            });
        }
        if (!(activityManager.isUserAMonkey())) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = inputEmail.getText().toString();
                    final String password = inputPassword.getText().toString();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //authenticate user
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(TrainerLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.

                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {
                                            inputPassword.setError("More than 6 characters");
                                        } else {
                                            Toast.makeText(TrainerLogin.this, "log in filed", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        String Tuid = auth.getCurrentUser().getUid().trim();
                                        checkType(Tuid);//Check the login use's type
                                    }
                                }
                            });
                }
            });
        }
        else //If it is monkey
        {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            String emaillocal = "hy30305340@gmail.com";
            final String passlocal = "123321";
            //authenticate user
            auth.signInWithEmailAndPassword(emaillocal, passlocal)
                    .addOnCompleteListener(TrainerLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.

                            if (!task.isSuccessful()) {
                                // there was an error
                                if (passlocal.length() < 6) {
                                    inputPassword.setError("More than 6 characters");
                                } else {
                                    Toast.makeText(TrainerLogin.this, "log in filed", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                String Tuid = auth.getCurrentUser().getUid().trim();
                                checkType(Tuid);//Check the login use's type
                            }
                        }
                    });

                }
            });
        }
    }

    public void checkType(final String Tuid){
        DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://togetherfit-148901.firebaseio.com/User");
        DatabaseReference databaseRef = database.child(Tuid).child("Type");

        // Attach a listener to read the data at our posts reference
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String CurrentType = (String)snapshot.getValue();
                if (CurrentType.equals("trainer")){
                    startActivity(new Intent(TrainerLogin.this, TrainerDashboard.class));}
                else {
                    Toast.makeText(TrainerLogin.this, "You are not a trainer, take you to student dashboard", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TrainerLogin.this, StudentDashboard.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseErr) {
                System.out.println("The read failed: " + databaseErr.getCode());
            }
        });
    }
}
