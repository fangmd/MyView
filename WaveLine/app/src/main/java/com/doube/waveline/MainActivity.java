package com.doube.waveline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private RecordView mWave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWave = ((RecordView) findViewById(R.id.main_wave));

        mWave.run();
    }

    private boolean flag;

    public void click(View view) {
        if (flag) {
            mWave.run();
            flag = !flag;
        } else {
            mWave.stop();
            flag = !flag;
        }
    }
}
