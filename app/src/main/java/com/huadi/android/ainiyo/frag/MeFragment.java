package com.huadi.android.ainiyo.frag;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.ActivityActivity;
import com.huadi.android.ainiyo.activity.InfoActivity;
import com.huadi.android.ainiyo.activity.LoginActivity;
import com.huadi.android.ainiyo.activity.ManagerActivity;
import com.huadi.android.ainiyo.activity.PhotoActivity;
import com.huadi.android.ainiyo.activity.VipHintActivity;
import com.huadi.android.ainiyo.activity.VipLeverActivity;
import com.huadi.android.ainiyo.activity.VipResponActivity;
import com.huadi.android.ainiyo.entity.UserData;
import com.huadi.android.ainiyo.util.SignInUtil;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.huadi.android.ainiyo.application.ECApplication.sessionId;

public class MeFragment extends Fragment{
    @ViewInject(R.id.info)
    private LinearLayout info;
    @ViewInject(R.id.logoff)
    private LinearLayout logoff;
    @ViewInject(R.id.xiangce)
    private LinearLayout xiangce;
    @ViewInject(R.id.manager)
    private LinearLayout manager;
    @ViewInject(R.id.vipapply)
    private LinearLayout vipapply;
    @ViewInject(R.id.vip_lever)
    private LinearLayout vip_lever;
    @ViewInject(R.id.activity)
    private LinearLayout activity;



    private TextView te;
    private TextView job_text;
    private TextView vip_text;
    private CircleImageView avatar_imag;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_me,null);
        // Inflate the layout for this fragment
        ViewUtils.inject(this,view);
        SharedPreferences pref = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String username = pref.getString("name", "");
        te = (TextView) view.findViewById(R.id.name);
        te.setText(username);//获取用户名//


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
                            if (msg.equals("success")) {
                            Gson gson = new Gson();
                            UserData userData = gson.fromJson(object.getJSONObject("Result").toString(), UserData.class);

                            //如果获取数据成功，则把数据加载到各项
                                String job = userData.getJob();
                                Boolean vip = userData.isVip();
                                String image = userData.getAvatar();
                                job_text = (TextView) getActivity().findViewById(R.id.job_text);
                                job_text.setText(job);
                                avatar_imag = (CircleImageView) getActivity().findViewById(R.id.avatar_imag);
                                if (!image.equals("")) {
                                    Glide.with(getActivity()).load(image).into(avatar_imag);
                                }
                                if (vip) {
                                    vip_text = (TextView) getActivity().findViewById(R.id.vip_text);
                                    vip_text.setText("VIP用户");
                                }
                                if (!vip) {
                                    vip_text = (TextView) getActivity().findViewById(R.id.vip_text);
                                    vip_text.setText("普通用户");
                                }


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
        return view;
    }

    @OnClick({R.id.info, R.id.logoff, R.id.xiangce, R.id.vipapply, R.id.vip_lever, R.id.manager,R.id.activity})
   public void OnClick(View v){
       switch (v.getId()){
           case R.id.info:
               startActivityForResult(new Intent(getActivity(),InfoActivity.class),1);
               break;
           case R.id.activity:
               startActivity(new Intent(getActivity(), ActivityActivity.class));
               break;
           case R.id.manager:
               startActivity(new Intent(getActivity(), ManagerActivity.class));
               break;
           case R.id.vip_lever:
               startActivity(new Intent(getActivity(), VipLeverActivity.class));
               break;
           case R.id.xiangce:
               startActivity(new Intent(getActivity(),PhotoActivity.class));
               break;
           case R.id.vipapply:
               RequestParams params = new RequestParams();
               params.addBodyParameter("sessionid", sessionId);
               new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/vipcheck", params, new RequestCallBack<String>() {
                   @Override
                   public void onSuccess(ResponseInfo<String> responseInfo) {
                       try {
                           JSONObject object = new JSONObject(responseInfo.result.toString());
                           int status = object.getInt("Status");
                           String result = object.getString("Result");
                           String msg = object.getString("Msg");
                           if (status == 0) {
                               startActivity(new Intent(getActivity(), VipHintActivity.class));
                           }
                           if (status == 99 || status == 20001 || status == 20002 || status == 20003) {
                               Intent intent = new Intent(getActivity(), VipResponActivity.class);
                               intent.putExtra("text", msg);
                               startActivity(intent);

                           }

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }

                   @Override
                   public void onFailure(HttpException error, String msg) {
                       Toast.makeText(getActivity(), "连接错误", Toast.LENGTH_SHORT).show();
                   }
               });
               break;
           case R.id.logoff:
               AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
               dialog.setTitle("提示");
               dialog.setMessage("是否退出登录？");
               dialog.setCancelable(false);
               dialog.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", MODE_PRIVATE).edit();
                       editor.putBoolean("islogin", false);
                       editor.apply();
                       SignInUtil.signOut();
                       startActivity(new Intent(getActivity(), LoginActivity.class));
                       Toast.makeText(getActivity(), "你已退出登录", Toast.LENGTH_LONG).show();
                       getActivity().finish();
                   }
               });
               dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                   }
               });
               dialog.show();
       }
   }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

}
