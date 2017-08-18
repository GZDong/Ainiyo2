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

import com.google.gson.Gson;
import com.huadi.android.ainiyo.activity.FindingDataAnlaysisActivity;
import com.huadi.android.ainiyo.activity.FindingDetailActivity;
import com.huadi.android.ainiyo.adapter.FindingAdapter;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.FindingInfo;
import com.huadi.android.ainiyo.entity.ModeLocalData;
import com.huadi.android.ainiyo.entity.ModeResult;
import com.huadi.android.ainiyo.entity.ModeWebData;
import com.huadi.android.ainiyo.entity.ResponseObject;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.ModeAddingActivity;
import com.huadi.android.ainiyo.activity.ModeDetailActivity;
import com.huadi.android.ainiyo.activity.ModeMeActivity;
import com.huadi.android.ainiyo.adapter.ModeAdapter;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.huadi.android.ainiyo.util.CONST;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.huadi.android.ainiyo.util.CONST.FINDING_USER_DESTINY;
import static com.huadi.android.ainiyo.util.CONST.RETURN_MODE;


public class FindingFragment extends Fragment {

    @ViewInject(R.id.finding_list_view)
    private PullToRefreshListView finding_list_view;
    private FindingAdapter findingAdapter;
    private ArrayList<FindingInfo> mList;

    private static final int REQUEST_CODE = 0x00000012;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_finding, null);

        // Inflate the layout for this fragment
        ViewUtils.inject(this, view);
        // Set a listener to be invoked when the list should be refreshed.
        finding_list_view.setMode(PullToRefreshBase.Mode.BOTH);
        finding_list_view.setScrollingWhileRefreshingEnabled(true);
        //Toast.makeText(getActivity(),"0000",Toast.LENGTH_SHORT).show();
        finding_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // Toast.makeText(getActivity(),"XXXXXXXXXX",Toast.LENGTH_SHORT).show();
                loadDatas(refreshView.getScrollY() < 0);
            }
        });

//        // 首次自动加载数据
//        new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                finding_list_view.setRefreshing();
//                Toast.makeText(getActivity(),"3333",Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        }).sendEmptyMessageDelayed(0, 200);

        return view;
    }

    private void loadDatas(final boolean direction) {

        RequestParams params = new RequestParams();

        ECApplication application = (ECApplication) getActivity().getApplication();
        params.addBodyParameter("sessionid", application.sessionId);

        new HttpUtils().send(HttpRequest.HttpMethod.POST, FINDING_USER_DESTINY, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                finding_list_view.onRefreshComplete();
                ResponseObject<ArrayList<FindingInfo>> object = new GsonBuilder().create().
                        fromJson(responseInfo.result, new TypeToken<ResponseObject<ArrayList<FindingInfo>>>() {
                        }.getType());

                if (object.getStatus() == 0) {
                    if (direction)// 头部刷新
                    {// 渲染内容到界面上
                        //清空原来的数据
                        mList = object.getResult();

                        findingAdapter = new FindingAdapter(getActivity(), mList);
                        finding_list_view.setAdapter(findingAdapter);

                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                finding_list_view.onRefreshComplete();
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.btn_finding_go})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_finding_go:
                startActivity(new Intent(getActivity(), FindingDataAnlaysisActivity.class));
        }

    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }
}
