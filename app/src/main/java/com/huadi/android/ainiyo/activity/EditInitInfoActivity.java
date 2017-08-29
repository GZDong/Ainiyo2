package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.AreaData;
import com.huadi.android.ainiyo.entity.City;
import com.huadi.android.ainiyo.entity.County;
import com.huadi.android.ainiyo.entity.Photo;
import com.huadi.android.ainiyo.entity.Province;
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
import com.lidroid.xutils.view.annotation.event.OnCheckedChange;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.huadi.android.ainiyo.application.ECApplication.sessionId;

public class EditInitInfoActivity extends AppCompatActivity implements LGImgCompressor.CompressListener {
    @ViewInject(R.id.radio_group)
    private RadioGroup radioGroup;
    @ViewInject(R.id.long_line)
    private View long_line;
    @ViewInject(R.id.short_line)
    private View short_line;
    @ViewInject(R.id.base)
    private RadioButton base;
    @ViewInject(R.id.emotion)
    private RadioButton emotion;
    @ViewInject(R.id.info_layout)
    private LinearLayout info_layout;
    @ViewInject(R.id.emotion_layout)
    private LinearLayout emotion_layout;
    @ViewInject(R.id.edit_avatar)
    private CircleImageView edit_avatar;
    @ViewInject(R.id.edit_job)
    private EditText edit_job;
    @ViewInject(R.id.edit_salary)
    private EditText edit_salary;
    @ViewInject(R.id.edit_birthday)
    private EditText edit_birthday;
    @ViewInject(R.id.edit_parentsalive1)
    private RadioButton edit_parentsalive1;
    @ViewInject(R.id.edit_parentsalive2)
    private RadioButton edit_parentsalive2;
    @ViewInject(R.id.edit_maritallstatus1)
    private RadioButton edit_maritallstatus1;
    @ViewInject(R.id.edit_maritallstatus2)
    private RadioButton edit_maritallstatus2;
    @ViewInject(R.id.edit_havekids1)
    private RadioButton edit_havekids1;
    @ViewInject(R.id.edit_havekids2)
    private RadioButton edit_havekids2;
    @ViewInject(R.id.edit_emotion)
    private EditText edit_emotion;
    @ViewInject(R.id.edit_hobby)
    private EditText edit_hobby;
    @ViewInject(R.id.edit_requir)
    private EditText edit_requir;
    @ViewInject(R.id.save)
    private TextView save;
    @ViewInject(R.id.provinceSpinner)
    private Spinner provinceSpinner;
    @ViewInject(R.id.citySpinner)
    private Spinner citySpinner;
    @ViewInject(R.id.countySpinner)
    private Spinner countySpinner;
    @ViewInject(R.id.change)
    private TextView change;
    @ViewInject(R.id.address)
    private TextView address;



    @ViewInject(R.id.progress)
    private ProgressBar progress;



    private String provincename_get;//
    private String cityname_get;//从用户详细信息获取的省，市，区名
    private String countyname_get;//


    private ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    private ArrayAdapter<String> cityAdapter = null;    //地级适配器
    private ArrayAdapter<String> countyAdapter = null;    //县级适配器
    private List<String> province = new ArrayList<>();
    private List<String> city = new ArrayList<>();
    private List<String> county = new ArrayList<>();

    private List<Province> provinces;//省对象
    private List<City> citys;//城市对象
    private List<County> countys;//区对象

    private int provinceId = 0;//
    private int cityId = 0;//地址ID
    private int countyId;//
    //用户信息//
    private int Id;
    private boolean Vip;
    private String Birthday;
    private int Area;
    private String Job;
    private double Salary = 1.111111;
    private boolean HaveKids;
    private boolean Parentsalive = true;
    private String Maritallstatus;
    private String Emotion;
    private String Hobby;
    private String Requir;
    private String Avatar;
    private int Userid;


    private List<String> image=new ArrayList<>();//从选择器得到的头像//
    private List<String> compressImages=new ArrayList<>();
    private String avatar_done;//上传完成的头像//

