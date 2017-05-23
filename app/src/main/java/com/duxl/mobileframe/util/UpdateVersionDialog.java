package com.duxl.mobileframe.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.duxl.mobileframe.view.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 版本下载并安装
 * Created by duxl on 2016/08/23.
 */
public class UpdateVersionDialog implements Runnable {

    public static boolean mIsUpdating; // 是否正确更新

    private String mApkUrl;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private OnCancelListener mOnCancelListener;

    public UpdateVersionDialog(Context context) {
        this.mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle("版本更新");
        mProgressDialog.setMessage("请稍等");
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(mContext, "后台更新中", Toast.LENGTH_SHORT).show();
                if(mOnCancelListener != null) {
                    mOnCancelListener.onCancel();
                }
            }
        });

    }

    public void setOnCancelListener(OnCancelListener listener) {
        this.mOnCancelListener = listener;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == 200) {
                int progress = msg.arg1;
                if(progress >= 100) {
                    progress = 100;

                    Message installMsg = new Message();
                    installMsg.what = 1;
                    installMsg.obj = msg.obj;
                    mHandler.sendMessageDelayed(installMsg, 100);

                }
                mProgressDialog.setProgress(progress);

            } else if(msg.what == 0) {
                mIsUpdating = false;
                mProgressDialog.dismiss();
                Toast.makeText(mContext, "更新失败", Toast.LENGTH_LONG).show();

            } else if(msg.what == 1) { // 下载完毕，进入安装界面
                mIsUpdating = false;
                mProgressDialog.dismiss();
                String apkUpdatePath = msg.obj.toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + apkUpdatePath), "application/vnd.android.package-archive");
                mContext.startActivity(intent);
            }
        }
    };

    /**
     * 开始下载，下载完毕后自动安装
     * @param mApkUrl
     * @param backgroundable 是否可后台更新
     */
    public void doUpdate(String mApkUrl, boolean backgroundable) {
        if(!mIsUpdating) {
            mIsUpdating = true;
            this.mApkUrl = mApkUrl;
            mProgressDialog.setCanceledOnTouchOutside(backgroundable);
            mProgressDialog.setCancelable(backgroundable);
            mProgressDialog.show();
            new Thread(this).start();
        }
    }

    @Override
    public void run() {
        File apkFile;
        File updateDir;
        try {
            URL url = new URL(mApkUrl);
            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
            InputStream in = hc.getInputStream();
            hc.setConnectTimeout(15000);
            int totalSize = hc.getContentLength();
            int curSize = 0;

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                updateDir = new File(Environment.getExternalStorageDirectory().getPath() + "/"+mContext.getPackageName()+"/apk/");
            } else {
                updateDir = new File("/data/data/"+mContext.getPackageName()+"/apk");
            }

            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            apkFile = new File(updateDir.getPath(), mApkUrl.hashCode()+".apk");
            if (apkFile.exists()) {
                apkFile.delete();
            }
            apkFile.createNewFile();

            // 修改文件夹及安装包的权限,供第三方应用访问
            try {
                Runtime.getRuntime().exec("chmod 705 " + updateDir.getPath());
                Runtime.getRuntime().exec("chmod 604 " + apkFile.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }

            FileOutputStream out = new FileOutputStream(apkFile);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
                curSize += len;
                Message msg = new Message();
                msg.what = 200;
                msg.obj = apkFile.getPath();
                msg.arg1 = curSize * 100 / totalSize;
                mHandler.sendMessage(msg);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(0);
            return;
        }
    }

    public interface OnCancelListener {
        void onCancel();
    }
}
