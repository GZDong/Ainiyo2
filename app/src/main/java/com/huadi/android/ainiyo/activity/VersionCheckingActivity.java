package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class VersionCheckingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_checking);

        ViewUtils.inject(this);

        // setImmersive();

        try {
            Log.i("verisiontest", "versionname: " + getVersionName()
                    + "  versionCode:" + getVersionCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getVersionCode() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        int version = packInfo.versionCode;
        return version;
    }

    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    public void setImmersive() {
        //设置状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
    }

    @OnClick({R.id.version_checking, R.id.version_about, R.id.version_complain, R.id.version_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.version_checking:
                //Toast.makeText(this, "已经是最新版本", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, VersionNewApkActivity.class));
                break;
            case R.id.version_about:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.version_complain:
                startActivity(new Intent(this, ComplainActivity.class));
                break;
            case R.id.version_back:
                finish();
                ToolKits.putInt(this, "fragment", 4);
                break;

        }
    }

    //按返回键时回到相应的fragment
    @Override
    public void onBackPressed() {
        ToolKits.putInt(this, "fragment", 4);
        super.onBackPressed();
    }
}
