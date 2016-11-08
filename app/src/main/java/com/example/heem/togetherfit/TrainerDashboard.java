package com.example.heem.togetherfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TrainerDashboard extends AppCompatActivity {

    //Variables
    Button findplacebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_dashboard);

        findplacebtn = (Button) findViewById(R.id.locationtrainerbutton);
        findplacebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // the name of the receiving activity is declared in the Intent Constructor
                Intent intent = new Intent(TrainerDashboard.this, FindPlaceTrainer.class);
                //start the activity
                startActivity(intent);
            }
        });
    }
}
