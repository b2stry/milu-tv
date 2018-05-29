package com.shallowan.milutv;

import android.app.Application;
import android.content.Context;

import com.shallowan.milutv.editprofile.CustomProfile;
import com.shallowan.milutv.utils.QnUploadHelper;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.TIMManager;
import com.tencent.TIMUserProfile;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ShallowAn.
 */

public class MiluApplication extends Application {

    private int mAppId = xxxxxxxx;
    private int mAccountType = xxxxxx;

    private static MiluApplication app;
    private static Context appContext;
    private ILVLiveConfig mLiveConfig;

    private TIMUserProfile mSelfProfile;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appContext = getApplicationContext();
        ILiveSDK.getInstance().initSdk(getApplicationContext(), mAppId, mAccountType);
        List<String> customInfos = new ArrayList<>();
        customInfos.add(CustomProfile.CUSTOM_GET);
        customInfos.add(CustomProfile.CUSTOM_SEND);
        customInfos.add(CustomProfile.CUSTOM_LEVEL);
        customInfos.add(CustomProfile.CUSTOM_RENZHENG);
        TIMManager.getInstance().initFriendshipSettings(CustomProfile.allBaseInfo, customInfos);


        //初始化直播场景
        mLiveConfig = new ILVLiveConfig();
        ILVLiveManager.getInstance().init(mLiveConfig);

        QnUploadHelper.init("xxxxxxx",
                "xxxxxxxx",
                "http://p16xf7hup.bkt.clouddn.com/",
                "shallowan");

        LeakCanary.install(this);

    }

    public static Context getContext() {
        return appContext;
    }

    public static MiluApplication getApplication() {
        return app;
    }

    public void setSelfProfile(TIMUserProfile userProfile) {
        mSelfProfile = userProfile;
    }

    public TIMUserProfile getSelfProfile() {
        return mSelfProfile;
    }

    public ILVLiveConfig getLiveConfig() {
        return mLiveConfig;
    }
}
