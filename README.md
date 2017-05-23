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
