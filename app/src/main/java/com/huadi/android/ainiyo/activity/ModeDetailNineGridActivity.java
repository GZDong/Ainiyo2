package com.huadi.android.ainiyo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.adapter.ModeCommentAdapter;
import com.huadi.android.ainiyo.adapter.ModeToCommentAdapter;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.ModeComment;
import com.huadi.android.ainiyo.entity.ModeCommentData;
import com.huadi.android.ainiyo.entity.ModeCommentResult;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.huadi.android.ainiyo.entity.ModeLocalData;
import com.huadi.android.ainiyo.entity.ModeWebData;
import com.huadi.android.ainiyo.entity.ResponseObject;
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
import com.lidroid.xutils.view.annotation.event.OnItemLongClick;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

public class ModeDetailNineGridActivity extends AppCompatActivity {

    @ViewInject(R.id.nineGrid)
    NineGridView nineGridView;
    @ViewInject(R.id.tv_mode_nine_grid_content)
    TextView tv_content;
    @ViewInject(R.id.tv_createTime)
    TextView tv_createTime;
    @ViewInject(R.id.lv_comments)
    ListView comments;
    @ViewInject(R.id.mode_ll_liuyan)
    LinearLayout mAmLlLiuyan;
    @ViewInject(R.id.mode_b_save)
    Button mAmBSave;
    @ViewInject(R.id.mode_et_msg)
    EditText mAmEtMsg;
    @ViewInject(R.id.sv_comment)
    ScrollView sv_comment;
    @ViewInject(R.id.lv_to_comments)
    ListView tocomments;

    private PopupWindow editWindow;
    private View rootView;
    private NineGridViewClickAdapter mGridAdapter;
    private ModeCommentAdapter mCommentAdapter;
    private ModeToCommentAdapter mToCommentAdapter;

    private ModeLocalData mld;

    private ModeCommentData[] mcd;

    private Handler Myhandle;
    private Message msg;
    private PopupWindow mPopWindow;

