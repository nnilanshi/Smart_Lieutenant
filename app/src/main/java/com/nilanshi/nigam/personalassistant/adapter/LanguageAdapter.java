package com.nilanshi.nigam.personalassistant.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nilanshi.nigam.personalassistant.R;
import com.nilanshi.nigam.personalassistant.holder.LanguageHolder;
import com.nilanshi.nigam.personalassistant.util.LanguageModel;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class LanguageAdapter extends RecyclerView.Adapter<LanguageHolder> {

    private final SharedPreferences lang_pref;
    Context context; //holds the values of activity
    ArrayList<LanguageModel> results;


    public LanguageAdapter(Context context, ArrayList<LanguageModel> results) {
        this.context = context;
        this.results = results;
        lang_pref = context.getSharedPreferences("lang_pref", MODE_PRIVATE);
    }

    @Override

    public LanguageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.languagepreference,parent,false);//it tells we are creating the object of the layout filr which we want to use as a single item or created for adapter
        return new LanguageHolder(v);
    }

    @Override
    public void onBindViewHolder(LanguageHolder holder, int position) {
        final LanguageModel model = results.get(position);
        holder.tvlanguage.setText(model.getLanguage());


        holder.containerLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lang_name = model.getLanguage();
                SharedPreferences.Editor editor= lang_pref.edit();//to save data the editable object need to created
                editor.putString("select_lang",lang_name);//data goes in app_pref file
                editor.apply();


            }
        });
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.shake);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}
