package org.chzz.demo.view.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import org.chzz.demo.R;
import org.chzz.demo.common.BaseActivity;
import org.chzz.demo.common.CommonPagerAdapter;
import org.chzz.demo.model.TitleEntity;
import org.chzz.demo.view.fragment.InformationFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by copy on 2017/10/12.
 */

public class InformationActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    CommonPagerAdapter contentPagerAdapter;
    Class[] fragmentClass = new Class[]{InformationFragment.class};

    @Override
    protected void initView() {
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);

    }

    @Override
    protected void setListener() {
        mTitle.setText("资讯");
        result(null);
    }

    private void result(TitleEntity entity) {
        stopRefreshing(mRefreshLayout);
        List<TitleEntity> list = new ArrayList<>();
        for (int a = 0; a < 15; a++) {
            TitleEntity bean = new TitleEntity();
            bean.setName("资讯" + a);
            list.add(bean);
        }
        initTableLayout(list);
    }


    private void initTableLayout(List<TitleEntity> list) {
        contentPagerAdapter = new CommonPagerAdapter(getSupportFragmentManager(), this, list, 0, fragmentClass);
        viewPager.setAdapter(contentPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(contentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
