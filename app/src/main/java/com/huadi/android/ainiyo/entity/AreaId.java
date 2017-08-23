package com.huadi.android.ainiyo.entity;

/**
 * Created by xiaoxing on 2017/8/23.
 */

public class AreaId {
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getProvince() {
        return Province;
    }

    public void setProvince(int province) {
        Province = province;
    }

    public int getCountry() {
        return Country;
    }

    public void setCountry(int country) {
        Country = country;
    }

    public int getCounty() {
        return County;
    }

    public void setCounty(int county) {
        County = county;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private int Id;
    private int Province;
    private int Country;
    private int County;
    private String Name;

}
