package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.Gson;
import com.huadi.android.ainiyo.MainActivity;
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
import org.w3c.dom.Text;

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
    @ViewInject(R.id.salary)
    private LinearLayout salary;
    @ViewInject(R.id.birthday)
    private LinearLayout birthday;
    @ViewInject(R.id.address)
    private LinearLayout address;
    @ViewInject(R.id.parent)
    private LinearLayout parent;
    @ViewInject(R.id.marriage)
    private LinearLayout marriage;
    @ViewInject(R.id.kid)
    private LinearLayout kid;
    @ViewInject(R.id.emotion)
    private LinearLayout emotion;
    @ViewInject(R.id.hobby)
    private LinearLayout hobby;
    @ViewInject(R.id.select)
    private LinearLayout select;
    @ViewInject(R.id.note)
    private LinearLayout note;


    @ViewInject(R.id.sex_text)
    private TextView sex_text;
    @ViewInject(R.id.job_text)
    private TextView job_text;
    @ViewInject(R.id.salary_text)
    private TextView salary_text;
    @ViewInject(R.id.birthday_text)
    private TextView birthday_text;
    @ViewInject(R.id.address_text)
    private TextView address_text;
    @ViewInject(R.id.parent_text)
    private TextView parent_text;
    @ViewInject(R.id.marriage_text)
    private TextView marriage_text;
    @ViewInject(R.id.kid_text)
    private TextView kid_text;


    @ViewInject(R.id.avatar_imag)
    private CircleImageView avatar_imag;
    @ViewInject(R.id.progress)
    private ProgressBar progress;
    @ViewInject(R.id.back)
    private ImageView back;



    private List<String> image = new ArrayList<>();//从选择器得到的头像
    private List<String> compressImages = new ArrayList<>();//压缩完的头像


    private String provincename_get;//
    private String cityname_get;//从用户详细信息获取的省，市，区名
    private String countyname_get;//





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
                        //在个人信息里获得用户上次写过的详细信息//
                        if (!Avatar.equals("")) {
                            Glide.with(InfoActivity.this).load(Avatar).into(avatar_imag);
                        }
                        if (Gentle == 1) {
                            sex_text.setText("男");
                        }
                        if (Gentle == 2) {
                            sex_text.setText("女");
                        }
                        job_text.setText(Job);
                        salary_text.setText(String.valueOf(Salary));
                        birthday_text.setText(Birthday.substring(0, 10));
                        if (Parentsalive) {
                            parent_text.setText("在世");
                        }
                        if (!Parentsalive) {
                            parent_text.setText("去世");
                        }
                        if (Maritallstatus.equals("未婚")) {
                            marriage_text.setText("未婚");
                        }
                        if (Maritallstatus.equals("已婚")) {
                            marriage_text.setText("已婚");
                        }
                        if (HaveKids) {
                            kid_text.setText("是");
                        }
                        if (!HaveKids) {
                            kid_text.setText("否");
                        }


                        //根据得到的地区代码，返回省，城市，区，然后sp.setSelection(arrayAdapter.getPosition("广东")设置默认值//
                        if (Area != 0) {
                            RequestParams params1 = new RequestParams();
                            params1.addBodyParameter("sessionid", sessionId);
                            params1.addBodyParameter("areaid", Area + "");
                            new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/search/area/id", params1, new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    try {
                                        JSONObject object = new JSONObject(responseInfo.result.toString());
                                        int status = object.getInt("Status");
                                        if (status == 1000) {
                                            Gson gson = new Gson();
                                            AreaData area = gson.fromJson(object.getJSONObject("Result").toString(), AreaData.class);
                                            provincename_get = area.getProvince();
                                            cityname_get = area.getCountry();
                                            countyname_get = area.getCounty();
                                            address_text.setText(provincename_get + cityname_get + countyname_get);

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(HttpException error, String msg) {
                                    Toast.makeText(InfoActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }


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


    @OnClick({R.id.back, R.id.avatar, R.id.sex, R.id.job, R.id.salary, R.id.birthday, R.id.address, R.id.parent, R.id.marriage, R.id.kid, R.id.emotion, R.id.hobby, R.id.select, R.id.note})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(InfoActivity.this, MainActivity.class));
                break;
            case R.id.avatar:
                ImageSelectorUtils.openPhoto(InfoActivity.this, 1, true, 0);
                break;
            case R.id.sex:
                Intent intent8 = new Intent(InfoActivity.this, SexActivity.class);
                intent8.putExtra("sex", Gentle + "");
                startActivity(intent8);
                break;
            case R.id.job:
                Intent intent1 = new Intent(InfoActivity.this, JobActivity.class);
                intent1.putExtra("job", Job);
                startActivity(intent1);
                break;
            case R.id.salary:
                Intent intent2 = new Intent(InfoActivity.this, SalaryActivity.class);
                intent2.putExtra("salary", Salary + "");
                startActivity(intent2);
                break;
            case R.id.birthday:
                Intent intent3 = new Intent(InfoActivity.this, BirthActivity.class);
                intent3.putExtra("birth", Birthday.substring(0, 10));
                startActivity(intent3);
                break;
            case R.id.address:
                Intent intent4 = new Intent(InfoActivity.this, AreaActivity.class);
                intent4.putExtra("area", Area + "");
                startActivity(intent4);
                break;
            case R.id.parent:
                Intent intent5 = new Intent(InfoActivity.this, ParentActivity.class);
                intent5.putExtra("parent", Parentsalive);
                startActivity(intent5);
                break;
            case R.id.marriage:
                Intent intent6 = new Intent(InfoActivity.this, MarriageActivity.class);
                intent6.putExtra("marriage", Maritallstatus);
                startActivity(intent6);
                break;
            case R.id.kid:
                Intent intent7 = new Intent(InfoActivity.this, KidActivity.class);
                intent7.putExtra("kid", HaveKids);
                startActivity(intent7);
                break;
            case R.id.emotion:
                Intent intent9 = new Intent(InfoActivity.this, EditEmoExprienceActivity.class);
                intent9.putExtra("emotion", Emotion);
                startActivity(intent9);
                break;

            case R.id.hobby:
                Intent intent10 = new Intent(InfoActivity.this, EditHobbyActivity.class);
                intent10.putExtra("hobby", Hobby);
                startActivity(intent10);
                break;
            case R.id.select:
                Intent intent11 = new Intent(InfoActivity.this, EditMateSignActivity.class);
                intent11.putExtra("select", Requir);
                startActivity(intent11);
                break;
            case R.id.note:
                Intent intent12 = new Intent(InfoActivity.this, EditNoteActivity.class);
                intent12.putExtra("note", Autograph);
                startActivity(intent12);
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
