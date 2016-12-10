package com.example.heem.togetherfit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import android.view.View;


import static com.example.heem.togetherfit.R.id.backbtn;


public class ProfileData extends AppCompatActivity {

    private StorageReference mStorage;
    private ProgressDialog mProgress;
    TextView profile;
    Button backBTN;
    Button uploadimg;
    ImageView picture;
    String currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String imageURL;
    //"https://firebasestorage.googleapis.com/v0/b/togetherfit-148901.appspot.com/o/default-medium.png?alt=media&token=61977a76-af1c-46d1-b350-8f669c2ed993";
    private DatabaseReference mRef;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_data);

        mStorage = FirebaseStorage.getInstance().getReference();

        profile = (TextView) findViewById(R.id.output);
        backBTN = (Button) findViewById(R.id.back);
        picture = (ImageView) findViewById(R.id.profilePic);
        uploadimg = (Button) findViewById(R.id.newPic);

        //final String imageURL = getIntent().getStringExtra("image");
        final String data = getIntent().getStringExtra("data");
        profile.setText(data);
        mRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://togetherfit-148901.firebaseio.com/User");


        //Once you got the current user id , you access User table in database to get the user info and send them to the fields
        database = FirebaseDatabase.getInstance().getReference().child("User"); //This is how we access the table
        //Now implement these methods
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageURL = dataSnapshot.child(currentUId).child("imageURL").getValue().toString();
                //personal portrait part
                Picasso.with(ProfileData.this).load(Uri.parse(imageURL)).fit().into(picture);
                mProgress = new ProgressDialog(ProfileData.this);
                uploadimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_PICK);
                        i.setType("image/*");
                        startActivityForResult(i, 400);
                    }

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });




        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*personal portrait part
        Picasso.with(ProfileData.this).load(Uri.parse(imageURL)).into(picture);
        mProgress = new ProgressDialog(this);
        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, 400);
            }

        });
*/
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 400 && resultCode == RESULT_OK) {
            mProgress.setMessage("Uploading image ...");
            mProgress.show();
            Uri uri = data.getData();
            StorageReference filepath = mStorage.child("portrait").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    Picasso.with(ProfileData.this).load(downloadUri).into(picture);
                    imageURL = downloadUri.toString().trim();
                    FirebaseDatabase.getInstance().getReference().child("User").child(currentUId).child("imageURL").setValue(imageURL);
                    Toast.makeText(ProfileData.this, "upload done, ", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}