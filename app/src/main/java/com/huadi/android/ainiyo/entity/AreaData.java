package com.huadi.android.ainiyo.entity;

/**
 * Created by xiaoxing on 2017/8/23.
 */

public class AreaData {
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getProvinceid() {
        return Provinceid;
    }

    public void setProvinceid(int provinceid) {
        Provinceid = provinceid;
    }

    public int getCountryid() {
        return Countryid;
    }

    public void setCountryid(int countryid) {
        Countryid = countryid;
    }

    public int getCountyid() {
        return Countyid;
    }

    public void setCountyid(int countyid) {
        Countyid = countyid;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    private int Id;
    private int Provinceid;
    private int Countryid;
    private int Countyid;
    private String Province;
    private String Country;
    private String County;
}
