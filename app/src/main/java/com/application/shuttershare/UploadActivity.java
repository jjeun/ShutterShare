package com.application.shuttershare;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;



/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: Upload activity that will show the user the progress of their
*       upload to the server.
*/


public class UploadActivity extends AppCompatActivity {

    // declaring global variables that will be used in the class
    private final String TAG = "UploadActivity";
    private ProgressBar progressBar;
    private String filePath = null;
    private String username = null;
    private String eventcode = null;
    private TextView uploadPercentage;
    private ImageView imgPreview;
    long totalSize = 0;


    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Receiving the data from previous activity
        Intent i = getIntent();


        // information that is being passed by previous activity captured in previous activity
        filePath = i.getStringExtra("filePath");
        username = i.getStringExtra("username");
        eventcode = i.getStringExtra("eventcode");

        // initializing the elements of the page to variables
        uploadPercentage = (TextView) findViewById(R.id.uploadPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);

        // condition that will execute if the filepath exists
        if (filePath != null) {
            previewMedia(); // calling previewMedia method
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();

            returnToMain();  // calling returnToMain method
        }

        new UploadFileToServer().execute();  // executing the class the UploadFileToServer
    }



    // method to disable on back press button
    @Override
    public void onBackPressed(){
        // intentionally left empty to disable back button
    }



    // method that will return user to the main activity without uploading the photo
    public void returnToMain(){
        // creating new intent and removing info from the stack so user cannot
        // return by pressing back button
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);  // starting new intent
    }



    // Displaying a preview of the image
    private void previewMedia() {
            // making the preview image visible
            imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options); // turning image into bitmap

            Bitmap orientedBitmap = ExifUtil.rotateBitmap(filePath, bitmap);  // adjuting orientation of the image

            imgPreview.setImageBitmap(orientedBitmap); // setting imageview to the adjust image
    }



    /* Class that will upload the file to the server*/
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        // pre execution method
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        // method to update the progress bar
        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            uploadPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        // method called to run in background
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();  // calling and returning value from method uploadFile.
        }


        // method that will upload file
        @SuppressWarnings("deprecation")
        private String uploadFile() {
            // declaring variables
            String responseString;

            // declaring and creating instances of http client / http post
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(UploadConfig.UPLOAD_URL);

            // try uploading
            try {

                // calling AndroidMultiPartEntity class that will monitor progress of upload
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                // creating File variable
                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("eventcode", new StringBody(eventcode));
                entity.addPart("username", new StringBody(username));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                // retrieving status code from server response
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        // method that executes once everything in class has run
        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            super.onPostExecute(result);

            Toast.makeText(getApplicationContext(),
                    "Image Uploaded Successfully!", Toast.LENGTH_SHORT)
                    .show();

            returnToMain();  // calling method returnToMain
        }
    }

}

