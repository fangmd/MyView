package com.adouble.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.adouble.customview.progress.DProgress;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DProgress progress = (DProgress) findViewById(R.id.main_dprogress);
        assert progress != null;
        progress.startCount();

        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.startCount();
            }
        });
    }

    public void toUploadProgressView(View view) {
        UploadProgressViewActivity.start(this);
    }
}
