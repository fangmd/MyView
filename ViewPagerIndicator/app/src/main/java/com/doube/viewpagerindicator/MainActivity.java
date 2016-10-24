package com.doube.viewpagerindicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPagerIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.vp_main);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.ll_indicator);


        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(BlankFragment.newInstance());
        fragments.add(BlankFragment.newInstance());
        FAdapter fAdapter = new FAdapter(getSupportFragmentManager(), fragments);


        mViewPager.setAdapter(fAdapter);
        mIndicator.setupWithViewPager(mViewPager);

    }
}
