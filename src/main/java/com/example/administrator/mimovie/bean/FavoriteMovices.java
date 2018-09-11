package com.example.administrator.mimovie.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by yuanye on 2018/1/5.
 */

public class FavoriteMovices extends DataSupport {
    private int movicesid;  //电影id
    private String tCn;     //中文标题
    private String dN;      //导演
    private String img;     //图片地址
    private String actors;  //演员
    private double r;       //电影评分

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "FavoriteMovices{" +
                "movicesid=" + movicesid +
                ", tCn='" + tCn + '\'' +
                ", dN='" + dN + '\'' +
                ", img='" + img + '\'' +
                ", actors='" + actors + '\'' +
                ", r=" + r +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
    private String releaseDate;//上映时间

    public int getMovicesid() {
        return movicesid;
    }

    public void setMovicesid(int movicesid) {
        this.movicesid = movicesid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public String gettCn() {
        return tCn;
    }

    public void settCn(String tCn) {
        this.tCn = tCn;
    }

    public String getdN() {
        return dN;
    }

    public void setdN(String dN) {
        this.dN = dN;
    }


}
