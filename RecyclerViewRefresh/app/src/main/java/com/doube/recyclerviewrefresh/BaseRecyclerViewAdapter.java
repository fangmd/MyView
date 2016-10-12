package com.doube.recyclerviewrefresh;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * footer,header
 * <p>
 * Created by double on 2016/10/11.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_NORMAL = 2;

    private ArrayList<T> mDatas;

    private View mHeaderView;
    private View mFooterView;

    private OnItemClickListener mListener;
    private RecyclerView mRecyclerView;

    public BaseRecyclerViewAdapter() {
        mDatas = new ArrayList<>();
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(pos, data);
                }
            });
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
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount());
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void addAll(ArrayList<T> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }


    class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
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

    interface OnItemClickListener<T> {
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
    private float startY = 0;
    private float endY;
    private float moveY = 0;
    private int lastItem;
    private int totalCount;
    private int firstVisible;
    private boolean isLoad = false;
    private boolean isTop = true;
    private boolean isRefreshing = false;
    private LoadMoreListener loadMoreListener;
    private PullToRefreshListener pullToRefresh;
    private int viewHeight;

    public interface LoadMoreListener {
        void onLoadMore();
    }

    public interface PullToRefreshListener {
        void onRefreshing();
    }

    public void touchMove(MotionEvent event) {
        endY = event.getY();
        moveY = endY - startY;
        //防止item向上滑出
        if (moveY > 0 && !isRefreshing) {
            //防止回退文本显示异常
            mRecyclerView.scrollToPosition(0);

            if (getHeaderView().getVisibility() == GONE)
                getHeaderView().setVisibility(VISIBLE);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) getHeaderView().getLayoutParams();
            params.width = RecyclerView.LayoutParams.MATCH_PARENT;
            params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
            //使header随moveY的值从顶部渐渐出现
            if (moveY >= 400) {
                moveY = 100 + moveY / 4;
            } else {
                moveY = moveY / 2;
            }
            viewHeight = getHeaderView().getHeight();
            if (viewHeight <= 0)
                viewHeight = 130;
            moveY = moveY - viewHeight;
            params.setMargins(0, (int) moveY, 0, 0);
            getHeaderView().setLayoutParams(params);
            if (moveY > 80) {
//                text.setText(getResources().getString(R.string.release_to_refresh));
            } else {
//                text.setText(getResources().getString(R.string.pull_to_refresh));
            }
        } else {
            if (getHeaderView().getVisibility() != GONE && !isRefreshing) {
                getHeaderView().setVisibility(GONE);
            }
        }
    }

    public void touchUp() {
        if (!isRefreshing && (endY - startY) != 0) {

            RecyclerView.LayoutParams params1 = (RecyclerView.LayoutParams) getHeaderView().getLayoutParams();
            params1.width = RecyclerView.LayoutParams.MATCH_PARENT;
            params1.height = RecyclerView.LayoutParams.WRAP_CONTENT;

            if (moveY >= 80) {
//                text.setText(getResources().getString(R.string.refreshing));
                params1.setMargins(0, 0, 0, 0);
                isRefreshing = true;
                //刷新数据
                pullToRefresh.onRefreshing();
            } else {
                if (viewHeight <= 0) {
                    viewHeight = 130;
                }
                params1.setMargins(0, -viewHeight, 0, 0);
                getHeaderView().setVisibility(GONE);
            }
            getHeaderView().setLayoutParams(params1);

        }
    }

    public void setPullToRefreshListener(PullToRefreshListener pullToRefresh) {
        if (loadMoreListener == null) {
            initListener();
        }
        this.pullToRefresh = pullToRefresh;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        if (pullToRefresh == null) {
            initListener();
        }
        this.loadMoreListener = loadMoreListener;
    }

    public void initListener() {

//        text = (TextView) getHeaderView(0).findViewById(R.id.header_text);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (lastItem == getItemCount() + 1 && newState == RecyclerView.SCROLL_STATE_IDLE && !isLoad) {
                    ViewGroup.LayoutParams params = getFooterView().getLayoutParams();
                    params.width = RecyclerView.LayoutParams.MATCH_PARENT;
                    params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    getFooterView().setLayoutParams(params);
                    getHeaderView().setVisibility(VISIBLE);
                    recyclerView.smoothScrollToPosition(totalCount);
                    isLoad = true;
                    loadMoreListener.onLoadMore();
                }
                if (firstVisible == 0) { // header is on 0
                    isTop = true;
                } else {
                    isTop = false;
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) getHeaderView().getLayoutParams();
                    params.width = RecyclerView.LayoutParams.MATCH_PARENT;
                    params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    params.setMargins(0, -getHeaderView().getHeight(), 0, 0);
                    getHeaderView().setLayoutParams(params);
                }

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
            }

        });

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isTop) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startY = event.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            touchMove(event);
                            break;
                        case MotionEvent.ACTION_UP:
                            touchUp();
                            break;
                    }
                }
                return false;
            }
        });

    }


    public void setLoadMoreComplete() {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) getFooterView().getLayoutParams();
        params.width = 0;
        params.height = 0;
        getFooterView().setLayoutParams(params);
        getFooterView().setVisibility(View.GONE);
        notifyItemChanged(0);
        isLoad = false;
    }

    public void setRefreshComplete() {
        RecyclerView.LayoutParams params1 = (RecyclerView.LayoutParams) getHeaderView().getLayoutParams();
        params1.width = RecyclerView.LayoutParams.MATCH_PARENT;
        params1.height = RecyclerView.LayoutParams.WRAP_CONTENT;
        params1.setMargins(0, -getHeaderView().getHeight(), 0, 0);
        getHeaderView().setLayoutParams(params1);
        getHeaderView().setVisibility(GONE);
        notifyItemChanged(0);
        isRefreshing = false;
    }


}
