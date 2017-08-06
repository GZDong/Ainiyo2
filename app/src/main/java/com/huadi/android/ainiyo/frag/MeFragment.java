package com.huadi.android.ainiyo.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.LoginActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class MeFragment extends Fragment {
    @ViewInject(R.id.Login1)
    private Button Login1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_me,null);
        // Inflate the layout for this fragment
        ViewUtils.inject(this,view);
        return view;
    }
    @OnClick({R.id.Login1})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.Login1:
                startActivity(new Intent(getActivity(),LoginActivity.class));
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
