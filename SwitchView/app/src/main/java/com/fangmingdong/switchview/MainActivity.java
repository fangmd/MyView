package com.fangmingdong.switchview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SwitchView viewById = (SwitchView) findViewById(R.id.sv);

        viewById.setSwitchViewStateListener(new SwitchView.SwitchViewStateListener() {
            @Override
            public void state(int state) {
                switch (state) {
                    case SwitchView.NORMAL:
                        Log.d(TAG, "state: NORMAL");
                        break;
                    case SwitchView.END:
                        Log.d(TAG, "state: END");
                        break;
                }
            }
        });

        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = viewById.getState();
                switch (state) {
                    case SwitchView.NORMAL:
                        viewById.setState(SwitchView.END);
                        break;
                    case SwitchView.END:
                        viewById.setState(SwitchView.NORMAL);
                        break;
                }
            }
        });
    }
}
