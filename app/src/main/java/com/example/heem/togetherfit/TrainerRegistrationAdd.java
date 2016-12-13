package com.example.heem.togetherfit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class TrainerRegistrationAdd extends AppCompatActivity {

    private EditText name, age, zipcode, CerName, CertifiID,address,city, state;
    private Spinner FitnessType;
    private Button btnSSignUp,uploadImage;
    private FirebaseAuth auth;
    private DatabaseReference mRef;
    private ArrayAdapter<String> adapter;
    String sType;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private ImageView imageView;
    private String defimageURL = "https://firebasestorage.googleapis.com/v0/b/togetherfit-148901.appspot.com/o/default-medium.png?alt=media&token=61977a76-af1c-46d1-b350-8f669c2ed993";
    private String imageURL = defimageURL;
    private String email="";
    private String password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_registration_additional);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        //load data
        btnSSignUp = (Button) findViewById(R.id.add_button_Trainer);

        name = (EditText) findViewById(R.id.Trainer_Name);
        age = (EditText) findViewById(R.id.Trainer_Age);
        zipcode = (EditText) findViewById(R.id.Trainer_Zipcode);
        FitnessType = (Spinner) findViewById(R.id.Type_spinner);
        CerName = (EditText) findViewById(R.id.OrganName);
        CertifiID = (EditText) findViewById(R.id.CerID);
        address = (EditText) findViewById(R.id.editText3);
        city = (EditText) findViewById(R.id.editText4);
        state = (EditText) findViewById(R.id.editText2);
        //create image function
        uploadImage = (Button) findViewById(R.id.button5);
        imageView = (ImageView) findViewById(R.id.imageView);
        mStorage = FirebaseStorage.getInstance().getReference();

        //get pass and email
        Intent intent = getIntent();
        email = (String) intent.getSerializableExtra("email");
        password = (String) intent.getSerializableExtra("password");


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

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(TrainerRegistrationAdd.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Toast.makeText(TrainerRegistrationAdd.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                if (!task.isSuccessful()) {
                                    Toast.makeText(TrainerRegistrationAdd.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    //create another database table into User, a personal file table
                                    String uid = auth.getCurrentUser().getUid();
                                    //String uemail = auth.getCurrentUser().getEmail();
                                    mRef = FirebaseDatabase.getInstance()
                                            .getReferenceFromUrl("https://togetherfit-148901.firebaseio.com/User");
                                    DatabaseReference mRefChild = mRef.child(uid);


                                    if (!isEmpty(name) && !isEmpty(age) && !isEmpty(address) && !isEmpty(zipcode) && !isEmpty(CerName)
                                            && !isEmpty(CertifiID) && !isEmpty(city) && !isEmpty(state) &&!imageURL.equalsIgnoreCase(defimageURL)){

                                        DatabaseReference mRefChildEmail = mRefChild.child("Type");
                                        mRefChildEmail.setValue("trainer");
                                        DatabaseReference mRefChildEmail1 = mRefChild.child("Email");
                                        mRefChildEmail1.setValue(email);
                                        DatabaseReference mRefChildEmail2 = mRefChild.child("Name");
                                        mRefChildEmail2.setValue(name.getText().toString().trim());
                                        DatabaseReference mRefChildEmail3 = mRefChild.child("Age");
                                        mRefChildEmail3.setValue(age.getText().toString().trim());
                                        DatabaseReference mRefChildEmail4 = mRefChild.child("ZipCode");
                                        mRefChildEmail4.setValue(zipcode.getText().toString().trim());
                                        DatabaseReference mRefChildEmail5 = mRefChild.child("FitnessType");
                                        mRefChildEmail5.setValue(sType);
                                        DatabaseReference mRefChildEmail6 = mRefChild.child("OrganName");
                                        mRefChildEmail6.setValue(CerName.getText().toString().trim());
                                        DatabaseReference mRefChildEmail7 = mRefChild.child("CerID");
                                        mRefChildEmail7.setValue(CertifiID.getText().toString().trim());
                                        DatabaseReference mRefChildEmail8 = mRefChild.child("Address");
                                        mRefChildEmail8.setValue(address.getText().toString().trim());
                                        DatabaseReference mRefChildEmail9 = mRefChild.child("City");
                                        mRefChildEmail9.setValue(city.getText().toString().trim());
                                        DatabaseReference mRefChildEmail10 = mRefChild.child("State");
                                        mRefChildEmail10.setValue(state.getText().toString().trim());
                                        DatabaseReference mRefChildEmail11 = mRefChild.child("imageURL");
                                        mRefChildEmail11.setValue(imageURL);
                                        startActivity(new Intent(TrainerRegistrationAdd.this, TrainerDashboard.class));
                                    }
                                    else{
                                        FirebaseUser user = auth.getCurrentUser();
                                        Toast.makeText(getApplicationContext(), "Don't leave it blank", Toast.LENGTH_SHORT).show();
                                        user.delete();
                                    }



                                    //link to Dashboard with authentication
                                    //startActivity(new Intent(TrainerRegistrationAdd.this, TrainerRegistrationAdd.class));
                                    //finish();
                                }
                            }
                        });
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

            }
        });
        //personal portrait part
        Picasso.with(TrainerRegistrationAdd.this).load(Uri.parse(imageURL)).fit().into(imageView);
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
                    Picasso.with(TrainerRegistrationAdd.this).load(downloadUri).fit().into(imageView);
                    imageURL = downloadUri.toString().trim();
                    Toast.makeText(TrainerRegistrationAdd.this, "upload done, ", Toast.LENGTH_LONG).show();
                }
            });

        }

    }
    private boolean isEmpty(EditText etText)
    {
        return etText.getText().toString().trim().length() == 0;
    }

}






