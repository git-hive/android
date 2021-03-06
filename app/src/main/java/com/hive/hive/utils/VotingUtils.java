package com.hive.hive.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hive.hive.R;
import com.hive.hive.model.user.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VotingUtils {

    //loads user data, and fill some views
    public static void fillUnfoldableUser(DocumentReference userRef, View view, Context context){
        TextView suggestedByTV = view.findViewById(R.id.expandable_suggestedByTV);
        ImageView suggestedByIV = view.findViewById(R.id.expandable_suggestedByIV);
        TextView suggestedTV = view.findViewById(R.id.expandable_content_avatar_title);

        if(userRef != null) {
            suggestedByIV.setVisibility(View.VISIBLE);
            suggestedByTV.setVisibility(View.VISIBLE);
            suggestedTV.setText(context.getResources().getString(R.string.suggested));

            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        suggestedByTV.setText(user.getName());
                        ProfilePhotoHelper.loadImage(view.getContext().getApplicationContext(), suggestedByIV, user.getPhotoUrl());
                        //Log.d(RequestAdapter.class.getSimpleName(), user.getPhotoUrl());
                    }
                }
            });
        }else {
            suggestedByIV.setVisibility(View.GONE);
            suggestedByTV.setVisibility(View.GONE);
            suggestedTV.setText(context.getResources().getString(R.string.created_by_association));
        }
    }

    // Use to get the drawables programmatically
    public static void initPossibleCategoryIcons(Context context, HashMap<String, String> iconsDrawablePaths, HashMap<String, Integer> iconsDrawable){
        List<String> iconsList = Arrays.asList("services", "cleaning", "gardening", "security");

        for (String icon: iconsList) {
            iconsDrawablePaths.put(icon, "ic_icones_"+icon+"_white");
            int imageResource = context.getResources()
                    .getIdentifier(iconsDrawablePaths.get(icon), "drawable", context.getPackageName());
            iconsDrawable.put(icon, imageResource);
        }
    }
    public static int getDrawable(String icon, HashMap<String, Integer> iconsDrawable){
        return iconsDrawable.get(icon);
    }
}
