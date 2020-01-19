
# react-native-advert

暂时只支持安卓平台

当前支持广告平台:
- 优量汇版本: 4.40.910
- 穿山甲版本: 2.5.2.6

当前支持广告类型:
- 开屏广告
- 激励视频

## Getting started

`$ npm install react-native-advert --save`

### Mostly automatic installation

`$ react-native link react-native-advert`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-advert` and add `RNAdvert.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNAdvert.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.qhkj.rn.advert.RNAdvertPackage;` to the imports at the top of the file
  - Add `new RNAdvertPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-advert'
  	project(':react-native-advert').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-advert/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-advert')
  	```




add code at android/setting.gradle
```
include ':qhkj-android-advert'
project(':qhkj-android-advert').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-advert/android/qhkj-android-advert')
```

modify MainApplication.java
```
import com.qhkj.rn.advert.Helper;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;


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

modify AndroidManifest.xml
```
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```


## Usage
```javascript
import RNAdvert from 'react-native-advert';

// TODO: What to do with the module?
RNAdvert;
```
  