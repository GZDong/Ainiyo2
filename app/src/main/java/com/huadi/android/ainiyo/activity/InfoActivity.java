package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.Gson;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.AreaData;
import com.huadi.android.ainiyo.entity.UserData;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static com.huadi.android.ainiyo.application.ECApplication.sessionId;

public class InfoActivity extends AppCompatActivity implements LGImgCompressor.CompressListener {
    @ViewInject(R.id.avatar)
    private LinearLayout avatar;
    @ViewInject(R.id.sex)
    private LinearLayout sex;
    @ViewInject(R.id.job)
    private LinearLayout job;


    @ViewInject(R.id.avatar_imag)
    private CircleImageView avatar_imag;
    @ViewInject(R.id.progress)
    private ProgressBar progress;


    private List<String> image = new ArrayList<>();//从选择器得到的头像
    private List<String> compressImages = new ArrayList<>();//压缩完的头像


    private int Id;
    private String Autograph;
    private int Gentle;
    private boolean Vip;
    private String Birthday;
    private int Area;
    private String Job;
    private double Salary;
    private boolean HaveKids;
    private boolean Parentsalive;
    private String Maritallstatus;
    private String Emotion;
    private String Hobby;
    private String Requir;
    private String Avatar;
    private int Userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ViewUtils.inject(this);
        LGImgCompressor.getInstance(this).withListener(this);

        //获取用户详细信息//
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionid", sessionId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/getuserinfo", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInFo) {
                String info = responseInFo.result.toString();
                try {
                    JSONObject object = new JSONObject(info);
                    String msg = object.getString("Msg");
                    Gson gson = new Gson();
                    UserData userData = gson.fromJson(object.getJSONObject("Result").toString(), UserData.class);

                    //如果获取数据成功，则把数据加载到各项
                    if (msg.equals("success")) {
                        Autograph = userData.getAutograph();
                        Gentle = userData.getGentle();
                        Id = userData.getId();
                        Vip = userData.isVip();
                        Birthday = userData.getBirthday();
                        Area = userData.getArea();
                        Job = userData.getJob();
                        Salary = userData.getSalary();
                        HaveKids = userData.isHaveKids();
                        Parentsalive = userData.isParentsalive();
                        Maritallstatus = userData.getMaritallstatus();
                        Emotion = userData.getEmotion();
                        Hobby = userData.getHobby();
                        Requir = userData.getRequir();
                        Avatar = userData.getAvatar();
                        Userid = userData.getUserid();
                        //在完善信息里获得用户上次写过的详细信息//
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });

    }


    @OnClick({R.id.avatar, R.id.sex, R.id.job})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
                ImageSelectorUtils.openPhoto(InfoActivity.this, 1, true, 0);
                break;
            case R.id.sex:
                startActivity(new Intent(InfoActivity.this, SexActivity.class));
                break;
            case R.id.job:
                startActivity(new Intent(InfoActivity.this, JobActivity.class));
                break;


        }
    }


    //得到从相册选择的图片//
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        image.clear();
        if (requestCode == 1 && data != null) {
            image = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            propressImg(image);//讲选择图片进行压缩//
        }
    }

    public void propressImg(List<String> images) {
        compressImages.clear();
        for (String srcImgUrl : images) {
            LGImgCompressor.getInstance(InfoActivity.this).starCompressWithDefault(srcImgUrl);
        }

    }

    @Override
    public void onCompressStart() {
    }

    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult imageOutPath) {
        compressImages.add(imageOutPath.getOutPath());
        //修改头像
        progress.setVisibility(View.VISIBLE);
        modifyImage(compressImages);


    }

    public void modifyImage(final List<String> images) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionid", sessionId);
        File file = new File(images.get(0));
        params.addBodyParameter("avatar", file);
        new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifyavatar", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject object = new JSONObject(responseInfo.result.toString());
                    int status = object.getInt("Status");
                    String result = object.getString("Result");
                    String msg = object.getString("Msg");
                    if (status == 5101) {
                        progress.setVisibility(View.GONE);
                        Glide.with(InfoActivity.this).load(images.get(0)).into(avatar_imag);//加载选择的图片在头像上
                        Toast.makeText(InfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                    } else {
                        progress.setVisibility(View.GONE);

                        Toast.makeText(InfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progress.setVisibility(View.GONE);
                Toast.makeText(InfoActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
