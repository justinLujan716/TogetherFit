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
import android.widget.TextView;
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
    private FirebaseAuth auth;private DatabaseReference mRef;


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
                if ((!email.equalsIgnoreCase(""))&&(!password.equalsIgnoreCase(""))){
                    Intent intent = new Intent(getApplicationContext(),TrainerRegistrationAdd.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Don't leave it blank", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }
}
