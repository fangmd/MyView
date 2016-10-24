package com.doube.messageview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by double on 2016/10/21.
 */

public class MessageView extends FrameLayout {

    private static final String TAG = MessageView.class.getSimpleName();
    private View mView;
    private Context mContext;
    private LinearLayout mLLRoot;
    private TextView mLastTV;

    public MessageView(Context context) {
        this(context, null);
    }

    public MessageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.message_view, this);
        mLLRoot = ((LinearLayout) mView.findViewById(R.id.ll_message_root));
        mLastTV = ((TextView) mLLRoot.findViewById(R.id.tv_message));

    }

    public void setErrorText(String s, int directory) {
        switch (directory) {
            case DIRECTORY.BOTTOM:
                TextView newTV = ((TextView) LayoutInflater.from(mContext).inflate(R.layout.message_tv_error, null, false));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mLastTV.getHeight());
                newTV.setLayoutParams(lp);
                animateRemove(mLastTV, newTV);
                break;
        }

    }


    private void animateRemove(TextView lastTV, final TextView newTV) {
        Log.d(TAG, "animateRemove: lastTV height:" + lastTV.getHeight() + " newTV height:" + newTV.getHeight());
        mLLRoot.addView(newTV);

        ViewPropertyAnimatorCompat animate = ViewCompat.animate(lastTV);
        animate.translationY(-lastTV.getHeight());
        animate.setDuration(500);

        final ViewPropertyAnimatorCompat animate2 = ViewCompat.animate(newTV);
        animate2.translationY(-lastTV.getHeight());
        animate2.setDuration(500);


        animate.setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
//                mLLRoot.addView(newTV);
//                animate2.start();
            }

            @Override
            public void onAnimationEnd(View view) {
//                mLLRoot.removeView(mLastTV);
//                mLastTV = newTV;
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });


        animate.start();


    }


    public class DIRECTORY {
        public static final int BOTTOM = 0;
    }
}
