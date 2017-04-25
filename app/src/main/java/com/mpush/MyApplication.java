package com.mpush;

import android.app.Application;

import com.letv.loginsdk.LetvLoginSdkManager;
import com.letv.loginsdk.LoginSdk;

/**
 * 功能说明： </br>
 *
 * @author: Yiheng Yan
 * @email： yanyiheng@le.com
 * @version: 1.0
 * @date: 16-12-27
 * @Copyright (c) 2016. yanyiheng Inc. All rights reserved.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LetvLoginSdkManager.initSDK(this, "lefinance_201", false, false, false, true, false, false);
        //第一个参数不能为空，否则退出的时候崩溃
        new LetvLoginSdkManager().initThirdLogin("1105283341", null, null, null, null, null, null, null, null, null);
        new LetvLoginSdkManager().initThirdLoginSwitch(false, false, false, false, false, false, false);
    }


}
