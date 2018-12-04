package com.poetrypavilion.poetrypavilion.activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.UnicodeSetSpanner;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpMainBean;
import com.poetrypavilion.poetrypavilion.R;
import com.poetrypavilion.poetrypavilion.Utils.FileAndBitmapAndBytes;
import com.poetrypavilion.poetrypavilion.Utils.StatusBarUtils;
import com.poetrypavilion.poetrypavilion.ViewModels.activityviewmodels.MainLoginActivityViewModel;
import com.poetrypavilion.poetrypavilion.databinding.ContentMainLoginBinding;

import java.io.File;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainLoginActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnTouchListener {
    private ContentMainLoginBinding dataBinding;
    private MainLoginActivityViewModel viewModel;
    private String register_email;
    private String register_password;
    private String register_confirm_password;
    private Level level;
    private String loginEmail;
    private String loginPassword;

    enum Level{
        Main,Login,Register
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        //让LinearLayout的背景适配状态栏
        StatusBarUtils.setImage(this);

        dataBinding = DataBindingUtil.setContentView(this,R.layout.content_main_login);
        //设置viewmodel
        viewModel = ViewModelProviders.of(this).get(MainLoginActivityViewModel.class);
        //设置监听器
        initListener();
        //设置变量的初始值
        init();
    }

    private void init(){
        level=Level.Main;
    }

    private void initListener(){
        dataBinding.LoginFirst.setOnClickListener(this);
        dataBinding.Register.setOnClickListener(this);
        dataBinding.registerCheckButton.setOnClickListener(this);
        dataBinding.registerSecond.setOnClickListener(this);
        dataBinding.registerEditPasswordImg.setOnTouchListener(this);
        dataBinding.registerConfirmPasswordImg.setOnTouchListener(this);
        dataBinding.loginBack.setOnClickListener(this);
        dataBinding.LoginSecond.setOnClickListener(this);

        //添加文字监听,只要编辑一个字，就会调用这个方法
        dataBinding.registerEditEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //减少判断次数
                if(! dataBinding.registerCheckButton.getText().toString().equals("检测"))
                    if(dataBinding.registerCheckButton.getText().toString().equals("可用")){
                        dataBinding.noteText.setText("请检测邮箱是否可用");
                        dataBinding.registerCheckButton.setText("检测");
                    }else if(dataBinding.registerCheckButton.getText().toString().equals("不可用")){
                        dataBinding.noteText.setText("请检测邮箱是否可用");
                        dataBinding.registerCheckButton.setText("检测");
                    }else if(dataBinding.registerCheckButton.getText().toString().equals("点击重试")){
                    //更改大小
                        dataBinding.registerCheckButton.getLayoutParams().width=dataBinding.registerCheckButton.getLayoutParams().width/7*5;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                dataBinding.registerEditEmail.setPaddingRelative(
                                        dataBinding.registerEditEmail.getPaddingStart(),dataBinding.registerEditEmail.getPaddingTop(),
                                        dataBinding.registerEditEmail.getPaddingEnd()/7*5,dataBinding.registerEditEmail.getPaddingBottom());
                            }
                        }
                        dataBinding.noteText.setText("请检测邮箱是否可用");
                        dataBinding.registerCheckButton.setText("检测");
                    }
            }
        });

        viewModel.setOnResponseBackListener(new MainLoginActivityViewModel.ResponseListener() {
            @Override
            public void onCheckEmailBack(boolean status, String message) {
                if(status){
                    runOnUiThread(()->{
                        dataBinding.registerCheckButton.setText("可用");
                        dataBinding.noteText.setText("邮箱可用");

                        //为了避免正在检测的时候，用户点击了返回按钮，当结果再次打开页面的时候结果返回结果出现，因此在这里需要清除信息
                        if(level==Level.Main){
                            clearMessage();
                        }
                    });
                }else {
                    runOnUiThread(()->{
                        if(message.equals("该邮箱已被注册")){
                            dataBinding.registerCheckButton.setText("不可用");
                            dataBinding.noteText.setText(message);
                        }else if(message.equals("连接服务器时出错")){
                            //更改按钮的大小和edittext的右侧padding
                            dataBinding.registerCheckButton.getLayoutParams().width=dataBinding.registerCheckButton.getLayoutParams().width/5*7;
                            //只有当之前的字长是2的时候才需要将其加长
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    dataBinding.registerEditEmail.setPaddingRelative(
                                            dataBinding.registerEditEmail.getPaddingStart(),dataBinding.registerEditEmail.getPaddingTop(),
                                            dataBinding.registerEditEmail.getPaddingEnd()/5*7,dataBinding.registerEditEmail.getPaddingBottom());
                                }
                            }
                            dataBinding.registerCheckButton.setText("点击重试");
                            dataBinding.noteText.setText("连接服务器超时");
                        }
                        //为了避免正在检测的时候，用户点击了返回按钮，当结果再次打开页面的时候结果返回结果出现，因此在这里需要清除信息
                        if(level==Level.Main){
                            clearMessage();
                        }
                    });
                }
            }
            @Override
            public void onRegisterRealBack(boolean status, String message) {
                if(status){
                    runOnUiThread(()->{
                        //注册成功
                        dataBinding.noteText.setText("注册成功");
                        dataBinding.registerSecond.setText("注册");
                        //这里需要这里需要跳转页面
                        Intent intent = new Intent(MainLoginActivity.this,EditUserInfoActivity.class);
                        startActivity(intent);
                        //为了避免正在检测的时候，用户点击了返回按钮，当结果再次打开页面的时候结果返回结果出现，因此在这里需要清除信息
                        if(level==Level.Main){
                            clearMessage();
                        }
                    });
                }else {
                    runOnUiThread(()->{
                        dataBinding.registerSecond.setText("注册");
                        switch (message) {
                            case "连接服务器时出错":
                                dataBinding.noteText.setText("连接服务器超时");
                                break;
                            case "该邮箱已被注册":
                                dataBinding.noteText.setText("该邮箱已被注册，请直接登录");
                                break;
                            case "注册失败":
                                dataBinding.noteText.setText("注册失败，请重试");
                                break;
                        }
                        //为了避免正在检测的时候，用户点击了返回按钮，当结果再次打开页面的时候结果返回结果出现，因此在这里需要清除信息
                        if(level==Level.Main){
                            clearMessage();
                        }
                    });
                }
            }

            @Override
            public void onLoginRealBack(boolean status, String message) {
                if(status){
                    runOnUiThread(()->{
                        if(level==Level.Main){
                            clearMessage();
                        }
                        if(message.equals("登录成功,但是获取用户头像失败")){
                            dataBinding.loginNoteText.setText("登录成功,但是获取用户头像失败");
                            Toast.makeText(getApplicationContext(),"登录成功,但获取用户头像失败",Toast.LENGTH_SHORT).show();
                            @SuppressLint("ResourceType")
                            InputStream is = getResources().openRawResource(R.drawable.default_user_head_img);
                            Bitmap mBitmap = BitmapFactory.decodeStream(is);

                            Intent intent = new Intent(MainLoginActivity.this,MainActivity.class);
                            intent.putExtra("head",FileAndBitmapAndBytes.BitmapToBytes(mBitmap));
                            intent.putExtra("name",viewModel.getMainLoginRepository().getLoginName());
                            startActivity(intent);
                        }else if(message.equals("登录成功")) {
                            dataBinding.loginNoteText.setText("登录成功");
                            Intent intent = new Intent(MainLoginActivity.this,MainActivity.class);
                            intent.putExtra("head",viewModel.getMainLoginRepository().getUserHeaderImg());
                            intent.putExtra("name",viewModel.getMainLoginRepository().getLoginName());
                            startActivity(intent);
                        }
                    });
                }else {
                    runOnUiThread(()->{
                        if(level==Level.Main){
                            clearMessage();
                        }
                        switch (message){
                            case "连接服务器时出错":
                                dataBinding.loginNoteText.setText("连接服务器时出错");
                                break;
                            case "邮箱不存在":
                                dataBinding.loginNoteText.setText("邮箱不存在");
                                break;
                            case "密码错误":
                                dataBinding.loginNoteText.setText("密码错误");
                                break;
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(level==Level.Login){
            level = Level.Main;
            dataBinding.loginMainLayout.setVisibility(View.VISIBLE);
            dataBinding.loginRealLayout.setVisibility(View.GONE);
            dataBinding.loginBack.setVisibility(View.GONE);
            //更改信息
            clearMessage();
        }else if(level==Level.Register){
            level = Level.Main;
            dataBinding.RegisterRealLayout.setVisibility(View.GONE);
            dataBinding.loginMainLayout.setVisibility(View.VISIBLE);
            dataBinding.loginBack.setVisibility(View.GONE);
            //更改信息
            clearMessage();
        }else {
            //返回上一级
            //TODO 这里可能需要一些处理，什么处理？
            super.onBackPressed();
        }
    }

    private void clearMessage(){
        register_password=null;
        register_confirm_password=null;
        register_email=null;
        if(dataBinding.registerCheckButton.getText().toString().equals("点击重试")){
            dataBinding.registerCheckButton.getLayoutParams().width=dataBinding.registerCheckButton.getLayoutParams().width/7*5;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    dataBinding.registerEditEmail.setPaddingRelative(
                            dataBinding.registerEditEmail.getPaddingStart(),dataBinding.registerEditEmail.getPaddingTop(),
                            dataBinding.registerEditEmail.getPaddingEnd()/7*5,dataBinding.registerEditEmail.getPaddingBottom());
                }
            }
        }
        dataBinding.registerCheckButton.setText("检测");
        dataBinding.registerEditEmail.setText("");
        dataBinding.registerEditPassword.setText("");
        dataBinding.registerConfirmPassword.setText("");
        dataBinding.noteText.setText("请输入相关信息");
        dataBinding.registerSecond.setText("注册");
        dataBinding.loginNoteText.setText("请输入相关信息");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Login_first:  //第一个登录的那个按钮
                level=Level.Login;
                dataBinding.loginMainLayout.setVisibility(View.GONE);
                dataBinding.loginRealLayout.setVisibility(View.VISIBLE);
                dataBinding.loginBack.setVisibility(View.VISIBLE);
                break;
            case R.id.Register:    //第一个注册按钮
                level=Level.Register;
                dataBinding.loginMainLayout.setVisibility(View.GONE);
                dataBinding.RegisterRealLayout.setVisibility(View.VISIBLE);
                dataBinding.loginBack.setVisibility(View.VISIBLE);
                break;
            case R.id.register_check_button: //注册界面 检测 按钮
                //先查看button的字是不是  检测  ，如果不是，点击的话就不去检测了
                if(dataBinding.registerCheckButton.getText().toString().equals("检测")
                        ||dataBinding.registerCheckButton.getText().toString().equals("点击重试")){
                    register_email = dataBinding.registerEditEmail.getText().toString();
                    if(isEmail(register_email)){
                        dataBinding.noteText.setText("请输入正确的邮箱格式");
                    }else {
                        if(dataBinding.registerCheckButton.getText().toString().equals("点击重试")){
                            dataBinding.registerCheckButton.getLayoutParams().width=dataBinding.registerCheckButton.getLayoutParams().width/7*5;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    dataBinding.registerEditEmail.setPaddingRelative(
                                            dataBinding.registerEditEmail.getPaddingStart(),dataBinding.registerEditEmail.getPaddingTop(),
                                            dataBinding.registerEditEmail.getPaddingEnd()/7*5,dataBinding.registerEditEmail.getPaddingBottom());
                                }
                            }
                        }
                        //检测邮箱是不是可用的
                        dataBinding.registerCheckButton.setText("检测中");
                        dataBinding.noteText.setText("正在检测邮箱是否可用...");
                        checkEmail();
                    }
                }

                break;
            case R.id.register_second:  //注册界面注册按钮
                if(dataBinding.registerSecond.getText().toString().equals("注册")){
                    register_email = dataBinding.registerEditEmail.getText().toString();
                    if(isEmail(register_email)){
                        dataBinding.noteText.setText("请输入正确的邮箱格式");
//                    Toast.makeText(this,"请输入正确的邮箱格式",Toast.LENGTH_SHORT).show();
                    }else {
                        checkRegisterPassword();
                    }
                }else if(dataBinding.registerSecond.getText().toString().equals("注册中")){
                    Toast.makeText(this,"正在为您注册，请勿重复点击",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_back: //如果点击了返回的按钮
                if(level==Level.Login){
                    level = Level.Main;
                    dataBinding.loginMainLayout.setVisibility(View.VISIBLE);
                    dataBinding.loginRealLayout.setVisibility(View.GONE);
                    dataBinding.loginBack.setVisibility(View.GONE);
                    //清除信息
                    clearMessage();
                }else if(level==Level.Register){
                    level = Level.Main;
                    dataBinding.RegisterRealLayout.setVisibility(View.GONE);
                    dataBinding.loginMainLayout.setVisibility(View.VISIBLE);
                    dataBinding.loginBack.setVisibility(View.GONE);
                    //清除信息
                    clearMessage();
                }
                break;
            case R.id.Login_second://第二个登录按钮
                loginEmail = dataBinding.loginEditEmail.getText().toString();
                if(isEmail(loginEmail)){
                    dataBinding.loginNoteText.setText("请输入正确的邮箱格式");
                }else {
                    checkLoginPassword();
                }
        }
    }

    private void checkLoginPassword(){
        loginPassword = dataBinding.loginEditPassword.getText().toString();
        if(loginPassword.isEmpty()){
            dataBinding.loginNoteText.setText("请输入密码");
        }else {
            dataBinding.loginNoteText.setText("正在登录...");
            LoginReal();
        }
    }

    private void checkRegisterPassword(){
        register_password = dataBinding.registerEditPassword.getText().toString();
        register_confirm_password = dataBinding.registerConfirmPassword.getText().toString();
        if(register_password.length()<=6||register_confirm_password.length()<=6){
            dataBinding.noteText.setText("密码长度必须大于6位");
        }else {
            if(register_password.equals(register_confirm_password)){
                dataBinding.noteText.setText("正在注册，请稍后...");
                dataBinding.registerSecond.setText("注册中");
                registerReal();
            }else {
                dataBinding.noteText.setText("两次输入的密码不一致");
            }
        }
    }

    private  boolean isEmail(String email){
        if (null==email || "".equals(email)) return true;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return !m.matches();
    }

    private void checkEmail(){
        new Thread(()-> viewModel.RegisterCheckEmail(register_email)).start();
    }

    private void LoginReal(){
        new Thread(()->{
            HttpMainBean checkRegisterBean = viewModel.LoginReal(loginEmail,loginPassword);
            //检测密码是否能加密
            if(!checkRegisterBean.isStatus()){
                runOnUiThread(()-> dataBinding.loginNoteText.setText(checkRegisterBean.getMessage()));
            }
        }).start();
    }

    private void registerReal(){
        new Thread(()->{
            HttpMainBean checkRegisterBean = viewModel.RegisterReal(register_email,register_password);
            //检测密码是否能加密
            if(!checkRegisterBean.isStatus()){
                runOnUiThread(()-> dataBinding.noteText.setText(checkRegisterBean.getMessage()));
            }
        }).start();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.register_edit_password_img:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域，密码隐藏
                        dataBinding.registerEditPasswordImg.setImageResource(R.drawable.eye_close);
                        dataBinding.registerEditPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域，显示密码
                        dataBinding.registerEditPasswordImg.setImageResource(R.drawable.eye_open);
                        dataBinding.registerEditPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        break;
                    default:
                        break;
                }
                break;
            case R.id.register_confirm_password_img:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域，密码隐藏
                        dataBinding.registerConfirmPasswordImg.setImageResource(R.drawable.eye_close);
                        dataBinding.registerConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域，显示密码
                        dataBinding.registerConfirmPasswordImg.setImageResource(R.drawable.eye_open);
                        dataBinding.registerConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        break;
                    default:
                        break;
                }
                break;
        }
        return true;
    }
}
