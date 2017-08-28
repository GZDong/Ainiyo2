package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hp.hpl.sparta.Text;
import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ActivityDetil extends AppCompatActivity {
    @ViewInject(R.id.title_add)
    private TextView title_add;
    @ViewInject(R.id.date_add)
    private TextView date_add;
    @ViewInject(R.id.article_add)
    private TextView article_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil);
        ImageView back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(ActivityDetil.this, ActivityActivity.class));
            }
        });

        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String date=intent.getStringExtra("date");
        String article=intent.getStringExtra("article");
        title_add.setText(title);
        date_add.setText(date);
        article_add.setText(article);
    }
}
