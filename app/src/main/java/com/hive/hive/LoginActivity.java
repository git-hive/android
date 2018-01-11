package com.hive.hive;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Getting References From Login Activity
        Button mSignIn = findViewById(R.id.mSignIn);
        TextView mSignUp = findViewById(R.id.mSignUp);


        mSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = LoginActivity.this.getApplicationContext();
                Class destinationActivity = MainActivity.class;
                Intent startNewActivityIntent = new Intent(context, destinationActivity);
                startNewActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startNewActivityIntent);
                finish(); // Call once you redirect to another activity
            }
        });

        mSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = LoginActivity.this.getApplicationContext();
                Class destinationActivity = SignupActivity.class;
                Intent startNewActivityIntent = new Intent(context, destinationActivity);
                //startNewActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startNewActivityIntent);
                //finish(); // Call once you redirect to another activity
            }
        });


    }


}
