package com.hive.hive.profiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hive.hive.R;
import com.hive.hive.login.LoginActivity;
import com.hive.hive.model.user.User;
import com.hive.hive.utils.DocReferences;
import com.hive.hive.utils.ProfilePhotoHelper;

public class UserProfileActivity extends AppCompatActivity {

    private TextView mNameTV;
    private TextView mAssociation;
    private TextView mRole;
    private TextView mLote;
    private ImageView mUserAvatar;
    private Button mLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initViews();
        ProfilesFirebaseHandle.getUser(DocReferences.getUserRef(), this);
        onClicks();
    }
    private void initViews(){
        mUserAvatar = findViewById(R.id.userAvatar);
        mNameTV = findViewById(R.id.nameTV);
        mAssociation = findViewById(R.id.associationTV);
        mRole = findViewById(R.id.roleTV);
        mLote = findViewById(R.id.loteTV);
        mLogout = findViewById(R.id.logoutBT);
    }
    private void onClicks(){
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
                }

            }
        });
    }
    public void updateUI(User user){
        ProfilePhotoHelper.loadImage(this, mUserAvatar, user.getPhotoUrl());
        mNameTV.setText(user.getName());
        LinearLayout associationLL = findViewById(R.id.associationLL);
        LinearLayout roleLL = findViewById(R.id.roleLL);
        LinearLayout lotLL = findViewById(R.id.lotLL);
        ProgressBar loadedUserPB = findViewById(R.id.loadedUserPB);

        mLogout.setVisibility(View.VISIBLE);
        associationLL.setVisibility(View.VISIBLE);
        roleLL.setVisibility(View.VISIBLE);
        lotLL.setVisibility(View.VISIBLE);
        loadedUserPB.setVisibility(View.GONE);

    }
}
