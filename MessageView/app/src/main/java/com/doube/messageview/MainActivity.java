package com.doube.messageview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private MessageView mMV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMV = (MessageView) findViewById(R.id.mv_main);
        Button btnError = (Button) findViewById(R.id.btn_message_error);
        btnError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMV.setErrorText("error from btn", MessageView.DIRECTORY.TOP);

//                View viewById = findViewById(R.id.view);
//                ObjectAnimator translateY = ObjectAnimator.ofFloat(viewById, "translationY" +
//                        "", 0f, ((float) -viewById.getHeight()));
//                translateY.setDuration(500);
//                translateY.start();
            }
        });

        Button viewById = (Button) findViewById(R.id.btn_from_bottom);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMV.setErrorText("error from btn", MessageView.DIRECTORY.BOTTOM);
            }
        });


    }
}
