package com.huadi.android.ainiyo.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.FindingAphorism;
import com.huadi.android.ainiyo.entity.ResponseObject;
import com.huadi.android.ainiyo.entity.VersionWebInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;

import static com.huadi.android.ainiyo.util.CONST.GETING_APHORISM;
import static com.huadi.android.ainiyo.util.CONST.GETING_LAST_EDITION;

public class VersionNewApkActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_version_describe_text)
    TextView tv_version_describe_text;
    @ViewInject(R.id.tv_version_download)
    TextView tv_version_download;

    private VersionWebInfo vwi;
    private Handler handle;
    private String ApkUrl;

    long mTaskId;
    DownloadManager downloadManager;

    public static final int UPDATE_VERSION_INFO = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_new_apk);

        ViewUtils.inject(this);

        initData();

        handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_VERSION_INFO:
                        vwi = (VersionWebInfo) msg.obj;
                        tv_version_describe_text.setText(vwi.getUpdatecontent());
                        ApkUrl = vwi.getUrl();
                        try {
//                            if (vwi.getEditon()>=getVersionCode())
//                            {
//                                tv_version_download.setText("已经最新版本");
//                                tv_version_download.setEnabled(false);
//                                tv_version_download.setBackground(getResources().getDrawable(R.drawable.movement_joined_selected_button));
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        };
    }

    private void initData() {
        RequestParams params = new RequestParams();

        new HttpUtils().send(HttpRequest.HttpMethod.POST, GETING_LAST_EDITION, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                final ResponseObject<VersionWebInfo> object = new GsonBuilder().create().
                        fromJson(responseInfo.result, new TypeToken<ResponseObject<VersionWebInfo>>() {
                        }.getType());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_VERSION_INFO;
                        message.obj = object.getResult();
                        handle.sendMessage(message);
                    }
                }).start();


            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    private int getVersionCode() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        int version = packInfo.versionCode;
        return version;
    }

    //使用系统下载器下载
    private void downloadAPK(String versionUrl, String versionName) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionUrl));
        request.setAllowedOverRoaming(false);//漫游网络是否可以下载

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(versionUrl));
        request.setMimeType(mimeString);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);

        //sdcard的目录下的download文件夹，必须设置
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "ainiyo.apk");
        //request.setDestinationInExternalFilesDir(),也可以自己制定下载路径

        //将下载请求加入下载队列
        downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        mTaskId = downloadManager.enqueue(request);

        //注册广播接收者，监听下载状态
        this.registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    //广播接受者，接收下载状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();//检查下载状态
        }
    };

    //检查下载状态
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    //MLog.i(">>>下载暂停");
                case DownloadManager.STATUS_PENDING:
                    // MLog.i(">>>下载延迟");
                case DownloadManager.STATUS_RUNNING:
                    //MLog.i(">>>正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    //MLog.i(">>>下载完成");
                    //下载完成安装APK
                    //downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + versionName;
                    installAPK(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/ainiyo.apk"));
                    break;
                case DownloadManager.STATUS_FAILED:
                    // MLog.i(">>>下载失败");
                    break;
            }
        }
    }

    //下载到本地后执行安装
    protected void installAPK(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + file.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //在服务中开启activity必须设置flag,后面解释
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }


    @OnClick({R.id.tv_version_download, R.id.version_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_version_download:
                downloadAPK(ApkUrl, "");
                break;
            case R.id.version_back:
                finish();
                break;
        }
    }

}
