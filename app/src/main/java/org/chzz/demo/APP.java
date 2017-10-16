package org.chzz.demo;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.tencent.smtt.sdk.QbSdk;

import org.chzz.core.app.Chzz;
import org.chzz.core.net.cookie.CookiesManager;
import org.chzz.demo.common.ConstantValues;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by copy on 2017/10/12.
 */

public class APP extends Application {

    private static APP mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
        mApp = this;
        //听诊器  如要使用网络听诊器 必须自定义 okHttpClient 传入Chzz
        Stetho.initializeWithDefaults(this);
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .followRedirects(false)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .cookieJar(new CookiesManager(this))
                .build();

        //初始化所有配置
        Chzz.init(this)
                .withApiHost(ConstantValues.BASE_URL)
                .withOkHttpClient(client)
                .configure();


    }

    public static APP getInstance() {

        return mApp;
    }
}
