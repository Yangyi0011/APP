package com.example.administrator.mimovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.mimovie.adapter.CityAdapter;
import com.example.administrator.mimovie.adapter.SortAdapter;
import com.example.administrator.mimovie.bean.City;
import com.example.administrator.mimovie.util.CityUtil;
import com.example.administrator.mimovie.util.okHttpUtils.CallBackUtil;
import com.example.administrator.mimovie.util.okHttpUtils.OkhttpUtil;
import com.example.administrator.mimovie.view.CitySortModel;
import com.example.administrator.mimovie.view.EditTextWithDel;
import com.example.administrator.mimovie.view.PinyinComparator;
import com.example.administrator.mimovie.view.PinyinUtils;
import com.example.administrator.mimovie.view.SideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
/**
 * @author: 宋君华
 * @description:
 * @projectName: LocationActivity
 * @date: 2018-01-08
 */
/**
 * @author: 汤昊成
 * @description: 修改 静态城市数据源 为 动态API获取数据源，更改相匹配的javabean，解决程序跳转错误，存储目的城市为文件到手机本地
 * @projectName: LocationActivity
 * @date: 2018-01-12
 */
public class LocationActivity extends AppCompatActivity {

    private List<City> cities = new ArrayList<>();  /*从api接受并处理城市的数据*/
    private static final String TAG = "LocationActivity";
    private ListView sortListView;              /*城市名称list*/
    private SortAdapter adapter;                /*城市名称list adapter*/
    private SideBar sideBar;                    /*用来显示abcd字母导航的bar*/
    private TextView dialog, mTvTitle;          /*顶上显示城市名称的title bar控件*/
    private EditTextWithDel mEtCityName;        /*输入城市名称的搜索框*/
    private List<CitySortModel> SourceDateList; /*城市字符串*/
    private ImageView back;
    private LocationActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mContext = this;

        initViews();
    }
    private void initViews() {
        mEtCityName = (EditTextWithDel) findViewById(R.id.et_search);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        back = (ImageView) findViewById(R.id.back);

        initDatas();
        initEvents();
        setAdapter();
    }

    private void setAdapter() {

        List<City> ct = new ArrayList<>();
            City c = new City();
            c.setId(0);
            c.setName("正在加载中请稍后...");
        ct.add(c);
        SourceDateList = filledData(ct); //初始化处理城市字符串

        Collections.sort(SourceDateList, new PinyinComparator());
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.addHeaderView(initHeadView());         /*初始化热门城市*/
        sortListView.setAdapter(adapter);

        getCities();//动态从网上获取城市户数据
    }

    private void initEvents() {
        //设置返回按钮点击监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();/*关闭当前选择城市页面*/
            }
        });
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position + 1);
                }
            }
        });

        //ListView的点击事件
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Log.i(TAG, "onItemClick: ----position-----:"+position);
                 /*存城市id到city.xml*/
                final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("city", MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putInt("cityId", ((CitySortModel) adapter.getItem(position - 1)).getCityId());
                editor.putBoolean("isSelect", true);
                editor.putString("cityName",((CitySortModel) adapter.getItem(position - 1)).getName());
                editor.apply();//提交数据到文件****切记！切记！****

                mTvTitle.setText(((CitySortModel) adapter.getItem(position - 1)).getName());
                Toast.makeText(view.getContext(),
                        "你选择的城市是："+((CitySortModel) adapter.getItem(position - 1)).getName(), Toast.LENGTH_SHORT).show();
                /*结束此页面*/
                finish();
            }
        });

        //根据输入框输入值的改变来过滤搜索
        mEtCityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initDatas() {
        sideBar.setTextView(dialog);
    }

    private View initHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.headview, null);
        GridView mGvCity = (GridView) headView.findViewById(R.id.gv_hot_city);

        /*获取数据源*/
        List<City> hotCities = new ArrayList<>();
        hotCities.add(new City(290,"北京"));
        hotCities.add(new City(292,"上海"));
        hotCities.add(new City(433,"贵阳"));
        hotCities.add(new City(291,"重庆"));
        hotCities.add(new City(366,"深圳"));
        hotCities.add(new City(365,"广州"));
        hotCities.add(new City(693,"长春"));
        hotCities.add(new City(950,"昆明"));

        CityAdapter adapter = new CityAdapter(mContext, R.layout.gridview_item, hotCities);
        mGvCity.setAdapter(adapter);
        return headView;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<CitySortModel> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = SourceDateList;
        } else {
            mSortList.clear();
            for (CitySortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
    }

    /*处理城市字符串*//* String[] date*/
    private List<CitySortModel> filledData(List<City> cities) {
        List<CitySortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);//得到单个城市的数据
            CitySortModel sortModel = new CitySortModel(); /*------citySortModel对象-------*/
            //设置显示的数据
            sortModel.setName(city.getName()); /*------citySortModel.setName(date[i])-------*/
            String pinyin = PinyinUtils.getPingYin(city.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                //设置显示数据拼音的首字母
                sortModel.setSortLetters(sortString.toUpperCase()); /*------citySortModel.setSortLetters(sortString.toUpperCase());-------*/
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }
            sortModel.setCityId(city.getId());

            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;
    }

    /* -createBy杨毅 - */
    /**
     * 获取正在热映的影片预览信息
     * 需要传入城市id
     */
    public void getCities(){
        String url = "https://api-m.mtime.cn/Showtime/HotCitiesByCinema.api";
        OkhttpUtil.okHttpGet(url, new CallBackUtil<List<City>>() {
            @Override
            public List<City> onParseResponse(Call call, Response response) {
                //response 转换成  List<MoviePreview>
                //content 就是返回的json字符串
                List<City> cityList = new ArrayList<>();
                String content = null;
                try {
                    content = response.body().string();
                    JSONObject object = new JSONObject(content);
                    JSONArray cityArray = (JSONArray) object.get("p");

                    for (int i = 0; i < cityArray.length(); i++){
                        JSONObject cityObj = (JSONObject) cityArray.get(i);
                        City city = CityUtil.getCity(cityObj);
                        cityList.add(city);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return cityList;
            }

            @Override
            public void onFailure(Call call, Exception e) {
                Log.i(TAG,"getCity：请求出错了！！！！！" );
            }
            /* -优化初始化- */
            @Override
            public void onResponse(List<City> response) {
               /*1.通过回调发放拿到返回的数据源*/
                cities.addAll(response);
//                Log.i(TAG, "onResponse: ----citiesList----"+cities);
                /*2.将数据源和Adapter进行绑定*/
                SourceDateList.clear();
                SourceDateList = filledData(cities); //从API上获取城市数据
//                Log.i(TAG, "onResponse: ----cityListByAPI----"+cityListByAPI);
                Collections.sort(SourceDateList, new PinyinComparator());/*根据a-z将数组排序*/
                adapter.updateListView(SourceDateList);
            }
        });
    }

}
