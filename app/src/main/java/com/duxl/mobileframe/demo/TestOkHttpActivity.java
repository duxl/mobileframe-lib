package com.duxl.mobileframe.demo;

import android.os.Bundle;
import android.view.View;

import com.duxl.mobileframe.BaseActivity;
import com.duxl.mobileframe.R;
import com.duxl.mobileframe.http.HttpRequest;
import com.duxl.mobileframe.view.Toast;


/**
 * HttpRequest 使用 okHttp示例
 * Created by duxl on 2016/3/3.
 */
public class TestOkHttpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_okhttp);
        findViewById(R.id.btnGet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm");
            }
        });

    }

    public void post(String url) {
        HttpRequest httpRequest = new HttpRequest(this);
        httpRequest.post(url, new HttpRequest.OnCallbackListener() {
            @Override
            public void onCallback(String json, int status, String error) {
                // TODO
            }
        });
    }

    public void get(String url) {
        showProgressDialog("获取手机号信息");
        HttpRequest httpRequest = new HttpRequest(this);
        httpRequest.addParam("tel", "13588888888");
        httpRequest.post(url, new HttpRequest.OnCallbackListener() {
            @Override
            public void onCallback(String json, int status, String error) {
                dismissProgressDialog();
                Toast.makeText(TestOkHttpActivity.this, json, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
