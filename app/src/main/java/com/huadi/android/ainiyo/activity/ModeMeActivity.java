package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.adapter.ModeAdapter;
import com.huadi.android.ainiyo.adapter.ModeMeAdapter;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.util.ArrayList;
import java.util.List;

public class ModeMeActivity extends AppCompatActivity {

    @ViewInject(R.id.mode_me_list_view)
    private PullToRefreshListView mode_me_list_view;
    @ViewInject(R.id.btn_mode_me_add)
    private ImageView btn_mode_add;
    private ModeMeAdapter mAdapter;
    private static final int REQUEST_CODE = 0x00000012;

    private int page = 0;
    private int size = 20;
    private int count = 0;

    private List<ModeInfo> mList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_me);
        ViewUtils.inject(this);
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

    private void loadDatas(final boolean direction,List<ModeInfo> mList)
    {
//        new HttpUtils().send(HttpRequest.HttpMethod.POST, phourl, new RequestCallBack<byte[]>() {
//
//            @Override
//            public void onSuccess(ResponseInfo<byte[]> responseInfo) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(responseInfo.result);
//            }
//            @Override
//            public void onFailure(HttpException error, String msg) {
//
//            }
//        });
//        ModeInfo md1=new ModeInfo("fengsam","hello world",null,null);
//        ModeInfo md2=new ModeInfo("geange","hello world too",null,null);
//        mList.add(md1);
//        mList.add(md2);
        if(direction)// 头部刷新
        {// 渲染内容到界面上
            mList= ToolKits.GettingModedata(ModeMeActivity.this,"modeMeInfoList");
            mAdapter=new ModeMeAdapter(ModeMeActivity.this,mList);
            mode_me_list_view.setAdapter(mAdapter);

            //防止刷新获取数据时候，时间太短,而出现的bug,最后为0.001秒
            mode_me_list_view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mode_me_list_view.onRefreshComplete();
                }
            },1);

        }else {// 尾部刷新
            //mList.addAll(object.getDatas());
            mAdapter.notifyDataSetChanged();
        }
        if (count == page) {// 如果是最后一页的话则底部就不能再刷新了
            mode_me_list_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
        mAdapter.setOnDeleteItemClickListener(new ModeMeAdapter.OnDeleteItemClickListener()
        {
            @Override
            public void OnDeleteItemClick(int position)
            {
                ToolKits.DeletingModeData(ModeMeActivity.this,"modeMeInfoList",position);
//                new Handler(new Handler.Callback() {
//                    @Override
//                    public boolean handleMessage(Message msg) {
//                        mode_me_list_view.setRefreshing();
//                        return true;
//                    }
//                }).sendEmptyMessageDelayed(0, 200);
//                //防止刷新获取数据时候，时间太短,而出现的bug,最后为0.001秒
//                mode_me_list_view.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mode_me_list_view.onRefreshComplete();
//                    }
//                },1);
            }
        });
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
                ToolKits.putInt(ModeMeActivity.this,"fragment",2);
                finish();
        }

    }

    @OnItemClick({R.id.mode_me_list_view})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ModeMeActivity.this, ModeDetailActivity.class);
        intent.putExtra("item", mAdapter.getItem(position-1).getImgUrlforContent());
        startActivity(intent);
    }
}
