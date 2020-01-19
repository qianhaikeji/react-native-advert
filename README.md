
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


## Usage
```javascript
import RNAdvert from 'react-native-advert';

// TODO: What to do with the module?
RNAdvert;
```
  