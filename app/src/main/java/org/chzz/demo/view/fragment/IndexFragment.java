package org.chzz.demo.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.chzz.banner.CHZZBanner;
import org.chzz.demo.R;
import org.chzz.demo.common.BaseFragment;
import org.chzz.demo.uitl.ToastUtil;
import org.chzz.demo.view.activity.InformationActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by copy on 2017/10/12.
 */

public class IndexFragment extends BaseFragment implements CHZZBanner.Adapter, CHZZBanner.Delegate {
    @BindView(R.id.iv_banner)
    CHZZBanner ivBanner;

    @Override
    protected void initView(Bundle savedInstanceState) {

        mContentView = View.inflate(getActivity(), R.layout.fragment_index, null);
        ButterKnife.bind(this, mContentView);
    }

    @Override
    protected void setListener() {
        setBanner();
    }
    @OnClick(R.id.tv_Information)
    public void onClick(){
        startActivity(new Intent(getContext(), InformationActivity.class));

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
