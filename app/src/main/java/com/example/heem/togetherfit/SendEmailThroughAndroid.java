package com.example.heem.togetherfit;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SendEmailThroughAndroid extends AppCompatActivity {

    //Variables
    private String toEmail;
    private String subSubject;
    private String messagebyuser;
    //Buttons and Fields
    EditText to;
    EditText sub;
    EditText msg;
    Button send;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email_through_android);

        to = (EditText) findViewById(R.id.to);
        sub = (EditText) findViewById(R.id.subject);
        msg = (EditText) findViewById(R.id.msg);
        send = (Button) findViewById(R.id.sendbutton);


        Intent intent = getIntent();
        String emailTo = intent.getStringExtra("emailTo");
        to.setText(emailTo);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Make sure all fields are not empty
                if (!isEmpty(to) && !isEmpty(sub)&& !isEmpty(msg))
                {
                    //get the information once the user click on send
                    toEmail = to.getText().toString();
                    subSubject = sub.getText().toString();
                    messagebyuser = msg.getText().toString();
                    //After getting the information, call send email methos
                    sendEmail(messagebyuser, subSubject);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // Actions to do after 10 seconds
                            Intent intent = new Intent(SendEmailThroughAndroid.this, EmailSentConfirmation.class);
                            startActivity(intent);
                        }
                    }, 3000);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Make sure there is no empty field!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        back = (Button) findViewById(R.id.backbutton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    /*
     * This method to email through the application
     */
    private void sendEmail(String msg, String subject2) {

         /* get user email to use in email content */
        String email = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //email address
            email = user.getEmail();
        }
        //Getting content for email
        String subject = "Don't Reply - this email sent through TogetherFit app";
        String message = ("Hello" + "..!\n\n\n\tUser With Email: " + email + " has sent you this message.\n" +
                "\n -----------------------------------------------------------------------------------\n"+ "Subject: " + subject2 + "\n" + msg
                + "\n -----------------------------------------------------------------------------------\n"+
                "\n\nThank you,\n TogetherFit team. " );
        //Creating SendEmail object
        SendEmail sm = new SendEmail(this, toEmail,subject, message);
        //Executing SendEmail to send email
        sm.execute();

    }

    /*
     * Method to check if edit text is empty
     */
    private boolean isEmpty(EditText etText)
    {
        return etText.getText().toString().trim().length() == 0;
    }
}
