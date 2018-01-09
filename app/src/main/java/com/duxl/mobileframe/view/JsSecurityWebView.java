package com.duxl.mobileframe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * Js安全的WebView，添加
 * Created by duxl on 2016/4/5.
 */
public class JsSecurityWebView extends WebView {

    private Object mJsObject;
    private WebChromeClient mWebChromeClient;
    private boolean mInited;

    public JsSecurityWebView(Context context) {
        super(context);
        init(context);
    }

    public JsSecurityWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public JsSecurityWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public JsSecurityWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 抛弃使用此方法，你可以访问 {@link #setJavascriptInterface(Object, String) 来达到相同的目的}
     * @param object
     * @param name
     */
    @Deprecated
    @Override
    public void addJavascriptInterface(Object object, String name) {
        super.addJavascriptInterface(object, name);
    }

    private void init(Context context) {
        mInited = true;
        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if(mWebChromeClient != null) {
                    return mWebChromeClient.onJsAlert(view, url, message, result);
                }
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                if(mWebChromeClient != null) {
                    return mWebChromeClient.onJsConfirm(view, url, message, result);
                }
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onCloseWindow(window);
                }
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                if(mWebChromeClient != null) {
                    return mWebChromeClient.onConsoleMessage(consoleMessage);
                }
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                if(mWebChromeClient != null) {
                    return mWebChromeClient.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
                }
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                if(mWebChromeClient != null) {
                    return mWebChromeClient.onJsBeforeUnload(view, url, message, result);
                }
                return super.onJsBeforeUnload(view, url, message, result);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if(mWebChromeClient != null) {
                    return mWebChromeClient.onShowFileChooser(webView, filePathCallback, fileChooserParams);
                }
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }

