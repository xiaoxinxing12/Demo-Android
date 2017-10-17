package org.chzz.demo.view.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.chzz.demo.R;
import org.chzz.demo.common.BaseActivity;
import org.chzz.demo.uitl.WebViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by copy on 2017/10/16.
 * Description:
 * User: copy
 * Date: 2017-10-16
 * Time: 下午10:32
 */
public class WebViewT extends BaseActivity {
    @BindView(R.id.webView)
    com.tencent.smtt.sdk.WebView mWebView;
    private String strJS;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
    }

    @Override
    protected void setListener() {

        startGather("https://h5.ele.me/hongbao/#hardware_id=&is_lucky_group=True&lucky_number=7&track_id=&platform=4&sn=29d2d8d25eabdcde&theme_id=1449&device_id=");
    }


    /**
     * 开始采集
     */
    private void startGather(String url) {
        WebViewUtils.setWebView(mWebView);
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView  view, String url) {
                view.loadUrl(url);
                if (url.contains("xui.ptlogin2.qq.com")) {
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                }

                return true;
            }

            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView  view, String url) {
                view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                        + "document.getElementsByTagName('*')[0].innerHTML+'</head>');");
                super.onPageFinished(view, url);
            }


            public void onLoadResource(com.tencent.smtt.sdk.WebView  view, String url) {

                super.onLoadResource(view, url);
            }
        });
    }
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
                    mWebView.loadUrl(strJS);
                    break;
                case 1:
                    LoginByPassword("3503564905", "a12345679");
                    break;
            }


            super.handleMessage(msg);
        }
    };
    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            Log.i("html", html);
        }
    }
}
