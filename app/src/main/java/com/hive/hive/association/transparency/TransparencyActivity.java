package com.hive.hive.association.transparency;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hive.hive.R;

public class TransparencyActivity extends AppCompatActivity {
    // Superior Tab items
    TextView mTitleTV;
    ImageView mUpButtonIV;
    ImageView mSearchIV;

    // Tab components
    TabLayout transparencyTL;
    ViewPager transparencyVP;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparency);

        mTitleTV = findViewById(R.id.transparency_activity_title_TV);
        mUpButtonIV = findViewById(R.id.up_button_transparency_IV);
        mSearchIV = findViewById(R.id.search_transparency_IV);


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        transparencyVP = (ViewPager) findViewById(R.id.transparency_VP);
        transparencyVP.setAdapter(new TransparencyFragmentPagerAdapter(getSupportFragmentManager(),
                TransparencyActivity.this));

        // Give the TabLayout the ViewPager
        transparencyTL = (TabLayout) findViewById(R.id.sliding_tabs_transparency_TL);
        transparencyTL.setupWithViewPager(transparencyVP);

        // Exit from activity
        mUpButtonIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSearchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TransparencyActivity.this, "Search not implemented yet.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // Set Style and Make up things
        styleThings();

    }

    public void styleThings(){
        SpannableString content = new SpannableString(getResources().getString(R.string.transparency_activity_title));
        content.setSpan(new UnderlineSpan(), 0+1, content.length()-1, 0);
        mTitleTV.setText(content);
    }
}
