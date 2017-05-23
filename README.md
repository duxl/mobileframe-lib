# 如何使用 mobileframe-lib
1、在项目的跟build.gradle文件中，添加仓库地址，就像下面一样  
--
allprojects {  
	repositories {  
		...  
		maven { url 'https://jitpack.io' }  
	}  
}

2、在app的build.gradle中添加如下依赖
--
dependencies {  
	compile 'com.github.duxl:mobileframe-lib:1.0'  
}  

## API功能介绍
* [网络接口](#网络接口)
* [圆角图片](#圆角图片)

***

### 网络接口 com.duxl.mobileframe.http.HttpRequest	
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
```java
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
	
	
	

