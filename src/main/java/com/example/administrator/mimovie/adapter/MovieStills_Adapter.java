package com.example.administrator.mimovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.mimovie.R;
import com.example.administrator.mimovie.util.SmartImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9/009.
 */

public class MovieStills_Adapter extends RecyclerView.Adapter<MovieStills_Adapter.ViewHolder> {

    private Context context;
    private List<String> imgStrList;

    public MovieStills_Adapter(){}

    public MovieStills_Adapter(Context context,List<String> imgStrList){
        this.imgStrList = imgStrList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(this.context).inflate(R.layout.moviestills_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        SmartImageView stageImg = holder.SmartImageView_Detail_StillsImg;
        String imgStrUrl = imgStrList.get(position);  //剧照对象
        if (imgStrUrl != null){
            stageImg.showImgFromNet(imgStrUrl,400,300);
        }
    }

    @Override
    public int getItemCount() {
        return imgStrList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final SmartImageView SmartImageView_Detail_StillsImg;//剧照

        public ViewHolder(View view) {
            super(view);

            //movieStills视图
            SmartImageView_Detail_StillsImg = (SmartImageView) itemView.findViewById(R.id.SmartImageView_Detail_StillsImg);

        }
    }
}
