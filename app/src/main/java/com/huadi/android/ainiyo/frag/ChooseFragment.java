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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.ChattingActivity;
import com.huadi.android.ainiyo.entity.Friends;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhidong on 2017/7/26.
 */

public class ChooseFragment extends Fragment {


    private List<Friends> frdList;


    private int transImg;


    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;

    private BroadcastReceiver mBroadcastReceiver;

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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        frdList = new ArrayList<>();
        FriendsLab friendsLab = FriendsLab.get(getActivity());
        frdList = friendsLab.getFriendses();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.huadi.android.ainiyo.newMessage");
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int newMsg = intent.getIntExtra("newM",0);
                String ID = intent.getStringExtra("ID");
                FriendsLab friendsLab = FriendsLab.get(getActivity());
                Friends friends = friendsLab.getFriend(ID);
                friends.setUnreadMeg(newMsg);
                frdList = friendsLab.getFriendses();
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

        mRecyclerView = (RecyclerView) v.findViewById(R.id.fri_recycler_view);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(lm);
        mMyAdapter = new MyAdapter(frdList);
        mRecyclerView.setAdapter(mMyAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL));

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
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

        public MyViewHolder(View v){
            super(v);
            textView = (TextView) v.findViewById(R.id.name_fri);
            mImageView = (ImageView) v.findViewById(R.id.img_friend);
            UnreadBtn= (Button) v.findViewById(R.id.unread_btn);
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
            String name_fri = mList.get(position).getName();
            int unreadM = mList.get(position).getUnreadMeg();
            int picture = mList.get(position).getPicture();
            holder.textView.setText(name_fri);
            holder.mImageView.setImageResource(picture);

            if (unreadM > 0){
                holder.UnreadBtn.setVisibility(View.VISIBLE);
                holder.UnreadBtn.setText(String.valueOf(unreadM));
            }else{
                holder.UnreadBtn.setVisibility(View.GONE);
            }

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
    private void signOut() {
        // 调用sdk的退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("lzan13", "logout success");
                // 调用退出成功，结束app
               getActivity().finish();
            }

            @Override
            public void onError(int i, String s) {
                Log.i("lzan13", "logout error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

}
