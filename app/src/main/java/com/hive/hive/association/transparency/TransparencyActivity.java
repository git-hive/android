package com.hive.hive.association.transparency;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hive.hive.R;
import com.hive.hive.association.transparency.tabs.staff.StaffFragment;
import com.hive.hive.utils.FileUtils;

public class TransparencyActivity extends AppCompatActivity implements StaffFragment.OnListFragmentInteractionListener {

    private static final String TAG = TransparencyActivity.class.getSimpleName();

    // Superior Tab items
    private TextView mTitleTV;
    private ImageView mUpButtonIV;
    private ImageView mSearchIV;

    // Tab components
    private TabLayout transparencyTL;
    private ViewPager transparencyVP;
    private TransparencyFragmentPagerAdapter mViewPagerAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparency);

        mTitleTV = findViewById(R.id.transparency_activity_title_TV);
        mUpButtonIV = findViewById(R.id.up_button_transparency_IV);
        mSearchIV = findViewById(R.id.search_transparency_IV);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        transparencyVP = (ViewPager) findViewById(R.id.transparency_VP);
        mViewPagerAdapter = new TransparencyFragmentPagerAdapter(
                getSupportFragmentManager(),
                TransparencyActivity.this,
                this
        );
        transparencyVP.setAdapter(mViewPagerAdapter);
        final TransparencyFragmentPagerAdapter ref = mViewPagerAdapter;
        transparencyVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                  ref.updateFAB(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

    }


    @Override // android recommended class to handle permissions
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case FileUtils.STORAGE_REQUEST_CODE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "Permission granted");
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.uujm
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Permission not granted");

                }
            }
        }
    }

    @Override
    public void onListFragmentInteraction(StaffFragment.Staff item) {
        Toast.makeText(this, item.name, Toast.LENGTH_SHORT).show();
    }
}
