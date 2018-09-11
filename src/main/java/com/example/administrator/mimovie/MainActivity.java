package com.example.administrator.mimovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.mimovie.adapter.MyFragmentPagerAdapter;
import com.example.administrator.mimovie.fragment.HotbroadcastFragmen;
import com.example.administrator.mimovie.fragment.ComingsoonFragment;
import com.example.administrator.mimovie.fragment.FavoriteFragment;

import java.util.ArrayList;

//实现ViewPager的OnPageChangeListener监听事件，重写onPageSelected()方法，实现左右滑动页面
public class MainActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String TAG = "MainActivity";
    private ViewPager myviewpager;

    //选项卡中的按钮
    private Button btn_first;
    private Button btn_second;
    private Button btn_third;
    private TextView text_location;

    private ArrayList<Fragment> fragments;

    /*首先创建两个数组，便于根据下标得到某个按钮以及对应的宽度：*/
    //所有按钮的宽度的集合
    private int[] widthArgs;
    //所有按钮的集合
    private Button[] btnArgs;
    //作为指示标签的按钮
    private ImageView cursor;
    //标志指示标签的横坐标
    float cursorX = 0;
    private PopupWindow mPopWindow_firstOpen;
    private PopupWindow mPopWindow_selectCity;
    private SharedPreferences sharedPreferences_firstOpen;
    private SharedPreferences sharedPreferences_city;
    private Intent intent;
    private String cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*创建或查找本地XML文件 firstOpen：是否第一次打开App -> Boolean("isOpen", true);*/
        sharedPreferences_firstOpen = getSharedPreferences("firstOpen", MODE_PRIVATE);
        /*创建或查找本地XML文件 city：选择定位的城市 -> Boolean("isSelect",false) Int("cityId",0) String("cityName","city")*/
        sharedPreferences_city = getSharedPreferences("city", MODE_PRIVATE);

        initView();
        initListener();
        initProgress();

    }

    /*当前窗体得到或失去焦点的时候的时候调用--生命周期的最后一层(此时布局已经加载好了，可以使用基于布局的测量长宽方法)*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        /*窗口得到焦点时(ture) 首先检测是否第一次打开APP，然后检查是否已经选择过城市*/
        if (hasFocus) {
            if (!sharedPreferences_firstOpen.getBoolean("isOpen", false)) {
                initpopuplayout_firstOpen();/*第一次打开APP，进入选择城市页面*/
            } else if (!sharedPreferences_city.getBoolean("isSelect", false)) {
                initpopuplayout_selectCity();    /*如果检测到还没有选择城市，就弹出此提示点击后进入选择城市页面*/
            }
            /*查看是否更新了城市*/
            String newcityName= sharedPreferences_city.getString("cityName","选择城市");;
            if(!cityName.equals(newcityName)){
                cityName = newcityName;
                text_location.setText("城市:"+cityName);
            }
        }
    }

    //初始化布局
    public void initView() {
        text_location = findViewById(R.id.textView_activity_main_location);
        myviewpager = findViewById(R.id.myviewpager);
        btn_first = findViewById(R.id.btn_first);
        btn_second = findViewById(R.id.btn_second);
        btn_third = findViewById(R.id.btn_third);
        //指示标签设置为红色
        cursor = findViewById(R.id.cursor_btn);
        cursor.setBackgroundColor(Color.RED);
        btn_first.setOnClickListener(this);
        btn_second.setOnClickListener(this);
        btn_third.setOnClickListener(this);
        //先重置所有按钮颜色
        resetButtonColor();
        //再将第一个按钮字体设置为红色，表示默认选中第一个
        btn_first.setTextColor(Color.RED);
        fragments = new ArrayList<Fragment>();
    }

    /*为所有标题按钮注册监听：*/
    public void initListener() {
        btn_first.setOnClickListener(this);
        btn_second.setOnClickListener(this);
        btn_third.setOnClickListener(this);
        text_location.setOnClickListener(this);
    }

    public void initProgress() {
        /*创建完adapter后，我们还要在MainActivity中将所有fragment添加到一个list并作为构造参数传到adapter中去：*/
        //fragment的集合，对应每个子页面
        fragments.add(new HotbroadcastFragmen());
        fragments.add(new ComingsoonFragment());
        fragments.add(new FavoriteFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);

        /*将装载了数据的adapter设置给viewpager*/
        //设定适配器
        ViewPager myviewpager = (ViewPager) findViewById(R.id.myviewpager);
        myviewpager.setAdapter(adapter);
        /*[可以反映出页面的变换]为myviewpager注册监听：*/
        myviewpager.addOnPageChangeListener(this);

        /*注意两个数组实例化的位置不同，btnArgs是像平常一样在onCreate方法中实例化，
        而widthArgs在滑动的时候再实例化，因为在onCreate方法中获取不了所有按钮的宽度，
        因为系统还未测量它们的宽度。
        btnArgs的实例化：*/
        //初始化按钮数组
        btnArgs = new Button[]{btn_first, btn_second, btn_third};

        //初始化指示器位置和大小：
        btn_first.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursor.getLayoutParams();
                //减去边距*2，以对齐标题栏文字
                lp.width = btn_first.getWidth() - btn_first.getPaddingLeft() * 2;
                cursor.setLayoutParams(lp);
                cursor.setX(btn_first.getPaddingLeft());
            }
        });

        cityName = sharedPreferences_city.getString("cityName","选择城市");
        text_location.setText("城市:"+cityName);
    }

    //重置所有按钮的颜色
    public void resetButtonColor() {
        btn_first.setBackgroundColor(Color.parseColor("#ffffff"));
        btn_second.setBackgroundColor(Color.parseColor("#ffffff"));
        btn_third.setBackgroundColor(Color.parseColor("#ffffff"));
        btn_first.setTextColor(Color.BLACK);
        btn_second.setTextColor(Color.BLACK);
        btn_third.setTextColor(Color.BLACK);
    }

    /*设置第一次打开APP的时候进入选择城市页面*/
    public void initpopuplayout_firstOpen() {

        /*第一次打开APP存入文件已经打开过APP了*/
        SharedPreferences.Editor editor = sharedPreferences_firstOpen.edit();
        editor.putBoolean("isOpen", true);
        editor.apply();//提交数据到文件

        //设置contentView
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_popuplayout_firstopenapp, null);
        mPopWindow_firstOpen = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopWindow_firstOpen.setContentView(contentView);
        //设置各个控件的点击响应
        RelativeLayout layout = contentView.findViewById(R.id.relativeLayout_item_firstOpenApp);
        layout.setOnClickListener(this);
        //显示PopupWindow
        View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        mPopWindow_firstOpen.showAtLocation(rootview, Gravity.CENTER_VERTICAL, 0, 0);
    }

    /*如果检测到还没有选择城市，就弹出此提示点击后进入选择城市页面*/
    public void initpopuplayout_selectCity() {
        //设置contentView
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_popuplayout_selectcity, null);
        mPopWindow_selectCity = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopWindow_selectCity.setContentView(contentView);
        //设置各个控件的点击响应
        LinearLayout layout = contentView.findViewById(R.id.relativeLayout_item_selectCity);
        layout.setOnClickListener(this);
        //显示PopupWindow
        View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        mPopWindow_selectCity.showAsDropDown(text_location);
    }

    @Override
    public void onClick(View whichbtn) {
        // TODO Auto-generated method stub

        switch (whichbtn.getId()) {
            case R.id.btn_first:
                myviewpager.setCurrentItem(0);
                cursorAnim(0);
                break;
            case R.id.btn_second:
                myviewpager.setCurrentItem(1);
                cursorAnim(1);
                break;
            case R.id.btn_third:
                myviewpager.setCurrentItem(2);
                cursorAnim(2);
                break;
            case R.id.textView_activity_main_location:
                intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
                break;
            case R.id.relativeLayout_item_firstOpenApp:     /*popuplayout点击事件*/
                intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
                mPopWindow_firstOpen.dismiss();
                break;
            case R.id.relativeLayout_item_selectCity:
                intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
                mPopWindow_selectCity.dismiss();
                break;
            default:
                break;
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }


    /*实现三个接口方法，这里关键在于重写onPageSelected方法，onPageSelected会在每次滑动ViewPager的时候触发，
      所以所有滑动时的变化都可以在这里面定义，比如标题按钮的颜色随着滑动的变化等 */
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
         /*跟踪调控小红条的长短和移动*/
        if (widthArgs == null) {
            widthArgs = new int[]{
                    btn_first.getWidth(),
                    btn_second.getWidth(),
                    btn_third.getWidth()
            };
        }
        //每次滑动首先重置所有按钮的颜色
        resetButtonColor();
        //将滑动到的当前按钮颜色设置为红色
        btnArgs[arg0].setTextColor(Color.RED);

        cursorAnim(arg0);
    }

    //指示器的跳转，传入当前所处的页面的下标
    public void cursorAnim(int curItem) {
        //每次调用，就将指示器的横坐标设置为0，即开始的位置
        cursorX = 0;
        //再根据当前的curItem来设置指示器的宽度
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursor.getLayoutParams();
        //减去边距*2，以对齐标题栏文字
        lp.width = widthArgs[curItem] - btnArgs[0].getPaddingLeft() * 2;
        cursor.setLayoutParams(lp);
        //循环获取当前页之前的所有页面的宽度
        for (int i = 0; i < curItem; i++) {
            cursorX = cursorX + btnArgs[i].getWidth();
        }
        //再加上当前页面的左边距，即为指示器当前应处的位置
        cursor.setX(cursorX + btnArgs[curItem].getPaddingLeft());
    }
}
