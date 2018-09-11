package com.example.administrator.mimovie.bean;

import java.util.List;

/**
 * Created by YangYi on 2018/1/7.
 */

public class MoviePreview {
    private int movieId;            //影片Id
    private String movieName;       //影片名称
    private String movieImgUrl;     //影片海报
    private double rating;          //影片评分
    private String director;        //导演
    private List<String> actors;    //演员
    private String releaseDate;     //上映时间

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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "MoviePreview{" +
                "movieId=" + movieId +
                ", movieName='" + movieName + '\'' +
                ", movieImgUrl='" + movieImgUrl + '\'' +
                ", rating=" + rating +
                ", director='" + director + '\'' +
                ", actors=" + actors +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
