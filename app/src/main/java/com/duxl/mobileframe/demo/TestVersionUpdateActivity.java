package com.duxl.mobileframe.demo;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.duxl.mobileframe.R;
import com.duxl.mobileframe.util.UpdateVersionDialog;
import com.duxl.mobileframe.util.UpdateVersionNotification;
import com.duxl.mobileframe.view.AlertDialog;
import com.duxl.mobileframe.view.Toast;

/**
 * 版本更新对话框
 * Created by Administrator on 2016/08/24.
 */
public class TestVersionUpdateActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private String mApkUrl = "http://192.168.1.133:8080/Test/test.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.add("通知栏更新");
        adapter.add("对话框更新");
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0) {
            if(UpdateVersionNotification.mIsUpdating) {
                Toast.makeText(this, "正在更新", Toast.LENGTH_SHORT).show();
            } else {
                UpdateVersionNotification versionNotification = new UpdateVersionNotification(this, R.drawable.adv_point_on);
                versionNotification.doUpdate(mApkUrl);
            }

        } else if(position == 1) {
            if(UpdateVersionDialog.mIsUpdating) {
                Toast.makeText(this, "正在更新", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog alertDialog = new AlertDialog(this);
                alertDialog.setTitle("版本更新");
                alertDialog.setMessage("新版本更新描述");
                alertDialog.setPositiveButton("立即更新", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(AlertDialog dialog) {
                        UpdateVersionDialog updateUtils = new UpdateVersionDialog(TestVersionUpdateActivity.this);
                        updateUtils.setOnCancelListener(new UpdateVersionDialog.OnCancelListener() {
                            @Override
                            public void onCancel() {
                                // 如果是启动页面，可以跳转到首页了
                            }
                        });
                        updateUtils.doUpdate(mApkUrl, true);
                    }
                });
                alertDialog.setNegativeButton("下次再说", null);
                alertDialog.show();
            }
        }
    }
}
