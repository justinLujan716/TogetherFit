package com.example.heem.togetherfit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StudentLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        //Changes the color (Control the style)
        TextView welcome = (TextView) findViewById(R.id.welcomeStudent);
        welcome.setTextColor(Color.parseColor("#ffffff"));
        Button studentLogin = (Button) findViewById(R.id.loginStudent);
        studentLogin.setBackgroundColor(Color.parseColor("#AFD3DF"));
        TextView registerLinkStudent = (TextView) findViewById(R.id.registerLinkStudent);
        registerLinkStudent.setTextColor(Color.parseColor("#AFD3DF"));

        //Make the registration word clickable and redirect the user to registration page
        registerLinkStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLogin.this, StudentRegistration.class);
                //start the activity
                startActivity(intent);
            }
        });


    }
}
