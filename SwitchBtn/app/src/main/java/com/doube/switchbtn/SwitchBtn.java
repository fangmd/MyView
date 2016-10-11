package com.doube.switchbtn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by double on 2016/10/10.
 */

public class SwitchBtn extends View {


    private Paint mBgPaint;
    private Paint mSelectedPaint;
    private Paint mNonPaint;
    private int mWidth;
    private int mHeight;

    private int mRadio;
    private int mBtnRadio;

    public SwitchBtn(Context context) {
        this(context, null);
    }

    public SwitchBtn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwitchBtn(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(getResources().getColor(R.color.colorPrimary));

        mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedPaint.setColor(getResources().getColor(R.color.colorAccent));

        mNonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNonPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
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

        mRadio = mHeight / 2 - 60;
        mBtnRadio = mRadio + 30;
        //MUST CALL THIS
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBg(canvas);

        drawBtn(canvas);

    }

    private void drawBtn(Canvas canvas) {
        canvas.drawCircle(mBtnRadio, mBtnRadio + 30, mBtnRadio, mSelectedPaint);
    }

    //TODO 30 适配
    private void drawBg(Canvas canvas) {
        Path path = new Path();
        path.moveTo(mBtnRadio, 60);
        path.lineTo(mWidth - mBtnRadio, 60);
        path.quadTo(mWidth + 20, mHeight / 2, mWidth - mBtnRadio, mHeight - 60);
        path.lineTo(mBtnRadio, mHeight - 60);
        path.quadTo(0 - 20, mHeight / 2, mRadio, 60);
        canvas.drawPath(path, mBgPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return super.onTouchEvent(event);
    }
}
