package com.doube.messageview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
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
        TextView newTV;
        LinearLayout.LayoutParams lp;
        switch (directory) {
            case DIRECTORY.BOTTOM:
                newTV = ((TextView) LayoutInflater.from(mContext).inflate(R.layout.message_tv_error, this, false));
                lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mLastTV.getHeight());
                newTV.setLayoutParams(lp);
                animateRemoveFromBottom(mLastTV, newTV);
                break;
            case DIRECTORY.TOP:
                newTV = ((TextView) LayoutInflater.from(mContext).inflate(R.layout.message_tv_error, this, false));
                lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mLastTV.getHeight());
                newTV.setLayoutParams(lp);
                Log.d(TAG, "setErrorText: " + mLastTV.getHeight());
                newTV.setTranslationY(-mLastTV.getHeight());
                animateRemoveFromTop(mLastTV, newTV);
                break;

        }

    }

    private void animateRemoveFromTop(TextView lastTV, final TextView newTV) {
        mLLRoot.addView(newTV, 0);

        Log.d(TAG, "animateRemoveFromTop: " + mLastTV.getHeight());
        Log.d(TAG, "animateRemoveFromTop: " + lastTV.getHeight() + "::" + lastTV.getTranslationY());

        ObjectAnimator translateY = ObjectAnimator.ofFloat(lastTV, "translationY", 0, mLastTV.getHeight());
        translateY.setDuration(500);

        ObjectAnimator translateY2 = ObjectAnimator.ofFloat(newTV, "translationY", -mLastTV.getHeight(), 0);
        translateY2.setDuration(500);

        translateY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLLRoot.removeView(mLastTV);
                mLastTV = newTV;
                newTV.setTranslationY(0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        translateY.start();
        translateY2.start();
    }


    private void animateRemoveFromBottom(TextView lastTV, final TextView newTV) {
        Log.d(TAG, "animateRemove: lastTV height:" + lastTV.getHeight() + lastTV.hashCode() + " newTV height:" + newTV.getHeight() + newTV.hashCode());
        mLLRoot.addView(newTV);

        ObjectAnimator translateY = ObjectAnimator.ofFloat(lastTV, "translationY", 0, -lastTV.getHeight());
        translateY.setDuration(500);

        ObjectAnimator translateY2 = ObjectAnimator.ofFloat(newTV, "translationY", 0, -lastTV.getHeight());
        translateY2.setDuration(500);

        translateY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLLRoot.removeView(mLastTV);
                mLastTV = newTV;
                newTV.setTranslationY(0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        translateY.start();
        translateY2.start();
    }


    public class DIRECTORY {
        public static final int BOTTOM = 0;
        public static final int TOP = 1;
    }
}
