package com.nilanshi.nigam.personalassistant.activity;

import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.nilanshi.nigam.personalassistant.R;
import com.nilanshi.nigam.personalassistant.util.VoiceControl;

public class SettingActivity extends BaseActivity {

    private ImageView ivPitch;
    private ImageView ivLang;
    private ImageView ivTheme;
    private ImageView ivBattery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivPitch = (ImageView) findViewById(R.id.ivPitch);
        ivLang = (ImageView) findViewById(R.id.ivLang);
        ivTheme = (ImageView) findViewById(R.id.ivTheme);
        ivBattery = (ImageView) findViewById(R.id.ivBattery);
            /*pitch controller*/
        ivPitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pitch = new Intent(SettingActivity.this, VoiceControl.class);
                startActivity(pitch);
            }
        });
            /*language controller*/
        ivLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lang = new Intent(SettingActivity.this, LanguagePreference.class);
                startActivity(lang);
            }
        });
            /*battery saver*/
        ivBattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i=new Intent(SettingActivity.this,)
            }
        });

    }

}
