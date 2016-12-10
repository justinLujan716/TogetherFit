package com.example.heem.togetherfit;

/**
 * Created by Administrator on 11/16/16.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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


public class StudentRegistrationAdd extends AppCompatActivity {

    private EditText name, age, zipcode,address,city, state;
    private Spinner FitnessType;
    private Button btnSSignUp,uploadImage;
    private FirebaseAuth auth;
    private DatabaseReference mRef;
    private ArrayAdapter<String> adapter;
    String sType;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private ImageView imageView;
    private String imageURL = "https://firebasestorage.googleapis.com/v0/b/togetherfit-148901.appspot.com/o/default-medium.png?alt=media&token=61977a76-af1c-46d1-b350-8f669c2ed993";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration_addtional);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        //load data
        btnSSignUp = (Button) findViewById(R.id.add_button_Trainer);

        name = (EditText) findViewById(R.id.Trainer_Name);
        age = (EditText) findViewById(R.id.Trainer_Age);
        zipcode = (EditText) findViewById(R.id.Trainer_Zipcode);
        FitnessType = (Spinner) findViewById(R.id.Type_spinner);
        address = (EditText) findViewById(R.id.editText7);
        city = (EditText) findViewById(R.id.editText);
        state = (EditText) findViewById(R.id.editText5);
        //create image function
        uploadImage = (Button) findViewById(R.id.button5);
        imageView = (ImageView) findViewById(R.id.imageView);
        mStorage = FirebaseStorage.getInstance().getReference();

        //---------------------------------------------
        //for the spinner part
        String FType[] = {"Yoga", "Flexibility", "Strength", "Balance"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FitnessType.setAdapter(adapter);
        FitnessType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                sType = adapter.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(getApplicationContext(),
                        "Selected Country : " + sType, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        //---------------------------------------------
        btnSSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                String uid = auth.getCurrentUser().getUid();
                mRef = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl("https://togetherfit-148901.firebaseio.com/User");
                DatabaseReference mRefChild = mRef.child(uid);
                DatabaseReference mRefChildEmail2 = mRefChild.child("Name");mRefChildEmail2.setValue(name.getText().toString().trim());
                DatabaseReference mRefChildEmail3 = mRefChild.child("Age");mRefChildEmail3.setValue(age.getText().toString().trim());
                DatabaseReference mRefChildEmail4 = mRefChild.child("ZipCode");mRefChildEmail4.setValue(zipcode.getText().toString().trim());
                DatabaseReference mRefChildEmail5 = mRefChild.child("FitnessType");mRefChildEmail5.setValue(sType);
                DatabaseReference mRefChildEmail8 = mRefChild.child("Address");mRefChildEmail8.setValue(address.getText().toString().trim());
                DatabaseReference mRefChildEmail9 = mRefChild.child("City");mRefChildEmail9.setValue(city.getText().toString().trim());
                DatabaseReference mRefChildEmail10 = mRefChild.child("State");mRefChildEmail10.setValue(state.getText().toString().trim());
                DatabaseReference mRefChildEmail11 = mRefChild.child("imageURL");mRefChildEmail11.setValue(imageURL);
                startActivity(new Intent(StudentRegistrationAdd.this, StudentDashboard.class));
            }
        });
        //personal portrait part
        Picasso.with(StudentRegistrationAdd.this).load(Uri.parse(imageURL)).fit().into(imageView);
        mProgress = new ProgressDialog(this);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, 400);
            }

        });
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
                    Picasso.with(StudentRegistrationAdd.this).load(downloadUri).fit().into(imageView);
                    imageURL = downloadUri.toString().trim();
                    Toast.makeText(StudentRegistrationAdd.this, "upload done, ", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}

