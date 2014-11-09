package com.athley.spinmywardrobe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.appacitive.core.AppacitiveFile;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.FileUploadUrlResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class CameraActivity extends Activity {

    @InjectView(R.id.category_spinner)
    Spinner mSpinner;

    @InjectView(R.id.camera_preview)
    ImageView mPreview;

    @InjectView(R.id.camera_confirm)
    Button mConfirm;

    @InjectView(R.id.camera_deny)
    Button mDeny;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.inject(this);
        mContext = this;
        Bitmap bitmap = (Bitmap) getIntent().getExtras().get("image");
        mPreview.setImageBitmap(bitmap);
        mConfirm.setOnClickListener(confirmClick);
        mDeny.setOnClickListener(denyClick);
        List<String> categories = new ArrayList<String>();
        for(Category c : Category.values())
            categories.add(c.toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categories);
        mSpinner.setAdapter(adapter);
    }

    private View.OnClickListener confirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AppacitiveFile.getUploadUrlInBackground("image/jpeg", null, -1, new Callback<FileUploadUrlResponse>() {
                @Override
                public void success(FileUploadUrlResponse result) {
                    Toast.makeText(mContext, "File upload functionality is under construction.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(FileUploadUrlResponse result, Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private View.OnClickListener denyClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
