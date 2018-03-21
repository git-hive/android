package com.hive.hive.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;

/**
 * Created by vplentz on 19/03/18.
 */
@GlideModule
public class ProfilePhotoHelper extends AppGlideModule{
    private static String TAG = ProfilePhotoHelper.class.getSimpleName();


    public static void loadImage(Context context, ImageView imageView, String url){
        GlideApp
                .with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_profile_photo)
                .fallback(R.drawable.ic_profile_photo)
                .into(imageView);
    }
    public static void updateProfileUrl(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("users").document(user.getUid()).update("photoUrl", user.getPhotoUrl().toString());
    }
}
