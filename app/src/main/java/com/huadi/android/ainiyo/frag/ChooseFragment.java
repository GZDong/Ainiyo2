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


    private List<Friends> frdList = new ArrayList<>();


    private int transImg;

    private RecyclerView mRecyclerView;
    private  MyAdapter mMyAdapter;

    private BroadcastReceiver mBroadcastReceiver;

    private ImageButton mPersons;

   // private TextView mTextView;

    /*public static ChooseFragment newInstance() {

        Bundle args = new Bundle();

        ChooseFragment fragment = new ChooseFragment();
        fragment.setArguments(args);
        return fragment;
    }*/



    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof MainActivity ) {
            UserInfoLab userInfoLab = UserInfoLab.get(getActivity());
            //这里获得传递进来的账号密码信息，然后存进数据库
            UserInfo userInfo = userInfoLab.getUserInfo();
            String name = userInfo.getUsername();
            String pass = userInfo.getPassword();
            SignInUtil.signIn(name,pass,getActivity());

            EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);

        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


       /* if (getActivity() instanceof MainActivity){
            UserInfoLab userInfoLab = UserInfoLab.get(getActivity());
            UserInfo userInfo = userInfoLab.getUserInfo();
            String name = userInfo.getUsername();
            String pass = userInfo.getPassword();

            SignInUtil.signIn(name,pass,getActivity());
            Log.e("_____________","onCreate() in ChooseFragment of Main");
        }*/

     /*  EMClient.getInstance().chatManager().addMessageListener(this);*/


        FriendsLab friendsLab = FriendsLab.get(getActivity());
        UserInfoLab userInfoLab = UserInfoLab.get(getActivity());
        UserInfo userInfo = userInfoLab.getUserInfo();
        if (userInfo == null){
            userInfoLab.initUser("xuniji","123",R.drawable.left_image);
            userInfo = userInfoLab.getUserInfo();
        }
        friendsLab.initFriends(userInfo);
        frdList = friendsLab.getFriendses();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.huadi.android.ainiyo.newMessage");
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int newMsg = intent.getIntExtra("newM",0);
                String ID = intent.getStringExtra("ID");
                String newTime = intent.getStringExtra("newT");
                FriendsLab friendsLab = FriendsLab.get(getActivity());
                Friends friends = friendsLab.getFriend(ID);
                friends.setUnreadMeg(newMsg);
                friends.setNewTime(newTime);
                friendsLab.reSort();

                for (Friends friends1 : frdList){
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
                });
               // frdList = friendsLab.getFriendses();
                mMyAdapter.notifyDataSetChanged();
            }
        };
        getActivity().registerReceiver(mBroadcastReceiver,intentFilter);
        //initFri();
       /* Log.d(")))))","____________1");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ECApplication.friList= EMClient.getInstance().contactManager().getAllContactsFromServer();

                }catch (HyphenateException e){
                    e.printStackTrace();
                }
            }
        });*/

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_choose,container,false);

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
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL));

        mPersons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FriendsListActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
     getActivity().unregisterReceiver(mBroadcastReceiver);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof MainActivity){
            EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
            //SignInUtil.signOut();
            Log.e("_____________","onPause() in ChooseFragment of Main");

        }

    }

    /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_manager,menu);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.add_friend:
               //增加好友
               Intent intent = new Intent(getActivity(),AddActivity.class);
               getActivity().startActivity(intent);
               return true;
           case R.id.sign_out:
               //退出登陆
               signOut();
           default:
               return super.onOptionsItemSelected(item);
       }


    }*/

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

                    FriendsLab friendsLab = FriendsLab.get(getActivity());
                    Friends fri = friendsLab.getFriend(textView.getText().toString());
                    fri.setUnreadMeg(0);
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(textView.getText().toString());
                    if (conversation != null){
                        conversation.markAllMessagesAsRead();
                    }
                    frdList = friendsLab.getFriendses();
                    mMyAdapter.notifyDataSetChanged();
                    if (getActivity() instanceof MainActivity){

                        Intent intent =  new Intent(getActivity(),ChattingActivity.class);
                        intent.putExtra("name",textView.getText());
                        for (Friends friends : frdList){
                            if (friends.getName() == textView.getText()){
                                transImg = friends.getPicture();
                            }
                        }
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
                        Fragment fragment = ChattingFragment.newInstance(textView.getText().toString(),transImg);
                        fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
                        //fm.beginTransaction().replace(R.id.fragment_container,fragment);
                    }



                    /*FragmentManager fm = getActivity().getSupportFragmentManager();
                    ChattingFragment chatFragment = (ChattingFragment) fm.findFragmentById(R.id.layout_content);
                    chatFragment.mDrawerLayout.closeDrawers();*/


                   /* MainActivity activity =(MainActivity) getActivity();
                    ActionBar actionBar = activity.getSupportActionBar();
                    actionBar.setTitle(textView.getText().toString());*/

                    /*for (Friends friends : frdList){
                        if (friends.getName() == textView.getText()){
                            transImg = friends.getPicture();
                        }
                    }

                    fm.beginTransaction().remove(chatFragment).commit();
                    Fragment fragment = ChattingFragment.newInstance(textView.getText().toString(),transImg);
                    fm.beginTransaction().add(R.id.layout_content,fragment).commit();*/
                    /*Intent intent = new Intent(getActivity(),ECChatActivity.class);
                    intent.putExtra("ec_chat_id",textView.getText());
                    startActivity(intent);*/
                }
            });
        }

    }

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
            holder.mImageView.setImageResource(picture);

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

  /*  private void initFri(){
        frdList = new ArrayList<>();
        Friends friend1 = new Friends("xuniji",0);
        Friends friend2 = new Friends("xiaoming",0);
        Friends friend3 = new Friends("long",0);
        frdList.add(friend1);
        frdList.add(friend2);
        frdList.add(friend3);
    }*/

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

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

    /*@Override
    public void onMessageReceived(List<EMMessage> messages) {
        Log.e("_____________","onReceived in ChooseFragment of Main");
        if (getActivity() instanceof MainActivity){

                for (EMMessage message : messages) {

                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(message.getFrom());
                    if (conversation != null){
                        int unread = conversation.getUnreadMsgCount();
                        Intent intent = new Intent("com.huadi.android.ainiyo.newMessage");
                        intent.putExtra("ID",message.getFrom());
                        intent.putExtra("newM",unread);
                        getActivity().sendBroadcast(intent);
                    }

                }
            Log.e("_____________","onReceived in ChooseFragment of Main");
        }




        if (getActivity() instanceof MainActivity){
            FriendsLab friendsLab = FriendsLab.get(getActivity());
            List<Friends> mListFri = friendsLab.getFriendses();
            for (EMMessage message : messages){
                for (Friends friends : mListFri){
                    if (message.getFrom().equals(friends.getName())){

                        friends.setUnreadMeg(friends.getUnreadMeg()+1);

                    }
                }
            }
            frdList = friendsLab.getFriendses();
            mMyAdapter.notifyDataSetChanged();
        }else if (getActivity() instanceof ChattingActivity){

        }

    }
    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        //收到透传消息
    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        //收到已读回执
    }
    @Override
    public void onMessageDelivered(List<EMMessage> message) {
        //收到已送达回执
    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {
        //消息状态变动
    }*/
}
