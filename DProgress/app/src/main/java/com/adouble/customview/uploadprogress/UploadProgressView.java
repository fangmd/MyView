package com.adouble.customview.uploadprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.adouble.customview.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: Created by fangmingdong on 2018/4/13-上午11:04
 * Description: 上传文件进度控件
 */
public class UploadProgressView extends View implements IProgressView {

    /**
     * 上传进度, max=100, min=0
     */
    private int mProgress = 0;

    /**
     * 当前状态
     */
    private int mType = UploadProgressView.STATE_NONE;

    /**
     * 文字颜色
     */
    private int mTextColor;
    private int mTextSize;
    private int mBgColor;
    /**
     * 正在上传时显示的文字
     */
    private String mUploadingText = "上传中";
    private Paint mTextPaint;
    private Paint mBgPaint;
    private boolean mViewIsReady;

    public UploadProgressView(Context context) {
        this(context, null);
    }

    public UploadProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UploadProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UploadProgressView);
        if (typedArray != null) {
            int DEFAULT_TEXT_COLOR = Color.WHITE;
            mTextColor = typedArray.getColor(R.styleable.UploadProgressView_upload_progress_view_text_color, DEFAULT_TEXT_COLOR);
            mTextSize= ((int) typedArray.getDimension(R.styleable.UploadProgressView_upload_progress_view_text_size, 13));
            int DEFAULT_BG_COLOR = 0x80000000;
            mBgColor = typedArray.getColor(R.styleable.UploadProgressView_upload_progress_view_bg, DEFAULT_BG_COLOR);
            String uploadingText = typedArray.getString(R.styleable.UploadProgressView_upload_progress_view_uploading_test);
            if (!TextUtils.isEmpty(uploadingText)) {
                mUploadingText = uploadingText;
            }

            typedArray.recycle();
        }

        initPaint();
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    private void initPaint() {
        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);

        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(mBgColor);
        mBgPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mType == STATE_NONE) {
            return;
        }

        drawBg(canvas);
        drawText(canvas);
    }

    private void drawBg(Canvas canvas) {
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
//        Rect r = new Rect(0, 0, width, height);
//        canvas.drawRect(r, mBgPaint);
//        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
//        mBgPaint.setXfermode(mode);

        int radius = Math.max(width, height) * 2;
        Rect circleRect = new Rect((width / 2) - radius, (height / 2) - radius, (width / 2) + radius, (height / 2) + radius);
        int angle = mProgress * 360 / 100;
        canvas.drawArc(new RectF(circleRect), 0, angle, true, mBgPaint);
//        mBgPaint.setXfermode(null);
    }

    private void drawText(Canvas canvas) {
        if (mType == STATE_UPLOADING) {
            float x = (getWidth() - mTextPaint.measureText(mUploadingText)) / 2;
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float y = getHeight() / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;

            canvas.drawText(mUploadingText, x, y, mTextPaint);
        }
    }

    @Override
    public int getProgress() {
        return mProgress;
    }

    @Override
    public void setProgress(int progress) {
        if (mProgress != progress) {
            mType = STATE_UPLOADING;
        }
        if (mProgress >= 100) {
            mType = STATE_NONE;
        }
        mProgress = progress;

        if (!mViewIsReady) {
            return;
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public int getType() {
        return mType;
    }

    public void setType(@UploadProgressViewType int type) {
        mType = type;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mViewIsReady = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mViewIsReady = false;
    }

    /**
     * 上传状态：正在上传
     */
    public static final int STATE_UPLOADING = 0x00000000;
    /**
     * 上传状态：NONE, 默认状态
     */
    public static final int STATE_NONE = 0x00000004;
    /**
     * 上传状态：停止
     */
    public static final int STATE_PAUSE = 0x00000008;

    @IntDef({STATE_UPLOADING, STATE_NONE, STATE_PAUSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UploadProgressViewType {
    }
}
