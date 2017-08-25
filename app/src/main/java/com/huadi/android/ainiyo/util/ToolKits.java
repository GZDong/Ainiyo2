package com.huadi.android.ainiyo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.ModeInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ToolKits {
	
	public static SharedPreferences getSharedPreferences(Context context){
		return context.getSharedPreferences("modeInfoList", Context.MODE_PRIVATE);
	}

	public static void SavingModeData(Context context,String key,ArrayList<ModeInfo> list)
	{
		List<ModeInfo> modeInfoList = list;
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(modeInfoList);
		Log.d("hello", "saved json is "+ json);
		editor.putString(key, json);
		editor.commit();
	}

	public static void AppendingModeData(Context context,String key,ModeInfo modeInfo)
	{
		List<ModeInfo> modeInfoList = GettingModedata(context,key);
		modeInfoList.add(modeInfo);
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(modeInfoList);
		editor.remove("modeInfoLst");
		editor.putString(key, json);
		editor.commit();
	}

	public static void DeletingModeData(Context context,String key,int position)
	{
		List<ModeInfo> modeInfoList = GettingModedata(context,key);
		modeInfoList.remove(position);
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(modeInfoList);
		editor.remove("modeInfoLst");
		editor.putString(key, json);
		editor.commit();
	}


	public static ArrayList<ModeInfo> GettingModedata(Context context,String key)
	{
		ArrayList<ModeInfo> list= new ArrayList<ModeInfo>();
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		String json = sharedPreferences.getString(key, null);
		if (json != null)
		{
			Gson gson = new Gson();
			Type type = new TypeToken<List<ModeInfo>>(){}.getType();
			list = gson.fromJson(json, type);
//            for(int i = 0; i < list.size(); i++)
//            {
//                Log.d("hi", list.get(i).getName()+":" + list.get(i).getX() + "," + list.get(i).getY());
//            }
		}
		return list;
	}

	public static void putInteger(Context context, String key, ArrayList<Integer> value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(value);
		editor.putString(key, json);
		editor.commit();
	}

    public static void appendInteger(Context context, String key, ArrayList<Integer> value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<Integer> mList = fetchInteger(context, key);
        value.addAll(mList);
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    }

    public static ArrayList<Integer> fetchInteger(Context context, String key) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		String json = sharedPreferences.getString(key, null);
		if (json != null) {
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<Integer>>() {
			}.getType();
			list = gson.fromJson(json, type);
		}
		return list;
	}

	public static void putInt(Context context,String key,int value){
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int fetchInt(Context context,String key,int defaultValue){
		return getSharedPreferences(context).getInt(key,defaultValue);
	}
	
	public static void putBooble(Context context,String key,boolean value){
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public static boolean fetchBooble(Context context,String key,boolean defaultValue){
		return getSharedPreferences(context).getBoolean(key, defaultValue);
	}
	
	public static void putString(Context context,String key,String value){
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String fetchString(Context context,String key){
		return getSharedPreferences(context).getString(key, null);
	}
	
	public static void clearShare(Context context){
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor= sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

    /**
     * 通过反射的方式获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context mContext) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return mContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

//	public static void immersion(Context mContext)
//	{
//		//设置状态栏沉浸
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			//透明状态栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			//透明导航栏
//			LinearLayout linear_bar = (LinearLayout) mContext.findViewById(R.id.status_bar_main);
//			linear_bar.setVisibility(View.VISIBLE);
//			//获取到状态栏的高度
//			int statusHeight = ToolKits.getStatusBarHeight(mContext);
//			//动态的设置隐藏布局的高度
//			linear_bar.getLayoutParams().height = statusHeight;
//		}
//	}
}
