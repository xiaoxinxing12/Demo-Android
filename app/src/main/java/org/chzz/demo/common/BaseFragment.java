package org.chzz.demo.common;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import org.chzz.adapter.CHZZDivider;
import org.chzz.demo.APP;
import org.chzz.demo.R;
import org.chzz.demo.uitl.ThreadUtil;
import org.chzz.refresh.CHZZMoocStyleRefreshViewHolder;
import org.chzz.refresh.CHZZRefreshLayout;
import org.chzz.widget.CHZZAlertDialog;
import org.chzz.widget.CHZZLoadDataLayout;

/**
 * Created by copy on 2017/4/17.
 */

public abstract class BaseFragment extends Fragment implements CHZZRefreshLayout.CHZZRefreshLayoutDelegate, View.OnClickListener {
    protected View mContentView;
    protected APP mApp = APP.getInstance();
    protected CHZZRefreshLayout mRefreshLayout;
    protected LinearLayoutManager layoutManager;
    protected CommonRecyclerAdapter mAdapter;
    protected CHZZLoadDataLayout loadDataLayout;
    protected RecyclerView mDataRv;
    protected int pageIndex = 1;
    protected int pageSize = 10;
    protected int pageCount = 0;
    private CHZZAlertDialog mLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(savedInstanceState);
        try {
            mRefreshLayout = (CHZZRefreshLayout) mContentView.findViewById(R.id.refreshLayout);
            mDataRv = (RecyclerView) mContentView.findViewById(R.id.rvData);
        } catch (Exception e) {

        }
        setListener();
        onUserVisible();
        return mContentView;
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void setListener();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }

    /**
     * 当fragment对用户可见时，会调用该方法，可在该方法中懒加载网络数据
     */
    public void onUserVisible() {
    }

    protected void setContentView(@LayoutRes int layoutResID) {
        mContentView = LayoutInflater.from(mApp).inflate(layoutResID, null);
    }

    /**
     * 结束刷新或加载更多
     *
     * @param refreshLayout
     */
    protected void stopRefreshing(CHZZRefreshLayout refreshLayout, CHZZLoadDataLayout loadDataLayout) {
        if (refreshLayout != null) {
            refreshLayout.endRefreshing();
            refreshLayout.endLoadingMore();
        }
        if (loadDataLayout != null) {
            loadDataLayout.setStatus(CHZZLoadDataLayout.SUCCESS);
        }
    }

    protected void stopRefreshing(CHZZRefreshLayout refreshLayout) {
        if (refreshLayout != null) {
            refreshLayout.endRefreshing();
            refreshLayout.endLoadingMore();
        }
        if (loadDataLayout != null) {
            loadDataLayout.setStatus(CHZZLoadDataLayout.SUCCESS);
        }
    }


    protected void initRefreshLayout(RecyclerView mDataRv, CHZZRefreshLayout mRefreshLayout, CommonRecyclerAdapter adapter) {
        CHZZMoocStyleRefreshViewHolder leftRefreshViewHolder = new CHZZMoocStyleRefreshViewHolder(mApp, true);
        leftRefreshViewHolder.setSpringDistanceScale(2);
        //刷新图标
        leftRefreshViewHolder.setOriginalImage(R.mipmap.ic_launcher);
        //刷新头背景色
        leftRefreshViewHolder.setUltimateColor(R.color.colorPrimary);
        //设置刷新头
        mRefreshLayout.setRefreshViewHolder(leftRefreshViewHolder);
        //布局管理器
        layoutManager = new LinearLayoutManager(mApp, GridLayoutManager.VERTICAL, false);
        mDataRv.setLayoutManager(layoutManager);
        mDataRv.addItemDecoration(new CHZZDivider(mApp));
        mDataRv.setAdapter(adapter);
    }


    @Override
    public void onCHZZRefreshLayoutBeginRefreshing(CHZZRefreshLayout refreshLayout) {
        ThreadUtil.runInUIThread(new Runnable() {
            @Override
            public void run() {
                pageIndex = 1;
                onUserVisible();
            }
        }, 2000);
    }

    @Override
    public boolean onCHZZRefreshLayoutBeginLoadingMore(CHZZRefreshLayout refreshLayout) {
        if (pageIndex <= pageCount) {
            ThreadUtil.runInUIThread(new Runnable() {
                @Override
                public void run() {
                    onUserVisible();
                }
            }, 200);
            return true;
        } else if (pageIndex > 2) {
            //ToastUtil.show("暂无更多数据");
            return false;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 加载图片
     *
     * @param url
     * @param head
     * @param
     */
    protected void loadImage(String url, ImageView head) {
        Glide.with(this).load(url)
                .placeholder(R.mipmap.banner)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.mipmap.banner)
                .thumbnail(0.1f)
                // .animate(R.anim.zoom_in)
                .fitCenter()
                // .override(1080, 1920)
                .into(head);
    }
}
