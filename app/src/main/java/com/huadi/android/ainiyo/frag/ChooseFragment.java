package com.huadi.android.ainiyo.frag;

import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhidong on 2017/7/26.
 */

public class ChooseFragment extends Fragment {


    private List<Friends> frdList;
    private int transImg;
    private RecyclerView mRecyclerView;
    private  MyAdapter mMyAdapter;
    private BroadcastReceiver mBroadcastReceiver;
    private ImageButton mPersons;

    private UserInfo mUserInfo;


    public static ChooseFragment newInstance() {

       // Bundle args = new Bundle();
        ChooseFragment fragment = new ChooseFragment();
       // fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        //*****************根据单例里的用户信息来登陆***************
        if (getActivity() instanceof MainActivity) { //这里登陆只是为了监听器的注册
          //  UserInfo userInfo = new UserInfo("xuniji", "123", R.drawable.left_image);

            //这里获得传递进来的账号密码信息，然后存进数据库
           mUserInfo = UserInfoLab.get(getActivity()).getUserInfo();
            String name = mUserInfo.getUsername();
            String pass = mUserInfo.getPassword();
            SignInUtil.signIn(name,pass,getActivity());
            //*******注册新消息监听器*********
            EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);

            //******************注册新信息的接受器***********************
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.huadi.android.ainiyo.newMessage");
            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int newMsg = intent.getIntExtra("newM", 0);
                    String ID = intent.getStringExtra("ID");
                    String newTime = intent.getStringExtra("newT");
                    Friends friends = FriendsLab.get(getActivity(), mUserInfo).getFriend(ID);
                    friends.setUnreadMeg(newMsg);
                    friends.setNewTime(newTime);
                    FriendsLab.get(getActivity(), mUserInfo).reSort();
                    frdList = FriendsLab.get(getActivity(), mUserInfo).getFriendses();

                    for (Friends friends1 : frdList) {

                        Log.e("eee", "排序后：" + friends1.getName() + "/n");

                    }
                /*for (Friends friends1 : frdList){
                    if (friends1.getName().equals(ID)){
                        friends1.setNewTime(DateUtil.getNowDate());
                    }
                }
                Collections.sort(frdList, new Comparator<Friends>() {
                    @Override
                    public int compare(Friends friends, Friends t1) {
                        if (friends.getNewTime().compareTo(t1.getNewTime())>0){
                            return -1;
                        }
                        if (friends.getNewTime().compareTo(t1.getNewTime())==0){
                            return 0;
                        }
                        return 1;
                    }
                });*/
                    MyAdapter myAdapter = new MyAdapter(frdList);
                    mMyAdapter = myAdapter;
                    mRecyclerView.setAdapter(mMyAdapter);
                    mMyAdapter.notifyDataSetChanged();
                }
            };
            getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
            //************************************************
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        //***************根据用户信息来刷新聊天列表**********
        if (getActivity() instanceof MainActivity) {
            frdList = FriendsLab.get(getActivity(), mUserInfo).getFriendses();
            MyAdapter myAdapter = new MyAdapter(frdList);
            mMyAdapter = myAdapter;
            mRecyclerView.setAdapter(mMyAdapter);
            mMyAdapter.notifyDataSetChanged();
        }
        Log.e("ee", "onResume_ChooseFragment");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

     //   UserInfo userInfo = new UserInfo("xuniji", "123", R.drawable.left_image);
        //这里获得传递进来的账号密码信息，然后存进数据库
        mUserInfo = UserInfoLab.get(getActivity()).getUserInfo();
        frdList = new ArrayList<>();
        frdList = FriendsLab.get(getActivity(), mUserInfo).getFriendses();

        Log.e("ee", "onCreate_ChooseFragment");


        if (getActivity() instanceof ChattingActivity) {
            //******************注册新信息的接受器***********************
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.huadi.android.ainiyo.newMessage");
            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int newMsg = intent.getIntExtra("newM", 0);
                    String ID = intent.getStringExtra("ID");
                    String newTime = intent.getStringExtra("newT");
                    Friends friends = FriendsLab.get(getActivity(), mUserInfo).getFriend(ID);
                    friends.setUnreadMeg(newMsg);
                    friends.setNewTime(newTime);
                    FriendsLab.get(getActivity(), mUserInfo).reSort();
                    frdList = FriendsLab.get(getActivity(), mUserInfo).getFriendses();

                    for (Friends friends1 : frdList) {

                        Log.e("eee", "排序后：" + friends1.getName() + "/n");

                    }
                    /*for (Friends friends1 : frdList){
                        if (friends1.getName().equals(ID)){
                            friends1.setNewTime(DateUtil.getNowDate());
                        }
                    }
                    Collections.sort(frdList, new Comparator<Friends>() {
                        @Override
                        public int compare(Friends friends, Friends t1) {
                            if (friends.getNewTime().compareTo(t1.getNewTime())>0){
                                return -1;
                            }
                            if (friends.getNewTime().compareTo(t1.getNewTime())==0){
                                return 0;
                            }
                            return 1;
                        }
                    });*/

                    MyAdapter myAdapter = new MyAdapter(frdList);
                    mMyAdapter = myAdapter;
                    mRecyclerView.setAdapter(mMyAdapter);
                    mMyAdapter.notifyDataSetChanged();
                }
            };
            getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
            //************************************************
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e("ee", "onCreateView_ChooseFragment");
        View v = inflater.inflate(R.layout.fragment_choose,container,false);


        //***********如果在聊天里打开就不要标题栏******
        if (getActivity() instanceof ChattingActivity){
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.bar);
            linearLayout.setVisibility(View.GONE);
        }


        mPersons = (ImageButton) v.findViewById(R.id.btn_friends);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.fri_recycler_view);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(lm);
        mMyAdapter = new MyAdapter(frdList);
        mRecyclerView.setAdapter(mMyAdapter);

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
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() instanceof ChattingActivity) {
            getActivity().unregisterReceiver(mBroadcastReceiver);
        }
        if (getActivity()instanceof MainActivity){
            Log.e("eee","onDestroy");
            SignInUtil.signOut();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof MainActivity){
            EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
            getActivity().unregisterReceiver(mBroadcastReceiver);

            Log.e("eee", "onPause()");

        }

    }

    //*****************适配器********************
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


                    Friends fri = FriendsLab.get(getActivity(), mUserInfo).getFriend(textView.getText().toString());
                    //****在本地置0*****
                    fri.setUnreadMeg(0);
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
           holder.mImageView.setImageBitmap(ImgScaleUtil.decodeBitmapFromResource(getResources(),picture,50,50));

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
