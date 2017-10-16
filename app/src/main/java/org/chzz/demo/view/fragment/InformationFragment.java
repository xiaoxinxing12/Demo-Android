package org.chzz.demo.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.chzz.adapter.CHZZOnRVItemClickListener;
import org.chzz.adapter.CHZZViewHolderHelper;
import org.chzz.banner.CHZZBanner;
import org.chzz.core.net.CHZZClient;
import org.chzz.core.net.callback.Success;
import org.chzz.core.util.MD5Util;
import org.chzz.demo.R;
import org.chzz.demo.common.BaseFragment;
import org.chzz.demo.common.CommonRecyclerAdapter;
import org.chzz.demo.model.InformationEntity;
import org.chzz.demo.uitl.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by copy on 2017/10/12.
 */

public class InformationFragment extends BaseFragment implements CommonRecyclerAdapter.IFillDataListener<InformationEntity.DataEntity>, CHZZOnRVItemClickListener, CHZZBanner.Adapter, CHZZBanner.Delegate {
    CHZZBanner ivBanner;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mContentView = View.inflate(getActivity(), R.layout.common_refresh_fra, null);
        ButterKnife.bind(this, mContentView);
    }

    @Override
    protected void setListener() {
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setPullDownRefreshEnable(false);
        View header = View.inflate(getContext(), R.layout.common_banner, null);
        ivBanner = (CHZZBanner) header.findViewById(R.id.iv_banner);
        mAdapter = new CommonRecyclerAdapter(mDataRv, R.layout.item_information_adapter, header, null, this, null);
        initRefreshLayout(mDataRv, mRefreshLayout, mAdapter);
        mAdapter.setOnRVItemClickListener(this);
        setBanner();
        //假数据
        result();
        //请求网络数据
        //testHttp();
    }

    //-----------------------请求数据------------------
    private void testHttp() {
        Map<String, Object> data = new HashMap<>();
        data.put("loginName", "hospital");
        data.put("password", MD5Util.getMD5Str32byte("123456"));
        CHZZClient.builder()
                .params(data)
                .url("请求地址")
                .success(login)
                .build()
                .post();
    }


    private Success<InformationEntity> login = new Success<InformationEntity>() {
        @Override
        public void onSuccess(InformationEntity entity) {
            mAdapter.setData(entity.getData());
        }
    };
    //----------------------------------------------

    private void result() {
        List<InformationEntity.DataEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            InformationEntity.DataEntity bean = new InformationEntity.DataEntity();
            bean.setTitle("标题" + i);
            list.add(bean);
        }
        if (pageIndex == 1) {
            //首次
            mAdapter.setData(list);
        } else {
            //加载更多
            mAdapter.addMoreData(list);
        }
        pageIndex = pageIndex + 1;
    }

    @Override
    public void setFillDataListener(CHZZViewHolderHelper chzzViewHolderHeler, int i, InformationEntity.DataEntity bean) {

        chzzViewHolderHeler.setText(R.id.tv_title, bean.getTitle());

    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        ToastUtil.show("点击第几项" + position);
    }


    private void setBanner() {
        //图片地址
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        //提示
        List<String> tip = new ArrayList<>();
        tip.add("在百度度秘事业部举办的");
        tip.add("DuerOS 唤醒之旅系列沙龙来深圳站上");
        tip.add("DuerOS 全球合作伙伴计划");

        ivBanner.setDelegate(this);
        ivBanner.setAdapter(this);
        ivBanner.setData(list, tip);
    }

    @Override
    public void fillBannerItem(CHZZBanner banner, View itemView, Object model, int position) {
        loadImage("url", (ImageView) itemView);
    }

    @Override
    public void onBannerItemClick(CHZZBanner banner, View itemView, Object model, int position) {
        ToastUtil.show("点击banner");
    }
}
