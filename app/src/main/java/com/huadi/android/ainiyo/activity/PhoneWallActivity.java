package com.huadi.android.ainiyo.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhidong on 2017/8/22.
 */

public class PhoneWallActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> mListUrl;
    private MyAdapter mMyAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_wall);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    localLayoutParams.flags);
        }

        mListUrl = new ArrayList<>();
        initListUrl();
        mRecyclerView = (RecyclerView) findViewById(R.id.phone_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mMyAdapter = new MyAdapter(mListUrl);
        mRecyclerView.setAdapter(mMyAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);  //模拟网络请求
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {   //切换到主线程
                            @Override
                            public void run() {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        public MyViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.picture_image);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        List<String> mList;

        public MyAdapter(List<String> list) {
            mList = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PhoneWallActivity.this).inflate(R.layout.item_phone, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (mList.get(position) != null) {
                Glide.with(PhoneWallActivity.this).load(mList.get(position)).into(holder.mImageView);
            } else {
                holder.mImageView.setImageResource(R.drawable.examplepicture);
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    public void initListUrl() {
        for (int i = 0; i < 10; i++) {
            String test = null;
            mListUrl.add(test);
        }
    }
}
