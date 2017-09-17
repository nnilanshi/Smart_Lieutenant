package com.nilanshi.nigam.personalassistant.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.bumptech.glide.Glide;
import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.cleveroad.audiovisualization.SpeechRecognizerDbmHandler;
import com.cleveroad.audiovisualization.VisualizerDbmHandler;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonElement;
import com.mapzen.speakerbox.Speakerbox;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nilanshi.nigam.personalassistant.R;
import com.nilanshi.nigam.personalassistant.adapter.ListAdapter;
import com.nilanshi.nigam.personalassistant.util.BuilderManager;
import com.nilanshi.nigam.personalassistant.util.VoiceControl;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum.HAM_4;
import static com.nilanshi.nigam.personalassistant.R.id.rvList;

public class CommandActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnBoomListener, RecognitionListener, AudioVisualization, ai.api.AIListener {

    private TextView tvName;
    private TextView tvEmail;
    private ImageView ivPic;
    private FirebaseUser user;
    private String displayName;
    private Uri photoUrl;
    private String email;
    private AudioVisualization audioVisualization;

    private SpeechRecognizer speech;
    private Intent recognizerIntent;

    private TextView tvText;
    private FloatingActionButton btnSpeak;
    private Speakerbox sbox;
    private ArrayList<String> list;
    private ListAdapter adapter;
    private BoomMenuButton leftBmb;
    private BoomMenuButton rightBmb;

