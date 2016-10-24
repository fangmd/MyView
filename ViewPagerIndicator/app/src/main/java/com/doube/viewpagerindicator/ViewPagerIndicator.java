package com.doube.viewpagerindicator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * 目前 指示器 没有动态生成数量
 *
 * Created by double on 2016/9/28.
 */

public class ViewPagerIndicator extends View {

    private static final String TAG = ViewPagerIndicator.class.getSimpleName();

    private boolean DEBUG = true;

    private ViewPager mViewPager;
    private int mCount;
    private int mCurrentItem;
    private int mWidth;
    private int mHeight;
    private int mSelectedColor;
    private int mNormalColor;
    private int mIndicatorSpan;
    private int mRadius;
    private Paint mNormalPaint;
    private Paint mSelectedPaint;
    private int mOffset;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, 0);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator, defStyleRes, 0);
        mIndicatorSpan = array.getInteger(R.styleable.ViewPagerIndicator_indicatorSpan, dp2px(10));
        mNormalColor = array.getColor(R.styleable.ViewPagerIndicator_normalColor, Color.GRAY);
        mSelectedColor = array.getColor(R.styleable.ViewPagerIndicator_selectedColor, Color.BLUE);
        mRadius = array.getInteger(R.styleable.ViewPagerIndicator_vp_radius_indicator, dp2px(4));
        array.recycle();

        mNormalPaint = new Paint();
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setColor(mNormalColor);

        mSelectedPaint = new Paint();
        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setColor(mSelectedColor);


    }

    private int dp2px(int i) {
        return ((int) (getContext().getResources().getDisplayMetrics().density * i));
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

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = Math.max(desiredWidth, widthSize);
        } else {
            mWidth = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            mHeight = Math.max(desiredHeight, heightSize);
        } else {
            mHeight = desiredHeight;
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (DEBUG) Log.e(TAG, "onDraw: " + mWidth + "*" + mHeight);


        for (int i = 0; i < mCount; i++) {
            // 奇数
            // --- + r + span + r + r + span + r + ---
//            canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mNormalPaint);
//            canvas.drawCircle(mWidth / 2 - mRadius - mIndicatorSpan, mHeight / 2, mRadius, mNormalPaint);
//            canvas.drawCircle(mWidth / 2 + mRadius + mIndicatorSpan, mHeight / 2, mRadius, mNormalPaint);

            // else 偶数
            //     + r  span/2 | span/2 r + r
            canvas.drawCircle(mWidth / 2 + mIndicatorSpan / 2 + mRadius, mHeight / 2, mRadius, mNormalPaint);
            canvas.drawCircle(mWidth / 2 - mIndicatorSpan / 2 - mRadius, mHeight / 2, mRadius, mNormalPaint);
            canvas.drawCircle(mWidth / 2 - mIndicatorSpan / 2 - mRadius + mOffset, mHeight / 2, mRadius, mSelectedPaint);

        }


    }

    public void setupWithViewPager(ViewPager viewPager) {
        mViewPager = viewPager;

        mCount = mViewPager.getAdapter().getCount();
        mCurrentItem = mViewPager.getCurrentItem();
        invalidate();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (DEBUG)
                    Log.d(TAG, "onPageScrolled: " + "position:" + position + " positionOffset:" + positionOffset + " positionOffsetPixels" + positionOffsetPixels);

                if (positionOffset != 0) {
                    mOffset = ((int) (positionOffset * (mRadius + mIndicatorSpan + mRadius)));
                    if (DEBUG) Log.d(TAG, "onPageScrolled: ");
                    invalidate();
                }
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


}
