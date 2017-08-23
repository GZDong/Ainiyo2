package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.util.LGImgCompressor;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.huadi.android.ainiyo.R;
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

public class VipApplyActivity extends AppCompatActivity implements LGImgCompressor.CompressListener {
    @ViewInject(R.id.first)
    private ImageView first;
    @ViewInject(R.id.second)
    private ImageView second;
    @ViewInject(R.id.third)
    private ImageView third;
    @ViewInject(R.id.finish)
    private Button finish;
    @ViewInject(R.id.back)
    private ImageView back;


    @ViewInject(R.id.progress)
    private ProgressBar progress;


    private List<String> fi = new ArrayList<>();//
    private List<String> se = new ArrayList<>();//还未进行压缩的URL//
    private List<String> th = new ArrayList<>();//
    private List<String> compressImages = new ArrayList<>();
    private List<String> ima = new ArrayList<>();
    private List<String> done1 = new ArrayList<>();//
    private List<String> done2 = new ArrayList<>();//压缩完成后的URL//
    private List<String> done3 = new ArrayList<>();//

    private String url1;//
    private String url2;//上传图片之后获得的URL
    private String url3;//


    private Boolean change1 = false;//
    private Boolean change2 = false;//判断图片是否改变
    private Boolean change3 = false;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_apply);
        ViewUtils.inject(this);
        LGImgCompressor.getInstance(this).withListener(this);
    }


    @OnClick({R.id.first, R.id.second, R.id.third, R.id.finish, R.id.back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first:
                ImageSelectorUtils.openPhoto(VipApplyActivity.this, 1, true, 0);
                break;
            case R.id.second:
                ImageSelectorUtils.openPhoto(VipApplyActivity.this, 2, true, 0);
                break;
            case R.id.third:
                ImageSelectorUtils.openPhoto(VipApplyActivity.this, 3, true, 0);
                break;
            case R.id.back:
                startActivity(new Intent(VipApplyActivity.this, VipHintActivity.class));
                break;
            case R.id.finish:
                progress.setVisibility(View.VISIBLE);
                //如果已经选择三张图片，才可以提交，如果没有上传三张图片，提示错误//
                if (change1) {

                    if (change2) {

                        if (change3) {
                            RequestParams params = new RequestParams();
                            params.addBodyParameter("sessionid", sessionId);
                            params.addBodyParameter("url1", url1);
                            params.addBodyParameter("url2", url2);
                            params.addBodyParameter("url3", url3);
                            HttpUtils http = new HttpUtils();
                            http.send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/viprequest", params, new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    String info = responseInfo.result.toString();
                                    try {
                                        JSONObject object = new JSONObject(info);
                                        String msg = object.getString("Msg");
                                        int status = object.getInt("Status");
                                        if (status == 0) {
                                            progress.setVisibility(View.GONE);
                                            Toast.makeText(VipApplyActivity.this, "提交成功，等待审核", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(VipApplyActivity.this, MainActivity.class));


                                        } else {
                                            progress.setVisibility(View.GONE);
                                            Toast.makeText(VipApplyActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(HttpException error, String msg) {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(VipApplyActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            progress.setVisibility(View.GONE);
                            Toast.makeText(VipApplyActivity.this, "请上传三张图片", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(VipApplyActivity.this, "请上传三张图片", Toast.LENGTH_LONG).show();
                    }

                } else {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(VipApplyActivity.this, "请上传三张图片", Toast.LENGTH_LONG).show();
                }
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            fi = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            propressImg(fi);
        }
        if (requestCode == 2 && data != null) {
            se = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            propressImg(se);
        }
        if (requestCode == 3 && data != null) {
            th = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            propressImg(th);
        }
    }

    public void propressImg(List<String> images) {
        ima = images;
        compressImages.clear();
        for (String srcImgUrl : images) {
            LGImgCompressor.getInstance(VipApplyActivity.this).starCompressWithDefault(srcImgUrl);
        }

    }

    @Override
    public void onCompressStart() {
    }

    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult imageOutPath) {
        compressImages.add(imageOutPath.getOutPath());
        if (ima == fi) {
            change1 = true;
            done1 = compressImages;


            sendImages(done1);

        }
        if (ima == se) {
            change2 = true;
            done2 = compressImages;
            //动画

            sendImages(done2);

        }
        if (ima == th) {
            change3 = true;
            done3 = compressImages;


            sendImages(done3);
        }


    }

    public void sendImages(final List<String> images) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionid", sessionId);
        File file = new File(images.get(0));
        params.addBodyParameter("photo", file);

        new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/uploadphoto", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject object = new JSONObject(responseInfo.result.toString());
                    int status = object.getInt("Status");
                    String result = object.getString("Result");
                    String msg = object.getString("Msg");
                    if (msg.equals("success")) {
                        if (images == done1) {
                            Glide.with(VipApplyActivity.this).load(R.drawable.chenggong).into(first);
                            url1 = result;
                        }
                        if (images == done2) {
                            Glide.with(VipApplyActivity.this).load(R.drawable.chenggong).into(second);
                            url2 = result;
                        }
                        if (images == done3) {
                            Glide.with(VipApplyActivity.this).load(R.drawable.chenggong).into(third);
                            url3 = result;
                        }

                    } else {
                        if (images == done1) {
                            Glide.with(VipApplyActivity.this).load(R.drawable.cuowu).into(first);
                        }
                        if (images == done2) {
                            Glide.with(VipApplyActivity.this).load(R.drawable.cuowu).into(second);
                        }
                        if (images == done3) {
                            Glide.with(VipApplyActivity.this).load(R.drawable.cuowu).into(third);
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(VipApplyActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

            }
        });


    }
}

