package com.qhkj.android.advert.tt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;

import com.qhkj.android.advert.R;
import com.qhkj.android.advert.common.MyToast;

public class TTRewardVideoActivity extends Activity {
    private Button mLoadAd;
    private Button mLoadAdVertical;
    private Button mShowAd;
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private String mHorizontalCodeId;
    private String mVerticalCodeId;
    private boolean mFinish = false;


    @SuppressWarnings("RedundantCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tt_activity_reward_video);
        // mLoadAd = (Button) findViewById(R.id.btn_reward_load);
        // mLoadAdVertical = (Button) findViewById(R.id.btn_reward_load_vertical);
        // mShowAd = (Button) findViewById(R.id.btn_reward_show);
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(getApplicationContext());
        getCodeId();

        loadAd(mVerticalCodeId, TTAdConstant.VERTICAL);

        // initClickEvent();
    }

    private void getCodeId() {
        Intent intent = getIntent();
        if(intent == null) {
            return;
        }
        mHorizontalCodeId = intent.getStringExtra("horizontal_rit");
        mVerticalCodeId = intent.getStringExtra("vertical_rit");
    }


    private void initClickEvent() {
        mLoadAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd(mHorizontalCodeId, TTAdConstant.HORIZONTAL);
            }
        });
        mLoadAdVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd(mVerticalCodeId, TTAdConstant.VERTICAL);
            }
        });
        mShowAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mttRewardVideoAd != null) {
                    //step6:在获取到广告后展示
                    //该方法直接展示广告
//                    mttRewardVideoAd.showRewardVideoAd(TTRewardVideoActivity.this);

                    //展示广告，并传入广告展示的场景
                    mttRewardVideoAd.showRewardVideoAd(TTRewardVideoActivity.this,TTAdConstant.RitScenes.CUSTOMIZE_SCENES,"scenes_test");
                    mttRewardVideoAd = null;
                } else {
                    showToast("请先加载广告");
                }
            }
        });
    }

    private void showAd () {
        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示
            //该方法直接展示广告
//                    mttRewardVideoAd.showRewardVideoAd(TTRewardVideoActivity.this);

            //展示广告，并传入广告展示的场景
            mttRewardVideoAd.showRewardVideoAd(TTRewardVideoActivity.this,TTAdConstant.RitScenes.CUSTOMIZE_SCENES,"scenes_test");
            mttRewardVideoAd = null;
        } else {
            showToast("请先加载广告");
        }
    }

    private boolean mHasShowDownloadActive = false;
    private void loadAd(String codeId, int orientation) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(3)  //奖励的数量
                .setUserID("user123")//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                showToast(message);
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                showToast("rewardVideoAd video cached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                // showToast("rewardVideoAd loaded 广告类型：" + getAdType(ad.getRewardVideoAdType()));
                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        showToast("rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        showToast("rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        showToast("rewardVideoAd close");
                        goToMainActivity();
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        showToast("rewardVideoAd complete");
                    }

                    @Override
                    public void onVideoError() {
                        showToast("rewardVideoAd error");
                        goToMainActivity();
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        showToast("verify:" + rewardVerify + " amount:" + rewardAmount +
                                " name:" + rewardName);
                        mFinish = true;
                    }

                    @Override
                    public void onSkippedVideo() {
                        showToast("rewardVideoAd has onSkippedVideo");
                        goToMainActivity();
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            showToast("下载中，点击下载区域暂停");
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        showToast("下载暂停，点击下载区域继续");
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        showToast("下载失败，点击下载区域重新下载");
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        showToast("下载完成，点击下载区域重新下载");
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        showToast("安装完成，点击下载区域打开");
                    }
                });

                showAd();
            }
        });
    }


    // private String getAdType(int type) {
    //     switch (type) {
    //         case TTAdConstant.AD_TYPE_COMMON_VIDEO:
    //             return "普通激励视频，type=" + type;
    //         case TTAdConstant.AD_TYPE_PLAYABLE_VIDEO:
    //             return "Playable激励视频，type=" + type;
    //         case TTAdConstant.AD_TYPE_PLAYABLE:
    //             return "纯Playable，type=" + type;
    //     }

    //     return "未知类型+type="+type;
    // }


    /**
     * 跳转到主页面
     */
    private void goToMainActivity() {
        setResult(mFinish ? RESULT_OK : RESULT_CANCELED);
        this.finish();
    }

    private void showToast(String msg) {
        // TToast.show(this, msg);
    }
}
