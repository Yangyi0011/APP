package com.example.administrator.mimovie.fragment;



import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.mimovie.R;
import com.example.administrator.mimovie.adapter.ComingsoonAdapter;
import com.example.administrator.mimovie.adapter.HotBroadcastAdapter;
import com.example.administrator.mimovie.bean.MoviePreview;
import com.example.administrator.mimovie.util.MovieUtil;
import com.example.administrator.mimovie.util.okHttpUtils.CallBackUtil;
import com.example.administrator.mimovie.util.okHttpUtils.OkhttpUtil;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class ComingsoonFragment extends Fragment {

    private static final String TAG = "HotbroadcastFragmen";
    private List<MoviePreview> comingsoonList = new ArrayList<>();
    private ComingsoonAdapter adapter;
    private RollPagerView mRollViewPager;/*滚动条*/
    private View v;
    private int cityId;/*城市ID*/
    private Context context;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        v = inflater.inflate(R.layout.fragment_comingsoon, container,false);
        context = v.getContext();

//        //测试数据
//        /*先初始化数据 = List<MoviePreview> hotMovieList*/
//        MoviePreview moviePreview = new MoviePreview();
//        moviePreview.setMovieImgUrl("http://img5.mtime.cn/mt/2017/01/13/191421.14582165_1280X720X2.jpg");
//        moviePreview.setMovieName("哈哈哈哈哈哈哈哈哈大作战");
//        moviePreview.setDirector("汤昊成");
//            List<String> actor = new ArrayList<String>();
//            actor.add("袁野");
//            actor.add("杨毅");
//            actor.add("宋君华");
//            actor.add("杨美美");
//        moviePreview.setActors(actor);
//        moviePreview.setReleaseDate("2018-2-2");
//        comingsoon.add(moviePreview);

         /*item初始化*/
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview_fragment_comingsoon_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager( v.getContext() );
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ComingsoonAdapter(comingsoonList);
        recyclerView.setAdapter(adapter);

        sharedPreferences = context.getSharedPreferences("city", MODE_PRIVATE);
        cityId = sharedPreferences.getInt("cityId",0) ;//取出数据，如果数据为空则取""
        getMoviePreview_will(cityId);

        return v;
    }

    /*如果城市名称得到改变-依据城市名更新列表*/
    @Override
    public void onResume() {
        super.onResume();
        int newCityId = sharedPreferences.getInt("cityId",0) ;//取出数据，如果数据为空则取"";
        if(cityId != newCityId){
            cityId = newCityId;
            getMoviePreview_will(cityId);
        }
    }


    /* -createBy杨毅 - */
    /**
     * 获取正在热映的影片预览信息
     * 需要传入城市id
     * @param cityId：城市id
     * @return
     */
    private void getMoviePreview_will(final int cityId) {

        String url = "https://api-m.mtime.cn/Movie/MovieComingNew.api?locationId=" + cityId;
        OkhttpUtil.okHttpGet(url, new CallBackUtil<List<MoviePreview>>() {

            @Override
            public List<MoviePreview> onParseResponse(Call call, Response response) {
                //response 转换成  List<MoviePreview>
                //content 就是返回的json字符串
                String content = null;
                List<MoviePreview> moviePreviewList = new ArrayList<>();

                try {
                    content = response.body().string();
                    JSONObject object = new JSONObject(content);
                    JSONArray movieArray = (JSONArray) object.get("attention");

                    for (int i = 0; i < movieArray.length(); i++) {

                        //获取单个影片预览信息
                        JSONObject movieJson = (JSONObject) movieArray.get(i);
                        MoviePreview moviePreview = MovieUtil.getMoviePreview_will(movieJson);

                        moviePreviewList.add(moviePreview);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return moviePreviewList;
            }

            @Override
            public void onFailure(Call call, Exception e) {
//                Log.i(TAG,"getMoviePreview_will：请求失败！！！！！");
            }

            /**
             * 在这里拿到数据源
             * @param response
             */
            @Override
            public void onResponse(List<MoviePreview> response) {
                //1.通过回调方法，拿到返回的数据源
                comingsoonList.addAll(response);

                //2.将数据与Adapter进行绑定，即更新Adapter数据源
                adapter.notifyDataSetChanged();
            }
        });
    }

    /*RollPagerView设置内部类*/
    private class TestNormalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.drawable.rollpagerview_a,
                R.drawable.rollpagerview_b,
                R.drawable.rollpagerview_c,
        };
        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }
        @Override
        public int getCount() {
            return imgs.length;
        }
    }

}
