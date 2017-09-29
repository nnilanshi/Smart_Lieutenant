package com.nilanshi.nigam.personalassistant.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nilanshi.nigam.personalassistant.R;

public class SignupActivity extends BaseActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText etMail;
    private EditText etPass;
    private Button btnReg;
    private EditText etConfPass;
    private int id;
    private EditText etUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etUser = (EditText) findViewById(R.id.etUser);
        etMail = (EditText) findViewById(R.id.etMail);
        etPass = (EditText) findViewById(R.id.etPass);
        etConfPass = (EditText) findViewById(R.id.etConfPass);
        btnReg = (Button) findViewById(R.id.btnReg);
        btnReg.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent i = new Intent(SignupActivity.this, CommandActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        };
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }




    @Override
    public void onClick(View v) {
        String email = etMail.getText().toString();
        String pass = etPass.getText().toString();
        String confpass = etConfPass.getText().toString();
        String user = etUser.getText().toString();
        if (user.isEmpty()) {
            Toast.makeText(this, "User field is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.isEmpty()) {
            Toast.makeText(this, "Password field is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (confpass.isEmpty()) {
            Toast.makeText(this, "Confirmation of password is not done", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.compareTo(confpass)!=0) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        id = v.getId();
        if (id == R.id.btnReg) {
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(SignupActivity.this,"User Successfully Registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            Intent i=new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(i);

        }
    }
}
