package com.example.heem.togetherfit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//---------------------------
//This part from Jingyu Wang
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//-----------------------

public class TrainerRegistration extends AppCompatActivity {


    private EditText inputEmail, inputPassword;
    private Button btnSSignUp;
    private FirebaseAuth auth;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_registration);
        //---------------------------------------------

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        //load data
        btnSSignUp = (Button) findViewById(R.id.sign_up_button_Trainer);
        inputEmail = (EditText) findViewById(R.id.userEmailTrainer);
        inputPassword = (EditText) findViewById(R.id.userPassTrainer);
        //---------------------------------------------

        btnSSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

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
                }

                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(TrainerRegistration.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(TrainerRegistration.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(TrainerRegistration.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    //create another database table into User, a personal file table
                                    String uid = auth.getCurrentUser().getUid();
                                    String uemail = auth.getCurrentUser().getEmail();
                                    mRef = FirebaseDatabase.getInstance()
                                            .getReferenceFromUrl("https://togetherfit-148901.firebaseio.com/User");
                                    DatabaseReference mRefChild = mRef.child(uid);
                                    DatabaseReference mRefChildEmail = mRefChild.child("Type");
                                    mRefChildEmail.setValue("trainer");
                                    DatabaseReference mRefChildEmail1 = mRefChild.child("Email");
                                    mRefChildEmail1.setValue(uemail);
                                    //link to Dashboard with authentication
                                    startActivity(new Intent(TrainerRegistration.this, TrainerRegistrationAdd.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }
}
