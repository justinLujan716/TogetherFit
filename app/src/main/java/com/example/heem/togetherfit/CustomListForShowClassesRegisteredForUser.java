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


public class CustomListForShowClassesRegisteredForUser  extends ArrayAdapter<String> {

        private final Activity context;
        private final ArrayList<String> classtitle;
        private final ArrayList<String> imageId;
        private final ArrayList<String> location;
        private final ArrayList<String> time;
        private final ArrayList<String> day;



        public CustomListForShowClassesRegisteredForUser(Activity context,
                                        ArrayList<String> classtitle, ArrayList<String> imageId, ArrayList<String> location,
                                        ArrayList<String> time, ArrayList<String> day) {
            super(context, R.layout.activity_classes_registered_for_user, classtitle);
            this.context = context;
            this.classtitle = classtitle;
            this.imageId = imageId;
            this.location = location;
            this.time = time;
            this.day = day;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.class_info_for_user_registered, null, true);
            TextView title = (TextView) rowView.findViewById(R.id.listTitle);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.listImage);
            TextView txtLocation = (TextView) rowView.findViewById(R.id.location);
            TextView timeShow = (TextView) rowView.findViewById(R.id.time);
            TextView dayshow = (TextView) rowView.findViewById(R.id.day);
            title.setText(classtitle.get(position));
            //Use Picasso Jar file to display the image
            Picasso.with(context).load(imageId.get(position)).fit().into(imageView);
            txtLocation.setText("Class Location: " + location.get(position));
            timeShow.setText("Time: " + time.get(position));
            dayshow.setText("Day: " + day.get(position));
            return rowView;
        }
}
