package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.City;
import com.huadi.android.ainiyo.entity.County;
import com.huadi.android.ainiyo.entity.Province;
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

import java.util.ArrayList;
import java.util.List;

import static com.huadi.android.ainiyo.application.ECApplication.sessionId;

public class AreaActivity extends AppCompatActivity {

    private ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    private ArrayAdapter<String> cityAdapter = null;    //地级适配器
    private ArrayAdapter<String> countyAdapter = null;    //县级适配器
    private List<String> province = new ArrayList<>();
    private List<String> city = new ArrayList<>();
    private List<String> county = new ArrayList<>();

    @ViewInject(R.id.back)
    private ImageView back;
    @ViewInject(R.id.save)
    private TextView save;
    @ViewInject(R.id.provinceSpinner)
    private Spinner provinceSpinner;
    @ViewInject(R.id.citySpinner)
    private Spinner citySpinner;
    @ViewInject(R.id.countySpinner)
    private Spinner countySpinner;

    private List<Province> provinces;//省对象
    private List<City> citys;//城市对象
    private List<County> countys;//区对象

    private int provinceId = 0;//
    private int cityId = 0;//地址ID
    private int countyId;//




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);
        ViewUtils.inject(this);







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
        getallprovince();

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
                                provinceAdapter = new ArrayAdapter<String>(AreaActivity.this,
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
                Toast.makeText(AreaActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

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
                                cityAdapter = new ArrayAdapter<String>(AreaActivity.this,
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
                Toast.makeText(AreaActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

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
                                countyAdapter = new ArrayAdapter<String>(AreaActivity.this,
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
                Toast.makeText(AreaActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

            }
        });


    }
    @OnClick({R.id.back,R.id.save})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.save:



                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionid", sessionId);
                params.addBodyParameter("area", (provinceId+"")+(cityId+"")+(countyId+""));
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifyarea", params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");

                            String result = object.getString("Result");
                            String msg = object.getString("Msg");

                            startActivity(new Intent(AreaActivity.this,InfoActivity.class));
                                Toast.makeText(AreaActivity.this, msg, Toast.LENGTH_SHORT).show();

                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(AreaActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });

                break;
        }
    }



}
