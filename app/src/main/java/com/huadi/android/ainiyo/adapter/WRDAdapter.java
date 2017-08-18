package com.huadi.android.ainiyo.adapter;

import com.huadi.android.ainiyo.entity.FindingInfo;
import com.timqi.windrosediagram.WindRoseDiagramAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengsam on 17-8-18.
 */

public class WRDAdapter extends WindRoseDiagramAdapter {

    ArrayList<Map<String, Object>> dataList;

    public WRDAdapter(FindingInfo fi) {
        this.dataList = buildData(fi);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getName(int position) {
        return (String) dataList.get(position).get("name");
    }

    @Override
    public float getPercent(int position) {
        return (float) dataList.get(position).get("percent");
    }

    private ArrayList<Map<String, Object>> buildData(FindingInfo fi) {

        ArrayList<Map<String, Object>> result = new ArrayList<>();

        Map<String, Object> map0 = new HashMap<>();
        map0.put("name", "爱好");
        map0.put("percent", fi.getHobby());
        result.add(map0);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "情感");
        map1.put("percent", fi.getEmotion());
        result.add(map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "工作");
        map2.put("percent", fi.getJobscore());
        result.add(map2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("name", "性格");
        map3.put("percent", fi.getImpression());
        result.add(map3);

        Map<String, Object> map4 = new HashMap<>();
        map4.put("name", "颜值");
        map4.put("percent", fi.getBeauty());
        result.add(map4);

        Map<String, Object> map5 = new HashMap<>();
        map5.put("name", "住处");
        map5.put("percent", fi.getResidence());
        result.add(map4);


        return result;
    }
}