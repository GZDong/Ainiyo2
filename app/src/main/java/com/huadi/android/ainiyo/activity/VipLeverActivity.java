package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.UserData;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static com.huadi.android.ainiyo.application.ECApplication.sessionId;

public class VipLeverActivity extends AppCompatActivity {
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_lever);
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

                            //如果获取数据成功，获得数据//
                            if (msg.equals("success")) {
                                if(userData.isVip()){
                                    text="VIP用户";
                                    TextView vip=(TextView)findViewById(R.id.vip);
                                    vip.setText(text);
                                }
                                if(!userData.isVip()){
                                    text="普通用户";
                                    TextView vip=(TextView)findViewById(R.id.vip);
                                    vip.setText(text);
                                }
                            } else {
                                Toast.makeText(VipLeverActivity.this, msg, Toast.LENGTH_SHORT).show();
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
        ImageView back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(VipLeverActivity.this, MainActivity.class));
            }
        });

    }
}
