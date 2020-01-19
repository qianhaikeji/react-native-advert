package com.qhkj.android.advert.gdt;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;

import java.util.Date;
import java.util.Locale;

import com.qhkj.android.advert.R;
import com.qhkj.android.advert.common.MyToast;

public class GDTRewardVideoActivity extends Activity implements RewardVideoADListener,
        AdapterView.OnItemSelectedListener {

  private static final String TAG = GDTRewardVideoActivity.class.getSimpleName();
  private RewardVideoAD rewardVideoAD;
  private EditText posIdEdt;
  private boolean adLoaded;//广告加载成功标志
  private boolean videoCached;//视频素材文件下载完成标志

  private Spinner spinner;
  // private PosIdSpinnerAdapter spinnerAdapter;

  private boolean mFinish = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.gdt_activity_reward_video);

    loadAd();
  }

  private String getPosId() {
    String posId = getIntent().getStringExtra("pos_id");
    return posId;
  }

  private String getAppId() {
    String appId = getIntent().getStringExtra("app_id");
    return appId;
  }



  private void loadAd () {
    // 1. 初始化激励视频广告
    rewardVideoAD = new RewardVideoAD(this, getAppId(), getPosId(), this);
    adLoaded = false;
    videoCached = false;
    // 2. 加载激励视频广告
    rewardVideoAD.loadAD();
  }

  private void showAd () {
    if (adLoaded && rewardVideoAD != null) {
      //广告展示检查1：广告成功加载，此处也可以使用videoCached来实现视频预加载完成后再展示激励视频广告的逻辑
      if (!rewardVideoAD.hasShown()) {//广告展示检查2：当前广告数据还没有展示过
        long delta = 1000;//建议给广告过期时间加个buffer，单位ms，这里demo采用1000ms的buffer
        //广告展示检查3：展示广告前判断广告数据未过期
        if (SystemClock.elapsedRealtime() < (rewardVideoAD.getExpireTimestamp() - delta)) {
          rewardVideoAD.showAD();
        } else {
          showToast("激励视频广告已过期，请再次请求广告后进行广告展示！");
        }
      } else {
        showToast("此条广告已经展示过，请再次请求广告后进行广告展示！");
      }
    } else {
      showToast("成功加载广告后再进行广告展示！");
    }
  }

  /**
   * 广告加载成功，可在此回调后进行广告展示
   **/
  @Override
  public void onADLoad() {
    adLoaded = true;
    String msg = "load ad success ! expireTime = " + new Date(System.currentTimeMillis() +
        rewardVideoAD.getExpireTimestamp() - SystemClock.elapsedRealtime());
    showToast(msg);
    // Log.d(TAG, "eCPM = " + rewardVideoAD.getECPM() + " , eCPMLevel = " + rewardVideoAD.getECPMLevel());

    showAd();
  }

  /**
   * 视频素材缓存成功，可在此回调后进行广告展示
   */
  @Override
  public void onVideoCached() {
    videoCached = true;
    Log.i(TAG, "onVideoCached");
  }

  /**
   * 激励视频广告页面展示
   */
  @Override
  public void onADShow() {
    Log.i(TAG, "onADShow");
  }

  /**
   * 激励视频广告曝光
   */
  @Override
  public void onADExpose() {
    Log.i(TAG, "onADExpose");
  }

  /**
   * 激励视频触发激励（观看视频大于一定时长或者视频播放完毕）
   */
  @Override
  public void onReward() {
    Log.i(TAG, "onReward");
    mFinish = true;
  }

  /**
   * 激励视频广告被点击
   */
  @Override
  public void onADClick() {
    // Map<String, String> map = rewardVideoAD.getExts();
    // String clickUrl = map.get("clickUrl");
    // Log.i(TAG, "onADClick clickUrl: " + clickUrl);
  }

  /**
   * 激励视频播放完毕
   */
  @Override
  public void onVideoComplete() {
    Log.i(TAG, "onVideoComplete");
  }

  /**
   * 激励视频广告被关闭
   */
  @Override
  public void onADClose() {
    Log.i(TAG, "onADClose");
    goToMainActivity();
  }

  /**
   * 广告流程出错
   */
  @Override
  public void onError(AdError adError) {
    String msg = String.format(Locale.getDefault(), "onError, error code: %d, error msg: %s",
        adError.getErrorCode(), adError.getErrorMsg());
    showToast(msg);
    goToMainActivity();
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    // spinnerAdapter.setSelectedPosition(position);
    // String str = Pattern.compile("[^0-9]").matcher(getResources().getStringArray(R.array.reward_video)[position]).replaceAll(",");
    // String[] split = str.split(",");
    // posIdEdt.setText(split[split.length - 1]);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

    /**
   * 跳转到主页面
   */
  private void goToMainActivity() {
    setResult(mFinish ? RESULT_OK : RESULT_CANCELED);
    // Intent intent = new Intent(TTSplashActivity.this, MainActivity.class);
    // startActivity(intent);
    this.finish();
  }

  private void showToast(String msg) {
    //   TToast.show(this, msg);
  }
}
