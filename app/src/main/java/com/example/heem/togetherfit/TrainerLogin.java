package com.example.heem.togetherfit;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TrainerLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_login);

        //To change the color only
        TextView welcome = (TextView) findViewById(R.id.welcomeTrainer);
        welcome.setTextColor(Color.parseColor("lightGray"));
        EditText trainerUserName = (EditText) findViewById(R.id.userNameTrainer);
        trainerUserName.setBackgroundColor(Color.parseColor("#AFD3DF"));
        EditText trainerPass = (EditText) findViewById(R.id.passTrainer);
        trainerPass.setBackgroundColor(Color.parseColor("#AFD3DF"));
        TextView registerLinkTrainer = (TextView) findViewById(R.id.registerLinkTrainer);
        registerLinkTrainer.setTextColor(Color.parseColor("#AFD3DF"));
        //Make the registration word clickable and redirect the user to registration page
        registerLinkTrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainerLogin.this, TrainerRegistration.class);
                //start the activity
                startActivity(intent);
            }
        });
    }
}
