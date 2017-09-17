package com.nilanshi.nigam.personalassistant.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.nilanshi.nigam.personalassistant.R;

import org.json.JSONArray;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private String displayName;
    private Uri photoUrl;
    private String email;
    private String number;
    private ImageView ivPicture;
    private TextView etUser;
    private TextView etEmail;
    private TextView etNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        etUser = (TextView) findViewById(R.id.etUser);
        etEmail = (TextView) findViewById(R.id.etEmail);
        etNumber = (TextView) findViewById(R.id.etNumber);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        displayName = user.getDisplayName();
        photoUrl = user.getPhotoUrl();
        email = user.getEmail();
        number = user.getPhoneNumber();
        Glide.with(this).load(photoUrl).into(ivPicture);
        //ivPicture.setImageURI(photoUrl);
        etEmail.setText(email);
        etUser.setText(displayName);
        etNumber.setText(number);

    }
/*Facebook details*/
}
