package com.example.administrator.mimovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.administrator.mimovie.R;
import com.example.administrator.mimovie.bean.Comment;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9/009.
 */

public class MovieComment_Adapter extends RecyclerView.Adapter<MovieComment_Adapter.ViewHolder> {

    private List<Comment> movieCommentList;    //数据源
    private Context context;

    public MovieComment_Adapter(){}

    public MovieComment_Adapter(Context context, List<Comment> movieCommentList ){
        this.context = context;
        this.movieCommentList = movieCommentList;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.moviecomment_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Button comment_people = holder.Button_Detail_comment_people;//评论人
        TextView comment_text = holder.TextView_Detail_comment_text;//评论正文

        Comment movieComment = this.movieCommentList.get(position); //评论对象

        comment_people.setText(movieComment.getUserName());
        comment_text.setText(movieComment.getCommentContent());
    }

    @Override
    public int getItemCount() {
        return movieCommentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final Button Button_Detail_comment_people;//评论人
        private final TextView TextView_Detail_comment_text;//评论正文

        public ViewHolder(View itemView) {
            super(itemView);

            //movieComment视图
            Button_Detail_comment_people = (Button) itemView.findViewById(R.id.Button_Detail_comment_people);
            TextView_Detail_comment_text = (TextView) itemView.findViewById(R.id.TextView_Detail_comment_text);
        }
    }
}