            @Override
            public void onGeolocationPermissionsHidePrompt() {
                super.onGeolocationPermissionsHidePrompt();
                if(mWebChromeClient != null) {
                    mWebChromeClient.onGeolocationPermissionsHidePrompt();
                }
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onGeolocationPermissionsShowPrompt(origin, callback);
                }
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                if(mWebChromeClient != null) {
                    mWebChromeClient.onHideCustomView();
                }
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                super.onPermissionRequest(request);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onPermissionRequest(request);
                }
            }

            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
                super.onPermissionRequestCanceled(request);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onPermissionRequestCanceled(request);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onProgressChanged(view, newProgress);
                }
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onReceivedIcon(view, icon);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onReceivedTitle(view, title);
                }
            }

            @Override
            public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
                super.onReceivedTouchIconUrl(view, url, precomposed);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onReceivedTouchIconUrl(view, url, precomposed);
                }
            }

            @Override
            public void onRequestFocus(WebView view) {
                super.onRequestFocus(view);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onRequestFocus(view);
                }
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onShowCustomView(view, callback);
                }
            }

            @Override
            public boolean onJsTimeout() {
                if(mWebChromeClient != null) {
                    return mWebChromeClient.onJsTimeout();
                }
                return super.onJsTimeout();

            }

            @Override
            public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
                super.onShowCustomView(view, requestedOrientation, callback);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onShowCustomView(view, requestedOrientation, callback);
                }
            }

            @Override
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                super.onConsoleMessage(message, lineNumber, sourceID);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onConsoleMessage(message, lineNumber, sourceID);
                }
            }

            @Override
            public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
                super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
                }
            }

            @Override
            public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
                super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
                if(mWebChromeClient != null) {
                    mWebChromeClient.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
                }
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String methodName, String paramsJson, JsPromptResult result) {
                if (!TextUtils.isEmpty(methodName) && mJsObject != null) {
                    try {
                        Method[] methods = mJsObject.getClass().getDeclaredMethods();
                        if (methods != null) {
                            for (Method m : methods) {
                                // 匹配到相同的方法名（js中没有方法重载）
                                if (methodName.equals(m.getName())) {
                                    // 构造参数
                                    Class[] clsParamsType = m.getParameterTypes();
                                    Object[] params = new Object[clsParamsType.length];

                                    if (!TextUtils.isEmpty(paramsJson)) {
                                        JSONObject jsonObj = new JSONObject(paramsJson);
                                        for (int i = 0; i < clsParamsType.length; i++) {
                                            String valueKey = "param" + i + "Value";
                                            String typeKey = "param" + i + "Type";
                                            if (jsonObj.has(valueKey) && jsonObj.has(typeKey)) {
                                                // 参数类型
                                                String typeValue = jsonObj.getString(typeKey);

                                                // 八大基本数据类型，直接取值
                                                if ("java.lang.String".equals(typeValue)
                                                        || "int".endsWith(typeValue)
                                                        || "short".endsWith(typeValue)
                                                        || "long".endsWith(typeValue)
                                                        || "double".endsWith(typeValue)
                                                        || "float".endsWith(typeValue)
                                                        || "char".endsWith(typeValue)
                                                        || "byte".endsWith(typeValue)) {

                                                    params[i] = jsonObj.get(valueKey);

                                                } else {
                                                    // 根据对应类型构造对象
                                                    params[i] = new Gson().fromJson(jsonObj.get(valueKey).toString(), Class.forName(typeValue));
                                                }
                                            }
                                        }
                                    }

                                    Class resultCls = m.getReturnType();

                                    if (resultCls == null || "void".equals(resultCls.getSimpleName().trim())) { // 无返回值
                                        m.invoke(mJsObject, params);
                                        result.cancel();

                                    } else { // 有返回值
                                        Object resultData = m.invoke(mJsObject, params);

                                        if (resultData instanceof Boolean
                                                || resultData instanceof Byte
                                                || resultData instanceof Character
                                                || resultData instanceof Short
                                                || resultData instanceof Integer
                                                || resultData instanceof Long
                                                || resultData instanceof Float
                                                || resultData instanceof Double) {
                                            // 返回值为八大基本数据类型
                                            result.confirm(resultData.toString());

                                        } else {
                                            result.confirm(new Gson().toJson(resultData));
                                        }


                                    }
                                    return true;
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (mWebChromeClient != null) {
                    return mWebChromeClient.onJsPrompt(view, url, methodName, paramsJson, result);
                }

                return super.onJsPrompt(view, url, methodName, paramsJson, result);
            }
        });

        mInited = false;
    }

    /**
     * 使用此方法，最好在调用 {@link #loadUrl(String)} 之前，该方法替换{@link #addJavascriptInterface(Object, String)}，避免API 17 以下的漏洞问题
     * @param obj
     * @param name
     */
    public void setJavascriptInterface(Object obj, String name) {
        if(Build.VERSION.SDK_INT >=17) {
            JsSecurityWebView.super.addJavascriptInterface(obj, name);
            return;
        }

        mJsObject = obj;
        StringBuffer javaScript = new StringBuffer("var " + name + " = new Object();");
        javaScript.append("\n\n");
        Method[] methods = obj.getClass().getDeclaredMethods();
        if(methods != null) {
            for(Method m : methods) {
                if("access$super".equals(m.getName())) {
                    continue;
                }

                Class[] paramsCls = m.getParameterTypes();
                Class resultCls = m.getReturnType();

                javaScript.append(name);
                javaScript.append(".");
                javaScript.append(m.getName());
                javaScript.append(" = function(");

                if(paramsCls != null) {
                    StringBuffer sbPType = new StringBuffer();
                    int i=0;
                    for(Class pCls : paramsCls) {
                        if(sbPType.length() > 0) {
                            sbPType.append(", ");
                        }
                        sbPType.append(pCls.getSimpleName());
                        sbPType.append("Param" + i);
                        i++;
                    }
                    javaScript.append(sbPType.toString());
                }

                javaScript.append(") {");
                javaScript.append("\n");
                javaScript.append("\t");

                if(!"void".equalsIgnoreCase(resultCls.getSimpleName().trim())) {
                    javaScript.append("return ");
                }
                javaScript.append("prompt(\"" + m.getName() + "\",");

                if(paramsCls != null && paramsCls.length > 0) {
                    StringBuffer sbParamValue = new StringBuffer("{");
                    int i= 0;
                    for(Class pCls : paramsCls) {
                        if(sbParamValue.length() > 1) {
                            sbParamValue.append(",");
                        }
                        sbParamValue.append("'param" + i + "Type':");
                        sbParamValue.append("'" + pCls.getName() + "',");
                        sbParamValue.append("'param" + i + "Value':");
                        sbParamValue.append("\"+"+pCls.getSimpleName()+"Param"+i +"+\"");

                        i++;
                    }
                    sbParamValue.append("}");
                    javaScript.append("\"" + sbParamValue.toString() + "\"");
                } else {
                    javaScript.append("\"\"");
                }
                javaScript.append(");\n");
                javaScript.append("};");
                javaScript.append("\n\n");
            }
        }

        loadUrl("javascript:" + javaScript.toString());
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        if(mInited) {
            super.setWebChromeClient(client);
        } else {
            mWebChromeClient = client;
        }
    }
}
