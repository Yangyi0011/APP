package com.example.administrator.mimovie.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.mimovie.DetailActivity;
import com.example.administrator.mimovie.R;
import com.example.administrator.mimovie.bean.MoviePreview;
import com.example.administrator.mimovie.util.SmartImageView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by TangHaoCheng on 2018/1/11.
 */

public class ComingsoonAdapter extends RecyclerView.Adapter<ComingsoonAdapter.ViewHolder> {

    private static final String TAG = "ComingsoonAdapter";
    private List<MoviePreview> mComingMovieList;
    private int cityId;

    public ComingsoonAdapter(List<MoviePreview> comingMovieList){
        mComingMovieList = comingMovieList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        /*初始化你的控件*/
        View mView;
        SmartImageView movieimage;  /*电影海报*/
        TextView moviename;         /*片名*/
        TextView director;          /*导演*/
        TextView actors;            /* 演员*2 */
        TextView showtime;          /*上映时间*/

        public ViewHolder(View view) {
            super(view);
            mView = view;
            movieimage = view.findViewById(R.id.smartimage_item_comingsoon_movieimage);
            moviename = view.findViewById(R.id.text_item_comingsoon_moviename);
            moviename.setSelected(true);/*电影名称：走马灯*/
            director = view.findViewById(R.id.text_item_comingsoon_director);
            actors = view.findViewById(R.id.text_item_comingsoon_actors);
            actors.setSelected(true);/*演员array：走马灯*/
            showtime = view.findViewById(R.id.text_item_comingsoon_showtime);
        }
    }

    @Override
    public ComingsoonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comingsoon, parent, false);

        final ViewHolder holder = new ViewHolder(view);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*获取电影id*/
                int position = holder.getAdapterPosition();
                MoviePreview movie = mComingMovieList.get(position);
                int moveId = movie.getMovieId();

                Intent intent = new Intent(view.getContext(),DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("moveId", moveId+"");
                final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("city", MODE_PRIVATE);
                cityId = sharedPreferences.getInt("cityId",0) ;//取出数据，如果数据为空则取""
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
    public void onBindViewHolder(ComingsoonAdapter.ViewHolder holder, int position) {
        MoviePreview moviePreview =  mComingMovieList.get(position);

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
        return mComingMovieList.size();
    }
}
