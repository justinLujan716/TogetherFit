package com.example.heem.togetherfit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 12/06/16.
 */

public class SelectChatPerson extends AppCompatActivity {
    private Button sendMessage,back;
    private ImageView portrait;
    private TextView type,name;//first layer
    private TextView viewEmail,viewAge,viewFT,viewL1,viewL2;//second layer
    DatabaseReference mRef;
    //================================================================
    private String portraitURL = "";
    private String uid = "";
    private String currentUid = "";
    private FirebaseAuth auth;
    String username,email,age,fitnesstype,zipcode,userAddress,city,userState,userType;
    ArrayList<String> senderInfo = new ArrayList<String>();
    ArrayList<String> receiverInfo = new ArrayList<String>();
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_chat);
        Intent intent = getIntent();
        uid = (String) intent.getSerializableExtra("One's ID");
        portraitURL = (String) intent.getSerializableExtra("One's Pic");
        auth = FirebaseAuth.getInstance();

        //layer 1
        portrait = (ImageView) findViewById(R.id.imageView3);
        type = (TextView) findViewById(R.id.textView1);
        name = (TextView) findViewById(R.id.textView2);

        //layer 2
        viewEmail = (TextView) findViewById(R.id.textView31);
        viewAge = (TextView) findViewById(R.id.textView32);
        viewFT = (TextView) findViewById(R.id.textView33);
        viewL1 = (TextView) findViewById(R.id.textView34);
        viewL2 = (TextView) findViewById(R.id.textView35);

        //buttons
        sendMessage = (Button) findViewById(R.id.button4);
        back = (Button) findViewById(R.id.button6);


        mRef = FirebaseDatabase.getInstance().getReference().child("User").child(uid);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //personal information part
                username = dataSnapshot.child("Name").getValue().toString();
                userType = dataSnapshot.child("Type").getValue().toString();
                email = dataSnapshot.child("Email").getValue().toString();
                age = dataSnapshot.child("Age").getValue().toString();
                fitnesstype = dataSnapshot.child("FitnessType").getValue().toString();
                zipcode = dataSnapshot.child("ZipCode").getValue().toString();
                userAddress = dataSnapshot.child("Address").getValue().toString();
                city = dataSnapshot.child("City").getValue().toString();
                userState = dataSnapshot.child("State").getValue().toString();
                //store information
                receiverInfo.add(username);
                receiverInfo.add(portraitURL);
                //fill up the layout "activity_before_chat.xml"
                //layer 1
                name.setText(username);
                type.setText(userType);
                //layer 2
                viewEmail.setText(email);
                viewAge.setText(age);
                viewFT.setText(fitnesstype);
                viewL1.setText(userAddress);
                viewL2.setText(city + " " + userState + "\n" + zipcode);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        Picasso.with(SelectChatPerson.this).load(Uri.parse(portraitURL)).fit().into(portrait);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentUid = auth.getCurrentUser().getUid();

                try {
                    Intent intent = new Intent(SelectChatPerson.this, LiveChat.class);
                    myRef = FirebaseDatabase.getInstance().getReference().child("User").child(currentUid);
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            String tempName = (String)snapshot.child("Name").getValue();
                            String tempImage = (String)snapshot.child("imageURL").getValue();
                            senderInfo.add(tempName);
                            senderInfo.add(tempImage);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseErr) {
                            System.out.println("The read failed: " + databaseErr.getCode());
                        }
                    });
                    intent.putExtra("receiver", uid);
                    intent.putExtra("receiverName",receiverInfo.get(0));
                    intent.putExtra("receiverURL",receiverInfo.get(1));
                    intent.putExtra("sender", currentUid);
                    intent.putExtra("senderName",senderInfo.get(0));
                    intent.putExtra("senderURL",senderInfo.get(1));
                    startActivity(intent);
                }catch (Exception e)
                {
                    Toast.makeText(SelectChatPerson.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectChatPerson.this, ChatRoomNew.class));
            }
        });
    }
}
