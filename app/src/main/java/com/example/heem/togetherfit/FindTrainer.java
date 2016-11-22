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

//Search v1.3 - Nick Boyd

public class FindTrainer extends AppCompatActivity {

    private EditText ByLocation;
    private Button zSearch;
    private Button wtSearch;
    private RadioButton zipBtn;
    private RadioButton workoutTypeBtn;
    private Spinner WorkoutDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find_trainer);
        TextView welcome = (TextView) findViewById(R.id.title);
        welcome.setTextColor(Color.parseColor("#ffffff"));


        ByLocation = (EditText) findViewById(R.id.ZipInput);
        zSearch = (Button) findViewById(R.id.zSearchBtn); //search button for zipcodes
        wtSearch = (Button) findViewById(R.id.wtSearchBtn); //search button for workouttype
        zipBtn = (RadioButton) findViewById(R.id.ZipRadio);
        workoutTypeBtn = (RadioButton) findViewById(R.id.WorkoutTypeRadio);

        //all code in indented section is related to Spinner
        WorkoutDropdown = (Spinner) findViewById(R.id.workoutDropdownBtn);
        List<String> list = new ArrayList<String>();
        list.add("Weight Loss");
        list.add("Weightlifting");
        list.add("Cardio");
        list.add("Other");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);// Create an ArrayAdapter using the string array and a default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// Specify the layout to use when the list of choices appears
        WorkoutDropdown.setAdapter(adapter);// Applying the adapter to the spinner
        //end of spinner stuff

        wtSearch.setBackgroundColor(Color.parseColor("#AFD3DF"));
        zSearch.setBackgroundColor(Color.parseColor("#AFD3DF"));

        zSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Zip = ByLocation.getText().toString();

                if (TextUtils.isEmpty(Zip) || Zip.length()!=5) {
                    Toast.makeText(getApplicationContext(), "Enter A Valid Zip Code", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Intent intent = new Intent(FindTrainer.this, ShowTrainer.class);
                    intent.putExtra("Zip", Zip); //sends Zip to next page
                    startActivity(intent);
                }
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
                findViewById(R.id.ZipInput).setVisibility(View.VISIBLE);
                findViewById(R.id.WorkoutTypeRadio).setVisibility(View.INVISIBLE);
                findViewById(R.id.zSearchBtn).setVisibility(View.VISIBLE);
                return;
            }
        });

        workoutTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.ZipRadio).setVisibility(View.INVISIBLE);
                findViewById(R.id.workoutDropdownBtn).setVisibility(View.VISIBLE);
                findViewById(R.id.wtSearchBtn).setVisibility(View.VISIBLE);
            }
        });

        //***************************************************************
        //              Buttons on the imported header
        //***************************************************************
        //Back button takes back to sign in activity
        //This is the way to refer to outside button from another laytout back button is in header.xml
        View myLayout = findViewById(R.id.backbtnlayout); // root View id from that link
        Button backbutton = (Button) myLayout.findViewById(R.id.backbtn); // id of a view contained in the included file
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // the name of the receiving activity is declared in the Intent Constructor, go back to log in page
                Intent back = new Intent(FindTrainer.this, StudentDashboard.class);
                //start the activity
                startActivity(back);
            }
        });
        //Log out button at the tool bar to take the user to main page
        View myLayout2 = findViewById(R.id.signOut); // root View id from that link
        Button signOut = (Button) myLayout2.findViewById(R.id.signOutbtn); // id of a view contained in the included file
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(FindTrainer.this, StudentLogin.class);
                finish();
                startActivity(logout);
            }
        });


    }
}

