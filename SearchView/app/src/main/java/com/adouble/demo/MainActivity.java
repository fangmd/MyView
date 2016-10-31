package com.adouble.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SearchView mSv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSv = (SearchView) findViewById(R.id.sv);
        mSv.setOnSearchListener(content -> Toast.makeText(MainActivity.this, "search:" + content, Toast.LENGTH_SHORT).show());

        mSv.setDEBUG(true);

    }

    @Override
    public void onBackPressed() {

        if (mSv.isHasFocus()) {
            mSv.clearFocus();
        } else {
            super.onBackPressed();
        }


    }
}
