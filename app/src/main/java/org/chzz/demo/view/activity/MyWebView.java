package org.chzz.demo.view.activity;


import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import org.chzz.demo.R;
import org.chzz.demo.common.BaseActivity;
import org.chzz.demo.model.AccoutEntity;
import org.chzz.demo.view.fragment.WebViewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by copy on 2017/10/16.
 */

public class MyWebView extends BaseActivity {
    List<AccoutEntity> account = new ArrayList<>();
    WebViewFragment fragment;
    @BindView(R.id.tv_get)
    TextView tvGet;
    @BindView(R.id.tv_get1)
    TextView tvGet1;

    @Override

    protected void initView() {
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
    }

    @Override
    protected void setListener() {
        tvGet.setOnClickListener(this);
        tvGet1.setOnClickListener(this);
        mTitle.setText("领红包助手");
        account.add(new AccoutEntity("454685121", "15875577046"));
        account.add(new AccoutEntity("3503564905", "a12345679"));
        account.add(new AccoutEntity("2165702506", "a12345679"));
        account.add(new AccoutEntity("3582207650", "a12345679"));
        replace(account.get(0).getName(), account.get(0).getPassword());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get:
                replace(account.get(0).getName(), account.get(0).getPassword());
                break;
            case R.id.tv_get1:
                replace(account.get(1).getName(), account.get(1).getPassword());
                break;
        }
    }

    private void replace(String userName, String passWord) {
        if (null != fragment) {
            fragment.onDestroy();
            fragment = null;
        }
        // 开启Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = new WebViewFragment();
        fragment.setInfo(userName, passWord);
        transaction.replace(R.id.container, fragment).commit();
    }


}
