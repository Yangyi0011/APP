package com.example.administrator.mimovie.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.mimovie.DetailActivity;
import com.example.administrator.mimovie.R;
import com.example.administrator.mimovie.bean.City;
import com.example.administrator.mimovie.bean.MoviePreview;
import com.example.administrator.mimovie.util.SmartImageView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by tanghaocheng on 2018/1/10.
 */

public class HotBroadcastAdapter extends RecyclerView.Adapter<HotBroadcastAdapter.ViewHolder> {

    private static final String TAG = "HotBroadcastAdapter";
    private List<MoviePreview> mhotMovieList;
    private int cityId;

    public HotBroadcastAdapter(List<MoviePreview> hotMovieList){
        mhotMovieList = hotMovieList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        /*初始化你的控件*/
        View mView;
        SmartImageView movieimage;  /*电影海报*/
        TextView moviename;         /*片名*/
        TextView director;          /*导演*/
        TextView actors;            /* 演员*2 */
        TextView content;           /*电影评分*/
        TextView showtime;          /*上映时间*/

        public ViewHolder(View view) {
            super(view);
            mView = view;
            movieimage = view.findViewById(R.id.smartimage_item_hotbroadcast_movieimage);
            moviename = view.findViewById(R.id.text_item_hotbroadcast_moviename);
                moviename.setSelected(true);
            director = view.findViewById(R.id.text_item_hotbroadcast_director);
            actors = view.findViewById(R.id.text_item_hotbroadcast_actors);
                actors.setSelected(true);
            content = view.findViewById(R.id.text_item_hotbroadcast_content);
            showtime = view.findViewById(R.id.text_item_hotbroadcast_showtime);
        }
    }

    @Override
    public HotBroadcastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hotbroadcast, parent, false);

        final ViewHolder holder = new ViewHolder(view);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*获取电影id*/
                int position = holder.getAdapterPosition();
                MoviePreview movie = mhotMovieList.get(position);
                int movieId = movie.getMovieId();
                final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("city", MODE_PRIVATE);
                cityId = sharedPreferences.getInt("cityId",0) ;//取出数据，如果数据为空则取""

                Intent intent = new Intent(view.getContext(),DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("moveId", movieId+"");
                    bundle.putString("cityId", cityId+"");
                    intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });

        return holder;
    }

    /*数据一一对应显示*/
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(HotBroadcastAdapter.ViewHolder holder, int position) {
        MoviePreview moviePreview =  mhotMovieList.get(position);

        if(moviePreview!=null){
            /*拼接演员字符串*/
            List<String> actor = moviePreview.getActors();
            String actors = "";
            int a;
            for (a = 0; a < actor.size(); a++){     //.size 从1开始计数
                actors = actors + actor.get(a);
                if(a < actor.size()-1){
                    actors = actors +"/";
                }
            }
            /*处理评分为空的情况*/
            if(moviePreview.getRating() > 0){
                holder.content.setText(moviePreview.getRating()+"");
            }
//        Log.i(TAG, "onBindViewHolder: ----------"+actors);
            holder.movieimage.showImgFromNet(moviePreview.getMovieImgUrl(), 150,180);
            holder.moviename.setText(moviePreview.getMovieName());
            holder.director.setText(moviePreview.getDirector());
            holder.actors.setText("主演："+actors);
            holder.showtime.setText(moviePreview.getReleaseDate());
        }

    }

    @Override
    public int getItemCount() {
        return mhotMovieList.size();
    }
}
