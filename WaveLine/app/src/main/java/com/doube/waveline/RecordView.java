package com.doube.waveline;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * 录音界面的一个自定义View
 * Created by double on 16/9/5.
 */

public class RecordView extends View {
    private Paint mRedPaint;
    private int mWidth;
    private int mHeight;
    private Paint mBGPaint;
    private Path mPath;
    private float mLevel = 1;

    private int mStep;
    private int mWaveLength;
    private int mWaveCount = 6;
    private ValueAnimator mAnimator;


    public RecordView(Context context) {
        this(context, null);
    }

    public RecordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBGPaint = new Paint();
        mBGPaint.setColor(Color.WHITE);
        mRedPaint = new Paint();
        mRedPaint.setColor(getResources().getColor(R.color.colorPrimary));

        mPath = new Path();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, mWidth, mHeight, mRedPaint);

        mWaveLength = mWidth / 4;
        int height = (int) (dp2px(160) * mLevel);

        mPath.reset();
        mPath.moveTo(mWidth, 0);
        mPath.lineTo(-(mWaveLength * 2), 0);
        mPath.lineTo(-(mWaveLength * 2), mHeight - height);


        //确定各个点的位置
        for (int i = 1; i <= mWaveCount; i++) {
            if (i % 2 != 0)
                mPath.quadTo(mWaveLength * i - (mWaveLength / 2) + mStep - (mWaveLength * 2), mHeight - height + height
                        , mWaveLength * i + mStep - (mWaveLength * 2), mHeight - height);
            else
                mPath.quadTo(mWaveLength * i - (mWaveLength / 2) + mStep - (mWaveLength * 2), mHeight - height - height
                        , mWaveLength * i + mStep - (mWaveLength * 2), mHeight - height);
        }


        mPath.lineTo(mWidth, 0);

        canvas.drawPath(mPath, mBGPaint);
    }

    private float dp2px(int i) {
        return getResources().getDisplayMetrics().density * i;
    }


    /**
     * 一秒移动一个周期不停去改变步长的值
     */
    public void run() {
        mAnimator = ValueAnimator.ofInt(0, mWaveLength * 2 );
        mAnimator.setDuration(3000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStep = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.start();
    }

    public void stop() {
        if (mAnimator != null) {
            mAnimator.end();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            mWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            mWidth = Math.max(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            mWidth = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            mHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            mHeight = Math.max(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            mHeight = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
