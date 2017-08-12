package com.huadi.android.ainiyo.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.huadi.android.ainiyo.Retrofit2.PostRequest_ReqFri_Interface;
import com.huadi.android.ainiyo.gson.ResultForRequset;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import org.litepal.LitePalApplication;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lz on 2016/4/16.
 * 项目的 Application类，做一些项目的初始化操作，比如sdk的初始化等
 */
public class ECApplication extends Application {

    // 上下文菜单
    private  static Context mContext;

    // 记录是否已经初始化
    private boolean isInit = false;

    public static String sessionId;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        LitePalApplication.initialize(mContext);

        // 初始化环信SDK
        initEasemob();
        //监听好友管理
        /*EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            //收到好友邀请
            @Override
            public void onContactInvited(String username, String reason) {
                //发送一条通知，附上对方姓名和添加理由，点击通知后跳转到新界面，在新界面决定是否同意添加
                //EMClient.getInstance().contactManager().acceptInvitation(username);接收好友的方法
                //EMClient.getInstance().contactManager().declineInvitation(username);拒绝好友的方法

                //模拟接受了
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(username);
                    Toast.makeText(ECApplication.this,"收到好友请求 " + username +" "+ reason,Toast.LENGTH_LONG).show();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
            //好友请求被同意
            @Override
            public void onFriendRequestAccepted(String username) {
                //把该好友的username上传到自己的服务器，然后加入数据库
                //调整自己服务器的接口，添加好友不需要附加信息
                Toast.makeText(ECApplication.this,"对方同意接受你为好友",Toast.LENGTH_LONG).show();
            }

            //被删除时回调此方法
            @Override
            public void onContactDeleted(String username) {
                //被删除时，从2个服务器删除好友关系
                //第三方删除 EMClient.getInstance().contactManager().deleteContact(username);
                //删除本地数据库内容
                //告诉用户被删除好友了
            }
            //增加了联系人时回调此方法
            @Override
            public void onContactAdded(String username) {
                //点击同意后，上传服务器，更新本地数据库和单例
                //刷新好友列表
                Toast.makeText(ECApplication.this,"对方同意接受你为好友",Toast.LENGTH_LONG).show();
            }
            //好友请求被拒绝
            @Override
            public void onFriendRequestDeclined(String username) {
            //发送一条通知，告诉好友请求被拒绝了
            }
        });*/
    }


    public static Context getContext(){
        return mContext;
    }

    /**
     *
     */
    private void initEasemob() {
        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
         */
        if (processAppName == null || !processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return;
        }
        if (isInit) {
            return;
        }

        // 调用初始化方法初始化sdk
        EMClient.getInstance().init(mContext, initOptions());

        // 设置开启debug模式
        EMClient.getInstance().setDebugMode(true);

        // 设置初始化已经完成
        isInit = true;
    }

    /**
     * SDK初始化的一些配置
     * 关于 EMOptions 可以参考官方的 API 文档
     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
     */
    private EMOptions initOptions() {

        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
       options.setRequireAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);

        return options;
    }

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param pid 进程的id
     * @return 返回进程的名字
     */
    private String getAppName(int pid) {
        String processName = null;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName;
                    // 返回当前进程名
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return processName;
    }
}