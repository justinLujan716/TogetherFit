package com.example.heem.togetherfit;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* Student & Trainer Side:
 * Show all users who are not in the current user's firned list and allow the user to add a new user as a friend
 */
public class AddFriends extends AppCompatActivity {

    //Database Reference
    DatabaseReference allusers;
    DatabaseReference getInfo;
    //Variables
    ArrayList<String> email = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    ArrayList<String> imageURL = new ArrayList<>();
    ArrayList<String> fitnesstype = new ArrayList<>();
    ArrayList<String> usertype = new ArrayList<>();
    ArrayList<String> currentFriends = new ArrayList<>();
    ListView list;
    EditText searchUser;
    CustomListForAddFriends adapter; //ArrayAdapter
    String emailCurrentUSer = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        searchUser = (EditText) findViewById(R.id.searchUser);
        list = (ListView) findViewById(R.id.listAll);
        list.setAdapter(null); //Make sure the list is null at the beginning before any adapter

        //get current user id, because user shoudl not its self !
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        allusers = FirebaseDatabase.getInstance().getReference().child("Friends");
        getInfo = FirebaseDatabase.getInstance().getReference().child("User");

                //First iterate through the friend list for this user
                allusers.child(currentUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Variables
                        email.clear();
                        ids.clear();
                        imageURL.clear();
                        fitnesstype.clear();
                        usertype.clear();
                        currentFriends.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String id = ds.getKey().toString();
                            currentFriends.add(id);//Add Id to the list
                        }
                        getInfo.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //Store current user email
                                emailCurrentUSer = dataSnapshot.child(currentUser).child("Email").getValue().toString();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                    //Iterate through the current user and make sure it is not there
                                    boolean flag = false;
                                    for (String user: currentFriends)
                                    {
                                        if (ds.getKey().toString().equalsIgnoreCase(user)) {
                                            flag = true;
                                        }
                                    }
                                    //List all users except the current friends and user it self
                                    if (ds.getKey().toString().equalsIgnoreCase(currentUser) || flag == true) { //Either current user or current friend
                                        continue;
                                    } else //You find a new user
                                    {
                                        email.add(ds.child("Email").getValue().toString());
                                        ids.add(ds.getKey().toString());//To record later when the user add a new user
                                        imageURL.add(ds.child("imageURL").getValue().toString());
                                        fitnesstype.add(ds.child("FitnessType").getValue().toString());
                                        usertype.add(ds.child("Type").getValue().toString());
                                    }
                                }


                                adapter = new
                                        CustomListForAddFriends(AddFriends.this, email, imageURL, fitnesstype, usertype);
                                list.setAdapter(adapter);

                                //perform a specific action when the user click on a class redirect the user to class's details
                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    //When they click on an Item
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        //Show dialaog to ask the user yes or no to add a friend
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddFriends.this);
                                        builder.setMessage("Are you sure you want to add " + email.get(+position) + "?")
                                                .setCancelable(true)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //Now add the user under the current use table
                                                        allusers.child(currentUser).child(ids.get(+position)).setValue(email.get(+position));
                                                        //Also add the current user under the new friend, In future we can do this part private
                                                        allusers.child(ids.get(+position)).child(currentUser).setValue(emailCurrentUSer);
                                                        //all users.child(currentUser).child(ids.get(+position)).child("UserName").setValue(email.get(+position));
                                                        Toast.makeText(getApplicationContext(), "You have successfully add " + email.get(+position), Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss(); //Cancel
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();//show daialog

                                    }
                                });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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
                onBackPressed();
            }
        });
        //Log out button at the tool bar to take the user to main page
        View myLayout2 = findViewById(R.id.signOut); // root View id from that link
        Button signOut = (Button) myLayout2.findViewById(R.id.signOutbtn); // id of a view contained in the included file
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent back = new Intent(AddFriends.this, MainActivity.class);
                //start the activity
                startActivity(back);
            }
        });


        searchUser = (EditText) findViewById(R.id.searchUser) ;
        searchUser.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //To update every-time
                adapter = new
                        CustomListForAddFriends(AddFriends.this, email, imageURL, fitnesstype, usertype);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                AddFriends.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub


            }
        });
    }
}
