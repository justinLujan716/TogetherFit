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

public class CustomListForShowClasses extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> classtitle;
    private final ArrayList<String> classId;
    private final ArrayList<String> imageId;
    private final ArrayList<String> location;
    private final ArrayList<String> cap;
    private final ArrayList<String> numReg;



    public CustomListForShowClasses(Activity context,
                                    ArrayList<String> classtitle, ArrayList<String> imageId,
                                    ArrayList<String> classId, ArrayList<String> location, ArrayList<String> cap,
                                    ArrayList<String> numReg) {
        super(context, R.layout.activity_show_classes, classtitle);
        this.context = context;
        this.classtitle = classtitle;
        this.imageId = imageId;
        this.classId = classId;
        this.location = location;
        this.cap = cap;
        this.numReg = numReg;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.classinfo2, null, true);
        TextView title = (TextView) rowView.findViewById(R.id.classtitle);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listImage);
        TextView id = (TextView) rowView.findViewById(R.id.id);
        TextView txtLocation = (TextView) rowView.findViewById(R.id.location);
        TextView capacity = (TextView) rowView.findViewById(R.id.capacity);
        TextView numOfReg = (TextView) rowView.findViewById(R.id.numOfReg);
        title.setText(classtitle.get(position));
        //Use Picasso Jar file to display the image
        Picasso.with(context).load(imageId.get(position)).into(imageView);
        id.setText("Class ID: " + classId.get(position));
        txtLocation.setText("Class Location: " + location.get(position));
        capacity.setText("Capacity: " + cap.get(position));
        numOfReg.setText("Number of Registration: " + numReg.get(position));
        return rowView;
    }
}
