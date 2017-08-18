package com.huadi.android.ainiyo.entity;

/**
 * Created by fengsam on 17-8-18.
 */

public class FindingInfo {
    private String Id;
    private float summary;
    private float beauty;
    private float hobby;
    private float jobscore;
    private float residence;
    private float agescore;
    private float impression;
    private float emotion;
    private String name;
    private boolean isVip;
    private String avatar;
    private int age;
    private String job;

    public FindingInfo(String id, float summary, float beauty, float hobby, float jobscore, float residence, float agescore, float impression, float emotion, String name, boolean isVip, String avatar, int age, String job) {
        Id = id;
        this.summary = summary;
        this.beauty = beauty;
        this.hobby = hobby;
        this.jobscore = jobscore;
        this.residence = residence;
        this.agescore = agescore;
        this.impression = impression;
        this.emotion = emotion;
        this.name = name;
        this.isVip = isVip;
        this.avatar = avatar;
        this.age = age;
        this.job = job;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public float getSummary() {
        return summary;
    }

    public void setSummary(float summary) {
        this.summary = summary;
    }

    public float getBeauty() {
        return beauty;
    }

    public void setBeauty(float beauty) {
        this.beauty = beauty;
    }

    public float getHobby() {
        return hobby;
    }

    public void setHobby(float hobby) {
        this.hobby = hobby;
    }

    public float getJobscore() {
        return jobscore;
    }

    public void setJobscore(float jobscore) {
        this.jobscore = jobscore;
    }

    public float getResidence() {
        return residence;
    }

    public void setResidence(float residence) {
        this.residence = residence;
    }

    public float getAgescore() {
        return agescore;
    }

    public void setAgescore(float agescore) {
        this.agescore = agescore;
    }

    public float getImpression() {
        return impression;
    }

    public void setImpression(float impression) {
        this.impression = impression;
    }

    public float getEmotion() {
        return emotion;
    }

    public void setEmotion(float emotion) {
        this.emotion = emotion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
