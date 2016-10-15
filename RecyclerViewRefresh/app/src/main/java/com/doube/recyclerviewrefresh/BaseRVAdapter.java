package com.doube.recyclerviewrefresh;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * footer,header , PullToRefresh, LoadMore
 * <p>
 * Created by double on 2016/10/11.
 */

public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_NORMAL = 2;

    private List<T> mDatas;

    private View mHeaderView;
    private View mFooterView;

    private OnItemClickListener mListener;
    private RecyclerView mRecyclerView;
    private int mFooterHeight;


    public BaseRVAdapter() {
        mDatas = new ArrayList<>();
        mHeadState = HEAD_DONE;
        isEnd = true;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderView != null) {
            return TYPE_HEADER;
        }

        if (position == getItemCount() - 1 && mFooterView != null) {
            return TYPE_FOOTER;
        }

        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) return new HeaderVH(mHeaderView);
        if (mFooterView != null && viewType == TYPE_FOOTER) return new FooterVH(mFooterView);
        return onCreateVH(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER) {
            // footer or header
            return;
        }

        final int pos = getRealPosition(holder);
        final T data = mDatas.get(pos);
        onBind(holder, pos, data);

        if (mListener != null) {
            holder.itemView.setOnClickListener(v -> mListener.onItemClick(pos, data));
        }

    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    /**
     * @return contain footer and header
     */
    @Override
    public int getItemCount() {
        int ret = mDatas.size();
        if (mHeaderView != null) {
            ret++;
        }
        if (mFooterView != null) {
            ret++;
        }
        return ret;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
        mHeadText = ((TextView) headerView.findViewById(R.id.tv_header));

        // useful when layout set height by certain value
        mHeaderViewHeight = mHeaderView.getLayoutParams().height;
        if (DEBUG)
            Log.d(TAG, "setHeaderView: mHeaderView.getLayoutParams().height mHeaderViewHeight" + mHeaderViewHeight);

        // useless:
//        mHeaderView.post(() -> {
//            mHeaderViewHeight = mHeaderView.getHeight();
//            Log.d(TAG, "setHeaderView: mHeaderViewHeight" + mHeaderViewHeight);
//            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
//            Log.d(TAG, "setHeaderView: mHeaderViewHeight.getMeasuredHeight" + mHeaderViewHeight);
//        });
        //-----------

        changeHeaderByState(HEAD_DONE);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount());
        mFootText = ((TextView) mFooterView.findViewById(R.id.tv_footer));
        mFooterHeight = mFooterView.getLayoutParams().height;
        if (DEBUG)
            Log.d(TAG, "setFooterView: mFooterHeight = mFooterView.getLayoutParams().height;" + mFooterHeight);

        changeFooterByState(HEAD_DONE);
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void addAll(List<T> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearAddAll(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeaderVH extends RecyclerView.ViewHolder {

        public HeaderVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FooterVH extends RecyclerView.ViewHolder {

        public FooterVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    // listener
    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data);
    }

    public abstract RecyclerView.ViewHolder onCreateVH(ViewGroup parent, final int viewType);

    public abstract void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, T data);


    //适配 其他 LayoutManger

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }

    // pull

    private int[] into;
    private int[] firstInto;
    //    private float startY = 0;
    private float endY;
    private float moveY = 0;
    private int lastItem;
    private int firstVisible;
    private boolean isLoad = false;
    private boolean isTop = true;
    private boolean isRefreshing = false;
    private LoadMoreListener mLoadMoreListener;
    private PullToRefreshListener mPullToRefreshListener;
    private int viewHeight;

    private static final int HEAD_DONE = 0;
    private static final int HEAD_PULL_TO_REFRESH = 1;
    private static final int HEAD_RELEASE_TO_REFRESH = 2;
    private static final int HEAD_REFRESHING = 3;
    private static final int HEAD_RATIO = 3;

    private static final int FOOT_DONE = 0;
    private static final int FOOT_LOAD_MORE = 1;
    private static final int FOOT_NO_MORE = 2;

    private int mHeadState;
    private int mFootState;

    private TextView mHeadText;
    private TextView mFootText;

    private int mHeaderViewHeight;
    private float startY;
    private float offsetY;
    private int mFirstVisibleItem;
    private boolean isRecord;
    private boolean isEnd;
    /**
     * 是否可刷新，在setPullToRefreshListener中设置为true
     */
    private boolean mIsRefreshable;

    /**
     * 是否可加载更多，在setLoadMoreListener中设置为true
     */
    private boolean mIsLoadMoreable;

    private FrameLayout mAnimContainer;
    private Animation animation;
    private AnimationDrawable secondAnim;
    private AnimationDrawable thirdAnim;


    public interface LoadMoreListener {
        void onLoadMore();
    }

    public interface PullToRefreshListener {
        void onRefreshing();
    }

    public void setPullToRefreshListener(PullToRefreshListener pullToRefresh) {
        if (mLoadMoreListener == null) {
            initListener();
        }
        mIsRefreshable = true;
        this.mPullToRefreshListener = pullToRefresh;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        if (mPullToRefreshListener == null) {
            initListener();
        }
        mIsLoadMoreable = true;
        this.mLoadMoreListener = loadMoreListener;
    }

    public void initListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (DEBUG)
                    Log.d(TAG, "onScrollStateChanged: mRecyclerView.getBottom():" + mRecyclerView.getBottom()
                            + "mRecyclerView.getHeight:" + mRecyclerView.getHeight() + "mScreenHeighe:" + getScreenHeight());

                if (lastItem >= getItemCount() - 2 && newState == RecyclerView.SCROLL_STATE_IDLE && !isLoad && mFirstVisibleItem != 0) {
                    ViewGroup.LayoutParams params = getFooterView().getLayoutParams();
                    params.width = RecyclerView.LayoutParams.MATCH_PARENT;
                    params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    getFooterView().setLayoutParams(params);
                    recyclerView.smoothScrollToPosition(getItemCount() - 1);
                    isLoad = true;
                    mLoadMoreListener.onLoadMore();
                }

