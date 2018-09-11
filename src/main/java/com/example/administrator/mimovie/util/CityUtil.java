package com.example.administrator.mimovie.util;



import com.example.administrator.mimovie.bean.City;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YangYi on 2018/1/8.
 */

public class CityUtil {

    /**
     * 获取单个城市的id和名字
     * @param jsonObject：城市的json对象
     * @return City：返回一个城市对象
     */
    public static City getCity(JSONObject jsonObject){
        City city = new City();

        try {
            int id = jsonObject.getInt("id");
            String name = jsonObject.getString("n");

            city.setId(id);
            city.setName(name);

            return city;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return city;
    }
}
