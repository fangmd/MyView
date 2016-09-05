package com.adouble.customviews.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adouble.customviews.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by double on 16-5-30.
 * 功能：自动轮播，标题，指示器
 * 使用：
 *       mDViewPager.addImgUrls(imgUrls);
 *       mDViewPager.setItemClickListener(this);
 *       mDViewPager.setLooping(true);
 *       mDViewPager.setAutoScroll(true);
 *
 * Project: CustomViews
 */
public class DViewPager extends FrameLayout {

    private ViewPager mViewPager;
    private TextView mTVTitle;
    private LinearLayout mDotContainer;

    // show indicator
    private boolean showIndicator;


    // selected page
    private int currentPage;

    private DPagerAdapter mAdapter;
    private List<ImageView> mDots;
    private List<String> mImgUrls;
    private Timer mTimer;

    public DViewPager(Context context) {
        this(context, null);
    }

    public DViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_d_viewpager, this);

        mViewPager = (ViewPager) findViewById(R.id.d_viewpager);
        mTVTitle = (TextView) findViewById(R.id.d_viewpager_tv_title);
        mDotContainer = (LinearLayout) findViewById(R.id.d_viewpager_dot);

        // init viewpager
        mAdapter = new DPagerAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new DOnPageChangeListener());

        showIndicator = true;
        mDots = new ArrayList<>();

    }

    private void initIndicator() {
        if (showIndicator) {
            int pageCount = mImgUrls.size();
            float density = getContext().getResources().getDisplayMetrics().density;
            for (int i = 0; i < pageCount; i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setBackgroundResource(R.drawable.dot_selector);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (6 * density), (int) (6 * density));
                lp.rightMargin = (int) (6 * density);
                mDots.add(imageView);
                mDotContainer.addView(imageView, lp);
            }
            mDots.get(0).setSelected(true); // default select first img
        }
    }

    public void addImgUrls(List<String> imgUrls) {
        mAdapter.addAll(imgUrls);
        initIndicator();
    }

    public boolean isShowIndicator() {
        return showIndicator;
    }

    public void setShowIndicator(boolean showIndicator) {
        this.showIndicator = showIndicator;
    }


    /**
     * set loop
     */
    private boolean isLooping;

    public void setLooping(boolean looping) {
        isLooping = looping;
        mAdapter.notifyDataSetChanged();
    }


    /**
     * OnItemClickListener
     */
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void itemClickListener(int postion);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * pager touch listener // item点击事件，没有必要？？
     */
    class DOnTouchListener implements OnTouchListener {

        int downx;
        long downTime;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downx = (int) getX();
                    downTime = System.currentTimeMillis();
                    if (isAutoScroll) {
                        stopScroll();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    int upx = (int) getX();
                    if (downx == upx && System.currentTimeMillis() - downTime < 300) {
                        if (itemClickListener != null) {
                            itemClickListener.itemClickListener(currentPage % mImgUrls.size());
                        }
                    }
                    if (isAutoScroll) {
                        startScroll();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    if (isAutoScroll) {
                        startScroll();
                    }
                    break;
            }
            return true;
        }
    }


    /**
     * auto scroll one youbug
     */
    public static final int WHAT_SCROLL = 0;

    private boolean isAutoScroll;

//    private MyHandler mHandler = new MyHandler(this);
//
//    private static class MyHandler extends Handler {
//        WeakReference<DViewPager> mWeakReference;
//
//        public MyHandler(DViewPager viewPager) {
//            mWeakReference = new WeakReference<>(viewPager);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == WHAT_SCROLL) {
//                if (null != mWeakReference) {
//                    DViewPager viewPager = mWeakReference.get();
//                    Log.d("log", "handleMessage: " + viewPager.getCurrentPage());
//                    if (viewPager != null) {
//                        int position = viewPager.getCurrentPage() + 1;
//                        viewPager.setCurrentItem(position);
//                    }
//                }
//            }
//
//        }
//    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_SCROLL) {
                scrollToNext();
                mHandler.sendEmptyMessageDelayed(WHAT_SCROLL, mDelayMislls);
                return true;
            }
            return false;
        }
    });

    public boolean isAutoScroll() {
        return isAutoScroll;
    }

    public void setAutoScroll(boolean autoScroll) {
        isAutoScroll = autoScroll;
        if (isAutoScroll) {
            startScroll();
        } else {
            stopScroll();
        }
    }

    private void startScroll() {
        mHandler.sendEmptyMessage(WHAT_SCROLL);
    }

    private void stopScroll() {
        mHandler.removeCallbacksAndMessages(null);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position % mImgUrls.size(), true);
    }

    private long mDelayMislls = 1000;

    public long getDelayMislls() {
        return mDelayMislls;
    }

    public void setDelayMislls(long delayMislls) {
        mDelayMislls = delayMislls;
    }

    /**
     * auto scroll two
     */
    public void scrollToNext() {
        mViewPager.setCurrentItem(currentPage++);
    }


    /**
     * OnPageChangeListener
     */
    class DOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            setDotSelected(position);
        }

        @Override
        public void onPageSelected(int position) {

        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }

        private void setDotSelected(int position) {
            mDots.get(currentPage % mImgUrls.size()).setSelected(false);
            currentPage = position;
            mDots.get(currentPage % mImgUrls.size()).setSelected(true);
        }
    }


    /**
     * viewpager adapter
     */
    class DPagerAdapter extends PagerAdapter {


        public DPagerAdapter() {
            mImgUrls = new ArrayList<>();
        }

        @Override
        public int getCount() {
            if (isLooping) {
                return Integer.MAX_VALUE;
            } else {
                return mImgUrls.size();
            }
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
            lp.width = ViewPager.LayoutParams.MATCH_PARENT;
            lp.height = ViewPager.LayoutParams.MATCH_PARENT;
            // load img
            Glide.with(getContext()).load(mImgUrls.get(position % mImgUrls.size())).into(imageView);
            // add imageView to container
            container.addView(imageView, lp);
            // add touchevent
            imageView.setOnTouchListener(new DOnTouchListener());
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((View) object));
        }

        public void addAll(List<String> imgUrls) {
            mImgUrls.addAll(imgUrls);
            notifyDataSetChanged();
        }

        public void add(String imgUrl) {
            mImgUrls.add(imgUrl);
            notifyDataSetChanged();
        }
    }
}
