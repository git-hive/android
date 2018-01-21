package com.hive.hive.login;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hive.hive.R;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView mFullNameTV;
    private TextView mEmailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFullNameTV = findViewById(R.id.textViewSignUpFullName);
        mEmailTV = findViewById(R.id.textViewSignUpEmail);

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

        Button mCompleteSignUp = findViewById(R.id.mCompleteSignUp);
        mCompleteSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = SignupActivity.this;
                String textToShow = "DATABASE CONNECTION NOT IMPLEMENTED YET!";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUI(FirebaseUser user) {
        if (user == null)
            return;

        mFullNameTV.setText(user.getDisplayName());
        mEmailTV.setText(user.getEmail());
    }
}
