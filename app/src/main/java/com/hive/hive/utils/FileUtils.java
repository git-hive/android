package com.hive.hive.utils;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
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
    private FileUtils(){}





    public static void downloadFile(
            @NonNull final Activity activity,
            @NonNull final Context context,
            @NonNull final String fileName,
            @NonNull final String fileExtension)
    {



        //--- Gets instace of Firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        //--- Gets ref to desired file
        StorageReference storageRef = storage.getReferenceFromUrl(basePath).child(fileName + "." + fileExtension);

        try {
            final File localFile = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(),
                    fileName + "." + fileExtension
            );

            //- Activity Log
            Log.d(TAG, "Starting download of file \"" + localFile.getAbsolutePath() + "\"...");

            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    //DO SOMETHING

                    //--- File Stream
                    FileOutputStream outputStream;

                    try {

                        //-- Gets stream to write to internal directory
                        outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);

                        //-- Write contents to stream
                        outputStream.write(taskSnapshot.toString().getBytes());

                        //-- Close stream
                        outputStream.close();

                        //- Activity log
                        Log.d(TAG, "File \"" + fileName + "." + fileExtension + "\" sucessfully downloaded.");

                        Log.d(TAG, "Downloaded " + Long.toString(taskSnapshot.getBytesTransferred() / (1024 * 1024)) + " Mbytes");

                        DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                        if (manager != null){
                            manager.addCompletedDownload(fileName + "." + fileExtension, "PDF", false, "pdf", localFile.getAbsolutePath(), localFile.length(), true);
                            Log.d(TAG, "Download adicionado no manager");
                        }
                        else{
                            Log.d(TAG, "Download Manager == null");
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    //-- System log
                    Log.e(TAG, "Error downloading file \"" + fileName + "." + fileExtension + "\" - ");
                    exception.printStackTrace();

                }
            });
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Upload File to Firebase Storage
     * @param context - context used to open FileOutputStream
     * @param fileName - name of the file
     * @param fileExtension - extension of the file
     */
    public static void uploadFile(final Context context, final String fileName, final String fileExtension){

        //- Activity Log
        Log.d(TAG, "Starting to upload file \"" + fileName + "." + fileExtension + "\"...");

        //--- Gets instace of Firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        //--- Gets ref to desired file
        StorageReference storageRef = storage.getReferenceFromUrl(basePath).child(fileName + "." + fileExtension);

        //--- File Stream
        FileInputStream inputStream;

        try{
            //-- Gets stream to read from internal directory
            Log.d(TAG, "Nome: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + fileName + "." + fileExtension );
            inputStream = new FileInputStream(
                    new File(
                            Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS).toString() +
                                    "/" +
                                    fileName +
                                    "." +
                                    fileExtension
                    )
            );

            //-- Byte array to store data
            byte[] data = new byte[inputStream.available()];

            //-- Read contents from stream to array
            int bytesRead = inputStream.read(data);

            //-- Close input stream
            inputStream.close();

            //-- Uploads array of read bytes
            UploadTask uploadTask = storageRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //- Activity log
                    Log.d(TAG, "File \"" + fileName + "." + fileExtension + "\" sucessfully uploaded.");

                    Toast.makeText(context, "Uploaded " + Long.toString(taskSnapshot.getBytesTransferred()/(1024*1024)) + " Mbytes" , Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //- Activity log
                    Log.e(TAG, "Error uploading file \"" + fileName + "." + fileExtension + "\".");
                }
            });
        } catch (Exception e){
            e.printStackTrace();
            Log.wtf(TAG, "Error uploading file");
        }


    }

}
