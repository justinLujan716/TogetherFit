package com.example.heem.togetherfit;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class TrainerLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_login);

        //Changes the color
        TextView welcome = (TextView) findViewById(R.id.welcomeTrainer);
        welcome.setTextColor(Color.parseColor("lightGray"));
        EditText trainerUserName = (EditText) findViewById(R.id.userNameTrainer);
        trainerUserName.setBackgroundColor(Color.parseColor("#AFD3DF"));
        EditText trainerPass = (EditText) findViewById(R.id.passTrainer);
        trainerPass.setBackgroundColor(Color.parseColor("#AFD3DF"));
        TextView registerLinkTrainer = (TextView) findViewById(R.id.registerLinkTrainer);
        registerLinkTrainer.setTextColor(Color.parseColor("#AFD3DF"));
    }
}
