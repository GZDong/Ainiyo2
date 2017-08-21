package com.huadi.android.ainiyo.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.Friends;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.frag.ChooseFragment;
import com.huadi.android.ainiyo.util.ImgScaleUtil;

import java.util.List;

/**
 * Created by zhidong on 2017/8/21.
 */

public class DeleteFriendActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private Button btnPosi;
    private Button btnNagi;
    private MyAdapter mMyAdapter;
    private Friends mFriends;
    private List<Friends> friList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletefri);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.theme_statusBar_red));
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar_delete);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setTitle(R.string.sure_reduce);

        btnPosi = (Button)findViewById(R.id.sure_delete);
        btnNagi = (Button) findViewById(R.id.cancel_delete);
        mRecyclerView = (RecyclerView) findViewById(R.id.friRV);
        friList = FriendsLab.get(DeleteFriendActivity.this).getFriendsesAll();
        mMyAdapter = new MyAdapter(friList);
        LinearLayoutManager lm = new LinearLayoutManager(DeleteFriendActivity.this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mMyAdapter);

        btnPosi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Friends friends: friList){
                    if (friends.getFlag_delete() == 1){
                        //删除数据库，删除服务器数据
                        FriendsLab.get(DeleteFriendActivity.this).deleteFriends(friends.getName());
                    }
                    mMyAdapter.notifyDataSetChanged();
                }
            }
        });
        btnNagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (Friends friends: friList){
                    if (friends.getFlag_delete() == 1){
                        friends.setFlag_delete(0);
                        mMyAdapter.notifyDataSetChanged();
                    }
                }
                btnPosi.setVisibility(View.INVISIBLE);
                btnNagi.setVisibility(View.INVISIBLE);
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mTextView;
        private CheckBox mCheckBox;

        public MyViewHolder(View view){
            super(view);
            mImageView =(ImageView) view.findViewById(R.id.image_person);
            mTextView = (TextView) view.findViewById(R.id.name);
            mCheckBox = (CheckBox) view.findViewById(R.id.chooseBox);
        }
    }
    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        List<Friends> mList;

        public MyAdapter(List<Friends> list) {
            mList = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(DeleteFriendActivity.this).inflate(R.layout.item_delete,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Friends friends = mList.get(position);
            mFriends = friList.get(position);

            if (!TextUtils.isEmpty(friends.getPicUrl())) {
                Glide.with(DeleteFriendActivity.this).load(friends.getPicUrl()).into(holder.mImageView);
            } else {
                holder.mImageView.setImageBitmap(ImgScaleUtil.decodeBitmapFromResource(getResources(), friends.getPicture(), 50, 50));
            }
            holder.mTextView.setText(friends.getName());
            holder.mCheckBox.setChecked(false);
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mFriends.setFlag_delete(1);
                    btnPosi.setVisibility(View.VISIBLE);
                    btnNagi.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
