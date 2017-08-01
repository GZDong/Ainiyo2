package com.huadi.android.ainiyo.frag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.Friends;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.huadi.android.ainiyo.util.SignInUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;

public class ChattingFragment extends Fragment implements EMMessageListener{

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_chatting,null);
        // Inflate the layout for this fragment
        return view;
    }*/

    // 聊天信息输入框
    private EditText mInputEdit;
    // 发送按钮
    private Button mSendBtn;

    public DrawerLayout mDrawerLayout;

    // 显示内容的 TextView
    //  private TextView mContentText;

    // 消息监听器
    private EMMessageListener mMessageListener;
    // 当前聊天的 ID
    private String mChatId;
    // 当前会话对象
    private int mImage;

    private int userImage;
    private EMConversation mConversation;

    private RecyclerView msgRecyclerView;
    private RecyclerView.Adapter<MyViewHodler> MyAdapter;

    private List<EMMessage> mMessages;

    private List<Friends> mFriendses;

    private View mView;



    public static ChattingFragment newInstance(String arg,int img) {

        Bundle args = new Bundle();
        args.putString("ec_chat_id",arg);
        args.putInt("ec_chat_img",img);
        ChattingFragment fragment = new ChattingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);



        mChatId = getArguments().getString("ec_chat_id");
        mImage = getArguments().getInt("ec_chat_img");

        UserInfoLab userInfoLab = UserInfoLab.get(getActivity());
        UserInfo userInfo = userInfoLab.getUserInfo();
        userImage = userInfo.getPicture();

        mMessageListener = this;
        mMessages = new ArrayList<>();

        mFriendses = new ArrayList<>();


        //signIn();


    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar,menu);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_conversation:
                //删除会话

                Snackbar.make(mView,"确定聊天记录删除吗？",Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EMClient.getInstance().chatManager().deleteConversation(mChatId, true);
                                mMessages.clear();
                                MyAdapter.notifyDataSetChanged();
                            }
                        }).show();

                return true;
            case android.R.id.home:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat,container,false);

        mView = v;
        mDrawerLayout = (DrawerLayout) v.findViewById(R.id.drawer_layout);


        mInputEdit = (EditText) v.findViewById(R.id.ec_edit_message_input);
        mSendBtn = (Button) v.findViewById(R.id.ec_btn_send);
        //  mContentText = (TextView) v.findViewById(R.id.ec_text_content);
        // 设置textview可滚动，需配合xml布局设置
        //   mContentText.setMovementMethod(new ScrollingMovementMethod());

        msgRecyclerView = (RecyclerView) v.findViewById(R.id.msg_recycler_view);
        mConversation = EMClient.getInstance().chatManager().getConversation(mChatId, null, true);
        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();
        int count = mConversation.getAllMessages().size();
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            mConversation.loadMoreMsgFromDB(msgId,20 - count);
        }
        if (mConversation.getAllMessages().size() > 0){
            mMessages = mConversation.getAllMessages();
        }

        //MyAdapter.notifyItemInserted(mMessages.size() - 1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        msgRecyclerView.setLayoutManager(layoutManager);
        MyAdapter = new MsgAdapter(mMessages);
        msgRecyclerView.setAdapter(MyAdapter);


        msgRecyclerView.scrollToPosition(mMessages.size() - 1);
        // 设置发送按钮的点击事件
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mInputEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    mInputEdit.setText("");
                    // 创建一条新消息，第一个参数为消息内容，第二个为接受者username
                    EMMessage message = EMMessage.createTxtSendMessage(content, mChatId);

                   /* if (message.getUserName() == mChatId){
                        //放置到右边
                    }else{
                        //放置到左边
                    }*/
                    // message.setAttribute("from","left");
                    mMessages.add(message);
                    MyAdapter.notifyItemInserted(mMessages.size() - 1);
                    msgRecyclerView.scrollToPosition(mMessages.size() - 1);
                    // 将新的消息内容和时间加入到下边
                    //  mContentText.setText(mContentText.getText() + "\n发送：" + content + " - time: " + message.getMsgTime());
                    // 调用发送消息的方法
                    EMClient.getInstance().chatManager().sendMessage(message);
                    // 为消息设置回调
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            // 消息发送成功，打印下日志，正常操作应该去刷新ui
                            Log.i("lzan13", "send message on success");
                        }

                        @Override
                        public void onError(int i, String s) {
                            // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                            Log.i("lzan13", "send message on error " + i + " - " + s);
                        }

                        @Override
                        public void onProgress(int i, String s) {
                            // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
                        }
                    });
                }
            }
        });

        // initConversation();
        return v;
    }

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    private void initConversation() {

        /**
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是绘画类型可以为空
         * 第三个表示如果会话不存在是否创建
         */


        // 打开聊天界面获取最后一条消息内容并显示
        // String content = "";
        if (mConversation.getAllMessages().size() > 0) {
            List<EMMessage> messges = mConversation.getAllMessages();
            for (EMMessage message : messges){

//                EMTextMessageBody body = (EMTextMessageBody) messges.get(i).getBody();
                //识别消息的来源
               /* if (messges.get(i).getUserName() == mChatId){
                    //放置到右边
                }else{
                    //放置到左边
                }*/
                mMessages.add(message);
                MyAdapter.notifyItemInserted(mMessages.size() - 1);
                msgRecyclerView.scrollToPosition(mMessages.size() - 1);
                // content = content +"聊天记录：" + body.getMessage() + " - time: " + mConversation.getLastMessage().getMsgTime() + "\n";
            }

            // 将消息内容和时间显示出来
            // mContentText.setText(content);
        }
    }

    /**
     * 自定义实现Handler，主要用于刷新UI操作
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    message.setFrom(mChatId);

                    // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
                    /*EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                    // 将新的消息内容和时间加入到下边
                    if(message.getUserName() == mChatId){
                        //放置到右边
                    }else   {
                        //放置到左边
                    }
                    mContentText.setText(mContentText.getText() + "\n接收：" + body.getMessage() + " - time: " + message.getMsgTime());*/
                    mMessages.add(message);
                    MyAdapter.notifyItemInserted(mMessages.size() - 1);
                    msgRecyclerView.scrollToPosition(mMessages.size() - 1);
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        if (!EMClient.getInstance().isLoggedInBefore()) {
            /*Intent intent = new Intent(ECMainActivity.this, ECLoginActivity.class);
            startActivity(intent);
            finish();*/

            UserInfoLab userInfoLab = UserInfoLab.get(getActivity());
            UserInfo userInfo = userInfoLab.getUserInfo();
            String name = userInfo.getUsername();
            String pass = userInfo.getPassword();
            userImage = userInfo.getPicture();
           SignInUtil.signIn(name,pass,getActivity());

        }
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }

    /**
     * --------------------------------- Message Listener -------------------------------------
     * 环信消息监听主要方法
     */
    /**
     * 收到新消息
     *
     * @param list 收到的新消息集合
     */
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        // 循环遍历当前收到的消息
        for (EMMessage message : list) {
            if (message.getFrom().equals(mChatId)) {
                // 设置消息为已读
                mConversation.markMessageAsRead(message.getMsgId());

                // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            } else {
                // 如果消息不是当前会话的消息发送通知栏通知
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(message.getFrom());
                int unread = conversation.getUnreadMsgCount();
                Intent intent = new Intent("com.huadi.android.ainiyo.newMessage");
                intent.putExtra("ID",message.getFrom());
                intent.putExtra("newM",unread);
                getActivity().sendBroadcast(intent);
            }
        }
    }

    /**
     * 收到新的 CMD 消息
     *
     * @param list
     */
    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        for (int i = 0; i < list.size(); i++) {
            // 透传消息
            EMMessage cmdMessage = list.get(i);
            EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
            Log.i("lzan13", body.action());
        }
    }

    /**
     * 消息的状态改变
     *
     * @param message 发生改变的消息
     * @param object  包含改变的消息
     */
    @Override
    public void onMessageChanged(EMMessage message, Object object) {
    }
    /**
     * 收到新的发送回执
     *
     *
     * 收到发送回执的消息集合
     */

    /**
     * 收到新的已读回执
     *
     * 收到消息已读回执
     */


    /*@Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }


    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }*/
    @Override
    public void onMessageDelivered(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {

    }

    private class MyViewHodler extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;

        LinearLayout rightLayout;

        TextView leftMsg;

        TextView rightMsg;

        ImageView leftImage;

        ImageView rightImage;

        public MyViewHodler(View view){
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);
            leftImage = (ImageView) view.findViewById(R.id.image_user_left);
            rightImage = (ImageView) view.findViewById(R.id.image_user_right);
        }



    }

    public class MsgAdapter extends RecyclerView.Adapter<MyViewHodler>{

        private List<EMMessage> mMsgList;


        public MsgAdapter(List<EMMessage> msgList){
            mMsgList = msgList;
        }

        @Override
        public MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_msg,parent,false);

            return new MyViewHodler(view);
        }

        @Override
        public void onBindViewHolder(MyViewHodler holder, int position) {

            EMMessage msg = mMsgList.get(position);

            if (msg.getFrom().equals(mChatId)) {   //mChatId是目标的id
                Log.e("test","-----------------");
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.VISIBLE);

                Bitmap bm = BitmapFactory.decodeResource(getResources(),mImage);

                //加载图片
                //  Glide.with(getActivity()).load(bm).fitCenter().into(holder.rightImage);
                holder.rightImage.setImageBitmap(ScaleBitmap(bm));
                EMTextMessageBody body = (EMTextMessageBody) msg.getBody();
                holder.rightMsg.setText(body.getMessage() ); //+ " - time: " + msg.getMsgTime()

            }else {
                Log.e("test","+++++++++++++++++");
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftLayout.setVisibility(View.VISIBLE);

                Bitmap bm = BitmapFactory.decodeResource(getResources(),userImage);
                //加载图片
                holder.leftImage.setImageBitmap(ScaleBitmap(bm));
                //Glide.with(getActivity()).load(bm).fitCenter().into(holder.leftImage);

                EMTextMessageBody body = (EMTextMessageBody) msg.getBody();
                holder.leftMsg.setText(body.getMessage() ); //+ " - time: " + msg.getMsgTime()
            }


        }

        @Override
        public int getItemCount() {

            return mMsgList.size();

        }

        public Bitmap ScaleBitmap(Bitmap bm){
            // 得到图片原始的高宽
            int rawHeight = bm.getHeight();
            int rawWidth = bm.getWidth();

            // 设定图片新的高宽
            int newHeight = 100;
            int newWidth = 100;

            // 计算缩放因子
            float heightScale = ((float) newHeight) / rawHeight;
            float widthScale = ((float) newWidth) / rawWidth;

            // 新建立矩阵
            Matrix matrix = new Matrix();
            matrix.postScale(heightScale, widthScale);


            //将图片大小压缩
            //压缩后图片的宽和高以及kB大小均会变化
            Bitmap newBitmap = Bitmap.createBitmap(bm, 0, 0, rawWidth,rawWidth, matrix, true);

            return newBitmap;
        }
    }
    /*private void signIn() {
       *//* mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在登陆，请稍后...");
        mDialog.show();*//*
        *//*String username = mUsernameEdit.getText().toString().trim();
        String password = mPasswordEdit.getText().toString().trim();*//*

        String username = "xuniji";
        String password = "123";
        *//*if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(ECLoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }*//*
        EMClient.getInstance().login(username, password, new EMCallBack() {
            *//**
             * 登陆成功的回调
             *//*
            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        *//*mDialog.dismiss();*//*

                        // 加载所有会话到内存
                        EMClient.getInstance().chatManager().loadAllConversations();
                        // 加载所有群组到内存，如果使用了群组的话
                        // EMClient.getInstance().groupManager().loadAllGroups();

                        // 登录成功跳转界面
                       *//* Intent intent = new Intent(ECLoginActivity.this, ECMainActivity.class);
                        startActivity(intent);
                        finish();*//*
                    }
                });
            }

            *//**
             * 登陆错误的回调
             * @param i
             * @param s
             *//*
            @Override
            public void onError(final int i, final String s) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        *//*mDialog.dismiss();*//*
                        Log.d("lzan13", "登录失败 Error code:" + i + ", message:" + s);
                        *//**
                         * 关于错误码可以参考官方api详细说明
                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                         *//*
                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                Toast.makeText(getActivity(), "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                Toast.makeText(getActivity(), "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                Toast.makeText(getActivity(), "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                Toast.makeText(getActivity(), "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                Toast.makeText(getActivity(), "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                Toast.makeText(getActivity(), "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                Toast.makeText(getActivity(), "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                Toast.makeText(getActivity(), "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                Toast.makeText(getActivity(), "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(getActivity(), "ml_sign_in_failed code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }*/

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

}
