package com.example.heem.togetherfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class LiveChat extends AppCompatActivity {

    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_conversation;

    private String sender,senderName,senderURL,receiver,receiverName,receiverURL, room_name;

    private DatabaseReference nameRef,senderRef1, senderRef2, receiverRef1, receiverRef2;
    private String temp_key;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat);

        btn_send_msg = (Button) findViewById(R.id.btn_send);
        input_msg = (EditText) findViewById(R.id.msg_input);
        chat_conversation = (TextView) findViewById(R.id.textView);

        sender = (String)getIntent().getExtras().get("sender");
        senderName = (String)getIntent().getExtras().get("senderName");
        senderURL = (String)getIntent().getExtras().get("senderURL");

        receiver = (String)getIntent().getExtras().get("receiver");
        receiverName = (String)getIntent().getExtras().get("receiverName");
        receiverURL = (String)getIntent().getExtras().get("receiverURL");


        room_name = (String)getIntent().getExtras().get("sender");
        setTitle(" Chatting With - " + receiverName);

        senderRef1 = FirebaseDatabase.getInstance().getReference("Chat").child(sender);
        receiverRef1 = senderRef1.child(receiver);

        senderRef2 = FirebaseDatabase.getInstance().getReference("Chat").child(receiver);
        receiverRef2 = senderRef2.child(sender);

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = receiverRef1.push().getKey();
                receiverRef1.updateChildren(map);
                receiverRef2.updateChildren(map);

                DatabaseReference message_root1 = receiverRef1.child(temp_key);
                DatabaseReference message_root2 = receiverRef2.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name",senderName);
                map2.put("msg",input_msg.getText().toString());

                message_root1.updateChildren(map2);
                message_root2.updateChildren(map2);
                input_msg.setText("");
            }
        });

        receiverRef1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Back button takes back to sign in activity
        //This is the way to refer to outside button from another laytout back button is in header.xml
        View myLayout = findViewById(R.id.backbtnlayout); // root View id from that link
        Button backbutton = (Button) myLayout.findViewById(R.id.backbtn); // id of a view contained in the included file
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent back = new Intent(LiveChat.this, .class);
                //start the activity
                //startActivity(back);
                finish();
            }
        });
        //Log out button at the tool bar to take the user to main page
        View myLayout2 = findViewById(R.id.signOut); // root View id from that link
        Button signOut = (Button) myLayout2.findViewById(R.id.signOutbtn); // id of a view contained in the included file
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent back = new Intent(LiveChat.this, MainActivity.class);
                //start the activity
                startActivity(back);
            }
        });

    }

    private String chat_msg,chat_user_name;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();
            chat_conversation.append(chat_user_name +" : "+chat_msg +" \n");
        }


    }
}