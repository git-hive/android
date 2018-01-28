package com.hive.hive.login;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hive.hive.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView mFullNameTV;
    private TextView mBirthdayTV;
    private TextView mCPF;
    private TextView mEmailTV;
    private TextView mPassword;
    private RadioButton mTermsAgreementRB;
    private Button mSignUpComplete;

    private Map<String, String> inputValues;

    private DatePickerDialog.OnDateSetListener mOnDateSetListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFullNameTV = findViewById(R.id.textViewSignUpFullName);
        mCPF = findViewById(R.id.textViewSignUpCPF);
        mEmailTV = findViewById(R.id.textViewSignUpEmail);
        mPassword = findViewById(R.id.textViewSignUpPassword);
        mTermsAgreementRB = findViewById(R.id.radioButtonSignUpTermsAgreement);
        inputValues = new HashMap<>();

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

        // Get firebase user and update fields with it
        mAuth = FirebaseAuth.getInstance();
        updateUI(mAuth.getCurrentUser());

        // Handle form submission
        mSignUpComplete = findViewById(R.id.buttonSignUpComplete);
        mSignUpComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allValid = validateInputs();
                if (allValid) {
                    updateUser();
                }
            }
        });


        mBirthdayTV = findViewById(R.id.textViewSignUpBirthday);
        // Create birth date date picker
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Months vary from 0 to 11, so increment to look natural
                month = month + 1;
                // Create date string
                String date = String.format("%d/%d/%d", dayOfMonth, month, year);
                mBirthdayTV.setText(date);
            }
        };

        // Set birthday field on focus listener to open the date picker
        mBirthdayTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });
    }

    /**
     * Updates input fields with the data provided by social auth
     * @param user Firebase user authenticated in the LoginActivity
     */
    private void updateUI(FirebaseUser user) {
        if (user == null)
            return;

        mFullNameTV.setText(user.getDisplayName());
        mEmailTV.setText(user.getEmail());
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
     * @return true is all fields are valid, false otherwise
     */
    private boolean validateInputs() {
        // Check name
        String name = getText(mFullNameTV);
        if (TextUtils.isEmpty(name)) {
            mFullNameTV.setError("Name is required");
            return false;
        }
        inputValues.put("name", name);

        // Check birthday
        String birthday = getText(mBirthdayTV);
        if (TextUtils.isEmpty(birthday)) {
            mBirthdayTV.setError("Birth date is required");
            return false;
        }
        inputValues.put("birthday", birthday);

        // TODO: proper CPF validation
        String cpf = getText(mCPF);
        if (TextUtils.isEmpty(cpf)) {
            mCPF.setError("CPF is required");
            return false;
        }
        inputValues.put("cpf", cpf);

        // Check user email
        String email = getText(mEmailTV);
        // Check blank
        if (TextUtils.isEmpty(email)) {
            mEmailTV.setError("Email is required");
            return false;
        }

        // Check invalid
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailTV.setError("Invalid email address");
            return false;
        }
        inputValues.put("email", email);

        // TODO: proper password validation
        String password = getText(mPassword);
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Password is required");
            return false;
        }
        inputValues.put("password", password);

        // Check if user agreed with the terms
        if (!mTermsAgreementRB.isChecked()) {
            mTermsAgreementRB.setError("You need to agree with the terms to use the app");
            return false;
        }

        // TODO: store inputValues in firestore

        return false;
    }

    /**
     * Stores field data into firestore
     */
    private void updateUser() {
        Toast.makeText(
                SignupActivity.this,
                "Action not implemented yet",
                Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * Extracts a trimmed string from a TextView element
     * @param v Field that contains the value to be extracted
     * @return TextView value as a trimmed string
     */
    private String getText(TextView v) {
        return v.getText().toString().trim();
    }
}
