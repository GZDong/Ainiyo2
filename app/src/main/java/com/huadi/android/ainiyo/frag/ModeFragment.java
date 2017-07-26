package com.huadi.android.ainiyo.frag;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.adapter.ModeAdapter;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
        ModeInfo md1=new ModeInfo("fengsam","hello world");
        ModeInfo md2=new ModeInfo("geange","hello world too");
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

    @OnClick({R.id.btn_mode_add})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_mode_add:
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
