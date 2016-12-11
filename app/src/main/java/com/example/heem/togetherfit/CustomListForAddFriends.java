package com.example.heem.togetherfit;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heem on 12/3/16.
 * This Custome List for Show All Reviews About A Specific User
 */

public class CustomListForAddFriends extends ArrayAdapter<String> implements Filterable {

    private Activity context;
    private ArrayList<String> UserName;
    private ArrayList<String> Image;
    private ArrayList<String> FitnessType;
    private ArrayList<String> UserType;



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


    public int getCount()
    {
        return UserName.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                userInfo user = (userInfo) results.values;
                UserName = user.getUsername();
                FitnessType = user.getFitnesstype();
                UserType = user.getType();
                Image = user.getImage();
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<String> FilteredArrayNames = new ArrayList<String>();
                ArrayList<String> FilteredArrayFitness = new ArrayList<String>();
                ArrayList<String> FilteredArrayType = new ArrayList<String>();
                ArrayList<String> FilteredArrayImage = new ArrayList<String>();
                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < UserName.size(); i++) {
                    String dataNames = UserName.get(i);
                    if (dataNames.toLowerCase().startsWith(constraint.toString()))  {
                        FilteredArrayNames.add(dataNames);
                        FilteredArrayFitness.add(FitnessType.get(i));
                        FilteredArrayType.add(UserType.get(i));
                        FilteredArrayImage.add(Image.get(i));
                    }
                }
                userInfo user = new userInfo(FilteredArrayNames,FilteredArrayFitness,FilteredArrayType,FilteredArrayImage);
                results.values = user;
                return results;
            }
        };

        return filter;
    }
}
