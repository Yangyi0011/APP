package com.example.administrator.mimovie.fragment;

/**
 * writeByYuanYe(袁野)
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.mimovie.DetailActivity;
import com.example.administrator.mimovie.R;
import com.example.administrator.mimovie.adapter.FavoriteAdapter;
import com.example.administrator.mimovie.bean.FavoriteMovices;
import com.example.administrator.mimovie.bean.MovieDetail;

import org.litepal.crud.DataSupport;

import java.util.List;

import javax.security.auth.login.LoginException;

public class FavoriteFragment extends Fragment {
    private static final String TAG = "FavoriteFragment";

    private static final int LOAD_SUCCESS = 1;
    private TextView title;
    private ListView movicelist;
    private int cityId = 433;
    View view;

    List<FavoriteMovices> movices= query();

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOAD_SUCCESS){
                List<FavoriteMovices> favoriteMovicesItems = (List<FavoriteMovices>) msg.obj;
                FavoriteAdapter favoriteAdapter =new FavoriteAdapter(view.getContext(), favoriteMovicesItems);
                movicelist.setAdapter(favoriteAdapter);
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_favorite, container,false);

        refreshListView();

        movicelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FavoriteMovices movie = movices.get(position);
                int movieId = movie.getMovicesid();

                Intent intent = new Intent(view.getContext(),DetailActivity.class);
                Bundle bundle = new Bundle();
                    bundle.putString("moveId", movieId+"");
                    bundle.putString("cityId", cityId+"");
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
                Log.i(TAG, "onItemClick: ----movieId----"+movieId+"----cityId----"+cityId);
            }
        });

        movicelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, long l) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("提示").setMessage("你确认删除吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                                deleteMovice(position);
                                refreshListView();
                            }
                        }).setNegativeButton("取消",null)
                        .show();
                return true;
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshListView();
    }

    private void initView(View view){
        title = view.findViewById(R.id.title);
        movicelist = view.findViewById(R.id.movicelist);

//        Save();   //测试插入数据
    }

    /*分线程run*/
    private void loadMovicesDataFromDB(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                List<FavoriteMovices> movices= query();
                Message message = Message.obtain();
                message.what = LOAD_SUCCESS;
                message.obj = movices;
                handler.sendMessage(message);
            }
        }.start();
    }
/*删除语句*/
    private void deleteMovice(int position){

        List<FavoriteMovices> movices= query();
        if (movices.size() > 0){
            String mId = movices.get(position).getMovicesid()+"";
            DataSupport.deleteAll(FavoriteMovices.class,"movicesid = "+mId);    //写成问号会报错
        }
    }

    public List<FavoriteMovices> query(){
        List<FavoriteMovices> movices = DataSupport.findAll(FavoriteMovices.class);
        return  movices;
    }

    private void refreshListView(){
        initView(view);
        loadMovicesDataFromDB();
    }
/*__ 测试插入数据到数据库 __*/
}