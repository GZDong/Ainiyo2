package com.huadi.android.ainiyo.frag;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.ChattingActivity;
import com.huadi.android.ainiyo.activity.FriendsListActivity;
import com.huadi.android.ainiyo.entity.Friends;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.huadi.android.ainiyo.util.DateUtil;
import com.huadi.android.ainiyo.util.ImgScaleUtil;
import com.huadi.android.ainiyo.util.SignInUtil;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by zhidong on 2017/7/26.
 */

public class ChooseFragment extends Fragment {

    private List<Friends> frdList;
    private int transImg;
    private RecyclerView mRecyclerView;
    private  MyAdapter mMyAdapter;
    private BroadcastReceiver mReceiverForNewMsg;
    private BroadcastReceiver mReceiverForRefresh;
    private ImageButton mPersons;

    private UserInfo mUserInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getActivity() instanceof MainActivity){
            Log.e("test", "onCreate____MainActivity");
        }else {
            Log.e("test", "onCreate____ChattingActivity");
        }

        //获取当前用户实例
        mUserInfo = UserInfoLab.get(getActivity()).getUserInfo();
        //frdList = new ArrayList<>();这里不应该给frdList创建实例，它应该是一个指引而已

        frdList = FriendsLab.get(getActivity(), mUserInfo).getFriendses();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_choose,container,false);

        mPersons = (ImageButton) v.findViewById(R.id.btn_friends);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.fri_recycler_view);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(lm);
        mMyAdapter = new MyAdapter(frdList);
        mRecyclerView.setAdapter(mMyAdapter);

        //如果是聊天活动的碎片，则在这里注册广播接收器
        if (getActivity() instanceof ChattingActivity) {
            Log.e("test", "onCreateView___ChattingActivity");
            //***********如果在聊天里打开就不要标题栏******
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.bar);
            linearLayout.setVisibility(View.GONE);

            //******************注册新信息的接受器***********************
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.huadi.android.ainiyo.newMessage");
            mReceiverForNewMsg = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int newMsg = intent.getIntExtra("newM", 0);
                    String ID = intent.getStringExtra("ID");
                    String newTime = intent.getStringExtra("newT");
                    //更新数据库和单例
                    FriendsLab.get(getActivity(), mUserInfo).updateTimeAndUnread(ID,newTime,newMsg);
                    //重新排序一下单例数据
                    FriendsLab.get(getActivity(), mUserInfo).reSort();
                    // frdList = FriendsLab.get(getActivity(), mUserInfo).getFriendses(); //其实这句是多余的
                    frdList = FriendsLab.get(getActivity(), mUserInfo).getFriendses();
                    for (Friends friends1 : frdList) {
                        if (friends1 != null) {
                            Log.e("test", "frdList  排序后：" + friends1.getName() + "/n");
                        }
                    }
                    mMyAdapter.mList = frdList;
                    mMyAdapter.notifyDataSetChanged();  //这里已经意味着必须要在适配器被创建过之后注册
                }
            };
            getActivity().registerReceiver(mReceiverForNewMsg, intentFilter);
            //************************************************
        }else {
            Log.e("test", "onCreateView___MainActivity");
        }

        mPersons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FriendsListActivity.class);
                intent.putExtra("userInfo", mUserInfo);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        //如果数据有变,那frdList也有变
        frdList = FriendsLab.get(getActivity(), mUserInfo).getFriendses();
        mMyAdapter.mList = frdList;
        mMyAdapter.notifyDataSetChanged();
        //*****************根据单例里的用户信息来登陆***************
        if (getActivity() instanceof MainActivity) { //这里登陆只是为了监听器的注册
            Log.e("test", "onStart___MainActivity");

            mUserInfo = UserInfoLab.get(getActivity()).getUserInfo();
            String name = mUserInfo.getUsername();
            String pass = mUserInfo.getPassword();
            SignInUtil.signIn(name,pass,getActivity());
            //*******注册新消息监听器*********
            EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);

            //******************注册新信息的接受器***********************
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.huadi.android.ainiyo.newMessage");
            mReceiverForNewMsg = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int newMsg = intent.getIntExtra("newM", 0);
                    String ID = intent.getStringExtra("ID");
                    String newTime = intent.getStringExtra("newT");
                    //更新数据库和单例
                    FriendsLab.get(getActivity(), mUserInfo).updateTimeAndUnread(ID,newTime,newMsg);
                    //重新排序一下单例数据
                    FriendsLab.get(getActivity(), mUserInfo).reSort();
                    frdList = FriendsLab.get(getActivity(), mUserInfo).getFriendses();

                    for (Friends friends1 : frdList) {
                        Log.e("test", "排序后：" + friends1.getName() + "/n");
                    }
                    mMyAdapter.mList = frdList;
                    mMyAdapter.notifyDataSetChanged();

                }
            };
            getActivity().registerReceiver(mReceiverForNewMsg, intentFilter);

            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction("com.huadi.android.ainiyo.refresh");
            mReceiverForRefresh = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    frdList = FriendsLab.get(getActivity(), mUserInfo).getFriendses();
                    mMyAdapter.mList = frdList;
                    mMyAdapter.notifyDataSetChanged();
                }
            };

            getActivity().registerReceiver(mReceiverForRefresh,intentFilter2);


            //************************************************
        }else {
            Log.e("test", "onStart_ChattingFragment");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity){
            Log.e("test", "onResume_MainActivity");
        }else {
            Log.e("test", "onResume_ChattingActivity");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() instanceof MainActivity) {
            EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
            getActivity().unregisterReceiver(mReceiverForNewMsg);
            getActivity().unregisterReceiver(mReceiverForRefresh);
            Log.e("test", "onStop___MainActivity");
        }else{
            Log.e("test", "onStop___ChattingActivity");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() instanceof ChattingActivity) {
            Log.e("test", "onDestroy___ChattingActivity");
            getActivity().unregisterReceiver(mReceiverForNewMsg);
            EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
        }
        if (getActivity() instanceof MainActivity) {
            Log.e("test", "onDestroy___MainActivity");
            SignInUtil.signOut();
        }
    }



    //********ViewHolder********
    private class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView mImageView;
        Button UnreadBtn;
        TextView timeText;

        public MyViewHolder(View v){
            super(v);
            textView = (TextView) v.findViewById(R.id.name_fri);
            mImageView = (ImageView) v.findViewById(R.id.img_friend);
            UnreadBtn= (Button) v.findViewById(R.id.unread_btn);
            timeText = (TextView) v.findViewById(R.id.text_time);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    FriendsLab.get(getActivity(), mUserInfo).clearUnread(textView.getText().toString());

                    //****在服务器端置0新信息****
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(textView.getText().toString());
                    if (conversation != null){
                        conversation.markAllMessagesAsRead();
                    }
                    frdList = FriendsLab.get(getActivity(), mUserInfo).getFriendses();
                    mMyAdapter.notifyDataSetChanged();
                    if (getActivity() instanceof MainActivity){

                        Intent intent =  new Intent(getActivity(),ChattingActivity.class);


                        intent.putExtra("name",textView.getText());
                        for (Friends friends : frdList){
                            if (friends.getName() == textView.getText()){
                                transImg = friends.getPicture();

                            }
                        }
                        intent.putExtra("userInfo", mUserInfo);
                        intent.putExtra("img",transImg);
                        startActivity(intent);
                    } else if (getActivity() instanceof ChattingActivity){


                        ChattingActivity chattingActivity = (ChattingActivity)getActivity();

                        FragmentManager fm = chattingActivity.getSupportFragmentManager();
                        ChattingFragment chatFragment = (ChattingFragment) fm.findFragmentById(R.id.fragment_container);

                        chatFragment.mDrawerLayout.closeDrawers();


                    ActionBar actionBar = chattingActivity.getSupportActionBar();
                    actionBar.setTitle(textView.getText().toString());

                        for (Friends friends : frdList){
                            if (friends.getName() == textView.getText()){
                                transImg = friends.getPicture();
                            }
                        }

                        fm.beginTransaction().remove(chatFragment).commit();
                        Fragment fragment = ChattingFragment.newInstance(textView.getText().toString(), transImg, mUserInfo);
                        fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
                        //fm.beginTransaction().replace(R.id.fragment_container,fragment);
                    }
                }
            });
        }

    }

    //*********适配器********
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<Friends> mList;
        public MyAdapter(List<Friends> friList) {

            mList = friList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_friend,parent,false);

            return  new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Friends friends = mList.get(position);
            String name_fri = friends.getName();
            int unreadM = friends.getUnreadMeg();
            int picture = friends.getPicture();
            String newTime = friends.getNewTime();
            holder.textView.setText(name_fri);
            //holder.mImageView.setImageResource(picture);
            if ( !TextUtils.isEmpty(friends.getPicUrl())){
                Glide.with(ChooseFragment.this).load(friends.getPicUrl()).into(holder.mImageView);
            }else{
                holder.mImageView.setImageBitmap(ImgScaleUtil.decodeBitmapFromResource(getResources(), picture, 50, 50));
            }

            if (unreadM != 0){
                holder.UnreadBtn.setVisibility(View.VISIBLE);
                holder.UnreadBtn.setText(String.valueOf(unreadM));
            }else{
                holder.UnreadBtn.setVisibility(View.GONE);
            }
            holder.timeText.setText(newTime);

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }


    //******************监听接口的实现*******************
    //当有新消息时，发送广播给聊天列表
    EMMessageListener mEMMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            for (EMMessage message : messages) {

                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(message.getFrom());
                int unread = conversation.getUnreadMsgCount();
                if (conversation != null && unread > 0 ){
                    Intent intent = new Intent("com.huadi.android.ainiyo.newMessage");
                    intent.putExtra("ID",message.getFrom());
                    intent.putExtra("newM",unread);
                    intent.putExtra("newT", DateUtil.getNowDate());
                    getActivity().sendBroadcast(intent);
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }
        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }
        @Override
        public void onMessageDelivered(List<EMMessage> messages) {
        }
        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }
}
