package com.hive.hive.association.transparency;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hive.hive.R;
import com.hive.hive.association.transparency.TransparencyFragmentPagerAdapter;

public class TransparencyActivity extends AppCompatActivity {
    // Superior Tab items
    TextView mHeaderAssembleia;
    ImageView mExitVotesIV;
    ImageView mSearchVotesIV;

    // Tab components
    TabLayout tabLayout;
    ViewPager viewPager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votes);

        mHeaderAssembleia = findViewById(R.id.assembleiaTV);
        mExitVotesIV = findViewById(R.id.exitVotesIV);
        mSearchVotesIV = findViewById(R.id.searchVotesIV);


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TransparencyFragmentPagerAdapter(getSupportFragmentManager(),
                TransparencyActivity.this));

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Exit from activity
        mExitVotesIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSearchVotesIV.setOnClickListener(new View.OnClickListener() {
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
        SpannableString content = new SpannableString(getResources().getString(R.string.assembleia));
        content.setSpan(new UnderlineSpan(), 0+1, content.length()-1, 0);
        mHeaderAssembleia.setText(content);
    }
}