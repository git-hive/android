package com.hive.hive.association.request;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.model.association.AssociationHelper;
import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.RequestCategory;
import com.hive.hive.utils.DocReferences;

import java.util.ArrayList;
import java.util.UUID;

public class NewRequestActivity extends AppCompatActivity {
    public static String TAG = NewRequestActivity.class.getSimpleName();

    //--- Budget categories
    
    // Ordinary
    private LinearLayout budgetCategoryOrdinaryLL;
    private ImageView budgetCategoryOrdinaryIV;
    private CheckBox budgetCategoryOrdinaryCB;

    // Savings
    private LinearLayout budgetCategorySavingsLL;
    private ImageView budgetCategorySavingsIV;
    private CheckBox budgetCategorySavingsCB;

        //OnClick Listeners
        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO association shouldnt be setted this way
                String requestUUID = UUID.randomUUID().toString();
                Request request = new Request(Calendar.getInstance().getTimeInMillis(), Calendar.getInstance().getTimeInMillis(),
                        DocReferences.getUserRef(), null, DocReferences.getAssociationRef("gVw7dUkuw3SSZSYRXe8s")
                        , titleET.getText().toString(),
                        descriptionET.getText().toString(), 0, 0, null);

    // Extra ordinary
    private LinearLayout budgetCategoryExtraordinaryLL;
    private ImageView budgetCategoryExtraordinaryIV;
    private CheckBox budgetCategoryExtraordinaryCB;

    private TextView selectedBudgetCategoryTV;

    //--- Request categories

    // Security category
    private LinearLayout requestCategorySecurityLL;
    private ImageView requestCategorySecurityIV;
    private CheckBox requestCategorySecurityCB;

    // Gardening category
    private LinearLayout requestCategoryGardeningLL;
    private ImageView requestCategoryGardeningIV;
    private CheckBox requestCategoryGardeningCB;

    // Services category
    private LinearLayout requestCategoryServicesLL;
    private ImageView requestCategoryServicesIV;
    private CheckBox requestCategoryServicesCB;

    // Cleaning category
    private LinearLayout requestCategoryCleaningLL;
    private ImageView requestCategoryCleaningIV;
    private CheckBox requestCategoryCleaningCB;

    private TextView selectedRequestCategoryTV;
    private Pair<DocumentReference, RequestCategory> selectedRequestCategoryPair;
    private ArrayList<Pair<DocumentReference, RequestCategory>> requestCategoriesPairs;


    // Buttons
    private Button saveBT;

    // EditTexts
    private EditText titleET;
    private EditText descriptionET;
    private EditText locationET;

    // Firestore
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();

    // Association
    final String associationID = "gVw7dUkuw3SSZSYRXe8s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        requestCategoriesPairs = new ArrayList<>();

