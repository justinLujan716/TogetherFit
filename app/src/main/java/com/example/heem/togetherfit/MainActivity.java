package com.example.heem.togetherfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Student Button: When the user click on Student button it take the student to log in screen
        Button sButton = (Button) findViewById(R.id.StudentButton);
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //declare our intent object which takes two parameters, the context and the new activity name
                // the name of the receiving activity is declared in the Intent Constructor
                Intent intent = new Intent(MainActivity.this, StudentLogin.class);
                //start the activity
                startActivity(intent);
            }
        });

        //Trainer Button: When the user click on Trainer button it take the trainer to log in screen
        Button tButton = (Button) findViewById(R.id.TrainerButton);
        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //declare our intent object which takes two parameters, the context and the new activity name
                Intent intent = new Intent(MainActivity.this, TrainerLogin.class);
                //start the activity
                startActivity(intent);
            }
        });
    }


}
