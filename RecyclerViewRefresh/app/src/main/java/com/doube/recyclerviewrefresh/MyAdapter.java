package com.doube.recyclerviewrefresh;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;

/**
 * Created by double on 2016/10/11.
 */

public class MyAdapter extends BaseRecyclerViewAdapter<String> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_NORMAL = 2;

    @Override
    public RecyclerView.ViewHolder onCreateVH(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, String data) {

        if (viewHolder instanceof Holder) {
            ((Holder) viewHolder).text.setText(data);
        }
    }


    class Holder extends BaseRecyclerViewAdapter.Holder {

        @BindView(R.id.tv_name)
        TextView text;

        public Holder(View itemView) {
            super(itemView);
        }
    }


}
