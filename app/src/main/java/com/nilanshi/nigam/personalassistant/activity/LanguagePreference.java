package com.nilanshi.nigam.personalassistant.activity;

import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;

import com.nilanshi.nigam.personalassistant.R;
import com.nilanshi.nigam.personalassistant.adapter.LanguageAdapter;
import com.nilanshi.nigam.personalassistant.util.LanguageModel;

import java.util.ArrayList;


public class LanguagePreference extends BaseActivity {

    private RecyclerView rvLanguages;
    private ArrayList<LanguageModel> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_preference);
        rvLanguages = (RecyclerView) findViewById(R.id.rvLanguage);
        dataGenerator();
        rvLanguages.setLayoutManager(new LinearLayoutManager(this));
        rvLanguages.setAdapter(new LanguageAdapter(this,results));

        /*RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        rvLanguages.setItemAnimator(itemAnimator);
rvLanguages.animate().setInterpolator(new AnticipateOvershootInterpolator());*/
        LanguagePreference.this.overridePendingTransition(R.anim.slide_up, R.anim.slide_up);
    }

    private void dataGenerator() {
        results=new ArrayList<>();
        results.add(new LanguageModel("English"));
        results.add(new LanguageModel("हिंदी Hindi"));
        results.add(new LanguageModel("தமிழ் Tamil"));
        results.add(new LanguageModel("తెలుగు Telugu"));
        results.add(new LanguageModel("français French"));
        results.add(new LanguageModel("Deutsche German"));
        results.add(new LanguageModel("Español Spanish"));
        results.add(new LanguageModel("italiano Italian"));
        results.add(new LanguageModel("русский Russian"));
        results.add(new LanguageModel("Latine Latin"));
    }
}
