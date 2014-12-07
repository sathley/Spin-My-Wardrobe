package com.athley.spinmywardrobe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appacitive.android.AppacitiveContext;
import com.appacitive.core.AppacitiveUser;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    @InjectView(R.id.login_username)
    EditText mUsername;

    @InjectView(R.id.login_password)
    EditText mPassword;

    @InjectView(R.id.login_login)
    Button mLogin;

    @InjectView(R.id.login_register)
    Button mRegister;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_login);
        AppacitiveContext.initialize("q8lSlsEZgOwsEK0FknL6Y0TMbIGo02ShAe8mkZu3k1Q=", Environment.sandbox, this);
        ButterKnife.inject(this);
        mContext = this;
        mLogin.setOnClickListener(loginClick);
        mRegister.setOnClickListener(registerClick);
    }

    OnClickListener registerClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent registerIntent = new Intent(mContext, SignupActivity.class);
            startActivity(registerIntent);
        }
    };

    OnClickListener loginClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            String username = mUsername.getText().toString();
            String password = mPassword.getText().toString();

            if(username.isEmpty()) {
                mUsername.setError("Username cannot be empty.");
                mUsername.findFocus();
                return;
            }
            if(password.isEmpty()) {
                mPassword.setError("Password cannot be empty.");
                return;
            }


            setProgressBarIndeterminateVisibility(true);
            AppacitiveUser.loginInBackground(username, password, -1, -1, new Callback<AppacitiveUser>() {
                @Override
                public void success(AppacitiveUser result) {
                    setProgressBarIndeterminateVisibility(false);
                    Toast.makeText(mContext, "Welcome " + result.getFirstName(), Toast.LENGTH_LONG).show();
                    SharedPreferences preferences = mContext.getSharedPreferences(getString(R.string.shared_preference_file), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong("user_id", result.getId());
                    editor.commit();
                    Intent mainIntent = new Intent(mContext, MainActivity.class);
                    startActivity(mainIntent);
                }

                @Override
                public void failure(AppacitiveUser result, Exception e) {
                    setProgressBarIndeterminateVisibility(false);
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    };
}



