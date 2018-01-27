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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hive.hive.R;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView mFullNameTV;
    private TextView mBirthdayTV;
    private TextView mCPF;
    private TextView mEmailTV;
    private TextView mPassword;
    private RadioButton mTermsAgreementRB;
    private Button mSignUpComplete;

    private DatePickerDialog.OnDateSetListener mOnDateSetListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFullNameTV = findViewById(R.id.textViewSignUpFullName);
        mCPF = findViewById(R.id.textViewSignUpCPF);
        mEmailTV = findViewById(R.id.textViewSignUpEmail);
        mPassword = findViewById(R.id.textViewSignUpPassword);
        mTermsAgreementRB = findViewById(R.id.radioButtonSignUpTermsAgreement);

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

        mAuth = FirebaseAuth.getInstance();
        updateUI(mAuth.getCurrentUser());

        mSignUpComplete = findViewById(R.id.buttonSignUpComplete);
        mSignUpComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
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

    private void updateUI(FirebaseUser user) {
        if (user == null)
            return;

        mFullNameTV.setText(user.getDisplayName());
        mEmailTV.setText(user.getEmail());
    }

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

    private void updateUser() {
        // Check name
        String name = mFullNameTV.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            mFullNameTV.setError("Name is required");
            return;
        }

        // Check bithday
        String bithday = mBirthdayTV.getText().toString().trim();
        if (TextUtils.isEmpty(bithday)) {
            mBirthdayTV.setError("Birth date is required");
            return;
        }

        // TODO: proper CPF validation
        String cpf = mCPF.getText().toString().trim();
        if (TextUtils.isEmpty(cpf)) {
            mCPF.setError("CPF is required");
            return;
        }

        // Check user email
        String email = mEmailTV.getText().toString().trim();
        // Check blank
        if (TextUtils.isEmpty(email)) {
            mEmailTV.setError("Email is required");
            return;
        }

        // Check invalid
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailTV.setError("Invalid email address");
            return;
        }

        // TODO: proper password validation
        String password = mPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Password is required");
            return;
        }

        // Check if user agreed with the terms
        if (!mTermsAgreementRB.isChecked()) {
            mTermsAgreementRB.setError("You need to agree with the terms to use the app");
            return;
        }
    }
}
