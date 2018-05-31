package com.hive.hive.login;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.main.MainActivity;
import com.hive.hive.model.association.Association;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.Mask;
import com.hive.hive.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.text.TextWatcher;
import android.widget.EditText;


public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    // Shape outside editables (Need this messy thing to set visibility GONE)
    private TextInputLayout mFullNameTVUpperName;
    private TextInputLayout mBirthdayTVUpperName;
    private TextInputLayout mCPFUpperName;
    private TextInputLayout mEmailTVUpperName;

    // Editable Text
    private TextInputEditText mFullNameTV;
    private TextInputEditText mBirthdayTV;
    private TextInputEditText mCPF;
    private TextInputEditText mEmailTV;
    private ProgressBar mSignupPB;
    private RadioButton mTermsAgreementRB;
    private Button mSignUpComplete;
    private TextView mHelloTV;
    private TextView mTermAgrementsTV;

    // Associations recycler
    private RecyclerView mAssociationsRecyclerView;
    private AssociationsAdapter mAssociationsAdapter; //recycler adapter
    // Sign up form inputs
    private User newUser;
    private TextWatcher cpfMask;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Hide keyboard when create screen
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        db = FirebaseFirestore.getInstance();
        newUser = new User();
        //Refence which enable messy Gone in outside shape of editText
        mFullNameTVUpperName = findViewById(R.id.textViewSignUpFullNameUpperName);
        mCPFUpperName = findViewById(R.id.textViewSignUpCPFUpperName);
        mEmailTVUpperName = findViewById(R.id.textViewSignUpEmailUpperName);
        // Others
        mFullNameTV = findViewById(R.id.textViewSignUpFullName);
        mCPF = findViewById(R.id.textViewSignUpCPF);
        mEmailTV = findViewById(R.id.textViewSignUpEmail);
        mEmailTV.setEnabled(false);
        mTermsAgreementRB = findViewById(R.id.radioButtonSignUpTermsAgreement);
        mSignupPB = findViewById(R.id.progress_bar_signup);
        mHelloTV = findViewById(R.id.helloTV);
        mTermAgrementsTV = findViewById(R.id.termsAgreementTV);


        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }


        // Setting CPF listener to format
        cpfMask = Mask.insert("###.###.###-##", (EditText) mCPF);
        mCPF.addTextChangedListener(cpfMask);


        // Get firebase user and update fields with it
        mAuth = FirebaseAuth.getInstance();

        // Update TextView inputs with social auth data
        updateUI(mAuth.getCurrentUser());

        // Handle form submission
        mSignUpComplete = findViewById(R.id.buttonSignUpComplete);
        mSignUpComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allValid = validateInputs();
                if (allValid) {
                    progressUser();
                    updateUser();
                }

            }
        });


        mBirthdayTV = findViewById(R.id.textViewSignUpBirthday);
        mBirthdayTVUpperName = findViewById(R.id.textViewSignUpBirthdayUpperName);
        // Create birth date date picker
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Calendar Class create proper date format
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.YEAR, year);

                // Converting DD/MM/YYYY format
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                Date d = cal.getTime();
                String strDate = dateFormatter.format(d);
                mBirthdayTV.setText(strDate);
            }
        };

        // Set birthday field on click listener to open the date picker
        mBirthdayTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Set birthday field on focus listener to open the date picker
        mBirthdayTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

        LoginAndSignUpFirebaseHandler.getAllAssoctiationsHandler(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }

    /**
     * Updates input fields with the data provided by social auth
     *
     * @param user Firebase user authenticated in the LoginActivity
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mFullNameTV.setText(user.getDisplayName());
            mFullNameTV.setSelection(mFullNameTV.getText().length());
            mEmailTV.setText(user.getEmail());
            mEmailTV.setSelection(mEmailTV.getText().length());


        }
    }

    public void setupRecyclerView(Pair<ArrayList<String>, HashMap<String, Association>> associations){
        mAssociationsRecyclerView = findViewById(R.id.associationsRV);
        mAssociationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAssociationsAdapter = new AssociationsAdapter(associations);
        mAssociationsRecyclerView.setAdapter(mAssociationsAdapter);
    }
    /**
     * Configures and displays a date picker used in the birth day field
     */
    public void showDatePickerDialog() {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SignupActivity.this,
                android.R.style.Theme_Holo_Light_Dialog,
                mOnDateSetListener,
                year,
                month,
                day
        );
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    /**
     * Validate all form inputs
     *
     * @return true is all fields are valid, false otherwise
     */
    private boolean validateInputs() {
        // Check name
        String name = getText(mFullNameTV);
        if (TextUtils.isEmpty(name)) {
            mFullNameTV.setError("Name is required");
            mFullNameTV.requestFocus();
            return false;
        }
        newUser.setName(name);
        // Check birthday
        String birthday = getText(mBirthdayTV);
        if (TextUtils.isEmpty(birthday)) {
            mBirthdayTV.setError("Birth date is required");
            mBirthdayTV.requestFocus();
            return false;
        }
        newUser.setBirthday(birthday);

        // TODO: proper CPF validation
        String cpf = getText(mCPF);
        if (TextUtils.isEmpty(cpf) || !Utils.isValid(cpf)) {
            mCPF.setError("A valid CPF is required");
            mCPF.requestFocus();
            return false;
        }

        newUser.setCpf(cpf);

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            newUser.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        // Check if user agreed with the terms
        if (!mTermsAgreementRB.isChecked()) {
            mTermAgrementsTV.setError("You need to agree with the terms to use the app");
            mTermAgrementsTV.requestFocus();

            return false;
        }

        if(!verifySelectedAssociations()){
            Toast.makeText(this, getResources().getString(R.string.should_select_one_association), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean verifySelectedAssociations(){
        if(mAssociationsAdapter.getmSelectedAssociationsIds().isEmpty()) return false;
        return  true;
    }
    /**
     * Stores field data into firestore
     */
    private void updateUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if(user.getPhotoUrl() != null)
                newUser.setPhotoUrl(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
            LoginAndSignUpFirebaseHandler
                    .saveUserDataAndSendSelectedIngressesRequests(this, newUser, mAssociationsAdapter.getmSelectedAssociationsIds());
        }else{
            Toast.makeText(this, getString(R.string.signup_problem), Toast.LENGTH_SHORT).show();
            startLogin();
        }
    }

    public void startHome() {
        Context ctx = SignupActivity.this.getApplicationContext();
        Intent startMainActivity = new Intent(ctx, MainActivity.class);
        startActivity(startMainActivity);
        finish();

    }

    public void startLogin() {
        Context ctx = SignupActivity.this.getApplicationContext();
        Intent startMainActivity = new Intent(ctx, LoginActivity.class);
        startActivity(startMainActivity);
        finish();

    }
    /**
     * Extracts a trimmed string from a TextView element
     *
     * @param v Field that contains the value to be extracted
     * @return TextView value as a trimmed string
     */
    public static String getText(TextInputEditText v) {
        return v.getText().toString().trim();
    }

    private void progressUser() {
        //progressBar
        mSignupPB.setVisibility(View.VISIBLE);


        // Messy
        mFullNameTVUpperName.setVisibility(View.GONE);
        mBirthdayTVUpperName.setVisibility(View.GONE);
        mCPFUpperName.setVisibility(View.GONE);
        mEmailTVUpperName.setVisibility(View.GONE);
        // Truly messy, hate java stuff!!!

        mFullNameTV.setVisibility(View.GONE);
        mBirthdayTV.setVisibility(View.GONE);
        mCPF.setVisibility(View.GONE);
        mEmailTV.setVisibility(View.GONE);
        mTermsAgreementRB.setVisibility(View.GONE);
        mSignUpComplete.setVisibility(View.GONE);
        //mHelloTV.setVisibility(View.GONE);
        mTermAgrementsTV.setVisibility(View.GONE);
    }

    private void returnFromProgressUser() {
        //progressBar
        mSignupPB.setVisibility(View.GONE);


        mFullNameTV.setVisibility(View.VISIBLE);
        mBirthdayTV.setVisibility(View.VISIBLE);
        mCPF.setVisibility(View.VISIBLE);
        mEmailTV.setVisibility(View.VISIBLE);
        mTermsAgreementRB.setVisibility(View.VISIBLE);
        mSignUpComplete.setVisibility(View.VISIBLE);
    }

}



