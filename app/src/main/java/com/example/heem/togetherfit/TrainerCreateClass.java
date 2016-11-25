package com.example.heem.togetherfit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 11/16/16.
 */


public class TrainerCreateClass extends AppCompatActivity {

    private EditText className, weekDate, timeFrom, timeTo, ageRange, price, capacity;
    private Button btn,btn2, uploadImage;
    private FirebaseAuth auth;
    private DatabaseReference mRef;
    private String placeNameBack = "";
    private Spinner WorkoutDropdown;
    //private Spinner FitnessType;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private ImageView imageView;
    private String imageURL = "";
    private String trainType = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        //load data
        btn = (Button) findViewById(R.id.button2);
        btn2 = (Button) findViewById(R.id.add_Place);
        uploadImage = (Button) findViewById(R.id.button3);
        className = (EditText) findViewById(R.id.creatClassContent);
        weekDate = (EditText) findViewById(R.id.editTextDate);
        timeFrom = (EditText) findViewById(R.id.editTextFromTime);
        timeTo = (EditText) findViewById(R.id.editTextToTime);
        ageRange = (EditText) findViewById(R.id.editTextageRange);
        price = (EditText) findViewById(R.id.editTextDes);
        capacity = (EditText) findViewById(R.id.editText6);
        imageView = (ImageView) findViewById(R.id.imageView2);
        //create image upload
        mStorage = FirebaseStorage.getInstance().getReference();
        //for the spinner
        WorkoutDropdown = (Spinner) findViewById(R.id.Type_Spinner_2);
        List<String> list = new ArrayList<String>();
        list.add("Weight Loss");
        list.add("Weightlifting");
        list.add("Cardio");
        list.add("Other");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);// Create an ArrayAdapter using the string array and a default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// Specify the layout to use when the list of choices appears
        WorkoutDropdown.setAdapter(adapter);

        mProgress = new ProgressDialog(this);

        WorkoutDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                trainType = adapter.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(getApplicationContext(),
                        "Selected Country : " + trainType, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = auth.getCurrentUser().getUid();
                try {
                    mRef = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://togetherfit-148901.firebaseio.com/CreatedClass");
                    //DatabaseReference mRefChild = mRef.child(className.getText().toString().trim());
                    String key = mRef.push().getKey();
                    DatabaseReference mRefChild = mRef.child(key);
                    DatabaseReference mRefChildEmail0 = mRefChild.child("ClassName");
                    mRefChildEmail0.setValue(className.getText().toString().trim());
                    DatabaseReference mRefChildEmail1 = mRefChild.child("Trainer");
                    mRefChildEmail1.setValue(uid);
                    DatabaseReference mRefChildEmail2 = mRefChild.child("WeekDate");
                    mRefChildEmail2.setValue(weekDate.getText().toString().trim());
                    DatabaseReference mRefChildEmail3 = mRefChild.child("Time");
                    mRefChildEmail3.setValue((timeFrom.getText().toString().trim() + " to " + timeTo.getText().toString().trim()));
                    DatabaseReference mRefChildEmail35 = mRefChild.child("AgeRange");
                    mRefChildEmail35.setValue(ageRange.getText().toString().trim());
                    DatabaseReference mRefChildEmail4 = mRefChild.child("Price");
                    mRefChildEmail4.setValue(price.getText().toString().trim());
                    DatabaseReference mRefChildEmail5 = mRefChild.child("Capacity");
                    mRefChildEmail5.setValue(capacity.getText().toString().trim());
                    DatabaseReference mRefChildEmail6 = mRefChild.child("TrainPlace");
                    mRefChildEmail6.setValue(placeNameBack);
                    DatabaseReference mRefChildEmail7 = mRefChild.child("ImageURL");
                    mRefChildEmail7.setValue(imageURL);
                    DatabaseReference mRefChildEmail8 = mRefChild.child("Type");
                    mRefChildEmail8.setValue(trainType);
                }catch (Exception e)
                {
                    Toast.makeText(TrainerCreateClass.this, "Failed to create a class", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(TrainerCreateClass.this, TrainerDashboard.class));
            }
        });

        //This method, Add place to class
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TrainerCreateClassAdd.class);
                startActivityForResult(i, 300);
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, 400);
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 300) {
            // Make sure the request was successful
            if (resultCode == 301) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                placeNameBack = data.getStringExtra("placeName");
                // Do something with the contact here (bigger example below)
            }
        }
        if (requestCode == 400 && resultCode == RESULT_OK) {
            mProgress.setMessage("Uploading image ...");
            mProgress.show();
            Uri uri = data.getData();
            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    Picasso.with(TrainerCreateClass.this).load(downloadUri).into(imageView);
                    imageURL = downloadUri.toString().trim();
                    Toast.makeText(TrainerCreateClass.this, "upload done, ", Toast.LENGTH_LONG).show();
                }
            });

        }
        //Toast.makeText(TrainerCreateClass.this, "message comes back", Toast.LENGTH_LONG).show();
    }
}
