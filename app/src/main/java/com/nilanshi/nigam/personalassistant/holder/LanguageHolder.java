package com.nilanshi.nigam.personalassistant.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nilanshi.nigam.personalassistant.R;

/**
 * Created by apple on 10/09/17.
 */

public class LanguageHolder extends RecyclerView.ViewHolder {

    public final TextView tvlanguage;
    public final CardView containerLanguage;

    public LanguageHolder(View itemView) {
        super(itemView);
        tvlanguage = (TextView) itemView.findViewById(R.id.tvlanguage);
        containerLanguage = (CardView) itemView.findViewById(R.id.Containerlanguage);


    }
}
