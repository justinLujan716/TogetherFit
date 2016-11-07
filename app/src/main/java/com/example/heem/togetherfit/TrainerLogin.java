package com.example.heem.togetherfit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class TrainerLogin extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin;

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


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //declare our intent object which takes two parameters, the context and the new activity name
                startActivity(new Intent(TrainerLogin.this, TrainerRegistration.class));
            }
        });
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
                                    Intent intent = new Intent(TrainerLogin.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });


    }
}
