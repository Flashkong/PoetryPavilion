package com.poetrypavilion.poetrypavilion.activities;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.poetrypavilion.poetrypavilion.Adapters.MyFragmentPagerAdapter;
import com.poetrypavilion.poetrypavilion.Fragments.Find.FindFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Message.MessageFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.PoetryFragment;
import com.poetrypavilion.poetrypavilion.R;
import com.poetrypavilion.poetrypavilion.Utils.BackHandle.BackHandlerHelper;
import com.poetrypavilion.poetrypavilion.ViewModels.Poetry.PoemViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,ViewPager.OnPageChangeListener {

    //声明变量
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private LinearLayout left_menu_open_icon;
    private NavigationView drawer_view;
    private ViewPager total_content;
    private ImageView poetry_pavilion;
    private ImageView message_box;
    private ImageView find_something;
    private ImageView add_something;
    private long lastBackPress;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(!BackHandlerHelper.handleBackPress(this)){
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        //初始化部件
        initIDs();

        //去除默认Title显示
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        //设置viewPager的content
        setViewPagerContent();
        //设置监听
        setListeners();
    }

    private void initIDs(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        left_menu_open_icon = (LinearLayout) findViewById(R.id.left_menu_open_icon) ;
        drawer_view = (NavigationView) findViewById(R.id.drawer_view);
        total_content = (ViewPager)findViewById(R.id.total_content);
        poetry_pavilion = (ImageView)findViewById(R.id.poetry_pavilion);
        message_box = (ImageView)findViewById(R.id.message_box);
        find_something = (ImageView)findViewById(R.id.find_something);
        add_something = (ImageView)findViewById(R.id.add_something);
    }
    private void setViewPagerContent(){
        ArrayList<Fragment> FragmentList = new ArrayList<>();
        FragmentList.add(new PoetryFragment());
        FragmentList.add(new MessageFragment());
        FragmentList.add(new FindFragment());
        // 注意使用的是：getSupportFragmentManager
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), FragmentList);
        total_content.setAdapter(adapter);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
        total_content.setOffscreenPageLimit(2);
        total_content.addOnPageChangeListener(this);
        //调用自己写的函数设置默认的页面
        setCurrentItem(0);
    }
    private void setListeners(){
        left_menu_open_icon.setOnClickListener(this);
        drawer_view.setNavigationItemSelectedListener(this);
        poetry_pavilion.setOnClickListener(this);
        message_box.setOnClickListener(this);
        find_something.setOnClickListener(this);
        add_something.setOnClickListener(this);
    }

    //左侧抽屉的select的选择方法
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //其他控件的点击方法
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_menu_open_icon:
                // 开启菜单
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.poetry_pavilion:
                //如果点击《诗词阁》的时候页面不是在诗词阁这一栏，那么就设置页面为诗词阁
                if(total_content.getCurrentItem()!=0){
                    this.setCurrentItem(0);
                }
                break;
            case R.id.message_box:
                if(total_content.getCurrentItem()!=1){
                    this.setCurrentItem(1);
                }
                break;
            case R.id.find_something:
                if(total_content.getCurrentItem()!=2){
                    this.setCurrentItem(2);
                }
                break;
            case R.id.add_something:
                break;
            default:
                    break;
        }
    }

    //这个方法会在屏幕滚动过程中不断被调用
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    /*这个方法有一个参数position，代表哪个页面被选中。
    当用手指滑动翻页的时候，如果翻动成功了（滑动的距离够长），手指抬起来就会立即执行这个方法，
    position就是当前滑动到的页面*/
    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
    }
    //这个方法在手指操作屏幕的时候发生变化。有三个值：0（END）,1(PRESS) , 2(UP)
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setCurrentItem(int itemNum){
        //由于每次进入函数就声明变量，所以不需要再case里面为另外的变量设置false
        boolean isPoetry=false, isMessage=false, isFind=false;
        switch (itemNum) {
            case 0:
                isPoetry = true;
                break;
            case 1:
                isMessage = true;
                break;
            case 2:
                isFind = true;
                break;
            default:
                isPoetry = true;
                break;
        }
        if(total_content.getCurrentItem()!=itemNum){
            total_content.setCurrentItem(itemNum);
        }
        //设置那三个图标的是否选中的状态
        poetry_pavilion.setSelected(isPoetry);
        message_box.setSelected(isMessage);
        find_something.setSelected(isFind);
    }
}
