package com.example.heem.togetherfit;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StudentLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        TextView text = (TextView) findViewById(R.id.welcomeStudent);
        text.setTextColor(Color.parseColor("#AFD3DF"));
    }
}
