package com.hive.hive.association.votes.tabs.SupportList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;
import com.hive.hive.model.user.User;

import java.util.ArrayList;

import static com.hive.hive.utils.Utils.getCharForNumber;

public class SupportListActivity extends AppCompatActivity {

    RecyclerView mSupportProfileRV;
    RecyclerView mSupportFilterRV;

    ArrayList<User> mUsers;
    ArrayList<String> mAlphabet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_list);

        // Init Dummy content
        initDataset();


        mSupportFilterRV = findViewById(R.id.superiorFilterRV);
        mSupportProfileRV = findViewById(R.id.supportProfileListRV);


        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);


        // Setting profile filter list content
        ProfileFilterAdapter filterListAdapter = new ProfileFilterAdapter(mAlphabet);

        mSupportFilterRV.setHasFixedSize(true);
        mSupportFilterRV.setLayoutManager(horizontalLayoutManager);
        mSupportFilterRV.setAdapter(filterListAdapter);


        LinearLayoutManager vertcalLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        // Setting profile list content
        ProfileListAdapter profileListAdapter = new ProfileListAdapter(mUsers);

        mSupportProfileRV.setHasFixedSize(true);
        mSupportProfileRV.setLayoutManager(vertcalLayoutManager);
        mSupportProfileRV.setAdapter(profileListAdapter);


    }

    private void initDataset(){
        mUsers = new ArrayList<>();
        for(int i=0;i<100;i++){
            User user = new User();
            user.setName("Guy Number "+i);
            mUsers.add(i, user);
        }

        mAlphabet = new ArrayList<>();
        mAlphabet.add(0, "Todos os 100");
        for(int i=1;i<27;i++){
            mAlphabet.add(i, getCharForNumber(i));
        }
    }
}
