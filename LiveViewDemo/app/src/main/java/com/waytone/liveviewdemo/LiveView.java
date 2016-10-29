package com.waytone.liveviewdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by double on 2016/10/29.
 */

public class LiveView extends View {


    private ValueAnimator mAnimator;
    private int mBluePos;
    private int mPinkkWidth;

    private int mViewWidth;
    private int mViewHeight;

    private int mBlueWidth;
    private Paint mBluePaint;
    private Paint mPinkPaint;

    private boolean mIsMove;
    private Paint mTextPaint;
    private Rect mTextBounds;
    private String mRightText;
    private Paint mRightBgPaint;


    public LiveView(Context context) {
        this(context, null);
    }

    public LiveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBluePaint = new Paint();
        mBluePaint.setAntiAlias(true);

        mPinkPaint = new Paint();
        mPinkPaint.setColor(context.getResources().getColor(R.color.pink));
        mPinkPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(context.getResources().getDimension(R.dimen.text_size));//20f
        mTextBounds = new Rect();

        mRightBgPaint = new Paint();
        mRightBgPaint.setAntiAlias(true);
        mRightBgPaint.setColor(context.getResources().getColor(R.color.bg_transparent));


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path rectPath = new Path();
        rectPath.moveTo(mBluePos + 0, 0);
        rectPath.lineTo(mBluePos + mBlueWidth, 0);
        rectPath.lineTo(mBluePos + mBlueWidth - 30, mViewHeight);
        rectPath.lineTo(mBluePos + 0 - 40, mViewHeight); //倾斜 40 LinearGradient 40
        rectPath.lineTo(mBluePos + 0, 0);

//        rectPath.addRect(mBluePos + 0f, 0f, ((float) (mBluePos + mBlueWidth)), ((float) mViewHeight), Path.Direction.CW);

        Path outPath = new Path();

        outPath.moveTo(mViewWidth / 11, 0f);
//        RectF oval = new RectF(0, 0, mViewWidth * 2 / 11, mViewHeight - mViewHeight / 5);
//        outPath.arcTo(oval, 90, 180, true);
        outPath.quadTo(-40, mViewHeight / 2 - mViewHeight / 10, mViewWidth / 11, mViewHeight - mViewHeight / 5);
        outPath.lineTo(mViewWidth / 3, mViewHeight - mViewHeight / 5);
        outPath.lineTo(mViewWidth / 3 + mViewWidth / 2 / 6, 0);
        outPath.lineTo(mViewWidth / 11, 0f);

        // draw bg pink
        canvas.drawPath(outPath, mPinkPaint);

        // draw blue
        LinearGradient linearGradient = new LinearGradient(((float) (mBluePos + 0)), 0f, ((float) (mBluePos + mBlueWidth / 2)), 0f + 40, getContext().getResources().getColor(R.color.pink), getContext().getResources().getColor(R.color.blue), LinearGradient.TileMode.MIRROR);
        mBluePaint.setShader(linearGradient);
//        canvas.drawRect(mBluePos + 0f, 0f, mBluePos + mBlueWidth, mViewHeight, mBluePaint);

        canvas.save();
        canvas.clipPath(outPath);
        canvas.clipPath(rectPath, Region.Op.INTERSECT);
        canvas.drawPaint(mBluePaint);
        canvas.restore();

        // draw left text
        String text = "Live";
        mTextPaint.getTextBounds(text, 0, text.length(), mTextBounds);
        float baseline = mViewHeight / 2 - mViewHeight / 10 + mTextPaint.getTextSize() / 2 - mTextPaint.getFontMetrics().descent;
        canvas.drawText(text, mViewWidth / 9, baseline, mTextPaint);

        // draw right bg
        Path rectRightPath = new Path();
        rectRightPath.moveTo(mViewWidth / 3 + mViewWidth / 2 / 6, mViewHeight / 10); // 间隙 。  mViewHeight / 10 = 高落差
        rectRightPath.lineTo(mViewWidth / 3, mViewHeight - mViewHeight / 5 + mViewHeight / 10); // 间隙 。  mViewHeight / 10 = 高落差
//        rectRightPath.lineTo();


        // draw right text
        mTextPaint.getTextBounds(mRightText, 0, mRightText.length(), mTextBounds);
        float baseline2 = mViewHeight / 2 + mViewHeight / 10 + mTextPaint.getTextSize() / 2 - mTextPaint.getFontMetrics().descent;
        canvas.drawText(text, mViewWidth / 9, baseline2, mTextPaint);


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
            mViewWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            mViewWidth = Math.max(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            mViewWidth = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            mViewHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            mViewHeight = Math.max(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            mViewHeight = desiredHeight;
        }


        mBlueWidth = mViewWidth / 5;
        //MUST CALL THIS
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    // animator
    public void run() {
        mAnimator = ValueAnimator.ofInt(0, mViewWidth);
        mAnimator.setDuration(3000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBluePos = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.start();
        mIsMove = true;
    }

    public void stop() {
        if (mAnimator != null) {
            mAnimator.end();
        }
        mIsMove = false;
    }

    public boolean isMove() {
        return mIsMove;
    }

    public void setRightText(String text) {
        mRightText = text;
    }
}
