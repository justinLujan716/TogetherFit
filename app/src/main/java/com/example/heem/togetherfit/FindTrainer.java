package com.example.heem.togetherfit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

//Search v1.5 - Nick Boyd - Complete

public class FindTrainer extends AppCompatActivity {

    //Variables
    private Button zSearch;
    private Button wtSearch;
    private RadioButton zipBtn;
    private RadioButton workoutTypeBtn;
    private Spinner WorkoutDropdown;
    private Button searchAgain;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find_trainer);
        TextView welcome = (TextView) findViewById(R.id.title);
        welcome.setTextColor(Color.parseColor("#ffffff"));

        //fields
        zSearch = (Button) findViewById(R.id.zSearchBtn); //search button for zipcodes
        wtSearch = (Button) findViewById(R.id.wtSearchBtn); //search button for workouttype
        zipBtn = (RadioButton) findViewById(R.id.ZipRadio);
        workoutTypeBtn = (RadioButton) findViewById(R.id.WorkoutTypeRadio);
        searchAgain = (Button) findViewById(R.id.redo);
        back = (Button) findViewById(R.id.back);

        //all code in indented section is related to Spinner
        WorkoutDropdown = (Spinner) findViewById(R.id.workoutDropdownBtn);
        List<String> list = new ArrayList<String>();
        list.add("Yoga");
        list.add("Flexibility");
        list.add("Strength");
        list.add("Balance");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);// Create an ArrayAdapter using the string array and a default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// Specify the layout to use when the list of choices appears
        WorkoutDropdown.setAdapter(adapter);// Applying the adapter to the spinner
        //end of spinner stuff

        wtSearch.setBackgroundColor(Color.parseColor("#AFD3DF"));
        zSearch.setBackgroundColor(Color.parseColor("#AFD3DF"));

        zSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When user try to find trainer by location, take the user to map
                Intent intent = new Intent(FindTrainer.this, showtrainermap.class);
                //start the activity
                startActivity(intent);
            }
        });

        wtSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String dropdownValue = String.valueOf(WorkoutDropdown.getSelectedItem());
                Intent intent = new Intent(FindTrainer.this, ShowTrainer.class);
                intent.putExtra("WorkoutType", dropdownValue); //sends WorkoutTpye to next page
                startActivity(intent);
            }
        });

        zipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.WorkoutTypeRadio).setVisibility(View.INVISIBLE);
                findViewById(R.id.zSearchBtn).setVisibility(View.VISIBLE);
                findViewById(R.id.redo).setVisibility(View.VISIBLE);
                findViewById(R.id.back).setVisibility(View.INVISIBLE);
                return;
            }
        });

        workoutTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.ZipRadio).setVisibility(View.INVISIBLE);
                findViewById(R.id.workoutDropdownBtn).setVisibility(View.VISIBLE);
                findViewById(R.id.wtSearchBtn).setVisibility(View.VISIBLE);
                findViewById(R.id.redo).setVisibility(View.VISIBLE);
                findViewById(R.id.back).setVisibility(View.INVISIBLE);
            }
        });

        searchAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent again = new Intent(FindTrainer.this, FindTrainer.class);
                startActivity(again);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(FindTrainer.this, StudentDashboard.class);
                startActivity(back);
            }
        });

    }
}

