package org.chzz.demo.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.tencent.smtt.sdk.CacheManager;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import org.chzz.demo.R;
import org.chzz.demo.common.BaseFragment;
import org.chzz.demo.model.AccountEntity;
import org.chzz.demo.model.CouponResult;
import org.chzz.demo.uitl.WebViewUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by copy on 2017/10/16.
 * Description:
 * User: copy
 * Date: 2017-10-16
 * Time: 下午9:17
 */
public class WebViewFragment extends BaseFragment {
    @BindView(R.id.webView)
    com.tencent.smtt.sdk.WebView mWebView;
    String url = "https://h5.ele.me/hongbao/#hardware_id=&is_lucky_group=True&lucky_number=7&track_id=&platform=4&sn=29d2d8d25eabdcde&theme_id=1449&device_id=";
    private String strJS;
    private boolean isGRT=false;
    private String userName, passWord;
    CouponResult couponResult;
    private boolean isResult=true;

    public WebViewFragment() {
    }

    public WebViewFragment(CouponResult couponResult) {
        this.couponResult = couponResult;
    }

    public void setInfo(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mContentView = View.inflate(getActivity(), R.layout.fragment_webview, null);
        ButterKnife.bind(this, mContentView);
    }

    @Override
    protected void setListener() {
        startGather(url);

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
            public void onPageFinished(com.tencent.smtt.sdk.WebView view, String url) {
                view.loadUrl("javascript:window.local_obj.showSource('<symbol>'+"
                        + "document.getElementsByTagName('*')[0].innerHTML+'</symbol>');");
                //  view.loadUrl("javascript:window.local_obj.showSource(document.body.innerHTML);");
                super.onPageFinished(view, url);
            }


            public void onLoadResource(com.tencent.smtt.sdk.WebView view, String url) {

                Log.i("url", url);

                if (url.contains("https://shadow.elemecdn.com/faas/h5/hongbao/") && url.contains(".js") && isGRT) {
                    mWebView.loadUrl(url);
                }


                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView view, String url) {
                view.loadUrl(url);
                if (url.contains("xui.ptlogin2.qq.com")) {
                    mHandler.sendEmptyMessageDelayed(1, 1000);

                }

                return true;
            }
        });
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            Log.i("html", html);
            if (html.contains("看朋友们手气如何")) {
                mWebView.loadUrl(url);
                if (null != couponResult && test(html).size() > 0&& isResult) {
                    couponResult.result(test(html));
                    isResult=false;
                }
                isGRT = false;
            }
        }
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
                    isGRT=true;
                    break;
                case 1:
                    LoginByPassword(userName, passWord);
                    break;
            }


            super.handleMessage(msg);
        }
    };

    @Override
    public void onDestroy() {
        CookieSyncManager.createInstance(getContext());  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

        mWebView.setWebChromeClient(null);
        mWebView.setWebViewClient(null);
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.clearCache(true);
        mWebView.clearFormData();
        File file = CacheManager.getCacheFileBaseDir();
        if (file != null && file.exists() && file.isDirectory()) {
            for (File item : file.listFiles()) {
                item.delete();
            }
            file.delete();
        }
        getContext().deleteDatabase("webview.db");
        getContext().deleteDatabase("webviewCache.db");

        super.onDestroy();
    }

    private List<AccountEntity> test(String json) {
        Document doc = Jsoup.parse(json);
        Elements elements = doc.getElementsByTag("strong");
        Elements elements1 = doc.getElementsByAttributeValue("style", "color: rgb(0, 120, 255);");
        elements1 = elements1.removeAttr("strong");
        for (int i = 0; i < elements1.size(); i++) {
            Element e = elements1.get(i);
            if (!e.text().contains("元")) {
                elements1.remove(i);
            }
        }
        List<AccountEntity> result = new ArrayList<>();
        for (int a = 0; a < elements.size(); a++) {
            Element link = elements.get(a);
            Element link1 = elements1.get(a);
            String linkText = link.text();
            Log.i("linkText", linkText + ">>>" + link1.text());
           // result.add(new AccountEntity(linkText, link1.text()));
        }
        return result;
    }


}
