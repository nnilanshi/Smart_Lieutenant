package com.nilanshi.nigam.personalassistant.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nilanshi.nigam.personalassistant.R;

/**
 * Created by HP on 03-Sep-17.
 */

public class ListHolder extends RecyclerView.ViewHolder {
    public TextView tvText;
    public View cardContainer;

    public ListHolder(View itemView) {

        super(itemView);
        tvText =(TextView)itemView.findViewById(R.id.tvText);
        cardContainer = (View)itemView.findViewById(R.id.cardContainer);
    }
}
