package com.example.administrator.mimovie.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.mimovie.R;
import com.example.administrator.mimovie.bean.MovieDetail;
import com.example.administrator.mimovie.util.SmartImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9/009.
 */

public class MovieInfo_Adapter extends RecyclerView.Adapter<MovieInfo_Adapter.ViewHolder>{
    private static final String TAG = "————————111————————";
    private List<MovieDetail> movieDetailList;
    private Context context;
    private boolean layout_isVisible;

    public MovieInfo_Adapter(){}

    public MovieInfo_Adapter(Context context, List<MovieDetail> movieDetailList){
        this.movieDetailList = movieDetailList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.movieinfo_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SmartImageView movie_poster = holder.SmartImageView_Detail_Movie_Poster;//海报
        Button Button_Detail_score = holder.Button_Detail_score;//评分
        TextView director_name = holder.TextView_Detail_director_name;//导演
        TextView director_type_specific = holder.TextView_Detail_movieType;//类型
        TextView time_length_specific = holder.TextView_Detail_timeLength;//时长
        TextView release_time = holder.TextView_Detail_releaseTime;//上映时间
        Button describe_title = holder.Button_Detail_movieName;//电影名
        Button describe_text = holder.Button_Detail_describe_text;//电影简介
        TextView videoUrl = holder.TextView_Detail_tidbits;//预告
        ImageView now_or_will = holder.ImageView_Detail_now_or_will;//热映还是即将上映
        final ConstraintLayout layout = holder.layout;/*整个包裹住的黑色半透明窗口*/
        LinearLayout mainLayout = holder.mainLayout;
        layout_isVisible = true;

        MovieDetail movieDetail = movieDetailList.get(position);
        if(movieDetail != null){
            movie_poster.showImgFromNet(movieDetail.getMovieImgUrl(),450,700);
            //做判断，评分为负则赋值为暂无
            if(movieDetail.getRating() > 0){
                Button_Detail_score.setText(movieDetail.getRating()+"");
            }else{
                Button_Detail_score.setText("暂无");
            }
            director_name.setText("导演: "+movieDetail.getDirector());

            //获取到的类型是一个list还要拆分循环取
            String types = "";
            for(int i = 0; i < movieDetail.getType().size(); i++){
                String type = movieDetail.getType().get(i);
                types += type;
                if(i != movieDetail.getType().size() -1){
                    types += "/";
                }
            }
            director_type_specific.setText("类型: "+ types);
            time_length_specific.setText("时长: "+movieDetail.getMins());
            release_time.setText("上映时间: "+movieDetail.getReleaseDate());
            describe_title.setText(movieDetail.getMovieName());
            describe_text.setText(movieDetail.getStory());
            videoUrl.setText(movieDetail.getVideoUrl());
            if(movieDetail.isHot()){
                //set like img
                now_or_will.setImageResource(R.drawable.now_logo);
            }else {
                now_or_will.setImageResource(R.drawable.will_logo);
            }
        }

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i(TAG, "onClick: ---layout_isVisible---:"+layout_isVisible);
                if(layout_isVisible){
                    layout_isVisible = false;
                    layout.setVisibility(View.INVISIBLE);
                }else {
                    layout_isVisible = true;
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final SmartImageView SmartImageView_Detail_Movie_Poster;//海报
        private final TextView TextView_Detail_director_name;//导演
        private final Button Button_Detail_score;//评分
        private final TextView TextView_Detail_movieType;//类型
        private final TextView TextView_Detail_timeLength;//时长
        private final TextView TextView_Detail_releaseTime;//上映时间
        private final Button Button_Detail_movieName;//电影名
        private final Button Button_Detail_describe_text;//电影简介
        private final TextView TextView_Detail_tidbits;//花絮网址
        private final ImageView ImageView_Detail_now_or_will;
        private final ConstraintLayout layout;
        private final LinearLayout mainLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            //movieInfo视图
            SmartImageView_Detail_Movie_Poster = (SmartImageView) itemView.findViewById(R.id.SmartImageView_Detail_Movie_Poster);
            Button_Detail_score = (Button) itemView.findViewById(R.id.Button_Detail_score);
            TextView_Detail_director_name = (TextView) itemView.findViewById(R.id.TextView_Detail_director_name);
            TextView_Detail_movieType = (TextView) itemView.findViewById(R.id.TextView_Detail_movieType);
            TextView_Detail_timeLength = (TextView) itemView.findViewById(R.id.TextView_Detail_timeLength);
            TextView_Detail_releaseTime = (TextView) itemView.findViewById(R.id.TextView_Detail_releaseTime);
            Button_Detail_movieName = (Button) itemView.findViewById(R.id.Button_Detail_movieName);
            Button_Detail_describe_text = (Button) itemView.findViewById(R.id.Button_Detail_describe_text);
            TextView_Detail_tidbits = (TextView) itemView.findViewById(R.id.TextView_Detail_tidbits);
            ImageView_Detail_now_or_will = (ImageView) itemView.findViewById(R.id.ImageView_Detail_now_or_will);
            layout = itemView.findViewById(R.id.constraintLayout_movieInfo_layout);
            mainLayout = itemView.findViewById(R.id.linearLayout_movieInfo_layout);
        }
    }
}
