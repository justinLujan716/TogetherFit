package com.example.heem.togetherfit;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class TrainerCreateClassAdd extends AppCompatActivity {

    //Firebase variable
    DatabaseReference database;
    ArrayList<String> toShow = new ArrayList<String>();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_class_add_place);
        // call filltoshow method to fill the toShow list;
        database = FirebaseDatabase.getInstance().getReference().child("TrainerPlaces");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //toShow = new ArrayList<String>(); //To update array list
                for (DataSnapshot chunk : snapshot.getChildren()) {
                    String name = (String) chunk.child("name").getValue();
                    String address = (String) chunk.child("address").getValue();
                    String zipcode = (String) chunk.child("zip").getValue();
                    toShow.add( name + ",\n    " + address + ", " + zipcode); //Add them to the toPrint list
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseErr) {
                System.out.println("The read failed: " + databaseErr.getCode());
            }
        });
        //display the content of list;
        //Toast.makeText(TrainerCreateClassAdd.this, "before click", Toast.LENGTH_LONG).show();
        showlist(toShow);
        //return value to previous page
        clickCallback();
    }
    private void showlist(ArrayList<String> toShow){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.da_showplace, toShow);
        list = (ListView) findViewById(R.id.placeList);
        list.setAdapter(adapter);
    }
    //return click value method
    private void clickCallback() {
        //ListView list = (ListView) findViewById(R.id.placeList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paret, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = textView.getText().toString();
                //int index = message.indexOf(",");
                //message = message.substring(0,index);
                Toast.makeText(TrainerCreateClassAdd.this, message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("placeName", message);
                setResult(301,intent);//result code is 300
                finish();
            }
        });
    }
}
