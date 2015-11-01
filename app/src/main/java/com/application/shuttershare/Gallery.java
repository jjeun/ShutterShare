package com.application.shuttershare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;



/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: Gallery Fragment - Will hold gallery functionality
*/


public class Gallery extends Fragment {

    // Declaring variables to be used in the class
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "GalleryFragment";
    public static final int LOAD_GALLERY_IMAGE = 1;
    private String outPutfileUri; // file url to store image

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page fragment page number
     * @return A new instance of fragment Gallery.
     */

    public static Gallery newInstance(int page) {
        Gallery fragment = new Gallery();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }


    public Gallery() {
        // Required empty public constructor
    }



    // onCreate variable for fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    // onCreateView method for fragment. Handles what will be displayed in the fragment page
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        SharedPreferences shared = this.getActivity().getSharedPreferences("SHUTTER_SHARE", Context.MODE_PRIVATE);

        String eventDescription = shared.getString("description", "ShutterShare");
        TextView event = (TextView) rootView.findViewById(R.id.eventDescription);
        event.setText(eventDescription);
        event.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        event.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        ImageButton buttonLoadImage = (ImageButton) rootView.findViewById(R.id.galleryButton);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, LOAD_GALLERY_IMAGE);

            }
        });
        return rootView;
    }


    // to receive result of camera
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if the result is capturing Image
        if (requestCode == LOAD_GALLERY_IMAGE) {
            if (resultCode == getActivity().RESULT_OK) {

                Uri image = data.getData();

                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getActivity().getContentResolver().query(image,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                outPutfileUri = cursor.getString(columnIndex);
                cursor.close();

                Toast.makeText(getActivity().getApplicationContext(),
                        "Image Upload Page", Toast.LENGTH_SHORT)
                        .show();

                launchUpload();

            } else if (resultCode == getActivity().RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getActivity().getApplicationContext(),
                        "Gallery Image Upload Cancelled", Toast.LENGTH_SHORT)
                        .show();

            } else {

                // failed to capture image
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sorry! Gallery Image Failed to Upload", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    // method to launch upload
    private void launchUpload(){
        SharedPreferences shared = this.getActivity().getSharedPreferences("SHUTTER_SHARE", Context.MODE_PRIVATE);

        String username = shared.getString("username", "");
        String eventcode = shared.getString("eventcode", "");
        Intent i = new Intent(this.getActivity(), UploadActivity.class);

        i.putExtra("filePath", outPutfileUri);
        i.putExtra("username", username);
        i.putExtra("eventcode", eventcode);
        startActivity(i);
    }
}
