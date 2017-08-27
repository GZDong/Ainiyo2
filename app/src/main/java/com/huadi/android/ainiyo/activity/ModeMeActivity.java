package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.adapter.ModeAdapter;
import com.huadi.android.ainiyo.adapter.ModeMeAdapter;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.huadi.android.ainiyo.entity.ModeLocalData;
import com.huadi.android.ainiyo.entity.ModeResult;
import com.huadi.android.ainiyo.entity.ModeWebData;
import com.huadi.android.ainiyo.entity.ResponseObject;
import com.huadi.android.ainiyo.entity.UserInfoLab;
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
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.huadi.android.ainiyo.util.CONST.RETURN_MODE;
import static com.huadi.android.ainiyo.util.CONST.SESSIONID;

public class ModeMeActivity extends AppCompatActivity {

    @ViewInject(R.id.mode_me_list_view)
    private PullToRefreshListView mode_me_list_view;
    @ViewInject(R.id.btn_mode_me_add)
    private ImageView btn_mode_add;
    private ModeMeAdapter mAdapter;
    private static final int REQUEST_CODE = 0x00000012;
    private ArrayList<String> return_images = new ArrayList<String>();

    private List<ModeLocalData> mList = new ArrayList<>();
    private ModeResult modeResult;
    private ModeWebData[] mwd;

