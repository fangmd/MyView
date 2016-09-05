package com.adouble.customview.progress;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adouble.customview.R;

import java.util.Locale;

/**
 * Created by double on 16-7-5.
 * Project: CustomView
 */
public class DProgress extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {

    private ProgressBar mProgress;
    private TextView mTV;
    private ObjectAnimator mObjectAnimator;

    public DProgress(Context context) {
        this(context, null);
    }

    public DProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.d_progress, this);
        mProgress = ((ProgressBar) view.findViewById(R.id.d_progress_progress));
        mTV = ((TextView) view.findViewById(R.id.d_progress_tv));
    }

    public void setProgress(int progress) {
        mProgress.setProgress(progress);
        mTV.setText(String.format(Locale.CHINA, "进度%d", progress));
    }

    public void startCount() {
        mObjectAnimator = ObjectAnimator.ofInt(mProgress, "progress", 0, 100);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
        mObjectAnimator.addUpdateListener(this);
        mObjectAnimator.setDuration(5000);
        mObjectAnimator.start();
    }

    public void stopCount() {
        if (mObjectAnimator != null) {
            mObjectAnimator.end();
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int animatedValue = (int) animation.getAnimatedValue();
        mTV.setText(String.format(Locale.CHINA, "进度%d", animatedValue));
    }
}
