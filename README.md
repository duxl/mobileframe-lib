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
### 网络接口 com.duxl.mobileframe.http.HttpRequest		
`HttpRequest httpRequest = new HttpRequest(context);`		
httpRequest.setTimeout(15000); // 设置15秒超时时间		
        String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=13588888888";		
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
	


a<br/>
b
