package com.hive.hive.utils;

import android.content.Context;
import android.support.annotation.NonNull;
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

    private final static String basePath = "gs://hive-mvp.appspot.com/";

    /**
     * Empty private constructor
     */
    private FileUtils(){}


    public static void downloadFile(final Context context, final String fileName, final String fileExtension){

        //- Activity Log
        Log.d(TAG, "Starting download of file \"" + fileName + "." + fileExtension + "\"...");

        //--- Gets instace of Firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        //--- Gets ref to desired file
        StorageReference storageRef = storage.getReferenceFromUrl(basePath).child(fileName + "." + fileExtension);


        try {
            final File localFile = new File(context.getFilesDir(), fileName + "." + fileExtension);
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

                        //-- Operation log
                        Log.d(TAG, "File \"" + fileName + "." + fileExtension + "\" sucessfully downloaded.");

                        Toast.makeText(context, "Transfered " + Long.toString(taskSnapshot.getBytesTransferred()/(1024*1024)) + " Mbytes" , Toast.LENGTH_SHORT).show();

                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    //-- System log
                    Log.e(TAG, "Error downloading file \"" + fileName + "." + fileExtension + "\".");

                }
            });
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Upload File to Firebase Storage
     * @param context - context used to open FileOutputStream
     * @param path - path to file
     * @param fileName - name of the file
     * @param fileExtension - extension of the file
     */
    public static void uploadFile(final Context context, final String path, final String fileName, final String fileExtension){

        //--- Gets instace of Firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        //--- Gets ref to desired file
        StorageReference storageRef = storage.getReferenceFromUrl(basePath).child(fileName + "." + fileExtension);

        //--- File Stream
        FileInputStream inputStream;

        try{
            //-- Gets stream to read from internal directory
            inputStream = context.openFileInput(path + fileName + "." + fileExtension);

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

                    //-- Operation log
                    Log.d(TAG, "File \"" + fileName + "." + fileExtension + "\" sucessfully uploaded.");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //-- Operation log
                    Log.d(TAG, "Error uploading file \"" + fileName + "." + fileExtension + "\".");
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }


    }

}