        // Toolbar
        Toolbar toolbar = findViewById(R.id.newRequestTB);
        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);


        titleET = findViewById(R.id.titleET);
        descriptionET = findViewById(R.id.descriptionET);
        locationET = findViewById(R.id.locationET);
        saveBT = findViewById(R.id.saveBT);

        //--- Budget categories
        selectedBudgetCategoryTV = findViewById(R.id.new_request_selected_budget_category_tv);

        // Ordinary
        budgetCategoryOrdinaryLL = findViewById(R.id.new_request_budget_category_ordinary_ll);
        budgetCategoryOrdinaryLL.setOnClickListener(budgetCategoriesOnClickListener());

        budgetCategoryOrdinaryIV = findViewById(R.id.new_request_budget_category_ordinary_iv);
        budgetCategoryOrdinaryCB = findViewById(R.id.new_request_budget_category_ordinary_cb);

        // Savings
        budgetCategorySavingsLL = findViewById(R.id.new_request_budget_category_savings_ll);
        budgetCategorySavingsLL.setOnClickListener(budgetCategoriesOnClickListener());

        budgetCategorySavingsIV = findViewById(R.id.new_request_budget_category_savings_iv);
        budgetCategorySavingsCB = findViewById(R.id.new_request_budget_category_savings_cb);

        // Extraordinary
        budgetCategoryExtraordinaryLL =
                findViewById(R.id.new_request_budget_category_extraordinary_ll);
        budgetCategoryExtraordinaryLL.setOnClickListener(budgetCategoriesOnClickListener());

        budgetCategoryExtraordinaryIV =
                findViewById(R.id.new_request_budget_category_extraordinary_iv);
        budgetCategoryExtraordinaryCB =
                findViewById(R.id.new_request_budget_category_extraordinary_cb);

        //--- Request categories
        selectedRequestCategoryTV = findViewById(R.id.new_request_selected_request_category_tv);

        // Security
        requestCategorySecurityLL = findViewById(R.id.new_request_request_category_security_ll);
        requestCategorySecurityLL.setOnClickListener(requestCategoriesOnClickListener());

        requestCategorySecurityIV = findViewById(R.id.new_request_request_category_security_iv);
        requestCategorySecurityCB = findViewById(R.id.new_request_request_category_security_cb);

        // Gardening
        requestCategoryGardeningLL = findViewById(R.id.new_request_request_category_gardening_ll);
        requestCategoryGardeningLL.setOnClickListener(requestCategoriesOnClickListener());

        requestCategoryGardeningIV = findViewById(R.id.new_request_request_category_gardening_iv);
        requestCategoryGardeningCB = findViewById(R.id.new_request_request_category_gardening_cb);

        // Maintenance
        requestCategoryServicesLL =
                findViewById(R.id.new_request_request_category_maintenance_ll);
        requestCategoryServicesLL.setOnClickListener(requestCategoriesOnClickListener());

        requestCategoryServicesIV =
                findViewById(R.id.new_request_request_category_maintenance_iv);
        requestCategoryServicesCB =
                findViewById(R.id.new_request_request_category_maintenance_cb);

        // Cleaning
        requestCategoryCleaningLL = findViewById(R.id.new_request_request_category_cleaning_ll);
        requestCategoryCleaningLL.setOnClickListener(requestCategoriesOnClickListener());

        requestCategoryCleaningIV = findViewById(R.id.new_request_request_category_cleaning_iv);
        requestCategoryCleaningCB = findViewById(R.id.new_request_request_category_cleaning_cb);


        // OnClick Listeners
        saveBT.setOnClickListener(view -> {
            handleOnSaveButtonClick();
            finish();
        });

        fetchAndSetRequestCategories();
    }

    /**
     * Fetches all association request categories from Firestore and add them to requestCategoriesPairs
     */
    private void fetchAndSetRequestCategories() {
        AssociationHelper.getAllRequestCategories(
                mDB,
                associationID
        )
                .addOnSuccessListener(documentSnapshots -> {
                    for (DocumentSnapshot categoryDoc : documentSnapshots) {
                        RequestCategory requestCategory =
                                categoryDoc.toObject(RequestCategory.class);
                        Pair<DocumentReference, RequestCategory> newPair =
                            Pair.create(categoryDoc.getReference(), requestCategory);

                        requestCategoriesPairs.add(newPair);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(
                            this,
                            "Failed to get categories",
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.e(TAG, "failed to get association categories: " + e.toString());
                });
    }

    private View.OnClickListener budgetCategoriesOnClickListener() {
        return v -> {
            resetSelectedBudgetCategory();
            switch (v.getId()) {
                case R.id.new_request_budget_category_ordinary_ll:
                    selectedBudgetCategoryTV.setText(R.string.budget_category_ordinary);
                    budgetCategoryOrdinaryIV.setImageResource(R.drawable.ic_budget_category_ordinary);
                    budgetCategoryOrdinaryCB.setSelected(true);
                    break;
                case R.id.new_request_budget_category_savings_ll:
                    selectedBudgetCategoryTV.setText(R.string.budget_category_savings);
                    budgetCategorySavingsIV.setImageResource(R.drawable.ic_budget_category_savings);
                    budgetCategorySavingsCB.setSelected(true);
                    break;
                case R.id.new_request_budget_category_extraordinary_ll:
                    selectedBudgetCategoryTV.setText(R.string.budget_category_extraordinary);
                    budgetCategoryExtraordinaryIV.setImageResource(R.drawable.ic_budget_category_extraordinary);
                    budgetCategoryExtraordinaryCB.setSelected(true);
                    break;
                default:
                    break;
            }
        };
    }

    private View.OnClickListener requestCategoriesOnClickListener() {
        return v -> {
            resetSelectedRequestCategory();
            switch (v.getId()) {
                case R.id.new_request_request_category_security_ll:
                    selectedRequestCategoryTV.setText(R.string.request_category_security);
                    requestCategorySecurityIV.setImageResource(R.drawable.ic_category_security);
                    requestCategorySecurityCB.setSelected(true);
                    searchAndSetRequestCategory("security");
                    break;
                case R.id.new_request_request_category_gardening_ll:
                    selectedRequestCategoryTV.setText(R.string.request_category_gardening);
                    requestCategoryGardeningIV.setImageResource(R.drawable.ic_category_gardening);
                    requestCategoryGardeningIV.setSelected(true);
                    searchAndSetRequestCategory("gardening");
                    break;
                case R.id.new_request_request_category_maintenance_ll:
                    selectedRequestCategoryTV.setText(R.string.request_category_maintenance);
                    requestCategoryServicesIV.setImageResource(R.drawable.ic_category_services);
                    requestCategoryServicesIV.setSelected(true);
                    searchAndSetRequestCategory("services");
                    break;
                case R.id.new_request_request_category_cleaning_ll:
                    selectedRequestCategoryTV.setText(R.string.request_category_cleaning);
                    requestCategoryCleaningIV.setImageResource(R.drawable.ic_category_cleaning);
                    requestCategoryCleaningCB.setSelected(true);
                    searchAndSetRequestCategory("cleaning");
                    break;
                default:
                    break;
            }
        };
    }

    /**
     * Reset all Budget Category fields to their default state
     */
    private void resetSelectedBudgetCategory() {
        selectedBudgetCategoryTV.setText("");

        budgetCategoryOrdinaryIV.setImageResource(R.drawable.ic_budget_category_ordinary_disabled);
        budgetCategoryOrdinaryCB.setSelected(false);

        budgetCategorySavingsIV.setImageResource(R.drawable.ic_budget_category_savings_disabled);
        budgetCategorySavingsCB.setSelected(false);

        budgetCategoryExtraordinaryIV.setImageResource(R.drawable.ic_budget_category_extraordinary_disabled);
        budgetCategoryExtraordinaryCB.setSelected(false);
    }

    /**
     * Reset all Request Category fields to their default state
     */
    private void resetSelectedRequestCategory() {
        selectedRequestCategoryTV.setText("");

        requestCategorySecurityIV.setImageResource(R.drawable.ic_category_security_disabled);
        requestCategorySecurityCB.setSelected(false);

        requestCategoryGardeningIV.setImageResource(R.drawable.ic_category_gardening_disabled);
        requestCategoryGardeningIV.setSelected(false);

        requestCategoryServicesIV.setImageResource(R.drawable.ic_category_services_disabled);
        requestCategoryServicesIV.setSelected(false);

        requestCategoryCleaningIV.setImageResource(R.drawable.ic_category_cleaning_disabled);
        requestCategoryCleaningCB.setSelected(false);
    }

    /**
     * Search for a category by name in requestCategoriesPairs, if found,
     * set as selectedRequestCategoryPair
     *
     * @param categoryName Self descriptive
     */
    private void searchAndSetRequestCategory(String categoryName) {
        for (Pair<DocumentReference, RequestCategory> pair : requestCategoriesPairs) {
            if (pair.second.getName().equals(categoryName)) {
                selectedRequestCategoryPair = pair;
                break;
            }
        }
    }

    private void handleOnSaveButtonClick() {
        String requestUUID = UUID.randomUUID().toString();
        long currentTimeMillis = System.currentTimeMillis();

        ArrayList<DocumentReference> categoriesRefs = new ArrayList<>();
        categoriesRefs.add(selectedRequestCategoryPair.first);

        Request request = new Request(
                requestUUID,
                currentTimeMillis,
                currentTimeMillis,
                DocReferences.getUserRef(),
                null,
                DocReferences.getAssociationRef(associationID),
                titleET.getText().toString(),
                descriptionET.getText().toString(),
                0,
                0,
                categoriesRefs
        );

        AssociationHelper.setRequest(
                mDB,
                associationID,
                requestUUID,
                request
        )
                .addOnSuccessListener(aVoid -> Toast.makeText(
                        this,
                        "Request saved",
                        Toast.LENGTH_SHORT
                ).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(
                            this,
                            "Failed to save request",
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.e(TAG, "[Request] failed to save: " + e.toString());
                });
    }
}
