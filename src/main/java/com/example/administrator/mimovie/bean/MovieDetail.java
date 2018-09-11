package com.example.administrator.mimovie.bean;

import java.util.List;

/**
 * Created by YangYi on 2018/1/7.
 */

public class MovieDetail {
    private int movieId;            //影片Id
    private String movieName;       //影片名称
    private String movieImgUrl;     //影片海报
    private List<String> type;      //影片类型
    private boolean isHot;          //是否为正在热映
    private String mins;            //影片时长
    private double rating;          //影片评分
    private String releaseDate;     //上映日期
    private String director;        //导演
    private List<String> actors;    //演员列表
    private List<String> roles;     //角色列表
    private String story;       //剧情简介
    private String videoUrl;    //预告连接
    private List<String> stageImgUrl; //剧照连接

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieImgUrl() {
        return movieImgUrl;
    }

    public void setMovieImgUrl(String movieImgUrl) {
        this.movieImgUrl = movieImgUrl;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public String getMins() {
        return mins;
    }

    public void setMins(String mins) {
        this.mins = mins;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<String> getStageImgUrl() {
        return stageImgUrl;
    }

    public void setStageImgUrl(List<String> stageImgUrl) {
        this.stageImgUrl = stageImgUrl;
    }

    @Override
    public String toString() {
        return "MovieDetail{" +
                "movieId=" + movieId +
                ", movieName='" + movieName + '\'' +
                ", movieImgUrl='" + movieImgUrl + '\'' +
                ", type=" + type +
                ", isHot=" + isHot +
                ", mins='" + mins + '\'' +
                ", rating=" + rating +
                ", releaseDate='" + releaseDate + '\'' +
                ", director='" + director + '\'' +
                ", actors=" + actors +
                ", roles=" + roles +
                ", story='" + story + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", stageImgUrl=" + stageImgUrl +
                '}';
    }
}
