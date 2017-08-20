package com.huadi.android.ainiyo.frag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.huadi.android.ainiyo.adapter.FindingCardSwlpeAdapter;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.FindingInfo;
import com.huadi.android.ainiyo.entity.FindingLikeList;
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

import me.yuqirong.cardswipelayout.CardConfig;
import me.yuqirong.cardswipelayout.CardItemTouchHelperCallback;
import me.yuqirong.cardswipelayout.CardLayoutManager;
import me.yuqirong.cardswipelayout.OnSwipeListener;

import static com.huadi.android.ainiyo.util.CONST.FINDING_USER_DESTINY;
import static com.huadi.android.ainiyo.util.CONST.RETURN_MODE;


public class FindingFragment extends Fragment {

//    @ViewInject(R.id.finding_list_view)
//    private PullToRefreshListView finding_list_view;
//    private FindingAdapter findingAdapter;
//    private ArrayList<FindingInfo> mList=new ArrayList<FindingInfo>();
//
//    private static final int REQUEST_CODE = 0x00000012;

    @ViewInject(R.id.finding_recyclerView)
    private RecyclerView recyclerView;
    private FindingCardSwlpeAdapter findingCardSwlpeAdapter;
    //private FindingCardSwlpeAdapter.MyViewHolder myHolder1;

    CardItemTouchHelperCallback cardCallback;

