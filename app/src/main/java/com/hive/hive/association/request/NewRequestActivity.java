package com.hive.hive.association.request;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.association.AssociationHelper;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.RequestCategory;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class NewRequestActivity extends AppCompatActivity {

    //-- Static
    public static String TAG = NewRequestActivity.class.getSimpleName();

    //-- Data
    private HashMap<String, RequestCategory> categories;
    private int chosenBudgetCategory = 0;

    //-- Views

    //-Buttons
    private Button saveBT;

    //-EditTexts
    private EditText titleET;
    private EditText locationET;
    private EditText descriptionET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        //-- Toolbar
        Toolbar toolbar = findViewById(R.id.newRequestTB);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        //Finding Views
        saveBT = findViewById(R.id.saveBT);
        titleET = findViewById(R.id.titleET);
        locationET = findViewById(R.id.locationET);
        descriptionET = findViewById(R.id.descriptionET);

        //OnClick Listeners
        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO association shouldnt be setted this way
                String requestUUID = UUID.randomUUID().toString();
                Request request = new Request(requestUUID, 0, 0, titleET.getText().toString(),
                        descriptionET.getText().toString(), 0);

                AssociationHelper.setRequest(FirebaseFirestore.getInstance(), "gVw7dUkuw3SSZSYRXe8s", requestUUID, request);
                finish();

            }
        });

        //Categories Dummy Data
        categories = new HashMap<>();

    }

    /**
     * Checkboxes onClick - receives clicked view and processes the category choice
     * @param view - clicked view
     */
    public void onCategoryClicked(View view) {

        CheckBox checkBox;
        ImageView imageView;

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cat1IV:

                //Gets views
                checkBox = findViewById(R.id.cat1CB);
                imageView = findViewById(R.id.cat1IV);

                //Insert category
                insertCategory(checkBox, "cat1", "cat1");

                //Changes image
                if (checkBox.isSelected())
                    imageView.setImageResource(R.drawable.ic_category_security_disabled);
                else
                    imageView.setImageResource(R.drawable.ic_category_security);

                //Inverts selected
                checkBox.setSelected(!checkBox.isSelected());

                break;
            case R.id.cat2IV:

                //Gets views
                checkBox = findViewById(R.id.cat2CB);
                imageView = findViewById(R.id.cat2IV);

                //Insert category
                insertCategory(checkBox, "cat2", "cat2");

                //Changes image
                if (checkBox.isSelected())
                    imageView.setImageResource(R.drawable.ic_category_gardening_disabled);
                else
                    imageView.setImageResource(R.drawable.ic_category_gardening);

                //Inverts selected
                checkBox.setSelected(!checkBox.isSelected());

                break;
            case R.id.cat3IV:

                //Gets views
                checkBox = findViewById(R.id.cat3CB);
                imageView = findViewById(R.id.cat3IV);

                //Insert category
                insertCategory(checkBox, "cat3", "cat3");

                //Changes image
                if (checkBox.isSelected())
                    imageView.setImageResource(R.drawable.ic_category_services_disabled);
                else
                    imageView.setImageResource(R.drawable.ic_category_services);

                //Inverts selected
                checkBox.setSelected(!checkBox.isSelected());

                break;
            case R.id.cat4IV:

                //Gets views
                checkBox = findViewById(R.id.cat4CB);
                imageView = findViewById(R.id.cat4IV);

                //Insert category
                insertCategory(checkBox, "cat4", "cat4");

                //Changes image
                if (checkBox.isSelected())
                    imageView.setImageResource(R.drawable.ic_category_security_disabled);
                else
                    imageView.setImageResource(R.drawable.ic_category_security);

                //Inverts selected
                checkBox.setSelected(!checkBox.isSelected());

                break;
            case R.id.cat5IV:

                //Gets views
                checkBox = findViewById(R.id.cat5CB);
                imageView = findViewById(R.id.cat5IV);

                //Insert category
                insertCategory(checkBox, "cat5", "cat5");

                //Changes image
                if (checkBox.isSelected())
                    imageView.setImageResource(R.drawable.ic_category_gardening_disabled);
                else
                    imageView.setImageResource(R.drawable.ic_category_gardening);

                //Inverts selected
                checkBox.setSelected(!checkBox.isSelected());

                break;

            case R.id.cat6IV:

                //Gets views
                checkBox = findViewById(R.id.cat6CB);
                imageView = findViewById(R.id.cat6IV);

                //Insert category
                insertCategory(checkBox, "cat6", "cat6");

                //Changes image
                if (checkBox.isSelected())
                    imageView.setImageResource(R.drawable.ic_category_services_disabled);
                else
                    imageView.setImageResource(R.drawable.ic_category_services);

                //Inverts selected
                checkBox.setSelected(!checkBox.isSelected());

                break;

            default:
                break;
        }
    }


    /**
     * Checkboxes onClick - receives clicked view and processes the category choice
     * @param view - clicked view
     */
    public void onBudgetCategoryClicked(View view) {

        ImageView imageView1, imageView2, imageView3;
        TextView textView;
        String budgetString;

        textView = findViewById(R.id.new_request_current_budget_value_tv);

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.new_request_budget_category_1_iv:

                //Gets views
                imageView1 = findViewById(R.id.new_request_budget_category_1_iv);
                imageView2 = findViewById(R.id.new_request_budget_category_2_iv);
                imageView3 = findViewById(R.id.new_request_budget_category_3_iv);


                //Changes images
                switch (this.chosenBudgetCategory){
                    case 0:

                        imageView1.setImageResource(R.drawable.ic_budget_category_ordinary);
                        this.chosenBudgetCategory = 1;
                        textView.setVisibility(View.VISIBLE);

                        break;
                    case 1:

                        imageView1.setImageResource(R.drawable.ic_budget_category_ordinary_disabled);
                        this.chosenBudgetCategory = 0;
                        textView.setVisibility(View.GONE);

                        break;
                    case 2:

                        imageView1.setImageResource(R.drawable.ic_budget_category_ordinary);
                        imageView2.setImageResource(R.drawable.ic_budget_category_savings_disabled);
                        this.chosenBudgetCategory = 1;

                        break;
                    case 3:
                        imageView1.setImageResource(R.drawable.ic_budget_category_ordinary);
                        imageView3.setImageResource(R.drawable.ic_budget_category_extraordinary_disabled);
                        this.chosenBudgetCategory = 1;
                        break;
                    default:
                        break;
                }


                budgetString = getResources().getString(R.string.new_request_current_budget_string1)
                        + "000.000.000,00"
                        + getResources().getString(R.string.new_request_current_budget_string2)
                        + getResources().getString(R.string.new_request_current_budget_ordinary_string);
                textView.setText(budgetString);

                break;

            case R.id.new_request_budget_category_2_iv:

                //Gets views
                imageView1 = findViewById(R.id.new_request_budget_category_1_iv);
                imageView2 = findViewById(R.id.new_request_budget_category_2_iv);
                imageView3 = findViewById(R.id.new_request_budget_category_3_iv);

                //Changes images
                switch (this.chosenBudgetCategory){
                    case 0:
                        imageView2.setImageResource(R.drawable.ic_budget_category_savings);
                        this.chosenBudgetCategory = 2;
                        textView.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        imageView1.setImageResource(R.drawable.ic_budget_category_ordinary_disabled);
                        imageView2.setImageResource(R.drawable.ic_budget_category_savings);
                        this.chosenBudgetCategory = 2;
                        break;
                    case 2:
                        imageView2.setImageResource(R.drawable.ic_budget_category_savings_disabled);
                        this.chosenBudgetCategory = 0;
                        textView.setVisibility(View.GONE);
                        break;
                    case 3:
                        imageView2.setImageResource(R.drawable.ic_budget_category_savings);
                        imageView3.setImageResource(R.drawable.ic_budget_category_extraordinary_disabled);
                        this.chosenBudgetCategory = 2;
                        break;
                    default:
                        break;
                }

                budgetString = getResources().getString(R.string.new_request_current_budget_string1)
                        + "000.000.000,00"
                        + getResources().getString(R.string.new_request_current_budget_string2)
                        + getResources().getString(R.string.new_request_current_budget_savings_string);
                textView.setText(budgetString);

                break;

            case R.id.new_request_budget_category_3_iv:

                //Gets views
                imageView1 = findViewById(R.id.new_request_budget_category_1_iv);
                imageView2 = findViewById(R.id.new_request_budget_category_2_iv);
                imageView3 = findViewById(R.id.new_request_budget_category_3_iv);

                //Changes images
                switch (this.chosenBudgetCategory){
                    case 0:
                        imageView3.setImageResource(R.drawable.ic_budget_category_extraordinary);
                        this.chosenBudgetCategory = 3;
                        textView.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        imageView1.setImageResource(R.drawable.ic_budget_category_ordinary_disabled);
                        imageView3.setImageResource(R.drawable.ic_budget_category_extraordinary);
                        this.chosenBudgetCategory = 3;
                        break;
                    case 2:
                        imageView2.setImageResource(R.drawable.ic_budget_category_savings_disabled);
                        imageView3.setImageResource(R.drawable.ic_budget_category_extraordinary);
                        this.chosenBudgetCategory = 3;
                        break;
                    case 3:
                        imageView3.setImageResource(R.drawable.ic_budget_category_extraordinary_disabled);
                        this.chosenBudgetCategory = 0;
                        textView.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }

                budgetString = getResources().getString(R.string.new_request_current_budget_string1)
                        + "000.000.000,00"
                        + getResources().getString(R.string.new_request_current_budget_string2)
                        + getResources().getString(R.string.new_request_current_budget_extraordinary_string);
                textView.setText(budgetString);

            default:
                break;
        }

        Log.d(TAG, "Current budget category: " + this.chosenBudgetCategory);
    }


    /**
     * Adds a category to the new Request
     * @param view - View with the checkgbox category
     * @param id - category id
     * @param categoryName - category name
     */
    private void insertCategory(View view, String id, String categoryName){
        if(((CheckBox) view).isChecked())
            categories.put(id, new RequestCategory(id, categoryName));
        else
            categories.remove(id);
        Log.d(TAG, categories.toString());
    }
}
