package com.waytone.animationdemo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AnimationnOneActivity extends AppCompatActivity {

    boolean visible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animationn_one);

        final ViewGroup transitionsContainer = (ViewGroup) findViewById(R.id.ll_container);
        final TextView text = (TextView) transitionsContainer.findViewById(R.id.tv_one);
        final Button button = (Button) transitionsContainer.findViewById(R.id.btn_one);

        button.setOnClickListener(v -> Snackbar.make(transitionsContainer, "haha", Snackbar.LENGTH_SHORT).show());

        button.setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(transitionsContainer);
            visible = !visible;
            text.setVisibility(visible ? View.VISIBLE : View.GONE);
        });


    }
}
