package com.example.heem.togetherfit;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomListForShowTrainer extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> trainerName;
    private final ArrayList<String> traineremail;
    private final ArrayList<String> imageId;
    private final ArrayList<String> location;
    private final ArrayList<String> fitnestype;



    public CustomListForShowTrainer(Activity context,
                                    ArrayList<String> trainerName, ArrayList<String> imageId,
                                    ArrayList<String> traineremail, ArrayList<String> location, ArrayList<String> fitnestype) {
        super(context, R.layout.activity_show_classes, trainerName);
        this.context = context;
        this.trainerName = trainerName;
        this.imageId = imageId;
        this.traineremail = traineremail;
        this.location = location;
        this.fitnestype = fitnestype;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.trainerinfo, null, true);
        TextView name = (TextView) rowView.findViewById(R.id.trainername);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listImage);
        TextView trainerEmail = (TextView) rowView.findViewById(R.id.traineremail);
        TextView txtLocation = (TextView) rowView.findViewById(R.id.location);
        TextView fitnesstype = (TextView) rowView.findViewById(R.id.trainerfitnesstype);
        name.setText("Trainer Name: " + trainerName.get(position));
        //Use Picasso Jar file to display the image
        Picasso.with(context).load(imageId.get(position)).resize(120,120).error(R.drawable.personal).into(imageView);
        trainerEmail.setText("Trainer Email : " + traineremail.get(position));
        txtLocation.setText("Trainer Location: " + location.get(position));
        fitnesstype.setText("Trainer fitness Type: " + fitnestype.get(position));
        return rowView;
    }
}
