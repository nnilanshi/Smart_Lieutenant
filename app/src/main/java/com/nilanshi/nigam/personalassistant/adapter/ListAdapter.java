package com.nilanshi.nigam.personalassistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nilanshi.nigam.personalassistant.R;
import com.nilanshi.nigam.personalassistant.holder.ListHolder;
import com.nilanshi.nigam.personalassistant.util.ListModel;

import java.util.ArrayList;


public class ListAdapter extends RecyclerView.Adapter<ListHolder> {

    //constructor
    private ArrayList<String> list;
    private Context context;

    public ListAdapter(ArrayList<String> list) {
        this.list = list;
        this.context=context;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        //to animate the holder
        String te = list.get(position);
        holder.tvText.setText(te);
        //animate(holder);

    }

   /* private void animate(ListHolder holder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context,R.anim.bounce_interpolator);
        holder.itemView.setAnimation(animAnticipateOvershoot);
    }*/

    @Override
    public int getItemCount() {
        return list.size();
    }
}