    //列表数据
    ArrayList<ModeComment> mCommentList;
    ArrayList<ModeComment> mToCommentList = new ArrayList<ModeComment>();
    //回复的内容
    String info = "";
    //标记位，是评论还是回复。默true认评论
    boolean isComment = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_detail_nine_grid);

        ViewUtils.inject(this);

        setImmersive();

        NineGridView.setImageLoader(new GlideImageLoader());
        final Intent intent = getIntent();
        mld = (ModeLocalData) intent.getSerializableExtra("item");
        //加载九宫格图片
        LoadNineGridPho(mld.getMi().getImgUrlforContent());
        tv_content.setText(mld.getMi().getContent());
        tv_createTime.setText(mld.getDate());

        initCommentData(mld.getId(), true);


        initToCommentItemDelete();
    }

    public void setImmersive() {
        //设置状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.status_bar_mode_detail_nine_grid);
            linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            int statusHeight = ToolKits.getStatusBarHeight(this);
            //动态的设置隐藏布局的高度
            linear_bar.getLayoutParams().height = statusHeight;
        }
    }


    //加载九宫格图片
    public void LoadNineGridPho(ArrayList<String> mPhoList) {
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        if (mPhoList != null) {
            for (String pl : mPhoList) {
                ImageInfo info = new ImageInfo();
                info.setBigImageUrl(pl);
                info.setThumbnailUrl(pl);
                imageInfo.add(info);
                //Toast.makeText(ModeDetailNineGridActivity.this,pl,Toast.LENGTH_SHORT).show();
            }
        }
        mGridAdapter = new NineGridViewClickAdapter(ModeDetailNineGridActivity.this, imageInfo);
        nineGridView.setAdapter(mGridAdapter);
    }

    private void initCommentData(int modeid, final boolean flag) {
//        mCommentList = new ArrayList<>();
//        ModeComment mc = null;
//        mc = new ModeComment("1", "David", "", "hihi", "8-13", "");
//        mCommentList.add(mc);
//        mCommentList.add(mc);


        RequestParams params = new RequestParams();
        ECApplication application = (ECApplication) getApplication();
        params.addBodyParameter("sessionid", application.sessionId);
        params.addBodyParameter("tid", String.valueOf(modeid));
        //Toast.makeText(ModeDetailNineGridActivity.this, "getCommenttid:  "+String.valueOf(mld.getId()), Toast.LENGTH_SHORT).show();
        params.addBodyParameter("ismood", "1");
        params.addBodyParameter("page", "1");
        params.addBodyParameter("pagesize", "10");

        new HttpUtils().send(HttpRequest.HttpMethod.POST, CONST.FETCH_COMMENT, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ResponseObject<ModeCommentResult> object = new GsonBuilder().create().
                        fromJson(responseInfo.result, new TypeToken<ResponseObject<ModeCommentResult>>() {
                        }.getType());
                if (object.getStatus() == 400) {
                    mcd = object.getResult().getData();
                    int sum = object.getResult().getSum();
                    //Toast.makeText(ModeDetailNineGridActivity.this, "sum:  "+String.valueOf(sum), Toast.LENGTH_SHORT).show();
                    mCommentList = new ArrayList<>();

                    for (int i = 0; i < sum; i++) {
                        ModeComment mc = null;
                        mc = new ModeComment(String.valueOf(mcd[i].getId()), String.valueOf(mcd[i].getUserid()), "", mcd[i].getContent(), mcd[i].getDate(), "");
                        mCommentList.add(mc);
                        if (flag) {
                            initToCommentData(mcd[i].getId(), i);
                        }
//                        Myhandle=new Handler(){
//                            public void handleMessage(Message msg)
//                            {
//                                ArrayList<ModeComment> mCommentList1=(ArrayList<ModeComment>)msg.getData().getSerializable("tocomment");
//                                mCommentList.addAll(mCommentList1);
//                                //initCommentAdapter();
//                                //Log.i("test Comment",msg.getData().getString("hi"));
//                            }
//                        };
                    }

                    initCommentAdapter();

                } else {
                    Toast.makeText(ModeDetailNineGridActivity.this, "getCommentstatus:  " + object.getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(ModeDetailNineGridActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initToCommentData(int id, int i) {
        if (i == 0) {
            mToCommentList.clear();
        }
        RequestParams params = new RequestParams();
        ECApplication application = (ECApplication) getApplication();
        params.addBodyParameter("sessionid", application.sessionId);
        params.addBodyParameter("tid", String.valueOf(id));
        //Toast.makeText(ModeDetailNineGridActivity.this, "getCommenttid:  "+String.valueOf(mld.getId()), Toast.LENGTH_SHORT).show();
        params.addBodyParameter("ismood", "0");
        params.addBodyParameter("page", "1");
        params.addBodyParameter("pagesize", "10");

        new HttpUtils().send(HttpRequest.HttpMethod.POST, CONST.FETCH_COMMENT, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ResponseObject<ModeCommentResult> object = new GsonBuilder().create().
                        fromJson(responseInfo.result, new TypeToken<ResponseObject<ModeCommentResult>>() {
                        }.getType());
                if (object.getStatus() == 400) {
                    mcd = object.getResult().getData();
                    int sum = object.getResult().getSum();
                    //Toast.makeText(ModeDetailNineGridActivity.this, "ToCommentsum:  "+String.valueOf(sum), Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < sum; i++) {
                        ModeComment mc = null;
                        mc = new ModeComment(String.valueOf(mcd[i].getId()), String.valueOf(mcd[i].getUserid()), "", mcd[i].getContent(), mcd[i].getDate(), String.valueOf(mcd[i].getUserid()));
                        mToCommentList.add(mc);
                    }

//                    Bundle bundle =new Bundle();
//                    bundle.putSerializable("tocomment",mCommentList);
//                   // bundle.putString("hi","hiComment");
//                    msg=Myhandle.obtainMessage();//每发送一次都要重新获取
//                    msg.setData(bundle);
//                    Myhandle.sendMessage(msg);
                    initToCommentAdapter();
                } else {
                    Toast.makeText(ModeDetailNineGridActivity.this, "getCommentstatus:  " + object.getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(ModeDetailNineGridActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initCommentAdapter() {
        mCommentAdapter = new ModeCommentAdapter(this, mCommentList);
        comments.setAdapter(mCommentAdapter);
    }

    private void initToCommentAdapter() {
        mToCommentAdapter = new ModeToCommentAdapter(this, mToCommentList);
        tocomments.setAdapter(mToCommentAdapter);
    }

    /**
     * Glide 加载
     */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    @OnItemClick({R.id.lv_comments})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(ModeDetailNineGridActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
        comment(2, position);
    }

    @OnItemLongClick({R.id.lv_comments})
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        // Toast.makeText(ModeDetailNineGridActivity.this,"Long:  "+String.valueOf(position),Toast.LENGTH_SHORT).show();
        //设置contentView
        View contentView = LayoutInflater.from(ModeDetailNineGridActivity.this).inflate(R.layout.mode_comment_delete_pop_window, null);
        mPopWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //设置各个控件的点击响应
        TextView tv1 = (TextView) contentView.findViewById(R.id.pop_computer);
        TextView tv2 = (TextView) contentView.findViewById(R.id.pop_financial);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ModeDetailNineGridActivity.this,"clicked Delete",Toast.LENGTH_SHORT).show();
                DeleteComment(mCommentList.get(position).getId(), position, true);
                mPopWindow.dismiss();
                //Toast.makeText(ModeDetailNineGridActivity.this,mCommentList.get(position).getContent(),Toast.LENGTH_SHORT).show();
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ModeDetailNineGridActivity.this,"clicked cancel",Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
            }
        });
        //显示PopupWindow
        View rootview = LayoutInflater.from(ModeDetailNineGridActivity.this).inflate(R.layout.activity_mode_detail_nine_grid, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);

        return true;
    }

    public void initToCommentItemDelete() {
        tocomments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                View contentView = LayoutInflater.from(ModeDetailNineGridActivity.this).inflate(R.layout.mode_comment_delete_pop_window, null);
                mPopWindow = new PopupWindow(contentView,
                        WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                mPopWindow.setContentView(contentView);
                //设置各个控件的点击响应
                TextView tv1 = (TextView) contentView.findViewById(R.id.pop_computer);
                TextView tv2 = (TextView) contentView.findViewById(R.id.pop_financial);
                tv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(ModeDetailNineGridActivity.this,"clicked Delete",Toast.LENGTH_SHORT).show();
                        DeleteComment(mToCommentList.get(position).getId(), position, false);
                        mPopWindow.dismiss();
                        //Toast.makeText(ModeDetailNineGridActivity.this,mCommentList.get(position).getContent(),Toast.LENGTH_SHORT).show();
                    }
                });

                tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(ModeDetailNineGridActivity.this,"clicked cancel",Toast.LENGTH_SHORT).show();
                        mPopWindow.dismiss();
                    }
                });
                //显示PopupWindow
                View rootview = LayoutInflater.from(ModeDetailNineGridActivity.this).inflate(R.layout.activity_mode_detail_nine_grid, null);
                mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);

                return true;
            }
        });
    }


    public void DeleteComment(String id, final int position, final boolean flag) {
        RequestParams params = new RequestParams();
        ECApplication application = (ECApplication) getApplication();
        params.addBodyParameter("sessionid", application.sessionId);
        params.addBodyParameter("commentid", id);


        new HttpUtils().send(HttpRequest.HttpMethod.POST, CONST.DELETE_COMMENT, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ResponseObject<String> object = new GsonBuilder().create().
                        fromJson(responseInfo.result, new TypeToken<ResponseObject<String>>() {
                        }.getType());
                if (object.getStatus() == 420) {
                    Toast.makeText(ModeDetailNineGridActivity.this, "comment delete success", Toast.LENGTH_SHORT).show();
                    if (flag) {
                        mCommentList.remove(position);
                        mCommentAdapter.notifyDataSetChanged();
                    } else {
                        mToCommentList.remove(position);
                        mToCommentAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(ModeDetailNineGridActivity.this, "status:  " + object.getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(ModeDetailNineGridActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.btn_mode_nine_grid_comment, R.id.mode_b_save, R.id.mode_detail_nine_grid_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_mode_nine_grid_comment:
                comment(1, 0);
                break;
//            case R.id.mode_b_save:
//                saveComment();
//                break;
            case R.id.mode_detail_nine_grid_back:
                ToolKits.putInt(this, "fragment", 2);
                finish();
                break;
        }
    }


    //这边应该上传用户留言，然后刷新界面
    private void updateComment(String inf, int i, String position) {
        ModeComment comment = null;
        if (i == 1) {
//            comment = new ModeComment(666 + "", "张三" + 666, "http://d.hiphotos.baidu.com/image/h%3D360/sign=856d60650933874483c5297a610fd937/55e736d12f2eb938e81944c7d0628535e5dd6f8a.jpg", inf, "2015-03-04 23:02:06", null);
//            mCommentList.add(0, comment);
//            mCommentAdapter.notifyDataSetChanged();

            RequestParams params = new RequestParams();
            ECApplication application = (ECApplication) getApplication();
            params.addBodyParameter("sessionid", application.sessionId);
            params.addBodyParameter("tid", String.valueOf(mld.getId()));
            params.addBodyParameter("comment", inf);
            params.addBodyParameter("flag", "1");

            new HttpUtils().send(HttpRequest.HttpMethod.POST, CONST.COMMIT_COMMENT, params, new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    ResponseObject<String> object = new GsonBuilder().create().
                            fromJson(responseInfo.result, new TypeToken<ResponseObject<String>>() {
                            }.getType());
                    if (object.getStatus() == 410) {
                        Toast.makeText(ModeDetailNineGridActivity.this, "comment commit success", Toast.LENGTH_SHORT).show();
                        initCommentData(mld.getId(), false);
                        comments.setVisibility(View.GONE);
                        mCommentAdapter.notifyDataSetChanged();
                        comments.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ModeDetailNineGridActivity.this, "status:  " + object.getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    Toast.makeText(ModeDetailNineGridActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });


        } else if (i == 2) {
            RequestParams params = new RequestParams();
            ECApplication application = (ECApplication) getApplication();
            params.addBodyParameter("sessionid", application.sessionId);
            params.addBodyParameter("tid", position);
            params.addBodyParameter("comment", inf);
            params.addBodyParameter("flag", "0");

            new HttpUtils().send(HttpRequest.HttpMethod.POST, CONST.COMMIT_COMMENT, params, new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    ResponseObject<String> object = new GsonBuilder().create().
                            fromJson(responseInfo.result, new TypeToken<ResponseObject<String>>() {
                            }.getType());
                    if (object.getStatus() == 410) {
                        // Toast.makeText(ModeDetailNineGridActivity.this, "ToCommentStatus...: " + object.getStatus(), Toast.LENGTH_SHORT).show();
                        initCommentData(mld.getId(), true);
                        mCommentAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ModeDetailNineGridActivity.this, "ToCommentstatus:  " + object.getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    Toast.makeText(ModeDetailNineGridActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void comment(final int i, final int position) {

        if (i == 2)
            Toast.makeText(ModeDetailNineGridActivity.this, "size:  " + mCommentList.size(), Toast.LENGTH_SHORT).show();

        View editView = LayoutInflater.from(ModeDetailNineGridActivity.this).inflate(R.layout.mode_comment_reply_input, null);
        rootView = LayoutInflater.from(ModeDetailNineGridActivity.this).inflate(R.layout.activity_mode_detail_nine_grid, null);
        editWindow = new PopupWindow(editView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editWindow.setOutsideTouchable(true);
        editWindow.setFocusable(true);
        editWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        final EditText replyEdit = (EditText) editView.findViewById(R.id.mode_et_msg);
        replyEdit.setFocusable(true);
        replyEdit.requestFocus();
        // 以下两句不能颠倒
        editWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        editWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        editWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        // 显示键盘
        final InputMethodManager imm = (InputMethodManager) ModeDetailNineGridActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        editWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (imm.isActive()) imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
            }
        });


        Button btn = (Button) editView.findViewById(R.id.mode_b_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(replyEdit.getText())) {
                    info = replyEdit.getText().toString();
                    if (i == 2) {
                        //Toast.makeText(ModeDetailNineGridActivity.this,"id:  "+mCommentList.get(position).getContent(),Toast.LENGTH_SHORT).show();
                        updateComment(info, i, mCommentList.get(position).getId());
                    } else {
                        updateComment(info, i, "");
                    }
                    imm.hideSoftInputFromWindow(replyEdit.getWindowToken(), 0);
                    replyEdit.setText("");
                } else {
                    Toast.makeText(ModeDetailNineGridActivity.this, "请输入内容后在留言", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
