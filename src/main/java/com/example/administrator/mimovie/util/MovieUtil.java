package com.example.administrator.mimovie.util;



import com.example.administrator.mimovie.bean.MovieDetail;
import com.example.administrator.mimovie.bean.MoviePreview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 影片工具类
 * 用以获取影片的相关信息
 * Created by YangYi on 2018/1/7.
 */
public class MovieUtil {

    /**
     * 获取正在热映的单个影片的预览信息
     * @param jsonObject:预告影片的Json对象
     * @return 返回影片预告对象MoviePreview
     */
    public static MoviePreview getMoviePreview_now(JSONObject jsonObject) {

        MoviePreview moviePreview = new MoviePreview();

        try {
            int movieId = jsonObject.getInt("id");              //影片id
            String movieName = jsonObject.getString("t");       //影片名字
            String movieImgUrl = jsonObject.getString("img");   //影片海报URL
            double rating = jsonObject.getDouble("r");         //影片评分
            String director = jsonObject.getString("dN");      //导演
            List<String> actors = new ArrayList<>();                  //影员列表
            String actor1 = jsonObject.getString("aN1");       //影员1
            actors.add(actor1);
            String actor2 = jsonObject.getString("aN2");       //影员1
            actors.add(actor2);

            //获取影片上映日期
            String releaseDate =  getDataStr(jsonObject.get("rd").toString());//换换成对应格式的日期

            moviePreview.setMovieId(movieId);
            moviePreview.setMovieName(movieName);
            moviePreview.setMovieImgUrl(movieImgUrl);
            moviePreview.setRating(rating);
            moviePreview.setDirector(director);
            moviePreview.setActors(actors);
            moviePreview.setReleaseDate(releaseDate);

            return moviePreview;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return moviePreview;
    }

    /**
     * 获取单个即将上映影片的预览信息
     * @param jsonObject：影片的json对象
     * @return MoviePreview：返回影片预览对象
     */
    public static MoviePreview getMoviePreview_will(JSONObject jsonObject){
        MoviePreview moviePreview = new MoviePreview();

        try {
            int movieId = jsonObject.getInt("id");              //影片id
            String movieName = jsonObject.getString("title");       //影片名字
            String movieImgUrl = jsonObject.getString("image");   //影片海报URL
            double rating = -1 ;                                        //影片评分,影片尚未上映，故没有评分
            String director = jsonObject.getString("director");   //导演
            List<String> actors = new ArrayList<>();                  //影员列表
            String actor1 = jsonObject.getString("actor1");       //影员1
            actors.add(actor1);
            String actor2 = jsonObject.getString("actor2");       //影员1
            actors.add(actor2);

            String yyyy = jsonObject.get("rYear").toString();
            int month = jsonObject.getInt("rMonth");
            String MM = null;
            if (month < 10){
                MM = "0" + month;
            }else {
                MM = month + "";
            }
            int day = jsonObject.getInt("rDay");
            String dd = null;
            if (day < 10){
                dd = "0" + day;
            }else {
                dd = day + "";
            }

            String dateStr = yyyy + MM + dd;
            //获取影片上映日期
            String releaseDate = getDataStr(dateStr);//换换成对应格式的日期

            moviePreview.setMovieId(movieId);
            moviePreview.setMovieName(movieName);
            moviePreview.setMovieImgUrl(movieImgUrl);
            moviePreview.setRating(rating);
            moviePreview.setDirector(director);
            moviePreview.setActors(actors);
            moviePreview.setReleaseDate(releaseDate);

            return moviePreview;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return moviePreview;
    }

    /**
     * 获取单个影片的详细信息
     * @param jsonObject：影片的json对象
     * @return MovieDetail：返回详情对象
     */
    public static MovieDetail getMovieDetail(JSONObject jsonObject){
        MovieDetail movieDetail = new MovieDetail();

        try {
            int movieId = jsonObject.getInt("movieId");         //影片Id
            String movieName = jsonObject.getString("name");    //影片名称
            String movieImgUrl = jsonObject.getString("img");  //影片海报

            List<String> type = new ArrayList<>();                     //获取影片类型
            JSONArray typeArray = jsonObject.getJSONArray("type");
            for (int i = 0; i < typeArray.length(); i++){
                String typeName = typeArray.get(i).toString();
                type.add(typeName);
            }

            double rating = jsonObject.getDouble("overallRating");   //影片评分

            boolean isHot = false;      //是否为正在热映
            if (rating > 0){
                isHot = true;
            }

            String mins = jsonObject.getString("mins");        //影片时长
            String releaseDate = getDataStr(jsonObject.getString("releaseDate"));   //上映时间
            String director = jsonObject.getJSONObject("director").getString("name");    //导演

            List<String> actors = new ArrayList<>();    //演员列表
            List<String> roles = new ArrayList<>();     //角色列表
            JSONArray actorArray = jsonObject.getJSONArray("actors");
            for (int i = 0; i < actorArray.length(); i++){
                JSONObject actor = (JSONObject) actorArray.get(i);
                String actorName = actor.getString("name");
                String roleName = actor.getString("roleName");
                actors.add(actorName);      //获取演员名字
                roles.add(roleName);        //获取角色名字
            }

            String story = jsonObject.getString("story");       //剧情简介
            String videoUrl = jsonObject.getJSONObject("video").getString("url"); //预告连接（low画质）

            List<String> stageImgUrl = new ArrayList<>(); //剧照连接
            JSONArray imgArray = jsonObject.getJSONObject("stageImg").getJSONArray("list");
            for (int i = 0; i < imgArray.length(); i++){
                JSONObject img = (JSONObject) imgArray.get(i);
                String imgUrl = img.getString("imgUrl");
                stageImgUrl.add(imgUrl);
            }

            movieDetail.setMovieId(movieId);
            movieDetail.setMovieName(movieName);
            movieDetail.setMovieImgUrl(movieImgUrl);
            movieDetail.setType(type);
            movieDetail.setHot(isHot);
            movieDetail.setMins(mins);
            movieDetail.setRating(rating);
            movieDetail.setReleaseDate(releaseDate);
            movieDetail.setDirector(director);
            movieDetail.setActors(actors);
            movieDetail.setRoles(roles);
            movieDetail.setStory(story);
            movieDetail.setVideoUrl(videoUrl);
            movieDetail.setStageImgUrl(stageImgUrl);

            return movieDetail;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieDetail;
    }



    /**
     * 将20180101这种日期格式转成2018-01-01
     * @param str
     * @return
     */
    private static String getDataStr(String str){
        String dateStr = null;
        String yyyy = null;
        String MM = null;
        String dd = null;
        if(str != null && str.length() == 8){
            yyyy = str.substring(0,4);     //获取年
            MM = str.substring(4,6);     //获取月
            dd = str.substring(6,str.length());     //获取月

            dateStr = yyyy + "-" + MM + "-" + dd;
        }else {
            dateStr = "未知";
        }
        return dateStr;
    }


}
