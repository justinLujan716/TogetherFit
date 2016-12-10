package com.example.heem.togetherfit;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Heem on 12/5/16.
 */

public class CustomListUsers extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> studentname;
    private final ArrayList<String> studentemail;
    private final ArrayList<String> studentPicture;



    public CustomListUsers(Activity context,
                           ArrayList<String> studentname, ArrayList<String> studentemail,
                           ArrayList<String> studentPicture) {
        super(context, R.layout.activity_user_reg_in_aclass, studentname);
        this.context = context;
        this.studentname = studentname;
        this.studentemail = studentemail;
        this.studentPicture = studentPicture;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.studentinfo, null, true);
        TextView studentName = (TextView) rowView.findViewById(R.id.studentName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.studentImage);
        TextView studentEmail = (TextView) rowView.findViewById(R.id.studentEmail);
        studentName.setText("Student Name: " + studentname.get(position));
        //Use Picasso Jar file to display the image
        Picasso.with(context).load(studentPicture.get(position)).fit().into(imageView);
        studentEmail.setText("Student Email : " + studentemail.get(position));
        return rowView;
    }
}
