package com.huadi.android.ainiyo.entity;

import java.io.Serializable;

/**
 * Created by fengsam on 17-8-18.
 */

public class FindingInfo implements Serializable {
    private String Id;
    private float Summary;
    private float Beauty;
    private float Hobby;
    private float Jobscore;
    private float Residence;
    private float Agescore;
    private float Impression;
    private float Emotion;
    private String Name;
    private boolean IsVip;
    private String Avatar;
    private int Age;
    private String Job;

    public FindingInfo(String id, float summary, float beauty, float hobby, float jobscore, float residence, float agescore, float impression, float emotion, String name, boolean isVip, String avatar, int age, String job) {
        Id = id;
        Summary = summary;
        Beauty = beauty;
        Hobby = hobby;
        Jobscore = jobscore;
        Residence = residence;
        Agescore = agescore;
        Impression = impression;
        Emotion = emotion;
        Name = name;
        IsVip = isVip;
        Avatar = avatar;
        Age = age;
        Job = job;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public float getSummary() {
        return Summary;
    }

    public void setSummary(float summary) {
        Summary = summary;
    }

    public float getBeauty() {
        return Beauty;
    }

    public void setBeauty(float beauty) {
        Beauty = beauty;
    }

    public float getHobby() {
        return Hobby;
    }

    public void setHobby(float hobby) {
        Hobby = hobby;
    }

    public float getJobscore() {
        return Jobscore;
    }

    public void setJobscore(float jobscore) {
        Jobscore = jobscore;
    }

    public float getResidence() {
        return Residence;
    }

    public void setResidence(float residence) {
        Residence = residence;
    }

    public float getAgescore() {
        return Agescore;
    }

    public void setAgescore(float agescore) {
        Agescore = agescore;
    }

    public float getImpression() {
        return Impression;
    }

    public void setImpression(float impression) {
        Impression = impression;
    }

    public float getEmotion() {
        return Emotion;
    }

    public void setEmotion(float emotion) {
        Emotion = emotion;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isVip() {
        return IsVip;
    }

    public void setVip(boolean vip) {
        IsVip = vip;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }
}
