package com.huadi.android.ainiyo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
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
import com.huadi.android.ainiyo.util.CONST;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import libcore.io.DiskLruCache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
                mListUrl.clear();
                initListUrl();
                mMyAdapter.notifyDataSetChanged();
                runOnUiThread(new Runnable() {   //切换到主线程
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMyAdapter.fluchCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyAdapter.cancelAllTasks();
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

        //记录所有正在下载或者等待下载的任务
        private Set<BitmapWorkerTask> taskCollection;

        private LruCache<String,Bitmap> mMemoryCache;
        private DiskLruCache mDiskLruCache;

        private int mItemHeight = 0;

        public MyAdapter(List<String> list) {
            mList = list;
            taskCollection = new HashSet<BitmapWorkerTask>();
            //内存缓存初始化
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxMemory /8 ;
            mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount();
                }
            };
            //磁盘缓存初始化
            try{
                File cacheDir = getDiskCacheDir(PhotoWallActivity.this,"thumb");
                if (!cacheDir.exists()){
                    cacheDir.mkdirs();
                }
                mDiskLruCache = DiskLruCache.open(cacheDir,getAppVersion(PhotoWallActivity.this),1,20*1024*1024);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PhotoWallActivity.this).inflate(R.layout.item_phone, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (mList.get(position) != null) {

                //给ImageView设置一个Tag，保证异步加载不会乱序
                holder.mImageView.setTag(mList.get(position));
                loadBitmaps(holder.mImageView,mList.get(position));
                //Glide.with(PhotoWallActivity.this).load(mList.get(position)).into(holder.mImageView);
                holder.setUrl(mList.get(position));

            } else {
                holder.mImageView.setImageResource(R.drawable.examplepicture);
            }
        }
        //将一张图片放置到内存缓存种，前提判断
        public void addBitmapToMemoryCache(String key,Bitmap bitmap){
            if (getBitmapFromMemoryCache(key) ==null){
                mMemoryCache.put(key,bitmap);
            }
        }
        public Bitmap getBitmapFromMemoryCache(String key){
            return mMemoryCache.get(key);
        }

        //加载bitmap
        public void loadBitmaps(ImageView imageView,String imageUrl){
            try{
                Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
                if (bitmap == null){
                    BitmapWorkerTask task = new BitmapWorkerTask();
                    taskCollection.add(task);
                    task.execute(imageUrl);
                }else {
                    if (imageView != null && bitmap !=null){
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //终止任务
        public void cancelAllTasks(){
            if (taskCollection != null){
                for (BitmapWorkerTask task:taskCollection){
                    task.cancel(false);
                }
            }
        }

        //获取硬盘缓存的路径地址
        public File getDiskCacheDir(Context context,String uniqueName){
            String cachePath;
            //如果SD卡存在，或者SD卡不可移动，地址为SD卡
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||
                    !Environment.isExternalStorageRemovable()){
                cachePath = context.getExternalCacheDir().getPath();
            }else {  //否则为缓存文件
                cachePath = context.getCacheDir().getPath();
            }
            return  new File(cachePath + File.separator + uniqueName);
        }

        //获取当前应用的版本号
        public int getAppVersion(Context context){
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
                return info.versionCode;
            }catch (PackageManager.NameNotFoundException e){
                e.printStackTrace();
            }
            return 1;
        }
     //*******MD5加密
        public String hashKeyForDisk(String key) {
            String cacheKey;
            try {
                final MessageDigest mDigest = MessageDigest.getInstance("MD5");
                mDigest.update(key.getBytes());
                cacheKey = bytesToHexString(mDigest.digest());
            } catch (NoSuchAlgorithmException e) {
                cacheKey = String.valueOf(key.hashCode());
            }
            return cacheKey;
        }
        private String bytesToHexString(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    sb.append('0');
                }
                sb.append(hex);
            }
            return sb.toString();
        }
    //***********

        public void fluchCache(){
            if (mDiskLruCache != null){
                try {
                    mDiskLruCache.flush();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        class BitmapWorkerTask extends AsyncTask<String,Void,Bitmap>{
            private String imageUrl;

            @Override
            protected Bitmap doInBackground(String... strings) {
                imageUrl = strings[0];
                FileDescriptor fileDescriptor = null;
                FileInputStream fileInputStream = null;
                DiskLruCache.Snapshot snapshot = null;
                try{
                    final String key = hashKeyForDisk(imageUrl);
                    snapshot = mDiskLruCache.get(key);
                    if (snapshot == null){
                        //网络请求
                        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                        if (editor != null){
                            OutputStream outputStream = editor.newOutputStream(0);
                            if (downloadUrlToStream(imageUrl,outputStream)){
                                editor.commit();
                            }else {
                                editor.abort();
                            }
                        }
                        snapshot = mDiskLruCache.get(key);
                    }
                    if (snapshot != null){
                        fileInputStream  = (FileInputStream) snapshot.getInputStream(0);
                        fileDescriptor = fileInputStream.getFD();
                    }

                    Bitmap bitmap = null;
                    if (fileDescriptor != null){
                        bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    }
                    if (bitmap != null){
                        addBitmapToMemoryCache(strings[0],bitmap);
                    }
                    return bitmap;
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    if (fileDescriptor == null&& fileInputStream !=null){
                        try {
                           fileInputStream.close();
                        }catch (IOException e){
                        }
                    }
                }
                return  null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                ImageView imageView = (ImageView) mRecyclerView.findViewWithTag(imageUrl);
                if (imageView != null && bitmap != null){
                    imageView.setImageBitmap(bitmap);
                }
                taskCollection.remove(this);
            }

            private boolean downloadUrlToStream(String urlString,OutputStream outputStream){
                HttpURLConnection urlConnection = null;
                BufferedOutputStream out = null;
                BufferedInputStream in = null;
                try {

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlString)
                            .build();
                    Response response = client.newCall(request).execute();

                   /* final URL url = new URL (urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();*/

                    in = new BufferedInputStream(response.body().byteStream(),8 *1024);
                    out = new BufferedOutputStream(outputStream, 8 * 1024);
                    int b;
                    //流用int来读取，-1是EOF
                    while((b = in.read()) != -1){
                        out.write(b);
                    }
                    return true;
                }catch (final IOException e){
                    e.printStackTrace();
                }finally {
                    try{
                        if (out != null){
                            out.close();
                        }
                        if (in != null){
                            in.close();
                        }
                    }catch (final IOException e){
                        e.printStackTrace();
                    }
                }
                return false;
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
                .baseUrl(CONST.NEW_HOST)
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
