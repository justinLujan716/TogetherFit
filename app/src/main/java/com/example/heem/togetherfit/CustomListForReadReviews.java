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

public class CustomListForReadReviews extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> UserName;
    private final ArrayList<String> Comment;
    private final ArrayList<String> Rate;
    private final ArrayList<String> Date;



    public CustomListForReadReviews(Activity context, ArrayList<String> UserName,
                                    ArrayList<String> Comment, ArrayList<String> Rate, ArrayList<String> Date) {
        super(context, R.layout.activity_write_review, UserName);
        this.context = context;
        this.UserName = UserName;
        this.Comment = Comment;
        this.Rate = Rate;
        this.Date = Date;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.reviewinfo, null, true);
        TextView username = (TextView) rowView.findViewById(R.id.username);
        TextView date = (TextView) rowView.findViewById(R.id.date);
        TextView rating = (TextView) rowView.findViewById(R.id.rate);
        TextView cmt = (TextView) rowView.findViewById(R.id.msg);
        username.setText("User Name: " + UserName.get(position));
        date.setText("Date: " + Date.get(position));
        rating.setText("Rating (Out of 5): " + Rate.get(position));
        cmt.setText("Comment:\n" + Comment.get(position));
        return rowView;
    }
}
