package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.entity.Photo;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.adapter.PhotoAdapter;
import com.huadi.android.ainiyo.util.LGImgCompressor;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.huadi.android.ainiyo.application.ECApplication.sessionId;


public class PhotoActivity extends AppCompatActivity implements LGImgCompressor.CompressListener {
    @ViewInject(R.id.add_pic)
    private ImageView add_pic;
    @ViewInject(R.id.recy_img)
    private RecyclerView recy_img;
    @ViewInject(R.id.back)
    private ImageView back;
    @ViewInject(R.id.progress)
    private ProgressBar progress;
    private List<String> images=new ArrayList<>();
    private List<String> compressImages=new ArrayList<>();
    private List<String> picName=new ArrayList<>();
    private List<String> show=new ArrayList<>();
    private int photocount=0;


    private GridLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ViewUtils.inject(this);
        LGImgCompressor.getInstance(this).withListener(this);
        getAllPhotoName();


    }

    @OnClick({R.id.add_pic, R.id.back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.add_pic:
                ImageSelectorUtils.openPhoto(PhotoActivity.this,1,false,9);
                break;
            case R.id.back:
                startActivity(new Intent(PhotoActivity.this, MainActivity.class));
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            images = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            propressImg(images);
        }
    }
    public void propressImg(List<String> images) {
        compressImages.clear();
        for(String srcImgUrl:images){
            LGImgCompressor.getInstance(PhotoActivity.this).starCompressWithDefault(srcImgUrl);
        }

    }

    @Override
    public void onCompressStart() {
    }

    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult imageOutPath) {
        compressImages.add(imageOutPath.getOutPath());
        if(compressImages.size()==images.size()){
            sendImages(compressImages);
        }
    }
    public void sendImages(final List<String> images) {
        for (int i = 0; i < images.size(); i++) {
            progress.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();
            params.addBodyParameter("sessionid", sessionId);
            File file = new File(images.get(i));
            params.addBodyParameter("alumb", file);

            new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/uploadalumb",params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    try {
                        JSONObject object = new JSONObject(responseInfo.result.toString());
                        int status = object.getInt("Status");
                        if (object.getString("Msg").equals("success")) {
                            Toast.makeText(PhotoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.GONE);
                            picName.clear();
                            show.clear();
                            getAllPhotoName();//如果上传成功，则再次加载一个recyclerview实现刷新

                        }
                        else{
                            Toast.makeText(PhotoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    Toast.makeText(PhotoActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
                }
            });
        }



    }
    public void getAllPhotoName() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionid", sessionId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/getalbums",params,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String info = responseInfo.result.toString();
                try {
                    JSONObject object = new JSONObject(info);
                    String msg=object.getString("Msg");
                    Gson gson = new Gson();
                    List<Photo> photo = gson.fromJson(object.getJSONArray("Result").toString(), new TypeToken<List<Photo>>() {
                    }.getType());
                    if(msg.equals("success")) {
                        for (int i = 0; i < photo.size(); i++) {
                            picName.add(photo.get(i).getPicname());//获得全部相册的URL
                        }
                        if (photocount == picName.size()) {
                            photocount = picName.size();
                        }//如果没有加入相片，则不需要加载recyclerview
                        else{photocount=picName.size();showPhoto();}



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(PhotoActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showPhoto() {

        for (int i = (picName.size()-1); i >=0; i--) {
            RequestParams params = new RequestParams();
            params.addBodyParameter("sessionid", sessionId);
            params.addBodyParameter("alumb",picName.get(i));
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/getalbum",params,new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String info = responseInfo.result.toString();
                    try {
                        JSONObject object = new JSONObject(info);
                        String msg = object.getString("Msg");
                        String result=object.getString("Result");
                        if(msg.equals("success")){
                            show.add(result);
                            if(show.size()==picName.size()) {
                                RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recy_img);
                                PhotoAdapter adapter = new PhotoAdapter(PhotoActivity.this, show);
                                GridLayoutManager layoutManager = new GridLayoutManager(PhotoActivity.this, 3);
                                mRecyclerView.setLayoutManager(layoutManager);
                                mRecyclerView.setAdapter(adapter);
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    Toast.makeText(PhotoActivity.this, msg, Toast.LENGTH_SHORT).show();
                }

            });
        }

    }


}