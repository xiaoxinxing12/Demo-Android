package org.chzz.demo.view.activity;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;

import com.just.agentwebX5.AgentWebX5;
import com.tencent.smtt.sdk.WebViewClient;

import org.chzz.demo.R;
import org.chzz.demo.common.BaseActivity;
import org.chzz.demo.model.AccoutEntity;
import org.chzz.demo.uitl.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by copy on 2017/10/16.
 */

public class WebView extends BaseActivity {
    protected AgentWebX5 mAgentWebX5;
    @BindView(R.id.container)
    LinearLayout mLinearLayout;
    String url = "https://h5.ele.me/hongbao/#hardware_id=&is_lucky_group=True&lucky_number=7&track_id=&platform=4&sn=29d2d8d25eabdcde&theme_id=1449&device_id=";
    private String strJS;
    private boolean isGRT;
    String a = "https://h5.ele.me/hongbao/#hardware_id=&is_lucky_group=True&lucky_number=7&track_id=&platform=4&sn=29d2d8d25eabdcde&theme_id=1449&device_id=?code=C4A904384DF7012A3FF083D8F854521E";
    private int accountIndex;

    List<AccoutEntity> account = new ArrayList<>();

    @Override

    protected void initView() {
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
    }

    @Override
    protected void setListener() {
        mTitle.setText("领红包助手");
        account.add(new AccoutEntity("454685121", "15875577046"));
        account.add(new AccoutEntity("3503564905", "a12345679"));
        account.add(new AccoutEntity("2165702506", "a12345679"));
        account.add(new AccoutEntity("3582207650", "a12345679"));
    }


    @OnClick({R.id.tv_get, R.id.tv_all})
    public void onClick() {

        initWebView();
    }


    private void initWebView() {
        if (null != mAgentWebX5) {
            mAgentWebX5.clearWebCache();
            // mAgentWebX5.getWebLifeCycle().onDestroy();
        }
        mAgentWebX5 = AgentWebX5.with(this)//
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))//
                .useDefaultIndicator()//
                .defaultProgressBarColor()
                .setSecutityType(AgentWebX5.SecurityType.strict)
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()//
                .ready()
                .go(url);

    }

    private void loadUrl() {
        mAgentWebX5.clearWebCache();
        mAgentWebX5.getLoader().reload();
        mAgentWebX5.getLoader().loadUrl(url);
        isGRT = false;
    }

    private WebViewClient mWebViewClient = new WebViewClient() {


        @Override
        public void onPageStarted(com.tencent.smtt.sdk.WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i("Info", "BaseWebActivity onPageStarted");
        }

        @Override
        public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView view, String url) {
            view.loadUrl(url);
            Log.i("url", url);
            if (url.contains("h5.ele.me/hongbao") && url.contains("code=")) {
                isGRT = true;
                mHandler.sendEmptyMessageDelayed(2, 2000);
            }
            if (url.contains("xui.ptlogin2.qq.com") && !isGRT) {
                mHandler.sendEmptyMessageDelayed(1, 3000);
            }

            return true;
        }
    };
    private com.tencent.smtt.sdk.WebChromeClient mWebChromeClient = new com.tencent.smtt.sdk.WebChromeClient() {

        @Override
        public void onProgressChanged(com.tencent.smtt.sdk.WebView webView, int i) {
            super.onProgressChanged(webView, i);

        }


    };

    //模拟用户登陆
    public void LoginByPassword(String username, String password) {
        strJS = String
                .format("javascript:document.getElementById('u').value='%s';document.getElementById('p').value='%s';"
                                + "document.getElementById('go').click();",
                        username, password);
        mHandler.sendEmptyMessage(0);

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mAgentWebX5.getLoader().loadUrl(strJS);
                    break;
                case 1:
                    if (account.size() > 0 && accountIndex < account.size()) {
                        LoginByPassword(account.get(accountIndex).getName(), account.get(accountIndex).getPassword());
                        ToastUtil.show(account.get(accountIndex).getName());
                        accountIndex = accountIndex + 1;
                    }
                    break;
                case 2:
                    loadUrl();
                    break;

            }


            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAgentWebX5.destroy();
        mAgentWebX5.clearWebCache();
        mAgentWebX5.getWebLifeCycle().onDestroy();

    }
}
