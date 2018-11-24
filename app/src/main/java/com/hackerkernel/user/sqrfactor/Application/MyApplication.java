package com.hackerkernel.user.sqrfactor.Application;

import android.app.Application;
import android.content.Context;

import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;

import net.gotev.uploadservice.BuildConfig;
import net.gotev.uploadservice.UploadService;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static MyApplication mInstance;
    private static MySharedPreferences mSp;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        mSp = MySharedPreferences.getInstance(this);

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        // Or, you can define it manually.
        UploadService.NAMESPACE = "com.hackerkernel.user.sqrfactor";
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }

}
