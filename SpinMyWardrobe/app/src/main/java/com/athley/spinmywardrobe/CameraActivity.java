package com.athley.spinmywardrobe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appacitive.core.AppacitiveConnection;
import com.appacitive.core.AppacitiveFile;
import com.appacitive.core.AppacitiveObject;
import com.appacitive.core.model.BatchCallRequest;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.FileUploadUrlResponse;

import com.squareup.picasso.Picasso;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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

    @InjectView(R.id.camera_image_name)
    TextView mName;

    private Context mContext;

    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_camera);
        ButterKnife.inject(this);
        mContext = this;

        mPath = getIntent().getStringExtra("path");
        Picasso.with(this).load(new File(mPath)).into(mPreview);

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
            AppacitiveFile.getUploadUrlInBackground(getMimeType(mPath), mName.getText().toString(), -1, new Callback<FileUploadUrlResponse>() {
                @Override
                public void success(FileUploadUrlResponse result) {
                    Toast.makeText(mContext, "Adding item...", Toast.LENGTH_SHORT).show();
                    setProgressBarIndeterminateVisibility(true);
                    new UploadFileTask().execute(result.url, mPath);

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

    private static String getMimeType(String url)
    {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private class UploadFileTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            String url = strings[0];
            String path = strings[1];
            int responseCode=0;
            try {
                URL fileUrl = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) fileUrl.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", getMimeType(path));
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();

                File upFile = new File(path);
                FileInputStream fis = new FileInputStream(upFile);
                BufferedInputStream bfis = new BufferedInputStream(fis);
                byte[] buffer = new byte[1024];
                int bufferLength = 0;

                while ((bufferLength = bfis.read(buffer)) > 0) {
                    os.write(buffer, 0, bufferLength);

                }
                os.close();

                responseCode = urlConnection.getResponseCode();

            }
            catch (Exception e)
            {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return responseCode;
        }

        @Override
        protected void onPostExecute(Integer result) {
            setProgressBarIndeterminateVisibility(false);
            if(result == 200)
            {
                createConnections();
            }
        }
    }

    private void createConnections()
    {
        final SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.shared_preference_file), Context.MODE_PRIVATE);
        long userId = preferences.getLong("user_id", 0);
        AppacitiveObject item = new AppacitiveObject(mSpinner.getSelectedItem().toString());
        item.setStringProperty("name", mName.getText().toString());
        item.setStringProperty("picture", mName.getText().toString());

        new AppacitiveConnection("owns_" + mSpinner.getSelectedItem().toString())
        .fromExistingUser("user", userId)
        .toNewObject(mSpinner.getSelectedItem().toString(), item)
        .createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(AppacitiveConnection result) {
                Toast.makeText(mContext, "Item added successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(AppacitiveConnection result, Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
