package com.example.heem.togetherfit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class StudentRegistration extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private Button btnSSignUp;
    private FirebaseAuth auth;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_reigsteration);
        //---------------------------------------------
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        //load data
        btnSSignUp = (Button) findViewById(R.id.sign_up_button_Student);
        inputEmail = (EditText) findViewById(R.id.userEmailStudent);
        inputPassword = (EditText) findViewById(R.id.userPassStudent);
        //---------------------------------------------

        btnSSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
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
                        .addOnCompleteListener(StudentRegistration.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(StudentRegistration.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(StudentRegistration.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    String uid = auth.getCurrentUser().getUid();
                                    String uemail = auth.getCurrentUser().getEmail();
                                    mRef = FirebaseDatabase.getInstance()
                                            .getReferenceFromUrl("https://togetherfit-148901.firebaseio.com/User");
                                    DatabaseReference mRefChild = mRef.child(uid);
                                    DatabaseReference mRefChildEmail = mRefChild.child("Type");
                                    mRefChildEmail.setValue("student");
                                    DatabaseReference mRefChildEmail1 = mRefChild.child("Email");
                                    mRefChildEmail1.setValue(uemail);
                                    startActivity(new Intent(StudentRegistration.this, StudentRegistrationAdd.class));
                                    finish();
                                }
                            }
                        });


            }
        });
    }

}

