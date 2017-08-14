package com.huadi.android.ainiyo.Async;

import android.os.AsyncTask;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by zhidong on 2017/8/12.
 */

public class AcceptFriendTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        try {
            EMClient.getInstance().contactManager().acceptInvitation(strings[0]);

        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return "后台网络接收好友执行完成";
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("test", "onAsyncTask_onPostExecute\n");
        Log.e("test", "执行一次Async完毕在监听器处");
    }
}
