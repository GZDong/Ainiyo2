package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.adapter.ImageAdapter;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.huadi.android.ainiyo.entity.ResponseObject;
import com.huadi.android.ainiyo.util.CONST;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.util.ArrayList;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class ModeAddingActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0x00000011;

    private RecyclerView rvImage;
    private ImageAdapter mAdapter;
    private Button btn_limit;
    private int requestCode;
    private ArrayList<String> images;
    private ArrayList<String> imagesUrl = new ArrayList<String>();
    @ViewInject(R.id.et_mode_add_saying)
    EditText et_mode_add_saying;
    public static final int UPDATE_TEXT = 1;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_adding);

        ViewUtils.inject(this);

        rvImage = (RecyclerView) findViewById(R.id.rv_image);
        rvImage.setLayoutManager(new GridLayoutManager(this, 3));

        mAdapter = new ImageAdapter(this);
        rvImage.setAdapter(mAdapter);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            images = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            mAdapter.refresh(images);
        }
    }


    @OnClick({R.id.mode_adding_back,R.id.mode_add_pic,R.id.tv_mode_add})
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.mode_adding_back:
                ToolKits.putInt(this,"fragment",2);
                startActivity(new Intent(ModeAddingActivity.this,MainActivity.class));
                break;
            case R.id.mode_add_pic:
                ImageSelectorUtils.openPhoto(ModeAddingActivity.this, REQUEST_CODE);
                break;
            case R.id.tv_mode_add:
                ToolKits.putInt(this,"fragment",2);
//                Intent t1=new Intent();
//
//                ModeInfo mi = new ModeInfo(null, et_mode_add_saying.getText().toString(), null, images);
//                ArrayList<ModeInfo> list1 = ToolKits.GettingModedata(ModeAddingActivity.this, "modeMeInfoList");
//                list1.add(0, mi);
//                ToolKits.SavingModeData(ModeAddingActivity.this, "modeMeInfoList", list1);
//                ArrayList<ModeInfo> list2 = ToolKits.GettingModedata(ModeAddingActivity.this, "modeInfoList");
//                list2.add(0, mi);
//                ToolKits.SavingModeData(ModeAddingActivity.this, "modeInfoList", list2);

                upLoadPho();
                //Toast.makeText(ModeAddingActivity.this,String.valueOf(imagesUrl.size()),Toast.LENGTH_SHORT).show();



                finish();
                break;

        }
    }

    //上传照片
    public void upLoadPho() {
        int i;
        for (i = 0; i < images.size(); i++) {

            Luban.with(this).load(new File(images.get(i))).setCompressListener(new OnCompressListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(File file) {
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("sessionid", CONST.SESSIONID);
                    params.addBodyParameter("photo", file);
                    new HttpUtils().send(HttpRequest.HttpMethod.POST, CONST.UPLOAD_PHOTO, params, new RequestCallBack<String>() {

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            ResponseObject<String> object = new GsonBuilder().create().
                                    fromJson(responseInfo.result, new TypeToken<ResponseObject<String>>() {
                                    }.getType());
                            String s = object.getResult();
                            //Toast.makeText(ModeAddingActivity.this, "result:" + s + "Status:" + object.getStatus() + "Msg" + object.getMsg(), Toast.LENGTH_SHORT).show();
                            imagesUrl.add(s);

                            //Toast.makeText(ModeAddingActivity.this,String.valueOf(imagesUrl.size()),Toast.LENGTH_SHORT).show();
                            Toast.makeText(ModeAddingActivity.this, "imagesUrl.size" + String.valueOf(imagesUrl.size()) + "   images.size()" + String.valueOf(images.size()), Toast.LENGTH_SHORT).show();
                            if (imagesUrl.size() == images.size()) {
                                upLoadContent(imagesUrl);
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(ModeAddingActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(Throwable e) {

                }
            }).launch();


        }
        // Toast.makeText(ModeAddingActivity.this,String.valueOf(imagesUrl.size()),Toast.LENGTH_SHORT).show();

    }

    //上传心情信息
    public void upLoadContent(ArrayList<String> mList) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionid", CONST.SESSIONID);
        //Toast.makeText(ModeAddingActivity.this,String.valueOf(imagesUrl.size()),Toast.LENGTH_SHORT).show();
        ModeInfo mi = new ModeInfo(null, et_mode_add_saying.getText().toString(), null, mList);
        Gson gson = new Gson();
        String json = gson.toJson(mi);
        params.addBodyParameter("content", json);
        new HttpUtils().send(HttpRequest.HttpMethod.POST, CONST.PUBLISH_MODE, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ResponseObject<String> object = new GsonBuilder().create().
                        fromJson(responseInfo.result, new TypeToken<ResponseObject<String>>() {
                        }.getType());
                Toast.makeText(ModeAddingActivity.this, "success?" + object.getStatus(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(ModeAddingActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    //按返回键时回到相应的fragment
    @Override
    public void onBackPressed()
    {
        ToolKits.putInt(this,"fragment",2);
        super.onBackPressed();
    }


//    public void SavingModeData(ArrayList<ModeInfo> list)
//    {
//        List<ModeInfo> modeInfoList = list;
//        SharedPreferences.Editor editor = getSharedPreferences("modeInfoList", MODE_PRIVATE).edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(modeInfoList);
//        Log.d("hello", "saved json is "+ json);
//        editor.putString("modeInfoList", json);
//        editor.commit();
//    }
//
//    public ArrayList<ModeInfo> GettingModedata()
//    {
//        ArrayList<ModeInfo> list= new ArrayList<ModeInfo>();
//        SharedPreferences preferences = getSharedPreferences("modeInfoList", MODE_PRIVATE);
//        String json = preferences.getString("modeInfoList", null);
//        if (json != null)
//        {
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<ModeInfo>>(){}.getType();
//            list = gson.fromJson(json, type);
////            for(int i = 0; i < list.size(); i++)
////            {
////                Log.d("hi", list.get(i).getName()+":" + list.get(i).getX() + "," + list.get(i).getY());
////            }
//        }
//        return list;
//    }
}
