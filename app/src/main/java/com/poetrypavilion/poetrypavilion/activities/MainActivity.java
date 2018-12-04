package com.poetrypavilion.poetrypavilion.activities;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.poetrypavilion.poetrypavilion.Adapters.MyFragmentPagerAdapter;
import com.poetrypavilion.poetrypavilion.Fragments.Find.FindFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Message.MessageFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.PoetryFragment;
import com.poetrypavilion.poetrypavilion.R;
import com.poetrypavilion.poetrypavilion.Utils.BackHandle.BackHandlerHelper;

import java.util.ArrayList;
import java.util.Objects;

import com.poetrypavilion.poetrypavilion.Utils.MyApplication;
import com.poetrypavilion.poetrypavilion.ViewModels.activityviewmodels.MainActivityViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,ViewPager.OnPageChangeListener {

    //声明变量
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private LinearLayout left_menu_open_icon;
    private NavigationView drawer_view;
    private ViewPager total_content_viewPager;
    private ImageView poetry_pavilion;
    private ImageView message_box;
    private ImageView find_something;
    private ImageView add_something;
    private MainActivityViewModel viewModel;
    private CircleImageView left_navigation_user_image;
    private TextView left_navigation_user_name;
    private LinearLayout left_navigation_login_layout;
    private LinearLayout navigation_top_linearLayout;
    private Button left_navigation_login;
    private CircleImageView left_menu_open_image;
    private Dialog AddDialog;
    private View DialogView;
    private LinearLayout PostPoem;
    private LinearLayout PostArtical;
    private boolean IsInitDislog = false;

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        setIntent(intent);

        getIntentData(getIntent());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);

        //设置viewmodel
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        //如果在注册过程中，活动被系统收回，则会调用onCreate方法，这时候需要得到Intent的数据
        getIntentData(getIntent());

        //初始化
        init();

        //去除默认Title显示
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        //设置viewPager的content
        setViewPagerContent();
        //设置监听
        setListeners();
    }

    private void init(){
        //需要在从数据库请求数据之前就设置监听
        viewModel.setGetLoginUserBackListener(() -> {
            if(viewModel.IsLogin){
                // 把之前的界面隐藏，显示新的界面
                runOnUiThread(this::changeUserHeadAndNameView);
            }else {
                runOnUiThread(()->{
                    Toast.makeText(MyApplication.getContext(),"当前没有用户登录",Toast.LENGTH_SHORT).show();
                });
            }
        });
        //首先就去获取是否登录的信息
        new Thread(()->
                viewModel.getUserInfo()
        ).start();

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        left_menu_open_icon = findViewById(R.id.left_menu_open_icon);
        drawer_view = findViewById(R.id.drawer_view);
        total_content_viewPager = findViewById(R.id.total_content);
        poetry_pavilion = findViewById(R.id.poetry_pavilion);
        message_box = findViewById(R.id.message_box);
        find_something = findViewById(R.id.find_something);
        add_something = findViewById(R.id.add_something);
        left_menu_open_image = findViewById(R.id.left_menu_open_image);
        left_navigation_user_image = drawer_view.getHeaderView(0).findViewById(R.id.left_navigation_user_head);
        left_navigation_user_name = drawer_view.getHeaderView(0).findViewById(R.id.left_navigation_user_name);
        left_navigation_login_layout = drawer_view.getHeaderView(0).findViewById(R.id.left_navigation_login_layout);
        navigation_top_linearLayout = drawer_view.getHeaderView(0).findViewById(R.id.navigation_top_linearLayout);
        left_navigation_login = drawer_view.getHeaderView(0).findViewById(R.id.left_navigation_login);

        DialogView = LayoutInflater.from(this).inflate(R.layout.add_dialog_content, null);
        PostPoem = DialogView.findViewById(R.id.post_new_poems);
        PostArtical = DialogView.findViewById(R.id.post_artical);
    }

    private void setViewPagerContent(){
        ArrayList<Fragment> FragmentList = new ArrayList<>();
        FragmentList.add(new PoetryFragment());
        FragmentList.add(new MessageFragment());
        FragmentList.add(new FindFragment());
        // 注意使用的是：getSupportFragmentManager
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), FragmentList);
        total_content_viewPager.setAdapter(adapter);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
        total_content_viewPager.setOffscreenPageLimit(2);
        total_content_viewPager.addOnPageChangeListener(this);
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
        left_navigation_login.setOnClickListener(this);
        PostPoem.setOnClickListener(this);
        PostArtical.setOnClickListener(this);
    }

    private void getIntentData(Intent intent){
        byte[] image = intent.getByteArrayExtra("head");
        String name = intent.getStringExtra("name");
        if(image!=null&&name!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,
                    image.length);
            navigation_top_linearLayout.setVisibility(View.VISIBLE);
            left_navigation_login_layout.setVisibility(View.GONE);
            left_navigation_user_image.setImageBitmap(bitmap);
            left_menu_open_image.setImageBitmap(bitmap);
            left_navigation_user_name.setText(name);
        }
    }

    //左侧抽屉的select的选择方法
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

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
                if(total_content_viewPager.getCurrentItem()!=0){
                    this.setCurrentItem(0);
                }
                break;
            case R.id.message_box:
                if(total_content_viewPager.getCurrentItem()!=1){
                    this.setCurrentItem(1);
                }
                break;
            case R.id.find_something:
                if(total_content_viewPager.getCurrentItem()!=2){
                    this.setCurrentItem(2);
                }
                break;
            case R.id.add_something:
                if (!IsInitDislog) {
                    initDialog();
                    IsInitDislog = true;
                }
                AddDialog.show();
                break;
            case R.id.left_navigation_login:
                //去往登录和注册的页面
                goToUserLogin();
                //关闭菜单页面,延迟一秒钟
                new Handler().postDelayed(()->{
                    drawer.closeDrawer(GravityCompat.START);
                },1000);
                break;
            case R.id.post_new_poems:
                AddDialog.cancel();
                //判断当前是不是登录状态
                if(left_navigation_login_layout.getVisibility()==View.VISIBLE){
                    goToUserLogin();
                }else {
                    goToEditPoem();
                }
                break;
            case R.id.post_artical:
                //TODO 传递到新的活动中去
                break;
            default:
                break;
        }
    }

    private void initDialog() {
        //初始化dialog
        AddDialog = new Dialog(this, R.style.BottomDialog);
        AddDialog.setContentView(DialogView);
        ViewGroup.LayoutParams layoutParams = DialogView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        DialogView.setLayoutParams(layoutParams);
        Objects.requireNonNull(AddDialog.getWindow()).setGravity(Gravity.BOTTOM);
        AddDialog.setCanceledOnTouchOutside(true);
        AddDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
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
        if(total_content_viewPager.getCurrentItem()!=itemNum){
            total_content_viewPager.setCurrentItem(itemNum);
        }
        //设置那三个图标的是否选中的状态
        poetry_pavilion.setSelected(isPoetry);
        message_box.setSelected(isMessage);
        find_something.setSelected(isFind);
    }

    private void changeUserHeadAndNameView(){
        navigation_top_linearLayout.setVisibility(View.VISIBLE);
        left_navigation_login_layout.setVisibility(View.GONE);
        left_navigation_user_image.setImageBitmap(viewModel.user_Head);
        left_menu_open_image.setImageBitmap(viewModel.user_Head);
        left_navigation_user_name.setText(viewModel.user_name);
    }

    private void goToUserLogin(){
        Intent intent = new Intent(MainActivity.this,MainLoginActivity.class);
        startActivity(intent);
    }

    private void goToEditPoem(){
        Intent intent = new Intent(MainActivity.this,EditPoemActivity.class);
        startActivity(intent);
    }

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
}
