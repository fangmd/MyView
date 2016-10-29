package com.waytone.liveviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private LiveView mLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLV = (LiveView) findViewById(R.id.lv_main);


        Button btn_name = (Button) findViewById(R.id.btn_main);
        btn_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLV.isMove()) {
                    mLV.stop();
                } else {
                    mLV.run();
                }
            }
        });


    }
}
