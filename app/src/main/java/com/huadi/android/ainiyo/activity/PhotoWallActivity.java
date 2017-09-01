package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.Retrofit2.PostRequest_getfriendsalbums_Interface;
import com.huadi.android.ainiyo.Retrofit2.PostRequest_getfriendsalbums_next_Interface;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.gson.AlbumsName;
import com.huadi.android.ainiyo.gson.ResultForFriAlbums;
import com.huadi.android.ainiyo.gson.ResultForFriUrl;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhidong on 2017/8/22.
 */

public class PhotoWallActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> mListUrl;
    private MyAdapter mMyAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String FriId;
    private List<AlbumsName> albumsList;
    private static final String TAG = "test";

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
        FriId = getIntent().getStringExtra("id");
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
        private String url;

        public MyViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.picture_image);

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = PhotoShowActivity.newInstance(PhotoWallActivity.this,url);
                    startActivity(intent);
                }
            });
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        List<String> mList;

        public MyAdapter(List<String> list) {
            mList = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PhotoWallActivity.this).inflate(R.layout.item_phone, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (mList.get(position) != null) {
                Glide.with(PhotoWallActivity.this).load(mList.get(position)).into(holder.mImageView);
                holder.setUrl(mList.get(position));
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
        final String sessionid = ((ECApplication)getApplication()).sessionId;

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://120.24.168.102:8080/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostRequest_getfriendsalbums_Interface interfaceFirst = retrofit.create(PostRequest_getfriendsalbums_Interface.class);
        Log.e(TAG, "请求第一步传递的值："+sessionid +" " +FriId );
        Observable<ResultForFriAlbums> observableFirst = interfaceFirst.getObservable(sessionid,FriId);
        observableFirst.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultForFriAlbums>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted: 请求第一步完成" );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: 请求第一步异常" );
                    }

                    @Override
                    public void onNext(final ResultForFriAlbums resultForFriAlbums) {
                        Log.e(TAG, "onNext: 请求第一步无异常" );
                        if (resultForFriAlbums.getMsg().equals("success")){
                            Log.e(TAG, "        请求第一步成功" );
                            if (resultForFriAlbums.getResult()!=null){
                                albumsList = resultForFriAlbums.getResult();
                                for (AlbumsName albumsname: albumsList){
                                    PostRequest_getfriendsalbums_next_Interface interfaceSecond = retrofit.create(PostRequest_getfriendsalbums_next_Interface.class);
                                    Log.e(TAG, "第二步传递的参数是："+ sessionid + " "+ albumsname.getPicname()+ " " + FriId );
                                    Observable<ResultForFriUrl> observableSecond = interfaceSecond.getObservable(sessionid,albumsname.getPicname(),FriId);
                                    observableSecond.subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Subscriber<ResultForFriUrl>() {
                                                @Override
                                                public void onCompleted() {
                                                    Log.e(TAG, "onCompleted: 请求第二步完成" );
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Log.e(TAG, "onError: 请求第二步异常");
                                                }

                                                @Override
                                                public void onNext(ResultForFriUrl resultForFriUrl) {
                                                    Log.e(TAG, "onNext: 请求第二步无异常" );
                                                    if (resultForFriUrl!=null){
                                                        Log.e(TAG, "       请求第二步成功" );
                                                        if (!TextUtils.isEmpty(resultForFriUrl.getResult())){
                                                            Log.e(TAG, "图片："+resultForFriUrl.getResult() );
                                                            mListUrl.add(resultForFriUrl.getResult());
                                                            mMyAdapter.notifyDataSetChanged();
                                                        }else {
                                                            Toast.makeText(PhotoWallActivity.this,"该用户相册没有图片",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }else {
                                                        Toast.makeText(PhotoWallActivity.this,"该用户相册没有图片",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }else {
                                Toast.makeText(PhotoWallActivity.this,"对方没有上传照片",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