//                if (firstVisible <= 1) { // header is on 0
//                    isTop = true;
//                } else {
//                    isTop = false;
//                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) getHeaderView().getLayoutParams();
//                    params.width = RecyclerView.LayoutParams.MATCH_PARENT;
//                    params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
//                    params.setMargins(0, -getHeaderView().getHeight(), 0, 0);
//                    getHeaderView().setLayoutParams(params);
//                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    lastItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    firstVisible = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                } else {
                    into = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                    firstInto = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(firstInto);
                    lastItem = into[0];
                    firstVisible = firstInto[0];
                }

                if (DEBUG)
                    Log.d(TAG, "onScrolled: lastItem:" + lastItem + "firstVisible" + firstVisible);

                mFirstVisibleItem = firstVisible;
                mFirstVisibleItem = mFirstVisibleItem == 1 ? 0 : mFirstVisibleItem;
            }

        });

        mRecyclerView.setOnTouchListener((v, event) -> {
            if (isEnd) {//如果现在时结束的状态，即刷新完毕了，可以再次刷新了，在onRefreshComplete中设置,或者onLoadMoreComplete中设置
                if (mIsRefreshable) {//如果现在是可刷新状态   在setOnMeiTuanListener中设置为true
                    switch (event.getAction()) {
                        //用户按下
                        case MotionEvent.ACTION_DOWN:
                            //如果当前是在listview顶部并且没有记录y坐标
                            if (mFirstVisibleItem == 0 && !isRecord) {
                                //将isRecord置为true，说明现在已记录y坐标
                                isRecord = true;
                                //将当前y坐标赋值给startY起始y坐标
                                startY = event.getY();
                            }
                            break;
                        //用户滑动
                        case MotionEvent.ACTION_MOVE:
                            //再次得到y坐标，用来和startY相减来计算offsetY位移值
                            float tempY = event.getY();
                            //再起判断一下是否为listview顶部并且没有记录y坐标
                            if (mFirstVisibleItem == 0 && !isRecord) {
                                isRecord = true;
                                startY = tempY;
                            }
                            //如果当前状态不是正在刷新的状态，并且已经记录了y坐标
                            if (mHeadState != HEAD_REFRESHING && isRecord) {
                                //计算y的偏移量
                                offsetY = tempY - startY;
                                //计算当前滑动的高度
                                float currentHeight = (-mHeaderViewHeight + offsetY / 3);
                                //用当前滑动的高度和头部headerView的总高度进行比 计算出当前滑动的百分比 0到1
                                float currentProgress = 1 + currentHeight / mHeaderViewHeight;
                                //如果当前百分比大于1了，将其设置为1，目的是让第一个状态的椭圆不再继续变大
                                if (currentProgress >= 1) {
                                    currentProgress = 1;
                                }

                                //如果当前的状态是放开刷新，并且已经记录y坐标
                                if (mHeadState == HEAD_RELEASE_TO_REFRESH && isRecord) {
                                    mRecyclerView.smoothScrollToPosition(0);
                                    //如果当前滑动的距离小于headerView的总高度
                                    if (-mHeaderViewHeight + offsetY / HEAD_RATIO < 0) {
                                        //将状态置为下拉刷新状态
                                        mHeadState = HEAD_PULL_TO_REFRESH;
                                        //根据状态改变headerView，主要是更新动画和文字等信息
                                        changeHeaderByState(mHeadState);
                                        //如果当前y的位移值小于0，即为headerView隐藏了
                                    } else if (offsetY <= 0) {
                                        //将状态变为done
                                        mHeadState = HEAD_DONE;
                                        //根据状态改变headerView，主要是更新动画和文字等信息
                                        changeHeaderByState(mHeadState);
                                    }
                                }

                                //如果当前状态为下拉刷新并且已经记录y坐标
                                if (mHeadState == HEAD_PULL_TO_REFRESH && isRecord) {
                                    mRecyclerView.smoothScrollToPosition(0);
                                    //如果下拉距离大于等于headerView的总高度
                                    if (-mHeaderViewHeight + offsetY / HEAD_RATIO >= 0) {
                                        //将状态变为放开刷新
                                        mHeadState = HEAD_RELEASE_TO_REFRESH;
                                        //根据状态改变headerView，主要是更新动画和文字等信息
                                        changeHeaderByState(mHeadState);
                                        //如果当前y的位移值小于0，即为headerView隐藏了
                                    } else if (offsetY <= 0) {
                                        //将状态变为done
                                        mHeadState = HEAD_DONE;
                                        //根据状态改变headerView，主要是更新动画和文字等信息
                                        changeHeaderByState(mHeadState);
                                    }
                                }

                                //如果当前状态为done并且已经记录y坐标
                                if (mHeadState == HEAD_DONE && isRecord) {
                                    //如果位移值大于0
                                    if (offsetY >= 0) {
                                        //将状态改为下拉刷新状态
                                        mHeadState = HEAD_PULL_TO_REFRESH;
                                    }
                                }

                                //如果为下拉刷新状态
                                if (mHeadState == HEAD_PULL_TO_REFRESH) {
                                    //则改变headerView的padding来实现下拉的效果
                                    RecyclerView.LayoutParams params1 = (RecyclerView.LayoutParams) getHeaderView().getLayoutParams();
                                    params1.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                                    mHeaderView.setLayoutParams(params1);
                                    mHeaderView.setPadding(0, (int) (-mHeaderViewHeight + offsetY / HEAD_RATIO), 0, 0);

                                    //给第一个状态的View设置当前进度值
//                                    mFirstView.setCurrentProgress(currentProgress);
                                    //重画
//                                    mFirstView.postInvalidate();
                                }
                                //如果为放开刷新状态
                                if (mHeadState == HEAD_RELEASE_TO_REFRESH) {
                                    //改变headerView的padding值
                                    RecyclerView.LayoutParams params1 = (RecyclerView.LayoutParams) getHeaderView().getLayoutParams();
                                    params1.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                                    mHeaderView.setLayoutParams(params1);
                                    mHeaderView.setPadding(0, (int) (-mHeaderViewHeight + offsetY / HEAD_RATIO), 0, 0);
                                    //给第一个状态的View设置当前进度值
//                                    mFirstView.setCurrentProgress(currentProgress);
                                    //重画
//                                    mFirstView.postInvalidate();
                                }
                            }
                            break;
                        //当用户手指抬起时
                        case MotionEvent.ACTION_UP:
                            //如果当前状态为下拉刷新状态
                            if (mHeadState == HEAD_PULL_TO_REFRESH) {
                                //平滑的隐藏headerView
                                mRecyclerView.smoothScrollBy((int) (-mHeaderViewHeight + offsetY / HEAD_RATIO) + mHeaderViewHeight, 500);
                                //根据状态改变headerView
                                changeHeaderByState(mHeadState);
                            }
                            //如果当前状态为放开刷新
                            if (mHeadState == HEAD_RELEASE_TO_REFRESH) {
                                //平滑的滑到正好显示headerView
                                mRecyclerView.smoothScrollBy((int) (-mHeaderViewHeight + offsetY / HEAD_RATIO), 500);
                                //将当前状态设置为正在刷新
                                mHeadState = HEAD_REFRESHING;
                                //回调接口的onRefresh方法
                                mPullToRefreshListener.onRefreshing();
                                //根据状态改变headerView
                                changeHeaderByState(mHeadState);
                            }
                            //这一套手势执行完，一定别忘了将记录y坐标的isRecord改为false，以便于下一次手势的执行
                            isRecord = false;
                            break;
                    }
                }
            }
            return false;
        });

    }

    private int getScreenHeight() {
        return mRecyclerView.getContext().getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 根据状态改变headerView的动画和文字显示
     *
     * @param headState head view state
     */
    private void changeHeaderByState(int headState) {
        switch (headState) {
            case HEAD_DONE://如果的隐藏的状态
                //设置headerView的padding为隐藏
                RecyclerView.LayoutParams params1 = (RecyclerView.LayoutParams) mHeaderView.getLayoutParams();
                params1.height = 0;
                mHeaderView.setLayoutParams(params1);
                break;
            case HEAD_RELEASE_TO_REFRESH://当前状态为放开刷新
                //文字显示为放开刷新
                mHeadText.setText("放开刷新");
                break;
            case HEAD_PULL_TO_REFRESH://当前状态为下拉刷新
                //设置文字为下拉刷新
                mHeadText.setText("下拉刷新");
                break;
            case HEAD_REFRESHING://当前状态为正在刷新
                //文字设置为正在刷新
                mHeadText.setText("正在刷新");
                break;
            default:
                Log.e(TAG, "changeHeaderByState: error head state");
                break;
        }
    }

    /**
     * 根据状态改变FooterView
     *
     * @param footerState foot view state
     */
    private void changeFooterByState(int footerState) {
        mFootState = footerState;
        switch (footerState) {
            case FOOT_DONE:
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) mFooterView.getLayoutParams();
                lp.height = 0;
                mFooterView.setLayoutParams(lp);


                //
//                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) getFooterView().getLayoutParams();
//                params.height = 0;
//                mFooterView.setLayoutParams(params);
//                mFooterView.setVisibility(View.GONE);
                isLoad = false;
//        if (mRecyclerView != null) {
//            mRecyclerView.scrollToPosition(getItemCount() - 2);
//        }
                break;
            case FOOT_LOAD_MORE:
                mFootText.setText("正在加载....");
                break;
            case FOOT_NO_MORE:
                mFootText.setText("没有更多了");
                break;
            default:
                Log.e(TAG, "changeFooterByState: error foot state");
                break;
        }

    }

    /**
     * 刷新完毕，从主线程发送过来，并且改变headerView的状态和文字动画信息
     */
    public void setOnRefreshComplete() {
        //一定要将isEnd设置为true，以便于下次的下拉刷新
        isEnd = true;
        mHeadState = HEAD_DONE;
        changeHeaderByState(mHeadState);
    }

    /**
     * 加载更多完毕，改变footerView的状态
     */
    public void setLoadMoreComplete() {
        Log.e(TAG, "setLoadMoreComplete: ");
        isEnd = true;
        changeFooterByState(FOOT_DONE);
    }


    private boolean DEBUG = false;

    public void setDEBUG(boolean DEBUG) {
        this.DEBUG = DEBUG;
    }
}