    private Intent intent;
    private SharedPreferences app_pref;
    private TextToSpeech textToSpeech;
    private SharedPreferences lang_pref;
    private RecyclerView recycler;
    private String name_key;
    private TextView returnedTextView;
    private AIService aiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        tvText = (TextView) findViewById(R.id.tvText);
        btnSpeak = (FloatingActionButton) findViewById(R.id.btnSpeak);
        returnedTextView = (TextView) findViewById(R.id.returnedTextView);
        setupPermissions();
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnSpeak.isClickable()) {
                    aiService.startListening();
                    speech.startListening(intent);

                } else {
                    aiService.stopListening();
                    speech.stopListening();
                }
            }
        });
        sbox = new Speakerbox(getApplication());

        /*circular seekbaar*/
        app_pref = getSharedPreferences("app_pref", MODE_PRIVATE);
        int pitch = app_pref.getInt("name_key", 1);
        textToSpeech = sbox.getTextToSpeech();
        textToSpeech.setPitch((float) pitch / 100);
        Toast.makeText(context, "" + (float) pitch / 100, Toast.LENGTH_SHORT).show();
        /*circular seekbaar*/

        lang_pref = getSharedPreferences("lang_pref", MODE_PRIVATE);
        String lang = lang_pref.getString("select_string", "nothing selected");
        Locale locale = new Locale(lang);
        textToSpeech.setLanguage(locale);

        setupVisualizer();
        animateSpeak();

        //recyclerView
        recycler = (RecyclerView) findViewById(rvList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list = new ArrayList<>();
        adapter = new ListAdapter(list);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);
        // recycler.animate().setInterpolator(new BounceInterpolator());


        /*facebook details*/
        user = FirebaseAuth.getInstance().getCurrentUser();
        displayName = user.getDisplayName();
        photoUrl = user.getPhotoUrl();
        email = user.getEmail();
        ivPic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.ivPic);
        tvName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvName);
        tvEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        Glide.with(CommandActivity.this)
                .load(photoUrl)
                .into(ivPic);

        tvEmail.setText(email);
        tvName.setText(displayName);
        boom();

        //AI CONFIGURATION
        final AIConfiguration config = new AIConfiguration(" 51ec23192cde4860b128d909bfc071f9",
                AIConfiguration.SupportedLanguages.English, AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

    }

    private void boom() {
        /*Boom  Menu*/
        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View actionBar = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) actionBar.findViewById(R.id.title_text);
        mTitleTextView.setText(R.string.app_name);
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0, 0);

        rightBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_right_bmb);
        rightBmb.setButtonEnum(ButtonEnum.Ham);
        rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
        rightBmb.setButtonPlaceEnum(HAM_4);
        for (int i = 0; i < rightBmb.getPiecePlaceEnum().pieceNumber(); i++) {
            //rightBmb.addBuilder(BuilderManager.getHamButtonBuilderWithDifferentPieceColor());
            if (i == 0) {
                rightBmb.addBuilder(BuilderManager.getHamButtonBuilder("Share"));
            }
            if (i == 1) {
                rightBmb.addBuilder(BuilderManager.getHamButtonBuilder("Feedback"));
            }

            if (i == 2) {
                rightBmb.addBuilder(BuilderManager.getHamButtonBuilder("Themes"));
            }
            if (i == 3) {
                rightBmb.addBuilder(BuilderManager.getHamButtonBuilder("Logout"));
            }
        }
        rightBmb.setOnBoomListener(this);
    }

    private void setupVisualizer() {
        GLAudioVisualizationView visualizationView = (GLAudioVisualizationView) findViewById(R.id.visualizer_view);
        audioVisualization = (AudioVisualization) visualizationView;

        // set speech recognizer handler
        SpeechRecognizerDbmHandler speechRecHandler = DbmHandler.Factory.newSpeechRecognizerHandler(context);
        speechRecHandler.innerRecognitionListener();
        audioVisualization.linkTo(speechRecHandler);

        // set audio visualization handler. This will REPLACE previously set speech recognizer handler
        VisualizerDbmHandler vizualizerHandler = DbmHandler.Factory.newVisualizerHandler(this, 0);
        audioVisualization.linkTo(vizualizerHandler);
    }

    private void setupPermissions() {
        int permission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS}, 212);
                return;
            } else {
                setupVisualizer();
            }
        } else {
            setupVisualizer();
        }
    }

    /*speak speech*/
    private void animateSpeak() {
    /*animation*/
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int halfScreen = wm.getDefaultDisplay().getHeight() / 2;
        btnSpeak.setScaleX(0);
        btnSpeak.setScaleY(0);
        btnSpeak.animate()
                .setDuration(1000)
                .scaleX(1.5f)
                .scaleY(1.5f)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .setStartDelay(200)
                .translationYBy(-halfScreen + 56 * 2)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        sbox.play("Hey!!!! I am your Personal Assistant", null, new Runnable() {
                            @Override
                            public void run() {
                                sbox.play("Ask me Anything", null, new Runnable() {
                                    @Override
                                    public void run() {
                                        sbox.play("go to menu for options", null, new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        }, null);
                                    }
                                }, null);
                            }
                        }, null);
                    }
                });
        /*animation*/
    }

    @Override
    public <T> void linkTo(@NonNull DbmHandler<T> dbmHandler) {
        // set speech recognizer handler
        SpeechRecognizerDbmHandler speechRecHandler = DbmHandler.Factory.newSpeechRecognizerHandler(context);
        speechRecHandler.innerRecognitionListener();
        audioVisualization.linkTo(speechRecHandler);

        // set audio visualization handler. This will REPLACE previously set speech recognizer handler
        VisualizerDbmHandler vizualizerHandler = DbmHandler.Factory.newVisualizerHandler(this, 0);
        audioVisualization.linkTo(vizualizerHandler);

    }

    @Override
    public void onResume() {
        super.onResume();
        audioVisualization.onResume();
    }

    @Override
    public void onPause() {
        audioVisualization.onPause();
        super.onPause();
    }

    @Override
    public void release() {

    }

    @Override
    public void onDestroy() {
        audioVisualization.release();
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 212 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupVisualizer();
        } else {
            setupPermissions();
        }
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {


    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {
    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {
        String errorMessage = getErrorText(i);
        //Log.d(LOG_TAG, "FAILED " + errorMessage);
        tvText.setText(errorMessage);
        //  toggleButton.setChecked(false);
    }

    private String getErrorText(int i) {
        String message;
        switch (i) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        if (matches != null && matches.size() > 0) {
            String response = matches.get(0);
            list.add(response);
            adapter.notifyDataSetChanged();
            tvText.setText(response);
        }
    }


    @Override
    public void onPartialResults(Bundle partialResults) {
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.action_settings) {
            Intent setting = new Intent(CommandActivity.this, SettingActivity.class);
            startActivity(setting);
            finish();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*boom implementation*/
    @Override
    public void onClicked(int index, BoomButton boomButton) {
        switch (index) {

            case 0:
                /*Share*/
                Toast.makeText(context, "share", Toast.LENGTH_SHORT).show();
                break;

            case 1:
                /*Feedback*/
                Feedback();
                break;
            case 2:
                /*Themes*/
                Toast.makeText(context, "Themes", Toast.LENGTH_SHORT).show();
                break;
            case 3:
/*Logout*/
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                sbox.play("Hey!!!Are you sure you don't need me anymore?");
                new AwesomeInfoDialog(this)
                        .setTitle("Smart Lieutenant")
                        .setMessage(R.string.logout)
                        .setColoredCircle(R.color.dialogInfoBackgroundColor)
                        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                        .setCancelable(true)
                        .setPositiveButtonText(getString(R.string.dialog_yes_button))
                        .setPositiveButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
                        .setPositiveButtonTextColor(R.color.white)
                        .setNegativeButtonText(getString(R.string.dialog_no_button))
                        .setNegativeButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
                        .setNegativeButtonTextColor(R.color.white)
                        .setPositiveButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                //click
                                sbox.play("Yes");
                                Intent i = new Intent(CommandActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                //click
                                sbox.play("NO");
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void onBackgroundClick() {

    }

    @Override
    public void onBoomWillHide() {

    }

    @Override
    public void onBoomDidHide() {

    }

    @Override
    public void onBoomWillShow() {

    }

    @Override
    public void onBoomDidShow() {

    }
/*boom*/

    /*Feedback*/
    private void Feedback() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommandActivity.this);
        LayoutInflater inflater = CommandActivity.this.getLayoutInflater();
        email = user.getEmail();
        View view = inflater.inflate(R.layout.feedback_dialog, null);
        EditText etFeEmail = (EditText) view.findViewById(R.id.etFeEmail);
        final EditText feedback = (EditText) view.findViewById(R.id.etContent);
        builder.setView(view);
        etFeEmail.setText(email);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] adrress = {"nilanshinigam2014@gmail.com"};
                composeEmail(adrress, "Name: " +
                        user.getDisplayName() +
                        "\n\nMessage:" +
                        feedback.getText(), "send Feedback");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
    }

    public void composeEmail(String[] addresses, String feedback, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, feedback);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onResult(AIResponse response) {

        Result result1 = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result1.getParameters() != null && !result1.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result1.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        // Show results in TextView.
        returnedTextView.setText("Query:" + result1.getResolvedQuery() +
                "\nAction: " + result1.getAction() +
                "\nParameters: " + parameterString);
        aiService = null;

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
    /*feedback*/

}
