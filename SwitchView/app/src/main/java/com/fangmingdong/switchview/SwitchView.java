package com.fangmingdong.switchview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;


/**
 * Created by double on 2018/4/1.
 */

public class SwitchView extends View {

    private static final String TAG = SwitchView.class.getSimpleName();
    // paint
    private Paint mPaintBg;
    private Paint mPaintCircle;
    //
    private int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    private static final int DEFAULT_BG_COLOR = 0x3f3f3f3f;
    private static final int DEFAULT_CIRCLE_COLOR = 0xffff0000;
    private static final int MARGIN = 10;
    private static final int MAX_PROGRESS = 100;


    // default attr
    private int mBgColor = DEFAULT_BG_COLOR;
    private int mCircleColor = DEFAULT_CIRCLE_COLOR;
    private int mMargin = dp2px(MARGIN);

    private int mState = NORMAL;
    //

    private float mProgress;

    private int mWidth;
    private int mHeight;
    private float mLastX;
    private float mLastY;
    private float mDownX;
    private float mDownY;
    private VelocityTracker mVelocityTracker;

    public SwitchView(Context context) {
        this(context, null);
    }

    public SwitchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        handlerAttr(context, attrs);

        initPaint();
    }

    private void handlerAttr(Context context, AttributeSet attrs) {

        if (attrs == null) {
            return;
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchView);
        mBgColor = a.getColor(R.styleable.SwitchView_sv_bg_color, DEFAULT_BG_COLOR);
        mCircleColor = a.getColor(R.styleable.SwitchView_sv_circle_color, DEFAULT_CIRCLE_COLOR);

        a.recycle();
    }

    private void initPaint() {
        mPaintBg = new Paint();
        mPaintBg.setColor(mBgColor);
        mPaintBg.setAntiAlias(true);
        mPaintBg.setStrokeWidth(3);
        mPaintBg.setStyle(Paint.Style.FILL);

        mPaintCircle = new Paint();
        mPaintCircle.setColor(mCircleColor);
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStrokeWidth(3);
        mPaintCircle.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getSize();

        drawBg(canvas);

        drawCircle(canvas);
    }

    private void getSize() {
        mWidth = getWidth();
        mHeight = getHeight();
    }

    private void drawBg(Canvas canvas) {
        RectF rectF = new RectF(mMargin, mMargin, mWidth - mMargin, mHeight - mMargin);
        canvas.drawRoundRect(rectF, mHeight / 4, mHeight / 2, mPaintBg);
    }


    private void drawCircle(Canvas canvas) {
        // 状态不同显示不同的颜色
        switch (mState) {
            case NORMAL:

                break;
            case END:

                break;
            case SCROLLING:

                break;
        }

//        float left = (mProgress / 100) * mWidth;

        RectF oval = new RectF(mProgress, 0, mProgress + mHeight, mHeight);
        canvas.drawOval(oval, mPaintCircle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                mDownX = mLastX;
                mDownY = mLastY;
                Log.d(TAG, "onTouchEvent: DOWN" + mDownY);
                return true;
            case MotionEvent.ACTION_MOVE:
                float xDistance = event.getX() - mLastX;
                mLastX = event.getX();

                setProgress(mProgress + xDistance);

                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_UP:


                //
                if (mProgress < mWidth / 2) {
                    // normal
                    setState(SwitchView.NORMAL);
                }

                if (mProgress > mWidth / 2) {
                    // end
                    setState(SwitchView.END);
                }

                //点击事件
                callClickListener(event);


                //
                mVelocityTracker.computeCurrentVelocity(200);
                float xVelocity = mVelocityTracker.getXVelocity();

                if (Math.abs(xVelocity) > mTouchSlop) {
                    if (xVelocity > 0) {
                        setState(SwitchView.END);
                    } else {
                        setState(SwitchView.NORMAL);
                    }
                }

                releaseVelocityTracker();

                break;
            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void releaseVelocityTracker() {
        mVelocityTracker.clear();
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    private void callClickListener(MotionEvent event) {
        if (Math.abs(event.getX() - mDownX) < 10 && Math.abs(event.getY() - mDownY) < 10) {
            Log.d(TAG, "callClickListener: ");
            try {
                Field field = View.class.getDeclaredField("mListenerInfo");
                field.setAccessible(true);
                Object object = field.get(this);
                field = object.getClass().getDeclaredField("mOnClickListener");
                field.setAccessible(true);
                object = field.get(object);
                if (object != null && object instanceof OnClickListener) {
                    ((OnClickListener) object).onClick(this);
                }
            } catch (Exception e) {

            }
        }
    }


    // animate
    public void setProgress(float progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > (mWidth - mHeight)) {
            progress = mWidth - mHeight;
        }
        mProgress = progress;
        invalidate();
    }
    //

    public void setState(@SwitchViewState int state) {

        if (mState != state) {
            // 状态改变才调用监听
            if (mSwitchViewStateListener != null) {
                mSwitchViewStateListener.state(state);
            }
        }

        mState = state;

        switch (state) {
            case NORMAL:
                Log.d(TAG, "setState: NORMAL, mProgress=" + mProgress);
                ObjectAnimator animator = ObjectAnimator.ofFloat(this, "progress", mProgress, 0);
                animator.setDuration(600);
                animator.start();
                break;
            case END:
                Log.d(TAG, "setState: END, mProgress=" + mProgress);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(this, "progress", mProgress, mWidth);
                animator2.setDuration(600);
                animator2.start();
                break;
        }

    }

    public int getState() {
        return mState;
    }

    // 状态
    public static final int NORMAL = 0x00000000;
    public static final int END = 0x00000004;
    public static final int SCROLLING = 0x00000008;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NORMAL, END, SCROLLING})
    public @interface SwitchViewState {
    }
    //

    // listener
    private SwitchViewStateListener mSwitchViewStateListener;

    public void setSwitchViewStateListener(SwitchViewStateListener listener) {
        mSwitchViewStateListener = listener;
    }

    public interface SwitchViewStateListener {
        void state(@SwitchViewState int state);
    }
    //

    public int dp2px(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp + 0.5);
    }

}
