package org.chzz.demo.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.tencent.smtt.sdk.CacheManager;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import org.chzz.demo.R;
import org.chzz.demo.common.BaseActivity;
import org.chzz.demo.model.AccountEntity;
import org.chzz.demo.uitl.WebViewUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
    String userName, passWord, url;
    private boolean isGRT = false;
    private boolean isResult = true;
    AccountEntity bean;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        userName = getIntent().getStringExtra("userName");
        passWord = getIntent().getStringExtra("passWord");
        url = getIntent().getStringExtra("url");

    }

    @Override
    protected void setListener() {
        mTitle.setText(userName + "将登录领券");
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
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView view, String url) {
                view.loadUrl(url);
                if (url.contains("xui.ptlogin2.qq.com")) {
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                }

                return true;
            }

            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView view, String url) {
                view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                        + "document.getElementsByTagName('*')[0].innerHTML+'</head>');");
                super.onPageFinished(view, url);
            }


            public void onLoadResource(com.tencent.smtt.sdk.WebView view, String url) {

                if (url.contains("https://shadow.elemecdn.com/faas/h5/hongbao/") && url.contains(".js") && isGRT) {
                    mWebView.loadUrl(url);
                }
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
        mHandler.sendEmptyMessageDelayed(0, 2000);

    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            Log.i("html", html);
            if (html.contains("看朋友们手气如何")&&isResult) {
               // mWebView.loadUrl(url);
                 isGRT = false;
                bean = test(html);
                if (bean.getList().size() > 0 && isResult) {
                    isResult = false;
                    mHandler.sendEmptyMessageDelayed(2, 1000);
                }

            }
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mWebView.loadUrl(strJS);

                        isGRT = true;

                    break;
                case 1:
                    LoginByPassword(userName, passWord);
                    break;
                case 2:
                    Intent intent = new Intent();
                    intent.putExtra("bean", bean);
                    setResult(100, intent);
                    finish();
                    break;
            }


            super.handleMessage(msg);
        }
    };

    @Override
    public void onDestroy() {
        CookieSyncManager.createInstance(this);  //Create a singleton CookieSyncManager within a context
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
        deleteDatabase("webview.db");
        deleteDatabase("webviewCache.db");

        super.onDestroy();
    }

    private AccountEntity test(String json) {
        Document doc = Jsoup.parse(json);
        Elements elements = doc.getElementsByTag("strong");
       // Elements elements1 = doc.getElementsByAttributeValue("style", "color: rgb(");
        Elements elements1 =doc.getElementsByAttributeValueMatching("style", Pattern.compile("[^background]"));
        List<Element > list=new ArrayList<>();
        for (int i = 0; i < elements1.size(); i++) {
            Element e = elements1.get(i);
            Log.i("Element",e.text()+">>"+ e.tagName()+">>>"+e.className());
            if (e.text().contains("元")&&e.tagName().contains("div")&&e.className().contains("records")) {
                list.add(e);
            }
        }
        List<AccountEntity.DataEntity> result = new ArrayList<>();
        for (int a = 0; a < elements.size(); a++) {
            Element link = elements.get(a);
            Element link1 = list.get(a);
            String linkText = link.text();
            Log.i("linkText", linkText + ">>>" + link1.text());
            result.add(new AccountEntity.DataEntity(linkText, link1.text()));
        }
        AccountEntity bean = new AccountEntity();
        bean.setList(result);
        return bean;
    }


}
