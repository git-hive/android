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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Getting References From Login Activity
        Button mSignIn = findViewById(R.id.mSignIn);


        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }

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


    }


}
