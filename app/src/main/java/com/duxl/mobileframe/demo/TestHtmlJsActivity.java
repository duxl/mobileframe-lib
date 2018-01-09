package com.duxl.mobileframe.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.duxl.mobileframe.demo.model.UserInfo;
import com.duxl.mobileframe.view.JsSecurityWebView;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全的WebView使用示例
 * Created by duxl on 2016/4/1.
 */
public class TestHtmlJsActivity extends Activity {

    private JsSecurityWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new JsSecurityWebView(this);
        setContentView(webView);

        webView.getSettings().setJavaScriptEnabled(true);
        // 这里使用的是set方法替换add方法
        webView.setJavascriptInterface(new JavaScriptObject(this), "appClient");

        webView.loadUrl("file:///android_asset/testjs.html");

        // 处理业务逻辑上的onJsPrompt事件
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TestHtmlJsActivity.this);
                builder.setTitle("没有映射的方法，调用用户的onJsPrompt处理逻辑");
                builder.setMessage(message);
                EditText editText = new EditText(TestHtmlJsActivity.this);
                editText.setText(defaultValue);
                builder.setView(editText);
                builder.setPositiveButton("确定", null);
                builder.setNegativeButton("取消", null);
                builder.create().show();

                result.cancel();
                return true;
            }
        });

    }

    public static class JavaScriptObject {

        public Context mContext;

        public JavaScriptObject(Context mContext) {
            this.mContext = mContext;
        }

		@JavascriptInterface
        public void test() {
            Toast.makeText(mContext, "TestHtmlJsActivity#test", Toast.LENGTH_SHORT).show();
        }

		@JavascriptInterface
        public void sayHello(String name) {
            Toast.makeText(mContext, "TestHtmlJsActivity#sayHello=" + name, Toast.LENGTH_SHORT).show();
        }

		@JavascriptInterface
        public int add(int num1, int num2) {
            return num1 + num2;
        }

		@JavascriptInterface
        public void showInfo(String name, int age) {
            Toast.makeText(mContext, "TestHtmlJsActivity#name=" + name + ", age=" + age, Toast.LENGTH_SHORT).show();
        }

		@JavascriptInterface
        public List<String> getList() {
            List<String> list = new ArrayList<>();
            list.add("One");
            list.add("Two");
            list.add("Three");
            return list;
        }

		@JavascriptInterface
        public UserInfo getUser() {
            return new UserInfo();
        }

		@JavascriptInterface
        public void showUser(UserInfo userInfo) {
            Toast.makeText(mContext, "TestHtmlJsActivity#"+userInfo.toString(), 0).show();
        }
    }
}
