package com.hive.hive.utils;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.support.v4.content.FileProvider.getUriForFile;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Class with file upload and download methods
 * Created by Nícolas Oreques de Araujo on 4/2/18.
 */

public abstract class FileUtils {

    private final static String TAG = FileUtils.class.getSimpleName();
    public final static int STORAGE_REQUEST_CODE = 99;

    private final static String basePath = "gs://hive-mvp.appspot.com/";

    /**
     * Empty private constructor
     */
    private FileUtils() {
    }


    public static void downloadFile(
            @NonNull final Activity activity,
            @NonNull final Context context,
            @NonNull final String fileName,
            @NonNull final String fileExtension,
            @NonNull final ProgressBar progressBar) {


        //--- Gets instace of Firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        //--- Gets ref to desired file
        StorageReference storageRef = storage.getReference().child(fileName + fileExtension);

        File docPath = new File(context.getFilesDir(), "files");
        if (!docPath.exists()) docPath.mkdir();
        try {
            File newFile = File.createTempFile(fileName, fileExtension, docPath);
            storageRef.getFile(newFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Uri contentUri = getUriForFile(context, "com.hive.hive.fileprovider", newFile);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(contentUri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    activity.startActivity(intent);
                    // Local temp file has been created
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    int fprogress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                    progressBar.setProgress(fprogress);
                    Log.d(TAG, "progress " + fprogress);
                    if (fprogress == 100) {
                        progressBar.setVisibility(View.GONE);
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
