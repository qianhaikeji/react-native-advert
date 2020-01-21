
# react-native-advert

暂时只支持安卓平台

当前支持广告平台:
- 优量汇版本: 4.40.910
- 穿山甲版本: 2.5.2.6

当前支持广告类型:
- 开屏广告
- 激励视频

## Getting started

`$ yarn add react-native-advert`

### Install

#### iOS
暂不支持ios平台

#### Android
支持react native的Autolinking特性，

if your react native < 0.60.0
```
$ react-native link react-native-advert
```

### Manually Configure Part

#### iOS
暂不支持ios平台

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add code to the imports at the top of the file
    ```
    import com.qhkj.rn.advert.RNAdvertPackage;
    import com.qhkj.rn.advert.Helper;
    import com.bytedance.sdk.openadsdk.TTAdConfig;
    import com.bytedance.sdk.openadsdk.TTAdConstant;
    ```
  - if your want integrate 穿山甲广告平台, Add code to onCreate function
    ```
    @Override
      public void onCreate() {
      ...

        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        Helper.initTTAdManager(this, new TTAdConfig.Builder()
                .appId("your tt appId")
                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .appName("your app name")
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(false) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
                .supportMultiProcess(false)//是否支持多进程
                // .needClearTaskReset()
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build());
      }
    ```
  - if your react native < 0.60.0, Add `new RNAdvertPackage()` to the list returned by the `getPackages()` method
  
2. Append the following lines to `android/settings.gradle`:
  	```
    include ':qhkj-android-advert'
    project(':qhkj-android-advert').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-advert/android/qhkj-android-advert')
  	```
3. (Optional) Open up `android/app/src/main/java/[...]/AndroidManifest.xml`, add code to the file
    ```
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    ```


## Usage
```javascript
import Advert from 'react-native-advert';


// TODO: What to do with the module?
RNAdvert;
```

### Init advert module with AD platform appId and posId

```javascript
Advert.init({
      gdtAppId: '广点通appId',
      gdtSplashPosId: '广点通开屏广告位ID',
      gdtRewardVideoPosId: '广点通激励视频广告位ID',
      ttAppId: '穿山甲appId',
      ttSplashIsExpress: false,
      ttSplashPosId: '穿山甲开屏广告位ID',
      ttRewardVideoVPosId: '穿山甲激励视频竖屏广告位ID',
      ttRewardVideoHPosId: '穿山甲激励视频横屏广告位ID，暂不支持',
    })
```

### show splash ad
```javascript
try {
  await Advert.showSplash()
  console.log('splash finish!')
} catch (err) {
  console.log('splash failed!', err)
}
```

### show reward video ad
```javascript
try {
  const finish = await Advert.showRewardVideo()
  if (finish) {
    console.log('get reward')
  } else {
    console.log('no reward')
  }
} catch (err) {
  console.log(err)
}
```
  

## ToDo List
- 集成插屏广告接口