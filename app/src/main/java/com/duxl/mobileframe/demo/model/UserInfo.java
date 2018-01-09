package com.duxl.mobileframe.demo.model;

/**
 * 作者：Created by duxl on 2018/01/05.
 * 公司：重庆赛博丁科技发展有限公司
 * 类描述：xxx
 */

public class UserInfo {

    public String name = "张三";

    public int age = 25;

    public boolean isBoy = true;

    public float height = 1.73f;

    @Override
    public String toString() {
        return "UserInfo{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", isBoy=" + isBoy +
                ", height=" + height +
                '}';
    }
}
