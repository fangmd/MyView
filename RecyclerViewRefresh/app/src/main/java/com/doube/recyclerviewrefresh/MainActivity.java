package com.doube.recyclerviewrefresh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRV;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRV = (RecyclerView) findViewById(R.id.rv_main);

        mAdapter = new MyAdapter();
        mRV.setAdapter(mAdapter);

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            strings.add(String.format("haha %s", i));
        }
        mAdapter.addAll(strings);


        // add header footer
        View view = LayoutInflater.from(this).inflate(R.layout.header, mRV, false);
        mAdapter.setHeaderView(view);
        View view2 = LayoutInflater.from(this).inflate(R.layout.footer, mRV, false);
        mAdapter.setFooterView(view2);

        //onclick
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String data) {
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setLoadMoreListener(new BaseRecyclerViewAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "load more", Toast.LENGTH_SHORT).show();
                                mAdapter.setLoadMoreComplete();

                            }
                        });
                    }
                }).start();
            }
        });

        mAdapter.setPullToRefreshListener(new BaseRecyclerViewAdapter.PullToRefreshListener() {
            @Override
            public void onRefreshing() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "refresh", Toast.LENGTH_SHORT).show();
                                mAdapter.setRefreshComplete();
                            }
                        });
                    }
                }).start();
            }
        });


        mAdapter.setRefreshComplete();


    }
}
