package com.adouble.coordinatorlayoutdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnQuickReturn = (Button) findViewById(R.id.btn_quick_return);
        btnQuickReturn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, QuickReturnActivity.class));
        });


    }
}
