package com.hive.hive.utils.circularFilter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hive.hive.R;
import com.leochuan.ViewPagerLayoutManager;


/**
 * Created by birck on 18/02/18.
 */




public abstract class BaseFragment<V extends ViewPagerLayoutManager, S extends SettingPopUpWindow>
        extends Fragment {
    private RecyclerView recyclerView;
    private V viewPagerLayoutManager;
    private S settingPopUpWindow;

    protected abstract V createLayoutManager();

    protected abstract S createSettingPopUpWindow();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment, container, false);


        //setTitle(getIntent().getCharSequenceExtra(MainFragment.INTENT_TITLE));
        recyclerView = view.findViewById(R.id.recyclerMenu);
        viewPagerLayoutManager = createLayoutManager();
        //Set to scroll Infinitely
        viewPagerLayoutManager.setEnableBringCenterToFront(true);
        viewPagerLayoutManager.setSmoothScrollbarEnabled(false);
        viewPagerLayoutManager.setInfinite(true);
        recyclerView.setAdapter(new DataAdapter());
        recyclerView.setLayoutManager(viewPagerLayoutManager);



        return view;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.settings, menu);
//        MenuItem settings = menu.findItem(R.id.setting);
//        VectorDrawableCompat settingIcon =
//                VectorDrawableCompat.create(getResources(), R.drawable.ic_settings_white_48px, null);
//        settings.setIcon(settingIcon);
//        return super.onCreateOptionsMenu(menu);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                showDialog();
                return true;
            case android.R.id.home:
                //onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        if (settingPopUpWindow == null) {
            settingPopUpWindow = createSettingPopUpWindow();
        }
        settingPopUpWindow.showAtLocation(recyclerView, Gravity.CENTER, 0, 0);
    }

    public V getViewPagerLayoutManager() {
        return viewPagerLayoutManager;
    }

    public S getSettingPopUpWindow() {
        return settingPopUpWindow;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (settingPopUpWindow != null && settingPopUpWindow.isShowing())
            settingPopUpWindow.dismiss();
    }
}