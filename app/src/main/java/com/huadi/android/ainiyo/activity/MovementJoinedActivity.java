package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.adapter.MovementAdapter;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.ModeResult;
import com.huadi.android.ainiyo.entity.MovementContentData;
import com.huadi.android.ainiyo.entity.MovementData;
import com.huadi.android.ainiyo.entity.MovementResult;
import com.huadi.android.ainiyo.entity.ResponseObject;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.huadi.android.ainiyo.util.CONST.FETCH_ACTIVITY;
import static com.huadi.android.ainiyo.util.CONST.FETCH_JOINED_ACTIVITY;

public class MovementJoinedActivity extends AppCompatActivity {

    @ViewInject(R.id.movement_list_view)
    private PullToRefreshListView movement_list_view;

    private List<MovementContentData> mList = new ArrayList<>();
    private ModeResult modeResult;
    private MovementData[] mwd;
    private MovementAdapter mAdapter;
    private int page = 1;
    private int pagesize = 20;
    private int pagecount = 1;
    private static final int REQUEST_CODE = 0x00000012;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_movement);
        ViewUtils.inject(this);
        //Toast.makeText(this,"created",Toast.LENGTH_SHORT).show();
        // Set a listener to be invoked when the list should be refreshed.
        movement_list_view.setMode(PullToRefreshBase.Mode.BOTH);
        movement_list_view.setScrollingWhileRefreshingEnabled(true);
        movement_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MovementJoinedActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                loadDatas(refreshView.getScrollY() < 0);
            }
        });

        // 首次自动加载数据
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                movement_list_view.setRefreshing();
                return true;
            }
        }).sendEmptyMessageDelayed(0, 200);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("MOVE", "start");
        movement_list_view.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private void loadDatas(final boolean direction) {
        RequestParams params = new RequestParams();
        if (!direction) {// 如果是尾部刷新要重新计算分页数据
            page++;
        } else {
            page = 1;
        }

        ECApplication application = (ECApplication) getApplication();
        params.addBodyParameter("sessionid", application.sessionId);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("pagesize", "10");
        //params.addBodyParameter("type", "1");

        new HttpUtils().send(HttpRequest.HttpMethod.POST, FETCH_JOINED_ACTIVITY, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                movement_list_view.onRefreshComplete();
                String newResponse = responseInfo.result.replaceAll("\\n", "");
                //Log.e("MOVEMENT", newResponse);
                ResponseObject<MovementResult> object = new GsonBuilder().create().
                        fromJson(newResponse, new TypeToken<ResponseObject<MovementResult>>() {
                        }.getType());
                //Log.e("MOVEMENT", String.valueOf(object.getResult().getData()[0].getDate()));
                if (object.getStatus() == 0) {
                    if(direction){//head refresh
                        mList.clear();
                    }

                    pagecount = object.getResult().getPagecount();
                    mwd = object.getResult().getData();
                    int sum = object.getResult().getSum();
                    MovementData mwd1;
                    if (mwd != null) {
                        for (int i = 0; i <= mwd.length - 1; ++i) {
                            mwd1 = mwd[i];

                            String content = mwd1.getContent().replaceAll("[\\n]|[\\t]|[ ]", "");
                            if (mwd1.getContent() != null) {
                                Log.e("MOVEMENT", content);
                            }
                            Gson gson = new Gson();
                            Type type = new TypeToken<MovementContentData>() {
                            }.getType();

                            if (content.startsWith("{")) {//排除无效字符串
                                MovementContentData mcd = gson.fromJson(content, type);
                                mcd.setId(mwd1.getId());//ID同步校正
                                mcd.setJoined(mwd1.isAttended());

                                if (mcd != null) {
                                    mList.add(mcd);//
                                }
                            }
                        }
                    } else {
                        movement_list_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }

                    if(direction){//head refresh
                        mAdapter = new MovementAdapter(mList, ((ECApplication) getApplication()).sessionId, true);
                        movement_list_view.setAdapter(mAdapter);
                    }
                    else {//tail refresh
                        mAdapter.notifyDataSetChanged();
                    }

                    if (pagecount == page) {// 如果是最后一页的话则底部就不能再刷新了
                        movement_list_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                movement_list_view.onRefreshComplete();
                Toast.makeText(MovementJoinedActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

//        if (direction)// 头部刷新
//        {// 渲染内容到界面上
//            mList = ToolKits.GettingModedata(getActivity(), "modeInfoList");
//            mAdapter = new ModeAdapter(mList);
//            mode_list_view.setAdapter(mAdapter);
//
//            //防止刷新获取数据时候，时间太短,而出现的bug,最后为0.001秒
//            mode_list_view.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mode_list_view.onRefreshComplete();
//                }
//            }, 1);
//
//        } else {// 尾部刷新
//            //mList.addAll(object.getDatas());
//            mAdapter.notifyDataSetChanged();
//        }
//        if (pagecount == page) {// 如果是最后一页的话则底部就不能再刷新了
//            mode_list_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//        }
    }

    @OnItemClick({R.id.movement_list_view})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MovementJoinedActivity.this, MovementDetailActivity.class);


        MovementContentData mcd = mList.get(position-1);
        intent.putExtra("id", mcd.getId());
        intent.putExtra("title", mcd.getTitle());
        intent.putExtra("date", mcd.getDate());
        intent.putExtra("imageUrl", mcd.getImageUrl());
        intent.putExtra("article", mcd.getArticle());

        intent.putExtra("isJoined",true);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadDatas(true);
    }
}
