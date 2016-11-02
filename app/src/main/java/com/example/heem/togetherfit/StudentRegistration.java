package com.example.heem.togetherfit;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class StudentRegistration extends AppCompatActivity {

        // Variable Declaration should be in onCreate()
        private Button mSubmit;
        private Button mCancel;

        private EditText tFname;
        private EditText tLname;
        private EditText tUsername;
        private EditText tPassword;
        private EditText tEmail;
        private Spinner tGender;
        private String tDoB;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_trainer_registration);
            EditText trainerUser = (EditText) findViewById(R.id.userEmailTrainer2);
            trainerUser.setBackgroundColor(Color.parseColor("#AFD3DF"));

        }
    }
