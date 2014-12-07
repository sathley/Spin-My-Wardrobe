package com.athley.spinmywardrobe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appacitive.android.AppacitiveContext;
import com.appacitive.core.AppacitiveUser;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SignupActivity extends Activity {

    @InjectView(R.id.signup_name)
    EditText mName;

    @InjectView(R.id.signup_email)
    EditText mEmail;

    @InjectView(R.id.signup_password)
    EditText mPassword;

    @InjectView(R.id.signup_signup)
    Button mSignup;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_signup);
        mContext = this;
        ButterKnife.inject(this);
        mSignup.setOnClickListener(signupCLickListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener signupCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String username = mName.getText().toString();
            String password = mPassword.getText().toString();
            String email = mEmail.getText().toString();

            if(username.isEmpty() || email.isEmpty() || password.isEmpty())
                return;

            AppacitiveUser user = new AppacitiveUser();
            user.setPassword(password);
            user.setFirstName(username);
            user.setUsername(username);
            user.setEmail(email);
            setProgressBarIndeterminateVisibility(true);
            user.signupInBackground(new Callback<AppacitiveUser>() {
                @Override
                public void success(AppacitiveUser result) {
                    setProgressBarIndeterminateVisibility(false);
                    Toast.makeText(mContext, "Success!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
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
