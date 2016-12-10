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
 * Created by Heem on 12/3/16.
 * This Custome List for Show All Reviews About A Specific User
 */

public class CustomListForAddFriends extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> UserName;
    private final ArrayList<String> Image;
    private final ArrayList<String> FitnessType;
    private final ArrayList<String> UserType;



    public CustomListForAddFriends(Activity context, ArrayList<String> UserName,
                                    ArrayList<String> Image,  ArrayList<String> FitnessType,
                                   ArrayList<String> UserType) {
        super(context, R.layout.activity_add_friends, UserName);
        this.context = context;
        this.UserName = UserName;
        this.Image = Image;
        this.FitnessType = FitnessType;
        this.UserType = UserType;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.friends_info, null, true);
        TextView username = (TextView) rowView.findViewById(R.id.username);
        username.setText("User Name: " + UserName.get(position));
        TextView fitnesstype = (TextView) rowView.findViewById(R.id.fitnesstype);
        fitnesstype.setText("Fitness Type: " + FitnessType.get(position));
        TextView usertype = (TextView) rowView.findViewById(R.id.usertype);
        usertype.setText("User Type: " + UserType.get(position));
        ImageView image = (ImageView) rowView.findViewById(R.id.image);
        //Use Picasso Jar file to display the image
        Picasso.with(context).load(Image.get(position)).transform(new CircleTransform()).error(R.drawable.personalcircle).fit().into(image);
        return rowView;
    }
}