    private Animation move_to_left, move_to_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_init_info);
        ViewUtils.inject(this);
        LGImgCompressor.getInstance(this).withListener(this);
        //province下拉框选择
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 表示选项被改变的时候触发此方法
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //position为当前省级选中的值的序号,得到当前选中的provinceId
                provinceAdapter.notifyDataSetChanged();
                provinceId = provinces.get(position).getProvince();
                getallcity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        //下拉框选择
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 表示选项被改变的时候触发此方法
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //position为当前省级选中的值的序号,得到当前选中的Id
                cityAdapter.notifyDataSetChanged();
                cityId = citys.get(position).getCountry();
                getallcounty();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        //下拉框选择
        countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 表示选项被改变的时候触发此方法
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //position为当前省级选中的值的序号,得到当前选中的Id
                countyAdapter.notifyDataSetChanged();
                countyId = countys.get(position).getCounty();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        move_to_left = AnimationUtils.loadAnimation(this, R.anim.move_to_left);
        move_to_right = AnimationUtils.loadAnimation(this, R.anim.move_to_right);
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
                                if (!Avatar.equals("")) {
                                    Glide.with(EditInitInfoActivity.this).load(Avatar).into(edit_avatar);
                                }
                                edit_job.setText(Job);
                                if (Salary != 1.111111) {
                                    edit_salary.setText(String.valueOf(Salary));
                                }
                                if (!Birthday.equals("")) {
                                    edit_birthday.setText(Birthday.substring(0, 10));
                                }
                                //根据得到的地区代码，返回省，城市，区，然后sp.setSelection(arrayAdapter.getPosition("广东")设置默认值//
                                if(Area!=0){
                                    RequestParams params = new RequestParams();
                                    params.addBodyParameter("sessionid", sessionId);
                                    params.addBodyParameter("areaid",Area+"");
                                    new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/search/area/id", params, new RequestCallBack<String>() {
                                        @Override
                                        public void onSuccess(ResponseInfo<String> responseInfo) {
                                            try {
                                                JSONObject object = new JSONObject(responseInfo.result.toString());
                                                int status = object.getInt("Status");
                                                if (status == 1000) {
                                                    Gson gson = new Gson();
                                                    AreaData area=gson.fromJson(object.getJSONObject("Result").toString(),AreaData.class);
                                                    provinceId=area.getProvinceid();   //
                                                    cityId=area.getCountryid();//初始化成用户上次保存的ID
                                                    countyId=area.getCountyid();//
                                                    provincename_get=area.getProvince();
                                                    cityname_get= area.getCountry();
                                                    countyname_get= area.getCounty();
                                                    address.setText(provincename_get+cityname_get+countyname_get);

                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(HttpException error, String msg) {
                                            Toast.makeText(EditInitInfoActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }



                                if (Parentsalive) {
                                    edit_parentsalive1.setChecked(true);
                                }
                                if (!Parentsalive) {
                                    edit_parentsalive2.setChecked(true);
                                }
                                //判断是否空对象//
                                if (!Maritallstatus.equals("")) {
                                    if (Maritallstatus.equals("未婚")) {
                                        edit_maritallstatus1.setChecked(true);
                                    } else {
                                        edit_maritallstatus2.setChecked(true);
                                    }
                                } else {
                                    edit_maritallstatus1.setChecked(true);
                                }


                                if (!HaveKids) {
                                    edit_havekids1.setChecked(true);
                                }
                                if (HaveKids) {
                                    edit_havekids2.setChecked(true);
                                }


                                edit_emotion.setText(Emotion);
                                edit_hobby.setText(Hobby);
                                edit_requir.setText(Requir);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }


                }
        );


        getallprovince();



    }

    //基本信息和情感经历RadioButton的选择//
    @OnCheckedChange({R.id.radio_group})
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.base:
                short_line.startAnimation(move_to_left);
                info_layout.setVisibility(View.VISIBLE);
                emotion_layout.setVisibility(View.GONE);
                break;
            case R.id.emotion:
                short_line.startAnimation(move_to_right);
                info_layout.setVisibility(View.GONE);
                emotion_layout.setVisibility(View.VISIBLE);
                break;
        }
    }


    //事件监听//
    @OnClick({R.id.back, R.id.save, R.id.edit_avatar,R.id.change})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change:
                address.setVisibility(View.GONE);
                change.setVisibility(View.GONE);
                provinceSpinner.setVisibility(View.VISIBLE);
                citySpinner.setVisibility(View.VISIBLE);
                countySpinner.setVisibility(View.VISIBLE);
                break;
            case R.id.edit_avatar:
                ImageSelectorUtils.openPhoto(EditInitInfoActivity.this, 1, true, 0);
                break;
            case R.id.save:
                //判断是否已经完善信息//
                if (edit_birthday.getText().toString().trim().length() <= 0) {
                    Toast.makeText(EditInitInfoActivity.this, "没有完善信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edit_job.getText().toString().trim().length() <= 0) {
                    Toast.makeText(EditInitInfoActivity.this, "没有完善信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edit_salary.getText().toString().trim().length() <= 0) {
                    Toast.makeText(EditInitInfoActivity.this, "没有完善信息", Toast.LENGTH_SHORT).show();
                    return;
                }


                //保存个人信息//
                RequestParams params = new RequestParams();
                params.addHeader("sessionid", sessionId);
                params.addBodyParameter("sessionid", sessionId);
                params.addBodyParameter("birthday", edit_birthday.getText().toString());
                params.addBodyParameter("job", edit_job.getText().toString());
                params.addBodyParameter("province", provinceId + "");
                params.addBodyParameter("country", cityId + "");
                params.addBodyParameter("county", countyId + "");
                params.addBodyParameter("salary", edit_salary.getText().toString());

                if (edit_havekids1.isChecked()) {
                    params.addBodyParameter("havekids", "0");
                } else {
                    params.addBodyParameter("havekids", "1");
                }

                if (edit_parentsalive1.isChecked()) {
                    params.addBodyParameter("parentsalive", "1");
                } else {
                    params.addBodyParameter("parentsalive", "0");
                }

                if (edit_maritallstatus1.isChecked()) {
                    params.addBodyParameter("maritallstatus", "未婚");
                } else {
                    params.addBodyParameter("maritallstatus", "已婚");
                }


                if (edit_emotion.getText().toString().trim().length() > 0) {
                    params.addBodyParameter("emotion", edit_emotion.getText().toString());
                }
                if (edit_hobby.toString().trim().length() > 0) {
                    params.addBodyParameter("hobby", edit_hobby.getText().toString());
                }
                if (edit_requir.toString().trim().length() > 0) {
                    params.addBodyParameter("requir", edit_requir.getText().toString());
                }
                //如果没有修改头像,则头像参数就用原来的//

                if (avatar_done!=null) {
                    params.addBodyParameter("avatar", avatar_done);
                }
                if(avatar_done==null&&Avatar!=null){
                    params.addBodyParameter("avatar", Avatar);
                }
                HttpUtils http = new HttpUtils();
                http.send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/perfectinfor",params, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInFo) {
                                String info = responseInFo.result.toString();
                                try {
                                    JSONObject object = new JSONObject(info);
                                    String msg = object.getString("Msg");
                                    Toast.makeText(EditInitInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditInitInfoActivity.this,MainActivity.class));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                                Toast.makeText(EditInitInfoActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }


                        }
                );

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
            LGImgCompressor.getInstance(EditInitInfoActivity.this).starCompressWithDefault(srcImgUrl);
        }

    }

    @Override
    public void onCompressStart() {
    }

    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult imageOutPath) {
        compressImages.add(imageOutPath.getOutPath());

        if(Avatar==null&&avatar_done==null) {
            progress.setVisibility(View.VISIBLE);
            modifyImage(compressImages); //修改头像
        }
        else { progress.setVisibility(View.VISIBLE);sendImage(compressImages);  }
    }




    public void sendImage(final List<String> images) {
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
                    if (msg.equals("success")) {
                        progress.setVisibility(View.GONE);
                        avatar_done = result;
                        Glide.with(EditInitInfoActivity.this).load(result).into(edit_avatar);
                    } else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(EditInitInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progress.setVisibility(View.GONE);
                Toast.makeText(EditInitInfoActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

            }
        });

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
                    if (msg.equals("success")) {
                        progress.setVisibility(View.GONE);
                        avatar_done = result;
                        Glide.with(EditInitInfoActivity.this).load(result).into(edit_avatar);
                    } else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(EditInitInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progress.setVisibility(View.GONE);
                Toast.makeText(EditInitInfoActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

            }
        });

    }



    //获得省数组//
    public void getallprovince() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionid", sessionId);
        new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/search/area/province", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject object = new JSONObject(responseInfo.result.toString());
                    int status = object.getInt("Status");
                    if (status == 0) {
                        Gson gson = new Gson();
                        provinces = gson.fromJson(object.getJSONArray("Result").toString(), new TypeToken<List<Province>>() {
                        }.getType());
                        province.clear();
                        for (int i = 0; i < provinces.size(); i++) {
                            province.add(provinces.get(i).getName());
                            if (province.size() == provinces.size()) {
                                //设置province适配器
                                provinceAdapter = new ArrayAdapter<String>(EditInitInfoActivity.this,
                                        android.R.layout.simple_spinner_item, province);
                                provinceSpinner.setAdapter(provinceAdapter);

                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(EditInitInfoActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

            }
        });


    }

    //获得城市数组//
    public void getallcity() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionid", sessionId);
        params.addBodyParameter("province", provinceId + "");
        new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/search/area/country", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject object = new JSONObject(responseInfo.result.toString());
                    int status = object.getInt("Status");
                    if (status == 0) {
                        Gson gson = new Gson();
                        citys = gson.fromJson(object.getJSONArray("Result").toString(), new TypeToken<List<City>>() {
                        }.getType());
                        city.clear();
                        for (int i = 0; i < citys.size(); i++) {
                            city.add(citys.get(i).getName());
                            if (city.size() == citys.size()) {
                                //设置city适配器
                                cityAdapter = new ArrayAdapter<String>(EditInitInfoActivity.this,
                                        android.R.layout.simple_spinner_item, city);
                                citySpinner.setAdapter(cityAdapter);
                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(EditInitInfoActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

            }
        });


    }

    //获得区数组//
    public void getallcounty() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionid", sessionId);
        params.addBodyParameter("province", provinceId + "");
        params.addBodyParameter("country", cityId + "");
        new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/search/area/county", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject object = new JSONObject(responseInfo.result.toString());
                    int status = object.getInt("Status");
                    if (status == 0) {
                        Gson gson = new Gson();
                        countys = gson.fromJson(object.getJSONArray("Result").toString(), new TypeToken<List<County>>() {
                        }.getType());
                        county.clear();
                        for (int i = 0; i < countys.size(); i++) {
                            county.add(countys.get(i).getName());
                            if (county.size() == countys.size()) {
                                //设置county适配器
                                countyAdapter = new ArrayAdapter<String>(EditInitInfoActivity.this,
                                        android.R.layout.simple_spinner_item, county);
                                countySpinner.setAdapter(countyAdapter);
                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(EditInitInfoActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

            }
        });


    }


}
