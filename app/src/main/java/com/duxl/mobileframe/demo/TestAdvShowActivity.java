package com.duxl.mobileframe.demo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.duxl.mobileframe.BaseActivity;
import com.duxl.mobileframe.R;
import com.duxl.mobileframe.adapter.AdvAdapter;
import com.duxl.mobileframe.mobile.AdvInfo;
import com.duxl.mobileframe.util.AdvShowUtil;
import com.duxl.mobileframe.view.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Created by duxl on 2017/05/25.
 * 公司：重庆赛博丁科技发展有限公司
 * 类描述：演示AdvShowUtil的用法
 */

public class TestAdvShowActivity extends BaseActivity implements AdvAdapter.OnAdvItemClickListener {

    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;

    private AdvShowUtil mAdvShowUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_test_advshow);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_activity_activity_test_advshow);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_activity_activity_test_advshow);

        mAdvShowUtil = new AdvShowUtil(this, mViewPager, mRadioGroup);
        mAdvShowUtil.setOnItemClickListener(this);
        // mAdvShowUtil.setPointResid(R.drawable.adv_point_selector); 可自定义指示器图片

        List<AdvInfo> advItems = new ArrayList<>();
        advItems.add(new SimpleAdvInfo("http://pic47.nipic.com/20140828/10895078_153625323000_2.jpg"));
        advItems.add(new SimpleAdvInfo("http://pic96.nipic.com/file/20160425/11891102_114059158532_2.jpg"));
        advItems.add(new SimpleAdvInfo("http://pic50.nipic.com/file/20141011/19443578_110752620351_2.jpg"));

        mAdvShowUtil.showAdvData(advItems);


    }

    @Override
    public void onAdvItemClick(int position) {
        Toast.makeText(this, "onAdvItemClick=" + position, Toast.LENGTH_SHORT).show();
    }

    /**
     * 广告数据提示
     */
    public static final class SimpleAdvInfo implements AdvInfo {

        private String url;

        public SimpleAdvInfo(String url) {
            this.url = url;
        }

        @Override
        public String getImgUrl() {
            return url;
        }
    }

    @Override
    protected void onDestroy() {
        // 停止广告滚动，当页面销毁时需要调用此API，避免多次使用造成OOM问题
        mAdvShowUtil.recycle();
        super.onDestroy();
    }
}
