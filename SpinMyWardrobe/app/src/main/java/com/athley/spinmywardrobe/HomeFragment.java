package com.athley.spinmywardrobe;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

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
            Intent cameraIntent = new Intent(getActivity(), CameraActivity.class);
            getActivity().startActivity(cameraIntent);
        }
    };


}
