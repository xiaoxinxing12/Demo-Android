package org.chzz.demo.view.activity;


import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.chzz.adapter.CHZZOnItemChildClickListener;
import org.chzz.adapter.CHZZOnRVItemClickListener;
import org.chzz.adapter.CHZZViewHolderHelper;
import org.chzz.demo.R;
import org.chzz.demo.common.BaseActivity;
import org.chzz.demo.common.CommonRecyclerAdapter;
import org.chzz.demo.model.AccountEntity;
import org.chzz.demo.model.CouponResult;
import org.chzz.demo.uitl.ToastUtil;
import org.chzz.demo.uitl.WebViewUtils;
import org.chzz.demo.view.fragment.WebViewFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by copy on 2017/10/16.
 */

public class MyWebView extends BaseActivity implements CommonRecyclerAdapter.IFillDataListener<AccountEntity>, CHZZOnRVItemClickListener, CommonRecyclerAdapter.ItemChildListener, CouponResult, CHZZOnItemChildClickListener {
    List<AccountEntity> account = new ArrayList<>();
    WebViewFragment fragment;
    int bigCoupon, index;
    @BindView(R.id.et_url)
    EditText etUrl;
    private String url = "https://h5.ele.me/hongbao/#hardware_id=&is_lucky_group=True&lucky_number=9&track_id=&platform=4&sn=29d2d8d25eabdcde&theme_id=1449&device_id=";

    @Override

    protected void initView() {
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
    }

    @Override
    protected void setListener() {
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setPullDownRefreshEnable(false);
        mAdapter = new CommonRecyclerAdapter(mDataRv, R.layout.item_info_adapter, null, null, this, this);
        initRefreshLayout(mDataRv, mRefreshLayout, mAdapter);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);


        etUrl.setText(url);
        mTitle.setText("领红包助手");
        account.add(new AccountEntity("3503564905", "a12345679", "copys1"));
        account.add(new AccountEntity("2165702506", "a12345679", "copys2"));
        account.add(new AccountEntity("3582207650", "a12345679", "copys3"));
        mAdapter.setData(account);
    }

    @OnClick(R.id.tv_all)
    public void onClick() {
        String url = etUrl.getText().toString();
        if (url.isEmpty() || !url.contains("lucky_number")) {
            ToastUtil.show("请输入正确链接");
            return;
        }
        Map<String, String> Param = WebViewUtils.URLRequest(url);

        try {
            String u = url;
            u = u.substring(u.indexOf("lucky_number") + 13, u.length());
            u = u.substring(0, u.indexOf("&"));
            bigCoupon = Integer.parseInt(u);
        } catch (Exception e) {

        }


        List<AccountEntity> result = null;
        if (account.size() > 0) {
            replace(account.get(index).getName(), account.get(index).getPassword());
            //result = test(account.get(0).getName(), account.get(0).getPassword());
        }


    }

    private void replace(String userName, String passWord) {
        if (null != fragment) {
            fragment.onDestroy();
            fragment = null;
        }
        // 开启Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = new WebViewFragment(this);
        fragment.setInfo(userName, passWord);
        transaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        AccountEntity accountEntity = (AccountEntity) mAdapter.getData().get(position);
        replace(accountEntity.getName(), accountEntity.getPassword());
    }

    @Override
    public void setFillDataListener(CHZZViewHolderHelper chzzViewHolderHelper, int i, AccountEntity t) {

        chzzViewHolderHelper.setText(R.id.tv_nick, t.getNick())
                .setText(R.id.tv_name, t.getName())
                .setText(R.id.tv_coupon, t.getCoupon() == null ? "" : t.getCoupon());
    }

    @Override
    public void result(List<AccountEntity> result) {
        if (null != result) {
            for (AccountEntity bean : account) {
                for (AccountEntity resultBean : result) {
                    if (bean.getNick().equals(resultBean.getNick())) {
                        bean.setCoupon(resultBean.getCoupon());
                        continue;
                    }
                }
            }
            if (result.size() >= bigCoupon) {
                new AlertDialog.Builder(this)
                        .setTitle("信息提醒")
                        .setMessage("大红包已被领走")
                        .setPositiveButton("确定", null)
                        .show();
            }
            if (result.size() == bigCoupon - 1) {
                new AlertDialog.Builder(this)
                        .setTitle("信息提醒")
                        .setMessage("大红包要来了，是否使用Copy4领取")
                        .setPositiveButton("确定", null)
                        .show();
            }
            mAdapter.setData(account);
            index = index + 1;
        }
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {

    }

    @Override
    public void setItemChildListener(CHZZViewHolderHelper chzzViewHolderHelper) {
        chzzViewHolderHelper.setItemChildClickListener(R.id.tv_get);
    }
}
