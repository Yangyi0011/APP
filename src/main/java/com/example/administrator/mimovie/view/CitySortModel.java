package com.example.administrator.mimovie.view;

public class CitySortModel {

    private String name;//显示的数据
    private String sortLetters;//显示数据拼音的首字母
    private int cityId;//设置城市id

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    @Override
    public String toString() {
        return "CitySortModel{" +
                "name='" + name + '\'' +
                ", sortLetters='" + sortLetters + '\'' +
                ", cityId=" + cityId +
                '}';
    }
}
