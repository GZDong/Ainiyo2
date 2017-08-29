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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.FriendsInfoActivity;
import com.huadi.android.ainiyo.entity.Friends;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.huadi.android.ainiyo.util.DateUtil;
import com.huadi.android.ainiyo.util.ImgScaleUtil;
import com.huadi.android.ainiyo.util.SignInUtil;
import com.huadi.android.ainiyo.util.ToolKits;
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

    private static String TAG = "ChattingFragment";

    private EditText mInputEdit;
    private Button mSendBtn;
    public DrawerLayout mDrawerLayout;

    // 消息监听器
    private EMMessageListener mMessageListener;

    private String mChatId;
    private int mImage;
    private int userImage;

    private EMConversation mConversation;

    private RecyclerView msgRecyclerView;
    private RecyclerView.Adapter<MyViewHodler> MyAdapter;

    private List<EMMessage> mMessages;

    private View mView;

    private UserInfo mUserInfo;

    public static ChattingFragment newInstance(String arg, int img, UserInfo userInfo) {

        Bundle args = new Bundle();
        args.putString("ec_chat_id",arg);
        args.putInt("ec_chat_img",img);
        args.putSerializable("userInfo", userInfo);
        ChattingFragment fragment = new ChattingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        //获得指定的好友名称和头像
        mChatId = getArguments().getString("ec_chat_id");
        mImage = getArguments().getInt("ec_chat_img");
        mUserInfo = (UserInfo) getArguments().getSerializable("userInfo");

        //获得用户自己的头像
        userImage = mUserInfo.getPicture();
        mMessageListener = this;
        mMessages = new ArrayList<>();


        //*******在onCreate的方法里登陆********
        String name = mUserInfo.getUsername();
        String pass = mUserInfo.getPassword();
        SignInUtil.signIn(name,pass,getActivity());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_conversation:
                //删除记录
                Snackbar.make(mView,"确定清空聊天记录吗？",Snackbar.LENGTH_LONG)
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
                ToolKits.putInt(getActivity(),"fragment",1);
                //**********开启singleTask的MainActivity**********
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.delete_dialog:
                //删除会话
                Snackbar.make(mView,"确定删除当前会话吗？",Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FriendsLab.get(getActivity()).deleteDialog(mChatId);
                                getActivity().finish();
                            }
                        }).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat,container,false);

        mView = v;
        mDrawerLayout = (DrawerLayout) v.findViewById(R.id.drawer_layout);
        mInputEdit = (EditText) v.findViewById(R.id.ec_edit_message_input);
        mSendBtn = (Button) v.findViewById(R.id.ec_btn_send);
        msgRecyclerView = (RecyclerView) v.findViewById(R.id.msg_recycler_view);

        //************加载聊天信息***********
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

        //*********把加载到的聊天信息用来初始化适配器*********
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        msgRecyclerView.setLayoutManager(layoutManager);
        MyAdapter = new MsgAdapter(mMessages);
        msgRecyclerView.setAdapter(MyAdapter);
        msgRecyclerView.scrollToPosition(mMessages.size() - 1);


        // 设置发送按钮的点击事件
        mInputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                msgRecyclerView.scrollToPosition(mMessages.size() - 1);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mInputEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    String lastMsg = mInputEdit.getText().toString();
                    mInputEdit.setText("");
                    // 创建一条新消息，第一个参数为消息内容，第二个为接受者username
                    EMMessage message = EMMessage.createTxtSendMessage(content, mChatId);
                    mMessages.add(message);
                    MyAdapter.notifyItemInserted(mMessages.size() - 1);
                    msgRecyclerView.scrollToPosition(mMessages.size() - 1);

                    //更新该好友的最新时间
                    Friends friends = FriendsLab.get(getActivity(), mUserInfo).getFriend(mChatId);

                    Intent intent = new Intent("com.huadi.android.ainiyo.newMessage");
                    intent.putExtra("ID", friends.getName());
                    intent.putExtra("newM", friends.getUnreadMeg());

                    intent.putExtra("lastM",lastMsg);
                    getActivity().sendBroadcast(intent);

                    // 调用发送消息的方法
                    EMClient.getInstance().chatManager().sendMessage(message);

                    // 为消息设置回调
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            // 消息发送成功，打印下日志，正常操作应该去刷新ui
                            Log.i(TAG, "send message on success");
                        }

                        @Override
                        public void onError(int i, String s) {
                            // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                            Log.i(TAG, "send message on error " + i + " - " + s);
                        }

                        @Override
                        public void onProgress(int i, String s) {
                            // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
                        }
                    });
                }
            }
        });
        return v;
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

            UserInfo userInfo = UserInfoLab.get(getActivity(), mUserInfo).getUserInfo();
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
        Log.d(TAG, "在聊天碎片里接受到新消息");
        for (EMMessage message : list) {
            String id;
            int unread;
            //如果是当前会话消息就更新时间
            if (message.getFrom().equals(mChatId)) {

                id = mChatId;
                unread = 0;
                // 设置消息为已读
                mConversation.markMessageAsRead(message.getMsgId());

                // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            } else {
                // 如果消息不是当前会话的消息就发送广播，使聊天列表更新未读消息数，同时更新时间
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(message.getFrom());
                unread = conversation.getUnreadMsgCount();
                id = message.getFrom();
            }
            Intent intent = new Intent("com.huadi.android.ainiyo.newMessage");
            intent.putExtra("ID",id);
            intent.putExtra("newM",unread);
            intent.putExtra("newT", DateUtil.getNowDate());
            getActivity().sendBroadcast(intent);
        }
    }

    //*******其他的监听方法*********
    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        for (int i = 0; i < list.size(); i++) {
            // 透传消息
            EMMessage cmdMessage = list.get(i);
            EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
        }
    }
    @Override
    public void onMessageChanged(EMMessage message, Object object) {
    }
    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
    }
    @Override
    public void onMessageRead(List<EMMessage> messages) {
    }

    //**********适配器***********
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

            initClick();
        }

        private void initClick() {
            rightImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),FriendsInfoActivity.class);
                    intent.putExtra("name",mUserInfo.getUsername());
                    intent.putExtra("picture",mUserInfo.getPicture());
                    intent.putExtra("userInfo",mUserInfo);
                    intent.putExtra("from",TAG);
                    startActivity(intent);
                }
            });

            leftImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), FriendsInfoActivity.class);
                    intent.putExtra("name", mChatId);
                    intent.putExtra("picture", mImage);
                    intent.putExtra("userInfo", mUserInfo);
                    intent.putExtra("from", TAG);
                    startActivity(intent);
                }
            });
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

            Friends friends = FriendsLab.get(getActivity(), mUserInfo).getFriend(mChatId);

            if (msg.getFrom().equals(mChatId)) {   //mChatId是目标的id

                holder.rightLayout.setVisibility(View.GONE);
                holder.leftLayout.setVisibility(View.VISIBLE);

                //加载图片
                //  Glide.with(getActivity()).load(bm).fitCenter().into(holder.rightImage);
                if (!TextUtils.isEmpty(friends.getPicUrl())) {
                    Glide.with(ChattingFragment.this).load(friends.getPicUrl()).into(holder.leftImage);
                } else {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), mImage);
                    holder.leftImage.setImageBitmap(ImgScaleUtil.ScaleBitmap(bm, 100, 100));
                }
                EMTextMessageBody body = (EMTextMessageBody) msg.getBody();
                holder.leftMsg.setText(body.getMessage());

            }else {

                holder.leftLayout.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.VISIBLE);

                Log.e("test", "用户的Url " + UserInfoLab.get(getActivity()).getUserInfo().getPicUrl());
                if (UserInfoLab.get(getActivity()).getUserInfo().getPicUrl()==null){
                    Bitmap bm = BitmapFactory.decodeResource(getResources(),userImage);
                    //加载图片
                    holder.rightImage.setImageBitmap(ImgScaleUtil.ScaleBitmap(bm, 100, 100));
                }else {
                    Glide.with(getActivity()).load(UserInfoLab.get(getActivity()).getUserInfo().getPicUrl()).into(holder.rightImage);
                }

                EMTextMessageBody body = (EMTextMessageBody) msg.getBody();
                holder.rightMsg.setText(body.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return mMsgList.size();
        }
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

}
