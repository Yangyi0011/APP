package com.example.administrator.mimovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.mimovie.R;
import com.example.administrator.mimovie.bean.FavoriteMovices;
import com.example.administrator.mimovie.util.SmartImageView;

import java.util.List;

/**
 * Created by yuanye on 2018/1/5.
 */

public class FavoriteAdapter extends BaseAdapter {
    private Context context;
    private List<FavoriteMovices> favoriteMovicesList;

    public FavoriteAdapter(Context context, List<FavoriteMovices> favoriteMovicesList){
        this.context = context;
        this.favoriteMovicesList = favoriteMovicesList;
    }
    public FavoriteAdapter(){

    }
    @Override
    public int getCount() {
        return this.favoriteMovicesList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.favoriteMovicesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if(convertView == null){
            view = LayoutInflater.from(this.context).inflate(R.layout.item_favoritemovice,parent,false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.smartImageView = view.findViewById(R.id.movicesimage);
            viewHolder.movicetcn = view.findViewById(R.id.movicetcn);
            viewHolder.dN = view.findViewById(R.id.dN);
            viewHolder.rcontent = view.findViewById(R.id.rcontent);
            viewHolder.r = view.findViewById(R.id.r);
            viewHolder.actors = view.findViewById(R.id.actors);
            viewHolder.rdcontent = view.findViewById(R.id.rdcontent);
            viewHolder.releaseDate = view.findViewById(R.id.releaseDate);
            viewHolder.moviceid = view.findViewById(R.id.moviceid);
            view.setTag(viewHolder);
        }else{
            view = convertView;
        }
        FavoriteMovices favoriteMovices = favoriteMovicesList.get(position);

        ViewHolder viewH = (ViewHolder) view.getTag();
        viewH.smartImageView.showImgFromNet(favoriteMovices.getImg(),150,180);
        viewH.movicetcn.setText(favoriteMovices.gettCn());
        viewH.actors.setText("主演："+ favoriteMovices.getActors());
        viewH.releaseDate.setText(favoriteMovices.getReleaseDate());
        viewH.r.setText(""+ favoriteMovices.getR());
        viewH.dN.setText("导演："+ favoriteMovices.getdN());
        viewH.moviceid.setText(""+ favoriteMovices.getMovicesid());
        return view;
    }
    class ViewHolder{
        SmartImageView smartImageView;
        TextView movicetcn;
        TextView dN;
        TextView rcontent;
        TextView r;
        TextView actors;
        TextView rdcontent;
        TextView releaseDate;
        TextView moviceid;
    }
}
