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
	compile 'com.github.duxl:mobileframe-lib:1.0'  
}
```

## API功能介绍
* [网络接口](#网络接口)
* [圆角图片](#圆角图片)
* [版本更新](#版本更新)
* [下拉刷新列表](#下拉刷新列表)

***

### 网络接口
#### 实现类 com.duxl.mobileframe.http.HttpRequest	
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
* 使用自定义View：RoundedImageView
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
	
***

### 版本更新
* 通知栏更新 com.duxl.mobileframe.util.UpdateVersionNotification
```java
UpdateVersionNotification versionNotification = new UpdateVersionNotification(context, notifyIcon);
versionNotification.doUpdate(apkUrl);
```
* 对话框更新 com.duxl.mobileframe.util.UpdateVersionDialog
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

***

### 下拉刷新列表
#### 实现类 com.duxl.mobileframe.view.XListView
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
	
	
	

