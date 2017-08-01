package com.huadi.android.ainiyo.frag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.github.chrisbanes.photoview.PhotoView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.ModeAddingActivity;
import com.huadi.android.ainiyo.adapter.ImageAdapter;
import com.huadi.android.ainiyo.adapter.ModeAdapter;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.util.ArrayList;
import java.util.List;


public class ModeFragment extends Fragment {

    @ViewInject(R.id.mode_list_view)
    private PullToRefreshListView mode_list_view;
    @ViewInject(R.id.btn_mode_add)
    private ImageView btn_mode_add;

    private List<ModeInfo> mList=new ArrayList<>();
    private ModeAdapter mAdapter;
    private int page = 0;
    private int size = 20;
    private int count = 0;
    private static final int REQUEST_CODE = 0x00000012;
    private static final String phourl = "http://120.24.168.102:8080/getalumb?sessionid=5ca6b5f4b438030f123fb149ff19fd8769365789";

    private ImageAdapter mAdapter1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mode,null);
        // Inflate the layout for this fragment2

        ViewUtils.inject(this, view);
        // Set a listener to be invoked when the list should be refreshed.
        mode_list_view.setMode(PullToRefreshBase.Mode.BOTH);
        mode_list_view.setScrollingWhileRefreshingEnabled(true);
        mode_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                loadDatas(refreshView.getScrollY() < 0);
            }
        });
        // 首次自动加载数据
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                mode_list_view.setRefreshing();
                return true;
            }
        }).sendEmptyMessageDelayed(0, 200);
        return view;
    }

    private void loadDatas(final boolean direction)
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
        ModeInfo md1=new ModeInfo("fengsam","hello world",null,null);
        ModeInfo md2=new ModeInfo("geange","hello world too",null,null);
        mList.add(md1);
        mList.add(md2);
        if(direction)// 头部刷新
        {// 渲染内容到界面上
            //mList=
            mAdapter=new ModeAdapter(mList);
            mode_list_view.setAdapter(mAdapter);

            //防止刷新获取数据时候，时间太短,而出现的bug,最后为0.001秒
            mode_list_view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mode_list_view.onRefreshComplete();
                }
            },1);

        }else {// 尾部刷新
            //mList.addAll(object.getDatas());
            mAdapter.notifyDataSetChanged();
        }
        if (count == page) {// 如果是最后一页的话则底部就不能再刷新了
            mode_list_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
    }

    private void loadDatas2(Intent data)
    {
        ArrayList<String> images = data.getStringArrayListExtra("images");
        String et_mode_add_saying=data.getStringExtra("text");

        ModeInfo md1=new ModeInfo("fengsam1",et_mode_add_saying,null,images);
        mList.add(md1);

            mAdapter=new ModeAdapter(mList);
            mode_list_view.setAdapter(mAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null && resultCode==2) {
            loadDatas2(data);

        }
    }

//    @OnItemClick({R.id.mode_list_view})
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(getActivity(), ModeAddingActivity.class);
//        intent.putExtra("pho", mAdapter.getPhoItem(position));
//        startActivity(intent);
//    }

    @OnClick({R.id.btn_mode_add})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_mode_add:
                startActivityForResult(new Intent(getActivity(), ModeAddingActivity.class),REQUEST_CODE);
                break;
        }

    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }
}
