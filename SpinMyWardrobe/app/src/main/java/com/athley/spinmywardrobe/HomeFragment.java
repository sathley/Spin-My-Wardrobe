package com.athley.spinmywardrobe;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @InjectView(R.id.home_calendar)
    ImageButton mCalendar;

    @InjectView(R.id.home_camera)
    ImageButton mCamera;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }



    static final int REQUEST_IMAGE_CAPTURE = 1;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, rootView);
        mCalendar.setOnClickListener(calendarClick);
        mCamera.setOnClickListener(cameraClick);
        getActivity().getActionBar().setTitle(R.string.title_home);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    View.OnClickListener calendarClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent calendarIntent = new Intent(getActivity(), CalendarActivity.class);
            getActivity().startActivity(calendarIntent);
        }
    };

    View.OnClickListener cameraClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Intent cameraIntent = new Intent(getActivity(), CameraActivity.class);
//            cameraIntent.putExtra("image", imageBitmap);
            cameraIntent.putExtra("path", mCurrentPhotoPath);
            startActivity(cameraIntent);
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = Uri.fromFile(image).getPath();
        return image;
    }
}
