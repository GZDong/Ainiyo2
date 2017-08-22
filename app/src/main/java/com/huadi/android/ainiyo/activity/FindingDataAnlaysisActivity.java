package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.adapter.WRDAdapter;
import com.huadi.android.ainiyo.entity.FindingInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.timqi.windrosediagram.WindRoseClickListener;
import com.timqi.windrosediagram.WindRoseDiagramView;

import java.text.DecimalFormat;

public class FindingDataAnlaysisActivity extends AppCompatActivity {


    @ViewInject(R.id.windRoseDiagramView)
    WindRoseDiagramView windRoseDiagramView;
    @ViewInject(R.id.tv_finding_data_job_score)
    TextView tv_finding_job_score;
    @ViewInject(R.id.tv_finding_data_age_score)
    TextView tv_finding_data_age_score;
    @ViewInject(R.id.tv_finding_data_character_score)
    TextView tv_finding_data_character_score;
    @ViewInject(R.id.tv_finding_data_emotion_score)
    TextView tv_finding_data_emotion_score;
    @ViewInject(R.id.tv_finding_data_hobby_score)
    TextView tv_finding_data_hobby_score;
    @ViewInject(R.id.tv_finding_data_live_score)
    TextView tv_finding_data_live_score;
    @ViewInject(R.id.tv_finding_data_beauty_score)
    TextView tv_finding_data_beauty_score;


    //FindingInfo fi = new FindingInfo("", 0.60f, 0.94f, 0.90f, 0.80f, 0.70f, 0.30f, 0.33f, 0.94f, "", true, "", 1, "");
    FindingInfo fi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_data_anlaysis);
        ViewUtils.inject(this);

        final Intent intent = getIntent();
        fi = (FindingInfo) intent.getSerializableExtra("findingdataitem");

        //Toast.makeText(FindingDataAnlaysisActivity.this,fi.getName(),Toast.LENGTH_SHORT).show();

        DecimalFormat df = new DecimalFormat("#.##");

        WRDAdapter adapter = new WRDAdapter(fi);
        windRoseDiagramView.setAdapter(adapter);
        windRoseDiagramView.setAnchorWidth(5);
        windRoseDiagramView.setStartAngle(0);


        windRoseDiagramView.setWindRoseClickListener(windRoseClickListener);
        windRoseDiagramView.setOnClickListener(normalClickListener);


        tv_finding_data_age_score.setText(String.valueOf(df.format(fi.getAgescore())));
        tv_finding_data_character_score.setText(String.valueOf(df.format(fi.getImpression())));
        tv_finding_data_emotion_score.setText(String.valueOf(df.format(fi.getEmotion())));
        tv_finding_data_hobby_score.setText(String.valueOf(df.format(fi.getHobby())));
        tv_finding_data_live_score.setText(String.valueOf(df.format(fi.getResidence())));
        tv_finding_job_score.setText(String.valueOf(df.format(fi.getJobscore())));
        tv_finding_data_beauty_score.setText(String.valueOf(df.format(fi.getBeauty())));
    }

    private WindRoseClickListener windRoseClickListener
            = new WindRoseClickListener() {
        @Override
        public void onItemClick(int position) {
            finish();
        }
    };

    private View.OnClickListener normalClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @OnClick({R.id.finding_data_background})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finding_data_background:
                finish();
                break;
        }
    }

}
