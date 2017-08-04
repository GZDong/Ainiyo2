package com.huadi.android.ainiyo.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by rxvincent on 2017/7/31.
 */

public class InformationActivity extends AppCompatActivity {
    @ViewInject(R.id.behavior)
    Button behavior;

    @ViewInject(R.id.profile_image)
    ImageView avatar;

    @ViewInject(R.id.background)
    ImageView background;

    @ViewInject(R.id.nameField)
    TextView name;

    @ViewInject(R.id.signField)
    TextView sign;

    @ViewInject(R.id.list)
    ListView listView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        LoadResources(getIntent().getExtras());
    }


    private void LoadResources(Bundle bundle){
        avatar.setImageBitmap(BitmapFactory.decodeFile(bundle.getString("profile_image")));
        background.setImageBitmap(BitmapFactory.decodeFile(bundle.getString("background")));
        name.setText(bundle.getString("name"));
        sign.setText(bundle.getString("sign"));


        String[] data = bundle.getStringArray("information");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(InformationActivity.this,R.layout.support_simple_spinner_dropdown_item,data);

        listView.setAdapter(arrayAdapter);

        behavior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO
            }
        });
    }
}

