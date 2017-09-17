package com.nilanshi.nigam.personalassistant.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by HP on 8/2/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public static final String PACKAGE_NAME = "com.project.nigam.demo";
    public static final String APP_NAME = "DEMO APP";
    public Context context=this;
    private ProgressDialog dialog;


    public void showProgressDialog(String msg) {
        dialog = new ProgressDialog(this);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void hideProgressDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public void message(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void messageBar(View v, String msg) {
        //require design library
        //Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
    }

    //to throw alert box
    public void showAlert(String title, String msg, String yes, String no) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
    }

    //to check errors
    public void log(String data) {
        Log.d("com.project.nigam.demo", data);
    }

    public void onDestroyView() {
    }
}