    private int page = 1;
    private int pagesize = 20;
    private int pagecount = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_me);
        ViewUtils.inject(this);

        setImmersive();

        // Set a listener to be invoked when the list should be refreshed.
        mode_me_list_view.setMode(PullToRefreshBase.Mode.BOTH);
        mode_me_list_view.setScrollingWhileRefreshingEnabled(true);
        mode_me_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(ModeMeActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                loadDatas(refreshView.getScrollY() < 0,mList);
            }
        });
        // 首次自动加载数据
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                mode_me_list_view.setRefreshing();
                return true;
            }
        }).sendEmptyMessageDelayed(0, 200);
    }

    private void loadDatas(final boolean direction, final List<ModeLocalData> mList)
    {

        RequestParams params = new RequestParams();
        if (!direction) {// 如果是尾部刷新要重新计算分页数据
            page++;
        } else {
            page = 1;
        }

        ECApplication application = (ECApplication) getApplication();
        params.addBodyParameter("sessionid", application.sessionId);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("pagesize", "3");
        params.addBodyParameter("type", "1");

        new HttpUtils().send(HttpRequest.HttpMethod.POST, RETURN_MODE, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                mode_me_list_view.onRefreshComplete();
                ResponseObject<ModeResult> object = new GsonBuilder().create().
                        fromJson(responseInfo.result, new TypeToken<ResponseObject<ModeResult>>() {
                        }.getType());

                if (object.getStatus() == 400) {
                if (direction)// 头部刷新
                {// 渲染内容到界面上
                    //清空原来的数据
                    mList.clear();
                    ArrayList<Integer> idorder = new ArrayList<Integer>();

                    mwd = object.getResult().getData();
                    int sum = object.getResult().getSum();
                    ModeWebData mwd1;
                    for (int i = 0; i < sum; i++) {
                        mwd1 = mwd[i];

                        idorder.add(mwd1.getId());
                        ToolKits.putInteger(ModeMeActivity.this, "Integer", idorder);


                        int userid = mwd1.getUserid();
                        String content = mwd1.getContent();
                        Gson gson = new Gson();
                        Type type = new TypeToken<ModeInfo>() {
                        }.getType();
                        ModeInfo mi;
                        mi = gson.fromJson(mwd1.getContent(), type);
                        ModeLocalData mld = new ModeLocalData(mwd1.getId(), userid, mi, mwd1.getDate(), sum);

                        mList.add(mld);
                    }

//                    Toast.makeText(getActivity(),
//                            "content=" + content + ",userid=" + String.valueOf(userid)
//                                    + ",msg=" + object.getMsg() + ",Status=" + object.getStatus(),
//                            Toast.LENGTH_SHORT).show();

//                    Toast.makeText(getActivity(),
//                            "imageUrL:  "+mi.getImgUrlforContent().size(),
//                            Toast.LENGTH_SHORT).show();

                    //mList= ToolKits.GettingModedata(getActivity(),"modeInfoList");
                    mAdapter = new ModeMeAdapter(ModeMeActivity.this, mList);
                    mode_me_list_view.setAdapter(mAdapter);

                    //删除按钮监听
                    mAdapter.setOnDeleteItemClickListener(new ModeMeAdapter.OnDeleteItemClickListener() {
                        @Override
                        public void OnDeleteItemClick(int position) {
                            DeletingTheMode(ToolKits.fetchInteger(ModeMeActivity.this, "Integer").get(position));

                        }
                    });


                } else {// 尾部刷新
                    //mList.addAll(object.getDatas());
                    ArrayList<Integer> idorder = new ArrayList<Integer>();
                    mwd = object.getResult().getData();
                    int sum = object.getResult().getSum();
                    ModeWebData mwd1;
                    for (int i = 0; i < sum; i++) {
                        mwd1 = mwd[i];

                        idorder.add(mwd1.getId());
                        ToolKits.appendInteger(ModeMeActivity.this, "Integer", idorder);


                        int userid = mwd1.getUserid();
                        String content = mwd1.getContent();
                        Gson gson = new Gson();
                        Type type = new TypeToken<ModeInfo>() {
                        }.getType();
                        ModeInfo mi;
                        mi = gson.fromJson(mwd1.getContent(), type);
                        ModeLocalData mld = new ModeLocalData(mwd1.getId(), userid, mi, mwd1.getDate(), sum);

                        mList.add(mld);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                    if (pagecount == page) {// 如果是最后一页的话则底部就不能再刷新了
                        mode_me_list_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                }
            }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        mode_me_list_view.onRefreshComplete();
                        Toast.makeText(ModeMeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

            }
        );

//        if(direction)// 头部刷新
//        {// 渲染内容到界面上
//            mList= ToolKits.GettingModedata(ModeMeActivity.this,"modeMeInfoList");
//            mAdapter=new ModeMeAdapter(ModeMeActivity.this,mList);
//            mode_me_list_view.setAdapter(mAdapter);
//
//            //防止刷新获取数据时候，时间太短,而出现的bug,最后为0.001秒
//            mode_me_list_view.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mode_me_list_view.onRefreshComplete();
//                }
//            },1);
//
//        }else {// 尾部刷新
//            //mList.addAll(object.getDatas());
//            mAdapter.notifyDataSetChanged();
//        }
//        if (count == page) {// 如果是最后一页的话则底部就不能再刷新了
//            mode_me_list_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//        }
//

    }

    public void setImmersive() {
        //设置状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.status_bar_mode_me);
            linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            int statusHeight = ToolKits.getStatusBarHeight(this);
            //动态的设置隐藏布局的高度
            linear_bar.getLayoutParams().height = statusHeight;
        }
    }

    public void DeletingTheMode(int id) {
        RequestParams params = new RequestParams();
        ECApplication application = (ECApplication) getApplication();
        params.addBodyParameter("sessionid", application.sessionId);
        params.addBodyParameter("moodid", String.valueOf(id));
        new HttpUtils().send(HttpRequest.HttpMethod.POST, CONST.DELETE_MODE, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ResponseObject<ModeResult> object = new GsonBuilder().create().
                        fromJson(responseInfo.result, new TypeToken<ResponseObject<ModeResult>>() {
                        }.getType());

                if (object.getMsg().equals("success")) {
                    loadDatas(true, mList);
                    Toast.makeText(ModeMeActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ModeMeActivity.this, "删除的status: " + object.getStatus(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case 10:
                Bundle b = data.getExtras();
                return_images = b.getStringArrayList("return_images");
                String return_content = b.getString("return_content");

                Log.i("return_iamge_content", "requestCode: " + String.valueOf(requestCode)
                        + "resultCode:" + String.valueOf(resultCode)
                        + "  content: " + return_content);

                ModeInfo mi = new ModeInfo("", return_content, "", return_images);
                ModeLocalData mld = new ModeLocalData(0, Integer.parseInt(UserInfoLab.get(this).getUserInfo().getId()), mi, "", 0);
                mList.add(0, mld);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @OnClick({R.id.btn_mode_me_add,R.id.mode_me_back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_mode_me_add:
                startActivityForResult(new Intent(ModeMeActivity.this, ModeAddingActivity.class),REQUEST_CODE);
                break;
//            case R.id.mode_me_delete:
//                ToolKits.DeletingModeData(ModeMeActivity.this,"modeMeInfoList",);
            case R.id.mode_me_back:
                setResult(11, new Intent());
                ToolKits.putInt(ModeMeActivity.this,"fragment",2);
                finish();
        }

    }

    @OnItemClick({R.id.mode_me_list_view})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(ModeMeActivity.this, ModeDetailActivity.class);
//        intent.putExtra("item", mAdapter.getItem(position-1).getMi().getImgUrlforContent());
//        startActivity(intent);

        Intent intent = new Intent(this, ModeDetailNineGridActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", mAdapter.getItem(position - 1));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(11, new Intent());
        }
        return super.onKeyDown(keyCode, event);
    }
}