    //private List<Integer> mList = new ArrayList<>();
    private List<FindingInfo> mList = new ArrayList<>();
    private FindingLikeList fll = new FindingLikeList();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_finding, null);

        // Inflate the layout for this fragment
        ViewUtils.inject(this, view);
        /*
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

*/
        //initView();
        initData();
        return view;
    }

    private void initView() {
        findingCardSwlpeAdapter = new FindingCardSwlpeAdapter(getActivity(), mList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(findingCardSwlpeAdapter);
        cardCallback = new CardItemTouchHelperCallback(recyclerView.getAdapter(), mList);
        cardCallback.setOnSwipedListener(new OnSwipeListener<FindingInfo>() {

            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                FindingCardSwlpeAdapter.MyViewHolder myHolder = (FindingCardSwlpeAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);
                if (direction == CardConfig.SWIPING_LEFT) {
                    myHolder.dislikeImageView.setAlpha(Math.abs(ratio));
                } else if (direction == CardConfig.SWIPING_RIGHT) {
                    myHolder.likeImageView.setAlpha(Math.abs(ratio));
                } else {
                    myHolder.dislikeImageView.setAlpha(0f);
                    myHolder.likeImageView.setAlpha(0f);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, FindingInfo o, int direction) {
                FindingCardSwlpeAdapter.MyViewHolder myHolder = (FindingCardSwlpeAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1f);
                myHolder.dislikeImageView.setAlpha(0f);
                myHolder.likeImageView.setAlpha(0f);
                if (direction == CardConfig.SWIPED_RIGHT) {
                    fll.mList.add(o);
                }
                Toast.makeText(getActivity(), direction == CardConfig.SWIPED_LEFT ? "不喜欢" : "喜欢", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipedClear() {
                Toast.makeText(getActivity(), "data clear", Toast.LENGTH_SHORT).show();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }, 3000L);
            }

        });
        final ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        final CardLayoutManager cardLayoutManager = new CardLayoutManager(recyclerView, touchHelper);
        recyclerView.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void initData() {
//        FindingInfo fi1 = new FindingInfo("1", 0.60f, 0.94f, 0.90f, 0.80f, 0.70f, 0.30f, 0.33f, 0.94f, "刘奕宁1", true, "123", 20, "学生");
//        FindingInfo fi2 = new FindingInfo("1", 0.90f, 0.60f, 0.80f, 0.40f, 0.77f, 0.90f, 0.80f, 0.40f, "刘奕宁2", true, "123", 20, "教师");
//        FindingInfo fi3 = new FindingInfo("1", 0.90f, 0.60f, 0.80f, 0.40f, 0.77f, 0.90f, 0.80f, 0.40f, "刘奕宁3", true, "123", 20, "教师");
//        FindingInfo fi4 = new FindingInfo("1", 0.90f, 0.60f, 0.80f, 0.40f, 0.77f, 0.90f, 0.80f, 0.40f, "刘奕宁4", true, "123", 20, "教师");
//
//        mList.add(fi1);
//        mList.add(fi2);
//        mList.add(fi3);
//        mList.add(fi4);

        mList.clear();
        RequestParams params = new RequestParams();

        ECApplication application = (ECApplication) getActivity().getApplication();
        params.addBodyParameter("sessionid", application.sessionId);

        new HttpUtils().send(HttpRequest.HttpMethod.POST, FINDING_USER_DESTINY, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                ResponseObject<ArrayList<FindingInfo>> object = new GsonBuilder().create().
                        fromJson(responseInfo.result, new TypeToken<ResponseObject<ArrayList<FindingInfo>>>() {
                        }.getType());

                if (object.getStatus() == 0) {
                    // 渲染内容到界面上
                    //清空原来的数据
                    mList = object.getResult();

                    initView();

                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });


    }

//    private void loadDatas(final boolean direction) {
//
////        RequestParams params = new RequestParams();
////
////        ECApplication application = (ECApplication) getActivity().getApplication();
////        params.addBodyParameter("sessionid", application.sessionId);
////
////        new HttpUtils().send(HttpRequest.HttpMethod.POST, FINDING_USER_DESTINY, params, new RequestCallBack<String>() {
////
////            @Override
////            public void onSuccess(ResponseInfo<String> responseInfo) {
////
////                finding_list_view.onRefreshComplete();
////                ResponseObject<ArrayList<FindingInfo>> object = new GsonBuilder().create().
////                        fromJson(responseInfo.result, new TypeToken<ResponseObject<ArrayList<FindingInfo>>>() {
////                        }.getType());
////
////                if (object.getStatus() == 0) {
////                    if (direction)// 头部刷新
////                    {// 渲染内容到界面上
////                        //清空原来的数据
////                        mList = object.getResult();
////
////                        findingAdapter = new FindingAdapter(getActivity(), mList);
////                        finding_list_view.setAdapter(findingAdapter);
////
////                    }
////                }
////            }
////
////            @Override
////            public void onFailure(HttpException error, String msg) {
////                finding_list_view.onRefreshComplete();
////                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
////            }
////        });
//
//        FindingInfo fi1 = new FindingInfo("1", 0.60f, 0.94f, 0.90f, 0.80f, 0.70f, 0.30f, 0.33f, 0.94f, "刘奕宁1", true, "123", 20, "学生");
//        FindingInfo fi2 = new FindingInfo("1", 0.90f, 0.60f, 0.80f, 0.40f, 0.77f, 0.90f, 0.80f, 0.40f, "刘奕宁2", true, "123", 20, "教师");
//
//        mList.add(fi1);
//        mList.add(fi2);
//        findingAdapter = new FindingAdapter(getActivity(), mList);
//                        finding_list_view.setAdapter(findingAdapter);
//
//        findingAdapter.setOnPipeiItemClickListener(new FindingAdapter.OnPiPeiItemClickListener() {
//            @Override
//            public void OnPiPeiItemClick(int position) {
//                Intent intent = new Intent(getActivity(),FindingDataAnlaysisActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("findingdataitem", findingAdapter.getItem(position));
//                intent.putExtras(bundle);
//                startActivity(intent);
//
////                FindingInfo fi=mList.get(position);
////
////                startActivity(intent);
//            }
//        });
//        //防止刷新获取数据时候，时间太短,而出现的bug,最后为0.001秒
//            finding_list_view.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finding_list_view.onRefreshComplete();
//                }
//            }, 1);
//    }

    @OnClick({R.id.btn_finding_go, R.id.iv_finding_left, R.id.iv_finding_right})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_finding_go:
                Intent t = new Intent(getActivity(), FindingDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("findinglike", fll);
                t.putExtras(bundle);
                startActivity(t);
                break;
            case R.id.iv_finding_left:
                //cardCallback.onSwiped(findingCardSwlpeAdapter.getViewHolder(),ItemTouchHelper.LEFT);
                break;
            case R.id.iv_finding_right:
                //cardCallback.onSwiped(findingCardSwlpeAdapter.getViewHolder(),ItemTouchHelper.RIGHT);
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
