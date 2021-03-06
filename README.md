# 如何使用 mobileframe-lib
###### 1、在项目的根build.gradle文件中，添加仓库地址，就像下面一样
```xml
allprojects {  
	repositories {  
		...  
		maven { url 'https://jitpack.io' }  
	}  
}
```

###### 2、在app的build.gradle中添加如下依赖
```xml
dependencies {  
	compile 'com.github.duxl:mobileframe-lib:1.3.2'  
}
```
引入库后，可以启动DemoActivity查看效果<br/>
startActivity(new Intent(context, com.duxl.mobileframe.demo.MainActivity.class));

---

## API功能介绍
* [网络接口](#网络接口 "点击查看说明")
* [圆角图片](#圆角图片 "点击查看说明")
* [版本更新](#版本更新 "点击查看说明")
* [下拉刷新列表](#下拉刷新列表 "点击查看说明")
* [侧滑删除](#侧滑删除 "点击查看说明")
* [防连续点击工具](#防连续点击工具 "点击查看说明")
* [自动切换广告](#自动切换广告 "点击查看说明")
* [时间日期工具](/app/src/main/java/com/duxl/mobileframe/util/DateUtil.java "点击查看源码")
* [Base64](/app/src/main/java/com/duxl/mobileframe/util/Base64.java "点击查看源码")
* [MD5Utils](/app/src/main/java/com/duxl/mobileframe/util/MD5Utils.java "点击查看源码")
* [网络连接判断工具](/app/src/main/java/com/duxl/mobileframe/util/NetworkUtil.java "点击查看源码")
* [双击退出程序](/app/src/main/java/com/duxl/mobileframe/util/DoubleClickExit.java "点击查看源码")
* [序列化存储](/app/src/main/java/com/duxl/mobileframe/util/SerializableDataUtil.java "点击查看源码")
* [SharedPreferencesUtil](/app/src/main/java/com/duxl/mobileframe/util/SharedPreferencesUtil.java "点击查看源码")
* [软键盘显示&隐藏](/app/src/main/java/com/duxl/mobileframe/util/SoftKeyboardUtil.java "点击查看源码")
* [字符串工具类](/app/src/main/java/com/duxl/mobileframe/util/StringUtils.java "点击查看源码")
* [身份证验证](/app/src/main/java/com/duxl/mobileframe/util/IDCardVeryer.java "点击查看源码")
* [价格运算&格式化](/app/src/main/java/com/duxl/mobileframe/util/PriceUtil.java "点击查看源码")
* [动图](#动图 "点击查看说明")
* [首字母快速定位](/app/src/main/java/com/duxl/mobileframe/demo/TestLetterListViewActivity.java "点击查看使用示例")
* [安全的浏览器控件](#安全的浏览器控件 "点击查看说明")
***

### 网络接口
#### 实现类 [HttpRequest](/app/src/main/java/com/duxl/mobileframe/http/HttpRequest.java "点击查看源码")
```java
HttpRequest httpRequest = new HttpRequest(context);
httpRequest.setTimeout(15000); // 设置15秒超时时间
// 1、GET请求
httpRequest.get(url, new HttpRequest.OnCallbackListener() {
    /**
     * @param json 返回的数据
     * @param status 状态：200成功、-1超时、-2无网络、0其他失败
     * @param error 错误描述
     */
    @Override
    public void onCallback(String json, int status, String error) {
	// TODO some codes edit
    }
});
// 2、POST请求一（通过addParam()方法设置post数据）
httpRequest.addParam("name", "zhangSan");
httpRequest.addParam("sex", "boy");
httpRequest.post(url, listener);
// 3、POST请求二（直接post byte数组）
httpRequest.post(url, byte[], listener);
```

***

### 圆角图片
* 使用自定义View：[RoundedImageView](/app/src/main/java/com/duxl/mobileframe/view/roundedimageview/RoundedImageView.java "点击查看源码")
```xml
	<com.duxl.mobileframe.view.roundedimageview.RoundedImageView
		android:id="@+id/ivRoundView_activity_test_roundimageview"
		android:layout_width="200dp"
		android:layout_height="200dp"
		android:layout_marginTop="20dp"
		android:scaleType="fitXY"
		app:riv_corner_radius="10dp"
		app:riv_border_width="3dp"
		app:riv_border_color="#0000FF"
		app:riv_tile_mode_y="clamp"
		app:riv_tile_mode_x="clamp"/>
```
* 使用系统的ImageView，在java代码中
```java
	ImageOptions options = new ImageOptions();
	options.targetWidth = 100; // 限制图片大小
	options.fileCache = true;
	options.memCache = true;
	options.round = 30; // 圆角半径
	new AQuery(this).id(ivImg).image(url, options)
```
* [Fresco](https://www.fresco-cn.org "点击查看")也支持圆角图片，接入[文档](https://www.fresco-cn.org/docs)
	
***

### 版本更新
* 通知栏更新 [UpdateVersionNotification](/app/src/main/java/com/duxl/mobileframe/util/UpdateVersionNotification.java "点击查看源码")
```java
UpdateVersionNotification versionNotification = new UpdateVersionNotification(context, notifyIcon);
versionNotification.doUpdate(apkUrl);
```
* 对话框更新 [UpdateVersionDialog](/app/src/main/java/com/duxl/mobileframe/util/UpdateVersionDialog.java "点击查看源码")
```java
UpdateVersionDialog updateUtils = new UpdateVersionDialog(context);
updateUtils.setOnCancelListener(new UpdateVersionDialog.OnCancelListener() {
    @Override
    public void onCancel() {
	// 切换到后台下载
    }
});
updateUtils.doUpdate(apkUrl, true);
```
`高版本系统更新说明` [动态存储权限分配](https://github.com/duxl/mobileframe-lib/blob/master/高于6.0系统动态分配权限问题说明 "点击查看代码实现")
***

### 下拉刷新列表
#### 实现类 [XListView](/app/src/main/java/com/duxl/mobileframe/view/XListView.java "点击查看源码")
* xml代码
```xml
<com.duxl.mobileframe.view.XListView
	android:id="@+id/listview_activity_test"
	android:cacheColorHint="@android:color/transparent"
	android:divider="@color/line_color"
	android:dividerHeight="1dp"
	android:layout_width="match_parent"
	android:layout_height="match_parent" />
```
* java代码
```java
XListView listview = (XListView) findViewById(R.id.listview_activity_test);
listview.setLoadMoreEnable(false);
listview.setXListViewListener(new XListView.IXListViewListener() {
    @Override
    public void onRefresh(XListView v) {
	// 下拉刷新回调
	loadData(true);
    }

    @Override
    public void onLoadMore(XListView v) {
	// 点击更多回调
	loadData(false);
    }
});
// 滑动到最后自动加载更多（点击加载更多设置为MANUAL）
listview.setLoadMoreType(XListView.LoadMore.AUTOMATIC);
listview.setAdapter(mAdapter);
// 显示刷新动画，此方法在第一次进入列表，用户没有下拉刷新而是程序自动加载第页数据的时候，可以调用此方法。
listview.showRefresh();
loadData(true);



/**
* 加载数据
* @param isRefresh 是否刷新
*/
public void loadData(final boolean isRefresh) {
	// 调用接口代码省略，下面代码放在接口返回数据后的
	listView.stopRefresh();
	listView.stopLoadMore();

	if(mListData.size() < totalCount) {
	    listView.setLoadMoreEnable(true);

	} else {
	    listView.setLoadMoreEnable(false);
	}
}
```

***

### 侧滑删除
#### 示例比较复杂，不在这里展示，请查看 [TestSlideViewActivity](/app/src/main/java/com/duxl/mobileframe/demo/TestSlideViewActivity.java "点击查看源码")

***

### 防连续点击工具
#### [ViewClickDelayUtil](/app/src/main/java/com/duxl/mobileframe/util/ViewClickDelayUtil.java "点击查看源码")
```java
// onClick事件代码如下编写
@Override
public void onClick(View v) {
	// 防止1秒内连续点击
	ViewClickDelayUtil.clickDelay(MainActivity.this, v, 1000);

	if(v.getId() == R.id.xxx) {

	} else if(v.getId() == R.id.yyy) {

	}
}
```

***

### 自动切换广告
#### 具体使用示例，请查看 [TestAdvShowActivity](/app/src/main/java/com/duxl/mobileframe/demo/TestAdvShowActivity.java "点击查看源码")
```java
// 实例化工具
AdvShowUtil advShowUtil= new AdvShowUtil(context, viewPager, radioGroup);
// 设置指示器样式
advShowUtil.setPointResid(R.drawable.adv_point_selector); 
// 显示广告并自动切换
advShowUtil.showAdvData(advItems);
// 停止广告滚动，当页面销毁时需要调用此API，避免多次使用造成OOM问题
advShowUtil.recycle();
```

***

### 动图
#### gif动图android本身不支持，本lib库也不包含，需要使用，请转移到[Fresco](https://www.fresco-cn.org "点击查看")，接入[文档](https://www.fresco-cn.org/docs)
* 使用示例（SimpleDraweeView必须指定具体高宽，否则不能显示）
```xml
// xml布局
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    xmlns:fresco="http://schemas.android.com/apk/res-auto"
    android:layout_height="match_parent">

    <com.facebook.drawee.view.SimpleDraweeView
        android:id="@+id/gifView_activity_test_gifview"
        fresco:placeholderImage="@mipmap/ic_launcher"
        fresco:viewAspectRatio="1"
        android:scaleType="fitXY"
        android:background="#0000FF"
        android:layout_width="200dp"
        android:layout_height="200dp" />

</LinearLayout>
```
```java
// java代码
SimpleDraweeView mGifView = (SimpleDraweeView) findViewById(R.id.gifView_activity_test_gifview);

Uri uri = Uri.parse("http://img.huofar.com/data/jiankangrenwu/shizi.gif");
DraweeController draweeController =
	Fresco.newDraweeControllerBuilder()
		.setUri(uri)
		.setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
		.build();
mGifView.setController(draweeController);
```

### 安全的浏览器控件
WebView注入Java对象给Js调用，在API 17以下有安全漏洞问题。这里提供一个安全的WebView，具体使用请参考 [TestHtmlJsActivity](/app/src/main/java/com/duxl/mobileframe/demo/TestHtmlJsActivity.java "点击查看源码")
	

