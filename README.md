# 如何使用 mobileframe-lib
1、在项目的跟build.gradle文件中，添加仓库地址，就像下面一样  
allprojects {  
	repositories {  
		...  
		maven { url 'https://jitpack.io' }  
	}  
}

2、在app的build.gradle中添加如下依赖    
dependencies {  
	compile 'com.github.duxl:mobileframe-lib:1.0'  
}  

## API功能介绍
###com.duxl.mobileframe.http.HttpRequest网络接口
设置超时时间：httpRequest.setTimeout(int time)  
get请求：httpRequest.get(String url, HttpRequest.OnCallbackListener listener)  
post请求：httpRequest.post(String url, HttpRequest.OnCallbackListener listener),参数在post之前，调用addParam(String, String)添加post数据  
post请求：httpRequest.post(String url, byte[] data, HttpRequest.OnCallbackListener listener)
