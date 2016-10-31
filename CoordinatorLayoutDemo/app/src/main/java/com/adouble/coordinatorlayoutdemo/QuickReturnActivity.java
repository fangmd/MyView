package com.adouble.coordinatorlayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class QuickReturnActivity extends AppCompatActivity {

    private RecyclerView mRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_return);

        mRV = (RecyclerView) findViewById(R.id.rv_main);
        mRV.setAdapter(new SimpleAdapter());
    }
}
