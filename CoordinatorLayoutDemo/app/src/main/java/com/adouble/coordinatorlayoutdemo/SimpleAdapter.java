package com.adouble.coordinatorlayoutdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by double on 2016/10/31.
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.TVViewHolder> {
    private List<String> mDatas;

    public SimpleAdapter() {
        mDatas = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            mDatas.add(String.format(Locale.CHINA, "item %d", i));
        }

    }

    @Override
    public TVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new TVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TVViewHolder holder, int position) {
        holder.mTV.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class TVViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTV;

        public TVViewHolder(View itemView) {
            super(itemView);
            mTV = ((TextView) itemView.findViewById(R.id.text));
        }
    }
}
