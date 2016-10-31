package com.adouble.coordinatorlayoutdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by double on 2016/10/31.
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.TVViewHolder> {


    @Override
    public TVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new TVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TVViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 40;
    }

    public class TVViewHolder extends RecyclerView.ViewHolder {

        public TVViewHolder(View itemView) {
            super(itemView);
        }
    }
}
