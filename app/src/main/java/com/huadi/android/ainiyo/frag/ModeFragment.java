package com.huadi.android.ainiyo.frag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.huadi.android.ainiyo.activity.FriendsInfoActivity;
import com.huadi.android.ainiyo.activity.LoginActivity;
import com.huadi.android.ainiyo.activity.ModeDetailNineGridActivity;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.FriendsLab;
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
import com.huadi.android.ainiyo.entity.UserInfoLab;
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
import com.lidroid.xutils.view.annotation.event.OnLongClick;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.huadi.android.ainiyo.util.CONST.RETURN_MODE;


public class ModeFragment extends Fragment {

    @ViewInject(R.id.mode_list_view)
    private PullToRefreshListView mode_list_view;
    @ViewInject(R.id.btn_mode_add)
    private ImageView btn_mode_add;
    @ViewInject(R.id.iv_topbar_mode_pic_head)
    ImageView iv_topbar_mode_pic_head;

    private List<ModeLocalData> mList = new ArrayList<>();
    private ModeResult modeResult;
    private ModeWebData[] mwd;
    private ModeAdapter mAdapter;
    private ArrayList<String> return_images = new ArrayList<String>();
    private int page = 1;
    private int pagesize = 20;
    private int pagecount = 10;
    private static final int REQUEST_CODE = 0x00000012;
    private static final String phourl = "http://120.24.168.102:8080/getalumb?sessionid=5ca6b5f4b438030f123fb149ff19fd8769365789";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mode, null);
        // Inflate the layout for this fragment2

        ViewUtils.inject(this, view);

        Glide.with(getActivity()).load(UserInfoLab.get(getActivity()).getUserInfo().getPicUrl()).placeholder(R.mipmap.ic_default_avater_dc).into(iv_topbar_mode_pic_head);

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
        RequestParams params = new RequestParams();
        if (!direction) {// 如果是尾部刷新要重新计算分页数据
            page++;
        } else {
            page = 1;
        }

        //获得sessionId，保存在Application里作为全局变量
        ECApplication application = (ECApplication) getActivity().getApplication();
        params.addBodyParameter("sessionid", application.sessionId);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("pagesize", "3");
        params.addBodyParameter("type", "2");

        new HttpUtils().send(HttpRequest.HttpMethod.POST, RETURN_MODE, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                mode_list_view.onRefreshComplete();
                ResponseObject<ModeResult> object = new GsonBuilder().create().
                        fromJson(responseInfo.result, new TypeToken<ResponseObject<ModeResult>>() {
                        }.getType());


                if (object.getStatus() == 400) {
                    if (direction)// 头部刷新
                    {// 渲染内容到界面上
                        //清空原来的数据
                        mList.clear();
                        //ArrayList<Integer> idorder = new ArrayList<Integer>();


                        mwd = object.getResult().getData();
                        int sum = object.getResult().getSum();
                        ModeWebData mwd1;
                        for (int i = 0; i < sum; i++) {
                            mwd1 = mwd[i];

//                            idorder.add(mwd1.getId());
//                            ToolKits.putInteger(getActivity(), "Integer", idorder);

                            int userid = mwd1.getUserid();
                            String content = mwd1.getContent();
                            Gson gson = new Gson();
                            Type type = new TypeToken<ModeInfo>() {
                            }.getType();
                            ModeInfo mi;
                            mi = gson.fromJson(mwd1.getContent(), type);

                            mi.setId(String.valueOf(mwd1.getUserid()));
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
                        mAdapter = new ModeAdapter(getActivity(), mList);
                        mode_list_view.setAdapter(mAdapter);

                        mAdapter.setmPicHeadItemClickListener(new ModeAdapter.OnPicHeadItemClickListener() {
                            @Override
                            public void OnPicHeadItemClick(int positon) {
                                Intent intent = null;
                                if (!String.valueOf(mList.get(positon).getUserid()).equals(UserInfoLab.get(getActivity()).getUserInfo().getId())) {
                                    FriendsLab.get(getActivity()).findNameById(String.valueOf(mList.get(positon).getUserid()));
                                    intent = new Intent(getActivity(), FriendsInfoActivity.class);
                                    intent.putExtra("name", FriendsLab.get(getActivity()).findNameById(String.valueOf(mList.get(positon).getUserid())));
                                    intent.putExtra("userInfo",UserInfoLab.get(getActivity()).getUserInfo());
                                    intent.putExtra("from","ModeFragment");
                                    intent.putExtra("picture",FriendsLab.get(getActivity()).getFriend(FriendsLab.get(getActivity()).findNameById(String.valueOf(mList.get(positon).getUserid()))).getPicture());
                                }else {
                                    intent = new Intent(getActivity(), FriendsInfoActivity.class);
                                    intent.putExtra("name", UserInfoLab.get(getActivity()).getUserInfo().getUsername());
                                    intent.putExtra("userInfo",UserInfoLab.get(getActivity()).getUserInfo());
                                    intent.putExtra("from","ModeFragment");
                                    intent.putExtra("picture",UserInfoLab.get(getActivity()).getUserInfo().getPicture());
                                }
                                startActivity(intent);
                                //Log.i("testpichead",String.valueOf(positon));
                            }
                        });
                    } else {// 尾部刷新
                        //mList.addAll(object.getDatas());
                        //ArrayList<Integer> idorder = new ArrayList<Integer>();
                        mwd = object.getResult().getData();
                        int sum = object.getResult().getSum();
                        ModeWebData mwd1;
                        for (int i = 0; i < sum; i++) {
                            mwd1 = mwd[i];

//                            idorder.add(mwd1.getId());
//                            ToolKits.appendInteger(getActivity(), "Integer", idorder);

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
                        mode_list_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                } else {
                    Toast.makeText(getActivity(), "status:  " + object.getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                mode_list_view.onRefreshComplete();
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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


//    private void loadDatas2(Intent data)
//    {
//        ArrayList<String> images = data.getStringArrayListExtra("images");
//        String et_mode_add_saying=data.getStringExtra("text");
//
//        ModeInfo md1=new ModeInfo("fengsam1",et_mode_add_saying,null,images);
//        mList.add(md1);
//
////            mAdapter=new ModeAdapter(mList);
////            mode_list_view.setAdapter(mAdapter);
//
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE && data != null && resultCode==2) {
//            loadDatas2(data);
//
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case 10:
                Bundle b = data.getExtras();
                return_images = b.getStringArrayList("return_images");
                String return_content = b.getString("return_content");

//            Log.i("return_iamge_content", "requestCode: " + String.valueOf(requestCode)
//                    + "resultCode:" + String.valueOf(resultCode)
//                    + "  content: " + return_content);

                ModeInfo mi = new ModeInfo("", return_content, "", return_images);
                ModeLocalData mld = new ModeLocalData(0, Integer.parseInt(UserInfoLab.get(getActivity()).getUserInfo().getId()),
                        mi, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), 0);
                mList.add(0, mld);
                mAdapter.notifyDataSetChanged();
                break;
            case 11:
                //删除心情后返回到心情页面主动加载数据，避免每次要下拉刷新
                loadDatas(true);
        }
    }


    @OnItemClick({R.id.mode_list_view})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ModeDetailNineGridActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", mAdapter.getItem(position - 1));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick({R.id.btn_mode_add, R.id.iv_topbar_mode_pic_head})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_mode_add:
                startActivityForResult(new Intent(getActivity(), ModeAddingActivity.class),REQUEST_CODE);
                break;
            case R.id.iv_topbar_mode_pic_head:
                startActivityForResult(new Intent(getActivity(), ModeMeActivity.class), 0);
        }

    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }
}
