package com.huadi.android.ainiyo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.adapter.WRDAdapter;
import com.huadi.android.ainiyo.entity.FindingInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.timqi.windrosediagram.WindRoseClickListener;
import com.timqi.windrosediagram.WindRoseDiagramView;

public class FindingDataAnlaysisActivity extends AppCompatActivity {


    @ViewInject(R.id.windRoseDiagramView)
    WindRoseDiagramView windRoseDiagramView;
    FindingInfo fi = new FindingInfo("", 0.60f, 0.94f, 0.90f, 0.80f, 0.70f, 0.30f, 0.33f, 0.94f, "", true, "", 1, "");
    WRDAdapter adapter = new WRDAdapter(fi);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_data_anlaysis);
        ViewUtils.inject(this);

        windRoseDiagramView.setAdapter(adapter);
        windRoseDiagramView.setOutlineWidth(3);
        windRoseDiagramView.setAnchorWidth(5);
        windRoseDiagramView.setStartAngle(0);

        windRoseDiagramView.setWindRoseClickListener(windRoseClickListener);
        windRoseDiagramView.setOnClickListener(normalClickListener);

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
