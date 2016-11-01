package com.example.heem.togetherfit;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StudentLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        //Changes the color
        TextView welcome = (TextView) findViewById(R.id.welcomeStudent);
        welcome.setTextColor(Color.parseColor("lightGray"));
        EditText studentUserName = (EditText) findViewById(R.id.userNameStudent);
        studentUserName.setBackgroundColor(Color.parseColor("#AFD3DF"));
        EditText studentPass = (EditText) findViewById(R.id.passStudent);
        studentPass.setBackgroundColor(Color.parseColor("#AFD3DF"));
        TextView registerLinkStudent = (TextView) findViewById(R.id.registerLinkStudent);
        registerLinkStudent.setTextColor(Color.parseColor("#AFD3DF"));


    }
}
