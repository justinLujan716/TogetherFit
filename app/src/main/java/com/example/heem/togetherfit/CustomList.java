package com.example.heem.togetherfit;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> title;
    private final Integer[] imageId;
    private final ArrayList<String> type;
    private final ArrayList<String> location;

    public CustomList(Activity context,
                      ArrayList<String> title, Integer[] imageId, ArrayList<String> type, ArrayList<String> location) {
        super(context, R.layout.activity_add_class, title);
        this.context = context;
        this.title = title;
        this.imageId = imageId;
        this.type = type;
        this.location = location;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.classinfo, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.listTitle);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listImage);
        TextView txtType = (TextView) rowView.findViewById(R.id.type);
        TextView txtLocation = (TextView) rowView.findViewById(R.id.location);
        txtTitle.setText(title.get(position));
        imageView.setImageResource(imageId[position]);
        txtType.setText("Fitness Type: " + type.get(position));
        txtLocation.setText("Class Location: " + location.get(position));
        return rowView;
    }
}
