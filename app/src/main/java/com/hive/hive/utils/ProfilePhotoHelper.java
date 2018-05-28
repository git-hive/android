package com.hive.hive.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by vplentz on 19/03/18.
 */
public class ProfilePhotoHelper{
    private static String TAG = ProfilePhotoHelper.class.getSimpleName();


    public static void loadImage(Context context, ImageView imageView, String url){
        Picasso.with(context).load(url).fit().centerCrop()
                .placeholder(R.drawable.ic_profile_photo)
                .error(R.drawable.ic_profile_photo)
                .transform(new CircleTransform())
                .into(imageView);
    }
    public static void updateProfileUrl(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getPhotoUrl() != null)
            FirebaseFirestore.getInstance().collection("users").document(user.getUid()).update("photoUrl", user.getPhotoUrl().toString());
        else
            FirebaseFirestore.getInstance().collection("users").document(user.getUid()).update("photoUrl", null);
    }

    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}

