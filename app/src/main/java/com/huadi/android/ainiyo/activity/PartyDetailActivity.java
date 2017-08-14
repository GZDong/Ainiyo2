package com.huadi.android.ainiyo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huadi.android.ainiyo.R;

import com.huadi.android.ainiyo.Retrofit2.GetRequest_party;
import com.lidroid.xutils.view.annotation.ViewInject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rxvincent on 2017/8/14.
 */

public class PartyDetailActivity extends AppCompatActivity {
    @ViewInject(R.id.partyTitleView)
    TextView title;
    @ViewInject(R.id.partyDateView)
    TextView date;
    @ViewInject(R.id.partyImage)
    ImageView image;
    @ViewInject(R.id.partyMainText)
    TextView main;

    private class PartyInformation {
        String title;
        String date;
        String picture;
        String article;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        getIntent().getStringExtra("id");
    }

    private void LoadAndShow(String id) {
        Retrofit caller = new Retrofit.Builder().baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_party request = caller.create(GetRequest_party.class);
        Call<ResponseBody> call = request.getPartyInformation(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override//同步中，待修改为异步
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    PartyInformation information = gson.fromJson(response.body().string(), PartyInformation.class);
                    title.setText(information.title);
                    date.setText(information.date);
                    main.setText(information.article);
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
