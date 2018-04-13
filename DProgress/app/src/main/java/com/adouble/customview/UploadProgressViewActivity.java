package com.adouble.customview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.adouble.customview.uploadprogress.IProgressView;
import com.adouble.customview.uploadprogress.UploadProgressView;

public class UploadProgressViewActivity extends AppCompatActivity {

    private IProgressView mUploadProgressView;

    int progress = 0;

    public static void start(Context context) {
        Intent starter = new Intent(context, UploadProgressViewActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_progress_view);

        mUploadProgressView = (UploadProgressView) findViewById(R.id.upv);


        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progress++;

                    mUploadProgressView.setProgress(progress);
                }
            }
        }).start();
    }
}
