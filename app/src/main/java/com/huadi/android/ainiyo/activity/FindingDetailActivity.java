package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.adapter.FindingAdapter;
import com.huadi.android.ainiyo.adapter.ModeAdapter;
import com.huadi.android.ainiyo.adapter.ModeMeAdapter;
import com.huadi.android.ainiyo.entity.FindingInfo;
import com.huadi.android.ainiyo.entity.FindingLikeList;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.util.ArrayList;
import java.util.List;

import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.view.annotation.event.OnItemLongClick;

public class FindingDetailActivity extends AppCompatActivity {

    @ViewInject(R.id.finding_detail_list_view)
    private PullToRefreshListView finding_detail_list_view;
    @ViewInject(R.id.btn_finding_detail_add)
    private ImageView btn_finding_detail_add;

    private FindingLikeList fll;

    private FindingAdapter findingAdapter;
    private ArrayList<FindingInfo> mList = new ArrayList<FindingInfo>();
    private static final int REQUEST_CODE = 0x00000012;

    private PopupWindow mPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_detail);
        ViewUtils.inject(this);

        final Intent t = getIntent();
        fll = (FindingLikeList) t.getSerializableExtra("findinglike");

        // Set a listener to be invoked when the list should be refreshed.
        finding_detail_list_view.setMode(PullToRefreshBase.Mode.BOTH);
        finding_detail_list_view.setScrollingWhileRefreshingEnabled(true);
        //Toast.makeText(getActivity(),"0000",Toast.LENGTH_SHORT).show();
        finding_detail_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(FindingDetailActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // Toast.makeText(getActivity(),"XXXXXXXXXX",Toast.LENGTH_SHORT).show();
                loadDatas(refreshView.getScrollY() < 0);
            }
        });
        // 首次自动加载数据
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                finding_detail_list_view.setRefreshing();
                return true;
            }
        }).sendEmptyMessageDelayed(0, 200);
    }

    private void loadDatas(final boolean direction) {

//        RequestParams params = new RequestParams();
//
//        ECApplication application = (ECApplication) getActivity().getApplication();
//        params.addBodyParameter("sessionid", application.sessionId);
//
//        new HttpUtils().send(HttpRequest.HttpMethod.POST, FINDING_USER_DESTINY, params, new RequestCallBack<String>() {
//
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//
//                finding_list_view.onRefreshComplete();
//                ResponseObject<ArrayList<FindingInfo>> object = new GsonBuilder().create().
//                        fromJson(responseInfo.result, new TypeToken<ResponseObject<ArrayList<FindingInfo>>>() {
//                        }.getType());
//
//                if (object.getStatus() == 0) {
//                    if (direction)// 头部刷新
//                    {// 渲染内容到界面上
//                        //清空原来的数据
//                        mList = object.getResult();
//
//                        findingAdapter = new FindingAdapter(getActivity(), mList);
//                        finding_list_view.setAdapter(findingAdapter);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                finding_list_view.onRefreshComplete();
//                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//            }
//        });

//        FindingInfo fi1 = new FindingInfo("1", 0.60f, 0.94f, 0.90f, 0.80f, 0.70f, 0.30f, 0.33f, 0.94f, "刘奕宁1", true, "123", 20, "学生");
//        FindingInfo fi2 = new FindingInfo("1", 0.90f, 0.60f, 0.80f, 0.40f, 0.77f, 0.90f, 0.80f, 0.40f, "刘奕宁2", true, "123", 20, "教师");
//
//        mList.add(fi1);
//        mList.add(fi2);

        mList = fll.mList;
        findingAdapter = new FindingAdapter(FindingDetailActivity.this, mList);
        finding_detail_list_view.setAdapter(findingAdapter);

        findingAdapter.setOnPipeiItemClickListener(new FindingAdapter.OnPiPeiItemClickListener() {
            @Override
            public void OnPiPeiItemClick(int position) {
                Intent intent = new Intent(FindingDetailActivity.this, FindingDataAnlaysisActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("findingdataitem", findingAdapter.getItem(position));
                intent.putExtras(bundle);
                startActivity(intent);

//                FindingInfo fi=mList.get(position);
//
//                startActivity(intent);
            }
        });
        //防止刷新获取数据时候，时间太短,而出现的bug,最后为0.001秒
            finding_detail_list_view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finding_detail_list_view.onRefreshComplete();
                }
            }, 1);
    }

    @OnClick({R.id.finding_detail_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finding_detail_back:
                finish();
                break;
        }
    }

//    @OnItemClick({R.id.finding_detail_list_view})
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//       startActivity(new Intent(FindingDetailActivity.this,FindingUserInfoActivity.class));
//        Toast.makeText(FindingDetailActivity.this, "position:  " + String.valueOf(position), Toast.LENGTH_SHORT).show();
//
//    }

}
