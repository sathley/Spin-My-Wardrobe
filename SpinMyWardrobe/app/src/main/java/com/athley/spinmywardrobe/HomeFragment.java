package com.athley.spinmywardrobe;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.net.URI;

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
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Intent cameraIntent = new Intent(getActivity(), CameraActivity.class);
            cameraIntent.putExtra("image", imageBitmap);
            startActivity(cameraIntent);
        }
    }
}
