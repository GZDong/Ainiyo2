package com.huadi.android.ainiyo.entity;

/**
 * Created by xiaoxing on 2017/8/17.
 */

public class UserData {


    private int Id;
    private boolean Vip;
    private String birthday;
    private int Area;
    private String Job;
    private double Salary;
    private Boolean HaveKids;
    private Boolean parentsalive;
    private String maritallstatus;
    private String Emotion;
    private String Hobby;
    private String Requir;
    private String avatar;
    private int Userid;


    public UserData(int Id, boolean Vip, String birthday, int Area, String Job, double Salary, Boolean HaveKids, Boolean parentsalive, String maritallstatus, String Emotion, String Hobby, String Requir, String avatar, int Userid) {
        this.Id = Id;
        this.Vip = Vip;
        this.birthday = birthday;
        this.Area = Area;
        this.Job = Job;
        this.Salary = Salary;
        this.HaveKids = HaveKids;
        this.parentsalive = parentsalive;
        this.maritallstatus = maritallstatus;
        this.Emotion = Emotion;
        this.Hobby = Hobby;
        this.Requir = Requir;
        this.avatar = avatar;
        this.Userid = Userid;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }


    public boolean isVip() {
        return Vip;
    }

    public void setVip(boolean vip) {
        Vip = vip;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getArea() {
        return Area;
    }

    public void setArea(int area) {
        Area = area;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }

    public double getSalary() {
        return Salary;
    }

    public void setSalary(double salary) {
        Salary = salary;
    }

    public Boolean getHaveKids() {
        return HaveKids;
    }

    public void setHaveKids(Boolean haveKids) {
        HaveKids = haveKids;
    }

    public Boolean getParentsalive() {
        return parentsalive;
    }

    public void setParentsalive(Boolean parentsalive) {
        this.parentsalive = parentsalive;
    }

    public String getMaritallstatus() {
        return maritallstatus;
    }

    public void setMaritallstatus(String maritallstatus) {
        this.maritallstatus = maritallstatus;
    }

    public String getEmotion() {
        return Emotion;
    }

    public void setEmotion(String emotion) {
        Emotion = emotion;
    }

    public String getHobby() {
        return Hobby;
    }

    public void setHobby(String hobby) {
        Hobby = hobby;
    }

    public String getRequir() {
        return Requir;
    }

    public void setRequir(String requir) {
        Requir = requir;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUserid() {
        return Userid;
    }

    public void setUserid(int userid) {
        Userid = userid;
    }


}
