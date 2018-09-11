package com.example.administrator.mimovie.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.mimovie.LocationActivity;
import com.example.administrator.mimovie.R;
import com.example.administrator.mimovie.bean.City;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * @author: 宋君华
 * @description:
 * @projectName: SelectCityDome
 * @date: 2018-01-08
 */
/**
 * @author: 汤昊成
 * @description: 修改点击时间，并更改城市名称源和数据类型，设置热门城市点击时间
 * @projectName: CityAdapter
 * @date: 2018-01-12
 */
public class CityAdapter extends ArrayAdapter<City> {
    /**
     * 需要渲染的item布局文件
     */
    private int resource;
    private final List<City> mhotCities;
    LocationActivity context;

    public CityAdapter(LocationActivity context, int textViewResourceId, List<City> hotCities) {
        super(context, textViewResourceId, hotCities);
        resource = textViewResourceId;
        this.mhotCities = hotCities;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        final City hotCity = mhotCities.get(position);

        if (convertView == null) {
            layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(resource, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        /*设置热门城市item*/
        Button name = layout.findViewById(R.id.tv_city);
        name.setText(hotCity.getName());
        /*热门城市list点击事件*/
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*存城市id到city.xml*/
                final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("city", MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putInt("cityId", hotCity.getId());
                editor.putBoolean("isSelect", true);
                editor.putString("cityName",hotCity.getName());
                editor.apply();//提交数据到文件****切记！切记！****
                Toast.makeText(view.getContext(), "你选择的城市是："+hotCity.getName(), Toast.LENGTH_SHORT).show();
                context.finish();
            }
        });


        return layout;
    }
}
