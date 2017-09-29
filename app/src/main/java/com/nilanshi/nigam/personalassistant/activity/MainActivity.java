package com.nilanshi.nigam.personalassistant.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nilanshi.nigam.personalassistant.R;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ResideMenu resideMenu;
    private MainActivity mContext;


    private ResideMenuItem itemLogin;
    private ResideMenuItem itemSignUp;
    private ResideMenuItem itemAbout;
    private ResideMenuItem itemRate;
    private ResideMenuItem itemContact;
    private Button title_bar_left_menu;
    private ImageView ivLogo;
    private ImageView ivHero;
    private Button title_bar_right_menu;

    private EditText etEmail;
    private RatingBar ratingBar;
    private EditText etUser;
    private FirebaseAuth auth;
    private TextView tvError;
    private FirebaseDatabase fbase;
    private DatabaseReference db;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_main);
        mContext = this;
        ivHero = (ImageView) findViewById(R.id.ivHero);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        title_bar_left_menu = (Button) findViewById(R.id.title_bar_left_menu);
        title_bar_right_menu = (Button) findViewById(R.id.title_bar_right_menu);
        Picasso.with(context).load(R.drawable.assist).into(ivHero);
        setUpMenu();
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking_animation);
        title_bar_left_menu.startAnimation(startAnimation);
        title_bar_right_menu.startAnimation(startAnimation);
    }

    private void setUpMenu() {
// attach to current activity;
        resideMenu = new ResideMenu(this);

        resideMenu.setBackground(

                R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
//valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);
// create menu items;
        itemAbout = new ResideMenuItem(this, R.drawable.icon_app, "About Me");
        itemRate = new ResideMenuItem(this, R.drawable.rate, "Rate Me");
        itemContact = new ResideMenuItem(this, R.drawable.contact, "Contact Me");
        itemLogin = new ResideMenuItem(this, R.drawable.icon_login, "Login");
        itemSignUp = new ResideMenuItem(this, R.drawable.icon_signup, "SignUp");

        itemLogin.setOnClickListener(this);
        itemSignUp.setOnClickListener(this);
        itemRate.setOnClickListener(this);

        resideMenu.addMenuItem(itemLogin, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSignUp, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemAbout, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemRate, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemContact, ResideMenu.DIRECTION_LEFT);
// You can disable a direction by setting ->
// resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);


            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemLogin) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);

        } else if (view == itemSignUp) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);

        } else if (view == itemRate) {
            userRating();
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(mContext, "Welcome!!!!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {

        }
    };

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    private void userRating() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();

        View view = inflater.inflate(R.layout.rate_dialog, null);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etUser = (EditText) view.findViewById(R.id.etUser);
        tvError =(TextView) view.findViewById(R.id.tvError);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        builder.setView(view);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        double rate = ratingBar.getRating();
                        String name = etUser.getText().toString();
                        String email = etEmail.getText().toString();
                        if (name.isEmpty()) {
                            tvError.setError("User Name is empty");
                            return;
                        }
                        if (email.isEmpty()) {
                            tvError.setError("enter correct rating");
                            return;
                        }


                        //connecting cctivity to server
                        fbase = FirebaseDatabase.getInstance();
                        auth = FirebaseAuth.getInstance();
                        DatabaseReference db = fbase.getReference("Ratings");
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("name", name);
                        data.put("email", email);
                        data.put("rating", rate);

                        db.push().setValue(data, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(MainActivity.this, "Thanks for rating!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
}

