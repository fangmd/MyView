package com.adouble.clipdrawabledemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mView = (ImageView) findViewById(R.id.main_record);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.main_btn:
                mView.getDrawable().setLevel(5000);
                break;
            case R.id.main_btn1:
                mView.getDrawable().setLevel(10000);
                break;
        }
    }
}
