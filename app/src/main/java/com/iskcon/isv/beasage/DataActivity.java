package com.iskcon.isv.beasage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class DataActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());


        setContentView(R.layout.activity_data);

        if(AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            Profile profile = Profile.getCurrentProfile();
            profile.getFirstName();
            TextView tvName = (TextView) findViewById(R.id.tvFirstName);
            ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
            tvName.setText(profile.getName());
            ivProfile.setImageURI(profile.getProfilePictureUri(200, 200));
        }


        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                        loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                profile.getFirstName();
                TextView tvName = (TextView) findViewById(R.id.tvFirstName);
                TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
                ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
                tvName.setText(profile.getName());
                ivProfile.setImageURI(profile.getProfilePictureUri(200, 200));

                Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
