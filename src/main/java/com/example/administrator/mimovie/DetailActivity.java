package com.example.administrator.mimovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;

import com.example.administrator.mimovie.adapter.MovieComment_Adapter;
import com.example.administrator.mimovie.adapter.MovieInfo_Adapter;
import com.example.administrator.mimovie.adapter.MovieStills_Adapter;
import com.example.administrator.mimovie.bean.Comment;
import com.example.administrator.mimovie.bean.FavoriteMovices;
import com.example.administrator.mimovie.bean.MovieDetail;
import com.example.administrator.mimovie.util.MovieUtil;
import com.example.administrator.mimovie.util.okHttpUtils.CallBackUtil;
import com.example.administrator.mimovie.util.okHttpUtils.OkhttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.administrator.mimovie.util.CommentUtil.getComment;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    private static List<MovieDetail> movieDetailList = new ArrayList<>() ;    //影片详细信息
    private static List<Comment> movieCommentList = new ArrayList<>();//影片评论信息
    private List<MovieDetail> movieStillsList = new ArrayList<>();//影片剧照信息

    private MovieInfo_Adapter movieInfo_adapter;
    private MovieComment_Adapter movieComment_adapter;
    private MovieStills_Adapter movieStills_Adapter;

    private RecyclerView movieInfoView;
    private RecyclerView movieiCommentView;
    private RecyclerView movieStillsView;
    private ImageView collectionImg;
    private boolean liked;
    private int movieId;
    private int cityId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent intent = getIntent();
        movieId = Integer.parseInt( intent.getStringExtra("moveId") );/*电影id*/
        cityId = Integer.parseInt( intent.getStringExtra("cityId") );/*城市id*/


        Log.i(TAG, "onCreate: ----cityId----"+ cityId +"----moveId----"+ movieId);



        LitePal.getDatabase(); //建立数据库

        //1.获取布局文件
        movieInfoView = (RecyclerView) findViewById(R.id.movieInfo);
        movieiCommentView = (RecyclerView) findViewById(R.id.movieComment);
        movieStillsView = (RecyclerView) findViewById(R.id.movieStills);
        initView();//获取


        //2.创建布局管理器并设置布局方式
        LinearLayoutManager manager = new LinearLayoutManager(DetailActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);   //竖直布局
        LinearLayoutManager manager1 = new LinearLayoutManager(DetailActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager manager2 = new LinearLayoutManager(DetailActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        //3.让布局文件加载布局管理器
        movieInfoView.setLayoutManager(manager);
        movieiCommentView.setLayoutManager(manager1);
        movieStillsView.setLayoutManager(manager2);

        //4.创建适配器
        movieInfo_adapter = new MovieInfo_Adapter(DetailActivity.this,movieDetailList);
        movieComment_adapter = new MovieComment_Adapter(DetailActivity.this,movieCommentList);
        movieStills_Adapter = new MovieStills_Adapter(DetailActivity.this,new ArrayList<String>());

        //5.让布局文件加载适配器
        movieInfoView.setAdapter(movieInfo_adapter);
        movieiCommentView.setAdapter(movieComment_adapter);
        movieStillsView.setAdapter(movieStills_Adapter);

        //6.获取数据源
        getMovieDetail(cityId,movieId);//获取影片详情
        getComments(movieId);//获取评论
        getMovieStills(cityId,movieId);//获取剧照

    }

    /**
     * 获取影片详情
     * 需要传入两个参数
     * @param cityId：城市id，从前台传过来
     * @param movieId:影片id，从前台传过来
     */
    public void getMovieDetail(final int cityId, final int movieId) {

        String url = "https://ticket-api-m.mtime.cn/movie/detail.api?locationId="+ cityId +"&movieId=" + movieId ;
        OkhttpUtil.okHttpGet(url, new CallBackUtil<MovieDetail>() {
            @Override
            public MovieDetail onParseResponse(Call call, Response response) {
                //response 转换成  List<MovieDetail>
                //content 就是返回的json字符串
                MovieDetail movieDetail = null;
                String content = null;
                try {
                    content = response.body().string();
                    JSONObject object = new JSONObject(content);
                    JSONObject data = object.getJSONObject("data");     //从content里取出data
                    JSONObject movieInfo = (JSONObject) data.get("basic");  //从data里提取影片信息
                    movieDetail = MovieUtil.getMovieDetail(movieInfo);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return movieDetail;
            }

            @Override
            public void onFailure(Call call, Exception e) {
                Log.i(TAG,"getMovieDetail：请求失败！！！！！！！！！！");

            }

            @Override
            public void onResponse(MovieDetail response) {
                //1.通过回调方法，拿到返回的数据源
                List<MovieDetail> list = new ArrayList<>();
                list.add(response);

                //2.将海报简介数据源和movieInfoAdap_adapter进行绑定
                MovieInfo_Adapter adapter = new MovieInfo_Adapter(DetailActivity.this,list);
                movieInfoView.setAdapter(adapter);

                //收藏函数，获得电影当前收藏状态
                islike(response);
            }
        });
    }

    /**
     * 获取评论详情
     * @param movieId 电影
     */
    private void getComments( int movieId){
        String url = "https://ticket-api-m.mtime.cn/movie/hotComment.api?movieId=" + movieId;
        OkhttpUtil.okHttpGet(url, new CallBackUtil<List<Comment>>() {

            @Override
            public List<Comment> onParseResponse(Call call, Response response) {

                List<Comment> commentList = new ArrayList<>();
                String content = null;
                try {
                    content = response.body().string();
                    JSONObject jsonObject = new JSONObject(content);
                    JSONObject data = jsonObject.getJSONObject("data").getJSONObject("mini");
                    JSONArray commentArray = data.getJSONArray("list");
                    for (int i = 0; i < commentArray.length(); i ++){
                        JSONObject commentObj = (JSONObject) commentArray.get(i);
                        Comment comment = getComment(commentObj);
                        commentList.add(comment);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return commentList;
            }

            @Override
            public void onFailure(Call call, Exception e) {
                Log.i(TAG,"getComments：请求出错了！！！！！" );

            }

            @Override
            public void onResponse(List<Comment> response) {

                List<Comment> commentList = response;

                //1.通过回调方法获取数据源绑定到MovieComment_Adapter
                MovieComment_Adapter adapter = new MovieComment_Adapter(DetailActivity.this,commentList);
                movieiCommentView.setAdapter(adapter);
            }
        });
    }

    /**
     * 获取剧照详情
     * @param cityId 城市id
     * @param movieId 电影id
     */
    public void getMovieStills (final int cityId, final int movieId) {

        String url = "https://ticket-api-m.mtime.cn/movie/detail.api?locationId="+ cityId +"&movieId=" + movieId ;
        OkhttpUtil.okHttpGet(url, new CallBackUtil<MovieDetail>() {
            @Override
            public MovieDetail onParseResponse(Call call, Response response) {
                //response 转换成  List<MovieDetail>
                //content 就是返回的json字符串
                MovieDetail movieDetail = null;
                String content = null;
                try {
                    content = response.body().string();
                    JSONObject object = new JSONObject(content);
                    JSONObject data = object.getJSONObject("data");     //从content里取出data
                    JSONObject movieInfo = (JSONObject) data.get("basic");  //从data里提取影片信息
                    movieDetail = MovieUtil.getMovieDetail(movieInfo);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return movieDetail;
            }

            @Override
            public void onFailure(Call call, Exception e) {
                Log.i(TAG,"getMovieStills：请求失败！！！！！！！！！！");

            }

            @Override
            public void onResponse(MovieDetail response) {
                //1.通过回调方法，拿到返回的数据源
                MovieDetail movieDetail = response;
                movieStills_Adapter = new MovieStills_Adapter(DetailActivity.this, movieDetail.getStageImgUrl());
                //2.将海报简介数据源和movieInfoAdap_adapter进行绑定
                movieStillsView.setAdapter(movieStills_Adapter);

            }
        });
    }

    /**
     * 获取收藏的控件
     */
    public void initView(){
        collectionImg = (ImageView) findViewById(R.id.collectionImg);
    }

    /**
     * 收藏电影
     * @param response 电影实体
     */
    public void islike(final MovieDetail response){
        //if like
        if(query(movieId)){
            collectionImg.setImageResource(R.drawable.collection_logo2);
            liked = true;
        }else{
            collectionImg.setImageResource(R.drawable.collection_logo1);
            liked = false;
        }

        collectionImg.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liked == true){
                    //set not like img
                    collectionImg.setImageResource(R.drawable.collection_logo1);
                    DataSupport.deleteAll(FavoriteMovices.class,"movicesid = ?",movieId+"");//取消收藏
                    liked = false;
                }else {
                    collectionImg.setImageResource(R.drawable.collection_logo2);
                    Save(response);
                    liked = true;
                }

            }
        });
    }

    /**
     * 电影实体存入数据库
     * @param movieDetail 电影实体
     */
    public void Save(MovieDetail movieDetail){
        FavoriteMovices favoriteMovices = new FavoriteMovices();
        favoriteMovices.settCn(movieDetail.getMovieName());
        favoriteMovices.setdN(movieDetail.getDirector());
        favoriteMovices.setImg(movieDetail.getMovieImgUrl());
        String actorNames = "";

        if (movieDetail.getActors().size() > 0){
            for( int i = 0; i < 3; i++){
                String actor = movieDetail.getActors().get(i);
                actorNames += actor;
                if(i != 2){
                    actorNames += "/";
                }
            }
        }

        favoriteMovices.setActors(actorNames);
        favoriteMovices.setR(movieDetail.getRating());
        favoriteMovices.setReleaseDate(movieDetail.getReleaseDate());
        favoriteMovices.setMovicesid(movieDetail.getMovieId());
        //在数据库中查找moviceid 是否存在，若不存在就存储进入数据库，若存在，不进行任何操作。
        if(DataSupport.where("movicesid = ?",movieDetail.getMovieId()+"").find(FavoriteMovices.class).size() == 0){
            favoriteMovices.save();
        }
    }

    /**
     * 根据电影id查询该电影是否被收藏过
     * @param movieId 电影id
     * @return 是否被收藏过，收藏过返回true,未收藏过返回false
     */
    public boolean query(int movieId){
        if(DataSupport.where("movicesid = ?",movieId+"").find(FavoriteMovices.class).size() == 0){
            return false;
        }else{
            return true;
        }

    }
}

