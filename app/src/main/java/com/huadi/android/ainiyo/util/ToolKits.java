package com.huadi.android.ainiyo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huadi.android.ainiyo.entity.ModeInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ToolKits {
	
	public static SharedPreferences getSharedPreferences(Context context){
		return context.getSharedPreferences("modeInfoList", Context.MODE_PRIVATE);
	}

	public static void SavingModeData(Context context,ArrayList<ModeInfo> list)
	{
		List<ModeInfo> modeInfoList = list;
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(modeInfoList);
		Log.d("hello", "saved json is "+ json);
		editor.putString("modeInfoList", json);
		editor.commit();
	}

	public static void AppendingModeData(Context context,ModeInfo modeInfo)
	{
		List<ModeInfo> modeInfoList = GettingModedata(context);
		modeInfoList.add(modeInfo);
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(modeInfoList);
		editor.remove("modeInfoLst");
		editor.putString("modeInfoList", json);
		editor.commit();
	}

	public static void DeletingModeData(Context context,int position)
	{
		List<ModeInfo> modeInfoList = GettingModedata(context);
		modeInfoList.remove(position);
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(modeInfoList);
		editor.remove("modeInfoLst");
		editor.putString("modeInfoList", json);
		editor.commit();
	}


	public static ArrayList<ModeInfo> GettingModedata(Context context)
	{
		ArrayList<ModeInfo> list= new ArrayList<ModeInfo>();
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		String json = sharedPreferences.getString("modeInfoList", null);
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

}
