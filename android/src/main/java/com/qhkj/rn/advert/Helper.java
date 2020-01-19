package com.qhkj.rn.advert;

import android.content.Context;

import com.qhkj.android.advert.tt.TTAdManagerHolder;
import com.bytedance.sdk.openadsdk.TTAdConfig;

public class Helper {
    public static void initTTAdManager(Context context, TTAdConfig ttAdConfig) {
        TTAdManagerHolder.init(context, ttAdConfig);
    }
}
