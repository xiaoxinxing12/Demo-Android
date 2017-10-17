package org.chzz.demo.view.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.chzz.adapter.CHZZOnRVItemClickListener;
import org.chzz.adapter.CHZZViewHolderHelper;
import org.chzz.demo.R;
import org.chzz.demo.common.BaseActivity;
import org.chzz.demo.common.CommonRecyclerAdapter;
import org.chzz.demo.model.AccountEntity;
import org.chzz.demo.uitl.ToastUtil;
import org.chzz.demo.view.fragment.WebViewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by copy on 2017/10/16.
 */

public class MyWebView extends BaseActivity implements CommonRecyclerAdapter.IFillDataListener<AccountEntity.DataEntity>, CHZZOnRVItemClickListener {
    List<AccountEntity.DataEntity> account = new ArrayList<>();
    WebViewFragment fragment;
    int bigCoupon, index;
    @BindView(R.id.et_url)
    EditText etUrl;
    private String url;

    @Override

    protected void initView() {
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
    }

    @Override
    protected void setListener() {
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setPullDownRefreshEnable(false);
        mAdapter = new CommonRecyclerAdapter(mDataRv, R.layout.item_info_adapter, null, null, this, null);
        initRefreshLayout(mDataRv, mRefreshLayout, mAdapter);
        mAdapter.setOnRVItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        etUrl.setText(url);
        mTitle.setText("饱了吧红包助手");
        account.add(new AccountEntity.DataEntity("3582207650", "a12345679", "copys"));
        account.add(new AccountEntity.DataEntity("2165702506", "a12345679", "copys1"));
        account.add(new AccountEntity.DataEntity("2655967705", "a12345679", "copys2"));
        account.add(new AccountEntity.DataEntity("3503564905", "a12345679", "copys3"));
        account.add(new AccountEntity.DataEntity("2821728287", "a12345679", "copy4", true));
        account.add(new AccountEntity.DataEntity("1577115646", "a12345679", "copys5"));
        account.add(new AccountEntity.DataEntity("3323125255", "a12345679", "copys7"));
        mAdapter.setData(account);
    }

    @OnClick(R.id.tv_all)
    public void onClick() {

        if (url == null || url.isEmpty()) {
            initUrl();
        }

    }

    private boolean initUrl() {
        String Path = etUrl.getText().toString();
        if (null == Path || Path.isEmpty() || !Path.contains("lucky_number")) {
            ToastUtil.show("请输入正确链接");
            return false;
        }
        url = Path;
        try {
            Path = Path.substring(Path.indexOf("lucky_number") + 13, Path.length());
            Path = Path.substring(0, Path.indexOf("&"));
            bigCoupon = Integer.parseInt(Path);
        } catch (Exception e) {

        }
        return true;
    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        if (url == null || url.isEmpty()) {
            if (!initUrl()) {
                return;
            }
        }

        final AccountEntity.DataEntity bean = (AccountEntity.DataEntity) mAdapter.getData().get(position);
        if (bean.isImportant()) {
            new AlertDialog.Builder(this)
                    .setTitle("信息提醒")
                    .setMessage("这个是你的大号哦")
                    .setPositiveButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            goLogin(bean);
                        }
                    })
                    .show();
        } else {
            goLogin(bean);
        }

    }

    private void goLogin(AccountEntity.DataEntity bean) {
        Intent intent = new Intent(this, WebViewT.class);
        intent.putExtra("userName", bean.getName());
        intent.putExtra("passWord", bean.getPassword());
        intent.putExtra("url", url);
        startActivityForResult(intent, 99);
    }

    @Override
    public void setFillDataListener(CHZZViewHolderHelper chzzViewHolderHelper, int i, AccountEntity.DataEntity t) {

        if (t.isImportant()) {
            chzzViewHolderHelper.setBackgroundColor(R.id.ll, getResources().getColor(R.color.colorPrimary1));
            chzzViewHolderHelper.setTextColor(R.id.tv_nick, getResources().getColor(R.color.white));
            chzzViewHolderHelper.setTextColor(R.id.tv_name, getResources().getColor(R.color.white));
            chzzViewHolderHelper.setTextColor(R.id.tv_coupon, getResources().getColor(R.color.white));
        }

        chzzViewHolderHelper.setText(R.id.tv_get, (t.getCoupon() == null || !t.getCoupon().contains("元") ? "未领" : "已领"));
        chzzViewHolderHelper.setBackgroundColor(R.id.tv_get, getResources().getColor((t.getCoupon() == null || !t.getCoupon().contains("元") ? R.color.colorPrimary : R.color.colorPrimary2)));
        chzzViewHolderHelper.setText(R.id.tv_nick, t.getNick())
                .setText(R.id.tv_name, t.getName())
                .setText(R.id.tv_coupon, t.getCoupon() == null ? "" : t.getCoupon());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != 100) {
            return;
        }
        AccountEntity beans = (AccountEntity) data.getSerializableExtra("bean");
        List<AccountEntity.DataEntity> result = beans.getList();
        if (null != result) {
            for (AccountEntity.DataEntity resultBean : result) {
                for (AccountEntity.DataEntity bean : account) {
                    if (bean.getNick().equals(resultBean.getNick())) {
                        bean.setCoupon(resultBean.getCoupon());
                        continue;
                    } else if(null==bean.getCoupon()) {
                        bean.setCoupon("未领");
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
                        .setMessage("大红包要来了，下一个将是大红包")
                        .setPositiveButton("确定", null)
                        .show();
            }
            mAdapter.setData(account);
            index = index + 1;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
