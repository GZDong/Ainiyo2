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

/**
 * Created by rxvincent on 2017/7/31.
 */

public class InformationActivity extends AppCompatActivity {
    Button behavior = (Button)findViewById(R.id.behavior);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        LoadResources(getIntent().getExtras());
    }


    private void LoadResources(Bundle bundle){

        ImageView avatar = (ImageView)findViewById(R.id.profile_image);
        avatar.setImageBitmap(BitmapFactory.decodeFile(bundle.getString("profile_image")));

        ImageView background = (ImageView)findViewById(R.id.background);
        background.setImageBitmap(BitmapFactory.decodeFile(bundle.getString("background")));

        TextView name = (TextView)findViewById(R.id.nameField);
        name.setText(bundle.getString("name"));

        TextView sign = (TextView)findViewById(R.id.signField);
        sign.setText(bundle.getString("sign"));


        String[] data = bundle.getStringArray("information");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(InformationActivity.this,R.layout.support_simple_spinner_dropdown_item,data);
        ListView listView = (ListView)findViewById(R.id.list);
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

