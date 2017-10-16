package org.chzz.demo.view.activity;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.chzz.demo.R;
import org.chzz.demo.common.BaseActivity;
import org.chzz.demo.model.TabEntity;
import org.chzz.demo.view.fragment.IndexFragment;
import org.chzz.demo.view.fragment.MyFragment;
import org.chzz.demo.view.fragment.SecondFragment;
import org.chzz.tablayout.CommonTabLayout;
import org.chzz.tablayout.listener.CustomTabEntity;
import org.chzz.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements OnTabSelectListener {

    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tl_menu)
    CommonTabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //标题
    private String[] mMenu = {"梧桐树", "发现", "消息", "我的"};
    //默认图标
    private int[] mIconUnselectIds = {R.mipmap.tab_home_unselect, R.mipmap.tab_contact_unselect, R.mipmap.tab_contact_unselect, R.mipmap.tab_contact_unselect};
    //选中图标
    private int[] mIconSelectIds = {R.mipmap.tab_home_select, R.mipmap.tab_contact_select, R.mipmap.tab_contact_select, R.mipmap.tab_contact_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        for (int i = 0; i < mMenu.length; i++) {
            mTabEntities.add(new TabEntity(mMenu[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mFragments.add(new IndexFragment());
        mFragments.add(new SecondFragment());
        mFragments.add(new MyFragment());
        mFragments.add(new MyFragment());
    }

    @Override
    protected void setListener() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mTabLayout.setTabData(mTabEntities, this, R.id.fl_content, mFragments);
        mTabLayout.setOnTabSelectListener(this);
        mTitle.setText(mMenu[0]);
    }


    @Override
    public void onTabSelect(int i) {
        mTitle.setText(mMenu[i]);
    }

    @Override
    public void onTabReselect(int i) {

    }


}
