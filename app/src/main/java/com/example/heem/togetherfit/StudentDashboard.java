package com.example.heem.togetherfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentDashboard extends AppCompatActivity {

    //Variables
    Button findplacebtn;
    Button findtrainerbtn;
    private Button launchChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        //Find location button takes to another acitivity
        findplacebtn = (Button) findViewById(R.id.locationtrainerbutton);
        findplacebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // the name of the receiving activity is declared in the Intent Constructor
                Intent intent = new Intent(StudentDashboard.this, FindPlaceStudent.class);
                //start the activity
                startActivity(intent);
            }
        });

        //Find Trainer button takes you to the find a trainer page
        findtrainerbtn = (Button) findViewById(R.id.findtrainerbutton);
        findtrainerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(StudentDashboard.this, FindTrainer.class);
                //start the activity
                startActivity(intent);
            }
        });

        //Back button takes back to sign in activity
        //This is the way to refer to outside button from another laytout back button is in header.xml
        View myLayout = findViewById(R.id.backbtnlayout); // root View id from that link
        Button backbutton = (Button) myLayout.findViewById(R.id.backbtn); // id of a view contained in the included file
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // the name of the receiving activity is declared in the Intent Constructor, go back to log in page
                Intent back = new Intent(StudentDashboard.this, StudentLogin.class);
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
                finish();
            }
        });


        launchChat = (Button) findViewById(R.id.livechatbutton);
        launchChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(StudentDashboard.this, ChatRoom.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
