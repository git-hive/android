package com.hive.hive.utils.circularFilter;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.hive.hive.R;
import com.hive.hive.utils.circularFilter.scale.ScaleLayoutActivity;


/**
 * Created by birck on 18/02/18.
 */

public class MainFragment extends AppCompatActivity implements View.OnClickListener {
    public final static String INTENT_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_circle).setOnClickListener(this);
        findViewById(R.id.bt_circle_scale).setOnClickListener(this);
        findViewById(R.id.bt_elevate_scale).setOnClickListener(this);
        findViewById(R.id.bt_gallery).setOnClickListener(this);
        findViewById(R.id.bt_rotate).setOnClickListener(this);
        findViewById(R.id.bt_scale).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_scale:
                startActivity(ScaleLayoutActivity.class, v);
                break;
        }
    }

    private void startActivity(Class clz, View view) {
        Intent intent = new Intent(this, clz);
        if (view instanceof AppCompatButton) {
            intent.putExtra(INTENT_TITLE, ((AppCompatButton) view).getText());
        }
        startActivity(intent);
    }
}
