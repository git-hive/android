package com.hive.hive.association.request;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.hive.hive.R;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.RequestCategory;

import java.util.ArrayList;
import java.util.HashMap;

public class NewRequestActivity extends AppCompatActivity {
    HashMap<String, RequestCategory> categories;
    //BUTTONS
    Button saveBT;
    //EDITTEXTS
    EditText titleET;
    EditText locationET;
    EditText descriptionET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);
        Toolbar toolbar = findViewById(R.id.newRequestTB);
        setSupportActionBar(toolbar);
        //FINDING VIEWS
        saveBT = findViewById(R.id.saveBT);
        titleET = findViewById(R.id.titleET);
        locationET = findViewById(R.id.locationET);
        descriptionET = findViewById(R.id.descriptionET);
        //ONCLICKS
        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Request request = new Request("0", 0, 0, FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        "0", "0", titleET.getText().toString(),
                        descriptionET.getText().toString(), 0, categories, null, null);
                DUMMYREQUESTS.requests.add(request);
                Log.d("NEWREQUESTACTIVITY", DUMMYREQUESTS.requests.toString());
                finish();

            }
        });
        //OTHER STUFF
        categories = new HashMap<>();

    }
    //CALLED ON ONCLICK METHOD INSIDE CHECKBOXES LAYOUT
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cat1CB:
                insertDummyRequest(view, "cat1", "cat1");
                break;
            case R.id.cat2CB:
                insertDummyRequest(view, "cat2", "cat2");
                break;
            case R.id.cat3CB:
                insertDummyRequest(view, "cat3", "cat3");
                break;
            case R.id.cat4CB:
                insertDummyRequest(view, "cat4", "cat4");
                break;
            case R.id.cat5CB:
                insertDummyRequest(view, "cat5", "cat5");
                break;
        }
    }
    private void insertDummyRequest(View view, String id, String categoryName){
        if(((CheckBox) view).isChecked())
            categories.put(id, new RequestCategory(id, categoryName));
        else
            categories.remove(id);
        Log.d("NEWREQUESTACTIVITY", categories.toString());
    }
}
