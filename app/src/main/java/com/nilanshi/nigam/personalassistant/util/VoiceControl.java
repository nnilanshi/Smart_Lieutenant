package com.nilanshi.nigam.personalassistant.util;



        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.Toast;

        import com.nilanshi.nigam.personalassistant.R;
        import com.nilanshi.nigam.personalassistant.activity.BaseActivity;

        import io.feeeei.circleseekbar.CircleSeekBar;

public class VoiceControl extends BaseActivity {


    public int pitch=0;
    public SharedPreferences app_pref;
    private CircleSeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_control);
        seekBar = (CircleSeekBar) findViewById(R.id.seekbar);
        app_pref = getSharedPreferences("app_pref",MODE_PRIVATE);

        seekBar.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onChanged(CircleSeekBar circleSeekBar, int pitch) {
                //Toast.makeText(VoiceControl.this, pitch + "", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor= app_pref.edit();//to save data the editable object need to created
                editor.putInt("name_key",pitch);//data goes in app_pref file
                editor.apply();

                Log.d("haha","pitch="+pitch);

            }
        });
    }
}

