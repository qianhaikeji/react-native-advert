package com.qhkj.rn.advert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import com.qhkj.android.advert.gdt.GDTRewardVideoActivity;
import com.qhkj.android.advert.gdt.GDTSplashActivity;
import com.qhkj.android.advert.tt.TTRewardVideoActivity;
import com.qhkj.android.advert.tt.TTSplashActivity;

public class RNAdvertModule extends ReactContextBaseJavaModule {

  private static final int SHOW_SPLASH_REQUEST = 1;
  private static final int SHOW_REWARD_VIDEO_REQUEST = 2;

  private ReadableMap mConfig;
  private Promise mAdvertPromise;

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      if (requestCode == SHOW_SPLASH_REQUEST
              || requestCode == SHOW_REWARD_VIDEO_REQUEST) {
        if (mAdvertPromise != null) {
          if (resultCode == Activity.RESULT_CANCELED) {
            mAdvertPromise.resolve(false);
          } else if (resultCode == Activity.RESULT_OK) {
            mAdvertPromise.resolve(true);
          }

          mAdvertPromise = null;
        }
      }
    }
  };

  public RNAdvertModule(ReactApplicationContext reactContext) {
    super(reactContext);

    // Add the listener for `onActivityResult`
    reactContext.addActivityEventListener(mActivityEventListener);
  }


  @Override
  public String getName() {
    return "RNAdvert";
  }

  @ReactMethod
  public void init(ReadableMap config) {
    mConfig = config;
  }

  @ReactMethod
  public void showSplash(final Promise promise) {
    Context context = getReactApplicationContext();
    Intent intent;

    mAdvertPromise = promise;

    double random = Math.random();
    if (random <= 0.5) {
      intent = new Intent(context, GDTSplashActivity.class);
      intent.putExtra("app_id", mConfig.getString("gdtAppId"));
      intent.putExtra("pos_id", mConfig.getString("gdtSplashPosId"));
    } else {
      intent = new Intent(context, TTSplashActivity.class); // mContext got from your overriden constructor
      intent.putExtra("is_express", mConfig.getBoolean("ttSplashIsExpress"));
      intent.putExtra("pos_id", mConfig.getString("ttSplashPosId"));
    }

    try {
      getCurrentActivity().startActivityForResult(intent, SHOW_SPLASH_REQUEST);
      getCurrentActivity().overridePendingTransition(0, 0);
    } catch (Exception e) {
      mAdvertPromise.reject("拉起开屏广告失败！", e);
      mAdvertPromise = null;
    }
  }

  @ReactMethod
  public void showRewardVideo(final Promise promise) {
    Context context = getReactApplicationContext();
    Intent intent;

    mAdvertPromise = promise;

    double random = Math.random();
    if (random <= 0.5) {
      intent = new Intent(context, GDTRewardVideoActivity.class);
      intent.putExtra("app_id", mConfig.getString("gdtAppId"));
      intent.putExtra("pos_id", mConfig.getString("gdtRewardVideoPosId"));
    } else {
      intent = new Intent(context, TTRewardVideoActivity.class); // mContext got from your overriden constructor
      intent.putExtra("horizontal_rit", mConfig.getString("ttRewardVideoHPosId"));
      intent.putExtra("vertical_rit", mConfig.getString("ttRewardVideoVPosId"));
    }

    try {
      getCurrentActivity().startActivityForResult(intent, SHOW_REWARD_VIDEO_REQUEST);
      getCurrentActivity().overridePendingTransition(0, 0);
    } catch (Exception e) {
      mAdvertPromise.reject("拉起激励视频广告失败！", e);
      mAdvertPromise = null;
    }
  }

}