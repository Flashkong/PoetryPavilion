package com.poetrypavilion.poetrypavilion.activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpMainBean;
import com.poetrypavilion.poetrypavilion.R;
import com.poetrypavilion.poetrypavilion.Utils.FileAndBitmapAndBytes;
import com.poetrypavilion.poetrypavilion.ViewModels.activityviewmodels.EditPoemActivityViewModel;
import com.poetrypavilion.poetrypavilion.databinding.PoemEditorBinding;

import java.io.File;

import jp.wasabeef.richeditor.RichEditor;

public class EditPoemActivity extends AppCompatActivity implements RichEditor.OnTextChangeListener,
        View.OnTouchListener {
    private PoemEditorBinding dataBinding;
    private EditPoemActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poem_editor);

        dataBinding = DataBindingUtil.setContentView(this,R.layout.poem_editor);
        viewModel = ViewModelProviders.of(this).get(EditPoemActivityViewModel.class);
        init();
        SetListener();
        setViewModelListener();
    }

    private void init(){
        dataBinding.poemEditor.setPadding(10, 10, 10, 10);
        dataBinding.poemEditor.setPlaceholder("编辑诗歌内容");
        if(viewModel.Text!=null){
            dataBinding.poemEditor.setHtml(viewModel.Text);
        }
        if(viewModel.PoemTitle!=null){
            dataBinding.editPoemTitle.setText(viewModel.PoemTitle);
        }
    }

    private void setViewModelListener(){
        viewModel.setOnEditPoemBackListener((status, message) -> {
            switch (message){
                case "连接服务器时出错":
                    runOnUiThread(()->{
                        Toast.makeText(this,"连接服务器时出错，请检查网络连接",Toast.LENGTH_SHORT).show();
                    });
                    break;
                case "用户不存在":
                    runOnUiThread(()->{
                        Toast.makeText(this,"登录用户信息出错，请重新登录",Toast.LENGTH_SHORT).show();
                    });
                    break;
                case "上传成功":
                    runOnUiThread(()->{
                        Toast.makeText(this,"上传成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditPoemActivity.this,MainActivity.class);
                        startActivity(intent);
                    });
                    break;
                case "上传失败":
                    runOnUiThread(()->{
                        Toast.makeText(this,"上传失败，请重试",Toast.LENGTH_SHORT).show();
                    });
                    break;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void SetListener(){
        dataBinding.poemEditor.setOnTextChangeListener(this);
        dataBinding.editPoemSendButton.setOnTouchListener(this);
        dataBinding.editPoemCancel.setOnTouchListener(this);
        dataBinding.editPoemTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.PoemTitle = dataBinding.editPoemTitle.getText().toString();
            }
        });
        dataBinding.actionUndo.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域，密码隐藏
                    dataBinding.actionUndo.setImageResource(R.drawable.undo);
                    dataBinding.poemEditor.undo();
                    break;
                case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域，显示密码
                    dataBinding.actionUndo.setImageResource(R.drawable.undo_press);
                    break;
                default:
                    break;
            }
            return true;
        });

        dataBinding.actionRedo.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域，密码隐藏
                    dataBinding.actionRedo.setImageResource(R.drawable.redo);
                    dataBinding.poemEditor.redo();
                    break;
                case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域，显示密码
                    dataBinding.actionRedo.setImageResource(R.drawable.redo_press);
                    break;
                default:
                    break;
            }
            return true;
        });

        dataBinding.actionBold.setOnClickListener(v->{
            dataBinding.poemEditor.setBold();
            if(viewModel.isBold){
                dataBinding.actionBold.setImageResource(R.drawable.bold);
            }else {
                dataBinding.actionBold.setImageResource(R.drawable.bold_press);
            }
            viewModel.isBold=!viewModel.isBold;
        });

        dataBinding.actionItalic.setOnClickListener(v->{
            dataBinding.poemEditor.setItalic();
            if(viewModel.isItalic){
                dataBinding.actionItalic.setImageResource(R.drawable.italic);
            }else {
                dataBinding.actionItalic.setImageResource(R.drawable.italic_press);
            }
            viewModel.isItalic=!viewModel.isItalic;
        });

        dataBinding.actionStrikethrough.setOnClickListener(v->{
            dataBinding.poemEditor.setStrikeThrough();
            if(viewModel.isStrikeThrough){
                dataBinding.actionStrikethrough.setImageResource(R.drawable.strikethrough);
            }else {
                dataBinding.actionStrikethrough.setImageResource(R.drawable.strikethrough_press);
            }
            viewModel.isStrikeThrough=!viewModel.isStrikeThrough;
        });

        dataBinding.actionUnderline.setOnClickListener(v->{
            dataBinding.poemEditor.setUnderline();
            if(viewModel.isUnderLine){
                dataBinding.actionUnderline.setImageResource(R.drawable.underline);
            }else {
                dataBinding.actionUnderline.setImageResource(R.drawable.underline_press);
            }
            viewModel.isUnderLine=!viewModel.isUnderLine;
        });

        dataBinding.actionHeading1.setOnClickListener(v->{
            dataBinding.poemEditor.setHeading(1);
            if(viewModel.isH1){
                dataBinding.actionHeading1.setImageResource(R.drawable.h1);
            }else {
                dataBinding.actionHeading1.setImageResource(R.drawable.h1_press);
                if(viewModel.isH2){
                    dataBinding.actionHeading2.setImageResource(R.drawable.h2);
                    viewModel.isH2=false;
                }
                if(viewModel.isH3){
                    dataBinding.actionHeading3.setImageResource(R.drawable.h3);
                    viewModel.isH3=false;
                }
                if(viewModel.isH4){
                    dataBinding.actionHeading4.setImageResource(R.drawable.h4);
                    viewModel.isH4=false;
                }
                if(viewModel.isH5){
                    dataBinding.actionHeading5.setImageResource(R.drawable.h5);
                    viewModel.isH5=false;
                }
                if(viewModel.isH6){
                    dataBinding.actionHeading6.setImageResource(R.drawable.h6);
                    viewModel.isH6=false;
                }
            }
            viewModel.isH1=!viewModel.isH1;
        });

        dataBinding.actionHeading2.setOnClickListener(v->{
            dataBinding.poemEditor.setHeading(2);
            if(viewModel.isH2){
                dataBinding.actionHeading2.setImageResource(R.drawable.h2);
            }else {
                dataBinding.actionHeading2.setImageResource(R.drawable.h2_press);
                if(viewModel.isH1){
                    dataBinding.actionHeading1.setImageResource(R.drawable.h1);
                    viewModel.isH1=false;
                }
                if(viewModel.isH3){
                    dataBinding.actionHeading3.setImageResource(R.drawable.h3);
                    viewModel.isH3=false;
                }
                if(viewModel.isH4){
                    dataBinding.actionHeading4.setImageResource(R.drawable.h4);
                    viewModel.isH4=false;
                }
                if(viewModel.isH5){
                    dataBinding.actionHeading5.setImageResource(R.drawable.h5);
                    viewModel.isH5=false;
                }
                if(viewModel.isH6){
                    dataBinding.actionHeading6.setImageResource(R.drawable.h6);
                    viewModel.isH6=false;
                }
            }
            viewModel.isH2=!viewModel.isH2;
        });

        dataBinding.actionHeading3.setOnClickListener(v->{
            dataBinding.poemEditor.setHeading(3);
            if(viewModel.isH3){
                dataBinding.actionHeading3.setImageResource(R.drawable.h3);
            }else {
                dataBinding.actionHeading3.setImageResource(R.drawable.h3_press);
                if(viewModel.isH2){
                    dataBinding.actionHeading2.setImageResource(R.drawable.h2);
                    viewModel.isH2=false;
                }
                if(viewModel.isH1){
                    dataBinding.actionHeading1.setImageResource(R.drawable.h1);
                    viewModel.isH1=false;
                }
                if(viewModel.isH4){
                    dataBinding.actionHeading4.setImageResource(R.drawable.h4);
                    viewModel.isH4=false;
                }
                if(viewModel.isH5){
                    dataBinding.actionHeading5.setImageResource(R.drawable.h5);
                    viewModel.isH5=false;
                }
                if(viewModel.isH6){
                    dataBinding.actionHeading6.setImageResource(R.drawable.h6);
                    viewModel.isH6=false;
                }
            }
            viewModel.isH3=!viewModel.isH3;
        });

        dataBinding.actionHeading4.setOnClickListener(v->{
            dataBinding.poemEditor.setHeading(4);
            if(viewModel.isH4){
                dataBinding.actionHeading4.setImageResource(R.drawable.h4);
            }else {
                dataBinding.actionHeading4.setImageResource(R.drawable.h4_press);
                if(viewModel.isH2){
                    dataBinding.actionHeading2.setImageResource(R.drawable.h2);
                    viewModel.isH2=false;
                }
                if(viewModel.isH3){
                    dataBinding.actionHeading3.setImageResource(R.drawable.h3);
                    viewModel.isH3=false;
                }
                if(viewModel.isH1){
                    dataBinding.actionHeading1.setImageResource(R.drawable.h1);
                    viewModel.isH1=false;
                }
                if(viewModel.isH5){
                    dataBinding.actionHeading5.setImageResource(R.drawable.h5);
                    viewModel.isH5=false;
                }
                if(viewModel.isH6){
                    dataBinding.actionHeading6.setImageResource(R.drawable.h6);
                    viewModel.isH6=false;
                }
            }
            viewModel.isH4=!viewModel.isH4;
        });

        dataBinding.actionHeading5.setOnClickListener(v->{
            dataBinding.poemEditor.setHeading(5);
            if(viewModel.isH5){
                dataBinding.actionHeading5.setImageResource(R.drawable.h5);
            }else {
                dataBinding.actionHeading5.setImageResource(R.drawable.h5_press);
                if(viewModel.isH2){
                    dataBinding.actionHeading2.setImageResource(R.drawable.h2);
                    viewModel.isH2=false;
                }
                if(viewModel.isH3){
                    dataBinding.actionHeading3.setImageResource(R.drawable.h3);
                    viewModel.isH3=false;
                }
                if(viewModel.isH4){
                    dataBinding.actionHeading4.setImageResource(R.drawable.h4);
                    viewModel.isH4=false;
                }
                if(viewModel.isH1){
                    dataBinding.actionHeading1.setImageResource(R.drawable.h1);
                    viewModel.isH1=false;
                }
                if(viewModel.isH6){
                    dataBinding.actionHeading6.setImageResource(R.drawable.h6);
                    viewModel.isH6=false;
                }
            }
            viewModel.isH5=!viewModel.isH5;
        });

        dataBinding.actionHeading6.setOnClickListener(v->{
            dataBinding.poemEditor.setHeading(6);
            if(viewModel.isH6){
                dataBinding.actionHeading6.setImageResource(R.drawable.h6);
            }else {
                dataBinding.actionHeading6.setImageResource(R.drawable.h6_press);
                if(viewModel.isH2){
                    dataBinding.actionHeading2.setImageResource(R.drawable.h2);
                    viewModel.isH2=false;
                }
                if(viewModel.isH3){
                    dataBinding.actionHeading3.setImageResource(R.drawable.h3);
                    viewModel.isH3=false;
                }
                if(viewModel.isH4){
                    dataBinding.actionHeading4.setImageResource(R.drawable.h4);
                    viewModel.isH4=false;
                }
                if(viewModel.isH5){
                    dataBinding.actionHeading5.setImageResource(R.drawable.h5);
                    viewModel.isH5=false;
                }
                if(viewModel.isH1){
                    dataBinding.actionHeading1.setImageResource(R.drawable.h1);
                    viewModel.isH1=false;
                }
            }
            viewModel.isH6=!viewModel.isH6;
        });

        dataBinding.actionIndent.setOnTouchListener((v,event)->{
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域，密码隐藏
                    dataBinding.actionIndent.setImageResource(R.drawable.indent);
                    dataBinding.poemEditor.setIndent();
                    break;
                case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域，显示密码
                    dataBinding.actionIndent.setImageResource(R.drawable.indent_press);
                    break;
                default:
                    break;
            }
            return true;
        });

        dataBinding.actionOutdent.setOnTouchListener((v,event)->{
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域，密码隐藏
                    dataBinding.actionOutdent.setImageResource(R.drawable.outdent);
                    dataBinding.poemEditor.setOutdent();
                    break;
                case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域，显示密码
                    dataBinding.actionOutdent.setImageResource(R.drawable.outdent_press);
                    break;
                default:
                    break;
            }
            return true;
        });

        dataBinding.actionAlignLeft.setOnClickListener(v->{
            dataBinding.poemEditor.setAlignLeft();
            if(viewModel.isAlignLeft){
                dataBinding.actionAlignLeft.setImageResource(R.drawable.justify_left);
            }else {
                dataBinding.actionAlignLeft.setImageResource(R.drawable.justify_left_press);
                if(viewModel.isAlignCenter){
                    dataBinding.actionAlignCenter.setImageResource(R.drawable.justify_center);
                    viewModel.isAlignCenter=false;
                }
                if(viewModel.isAlignRight){
                    dataBinding.actionAlignRight.setImageResource(R.drawable.justify_right);
                    viewModel.isAlignRight=false;
                }
            }
            viewModel.isAlignLeft=!viewModel.isAlignLeft;
        });

        dataBinding.actionAlignCenter.setOnClickListener(v->{
            dataBinding.poemEditor.setAlignCenter();
            if(viewModel.isAlignCenter){
                dataBinding.actionAlignCenter.setImageResource(R.drawable.justify_center);
            }else {
                dataBinding.actionAlignCenter.setImageResource(R.drawable.justify_center_press);
                if(viewModel.isAlignLeft){
                    dataBinding.actionAlignLeft.setImageResource(R.drawable.justify_left);
                    viewModel.isAlignLeft=false;
                }
                if(viewModel.isAlignRight){
                    dataBinding.actionAlignRight.setImageResource(R.drawable.justify_right);
                    viewModel.isAlignRight=false;
                }
            }
            viewModel.isAlignCenter=!viewModel.isAlignCenter;
        });

        dataBinding.actionAlignRight.setOnClickListener(v->{
            dataBinding.poemEditor.setAlignRight();
            if(viewModel.isAlignRight){
                dataBinding.actionAlignRight.setImageResource(R.drawable.justify_right);
            }else {
                dataBinding.actionAlignRight.setImageResource(R.drawable.justify_right_press);
                if(viewModel.isAlignLeft){
                    dataBinding.actionAlignLeft.setImageResource(R.drawable.justify_left);
                    viewModel.isAlignLeft=false;
                }
                if(viewModel.isAlignCenter){
                    dataBinding.actionAlignCenter.setImageResource(R.drawable.justify_center);
                    viewModel.isAlignCenter=false;
                }
            }
            viewModel.isAlignRight=!viewModel.isAlignRight;
        });

        dataBinding.actionBlockquote.setOnTouchListener((v,event)->{
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域，密码隐藏
                    dataBinding.actionBlockquote.setImageResource(R.drawable.blockquote);
                    dataBinding.poemEditor.setBlockquote();
                    break;
                case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域，显示密码
                    dataBinding.actionBlockquote.setImageResource(R.drawable.blockquote_press);
                    break;
                default:
                    break;
            }
            return true;
        });

        dataBinding.actionInsertBullets.setOnClickListener(v->{
            dataBinding.poemEditor.setBullets();
            if(viewModel.isBullets){
                dataBinding.actionInsertBullets.setImageResource(R.drawable.bullets);
            }else {
                dataBinding.actionInsertBullets.setImageResource(R.drawable.bullets_press);
                if(viewModel.isNumbers){
                    dataBinding.actionInsertNumbers.setImageResource(R.drawable.numbers);
                    viewModel.isNumbers=false;
                }
            }
            viewModel.isBullets=!viewModel.isBullets;
        });

        dataBinding.actionInsertNumbers.setOnClickListener(v->{
            dataBinding.poemEditor.setNumbers();
            if(viewModel.isNumbers){
                dataBinding.actionInsertNumbers.setImageResource(R.drawable.numbers);
            }else {
                dataBinding.actionInsertNumbers.setImageResource(R.drawable.numbers_press);
                if(viewModel.isBullets){
                    dataBinding.actionInsertBullets.setImageResource(R.drawable.bullets);
                    viewModel.isBullets=false;
                }
            }
            viewModel.isNumbers=!viewModel.isNumbers;
        });
    }

    @Override
    public void onTextChange(String text) {
        viewModel.Text=text;
        if(viewModel.Text!=null && !viewModel.Text.isEmpty()){
            setButtonNoPress();
        }else {
            setButtonOrigin();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.edit_poem_send_button:
                if(viewModel.Text!=null && !viewModel.Text.isEmpty()){
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域，密码隐藏
                            if(!viewModel.PoemTitle.isEmpty()){
                                File file = FileAndBitmapAndBytes.StringToFile(
                                        getFilesDir().getPath()+"/temp.txt",viewModel.Text);
                                new Thread(()->{
                                    HttpMainBean bean = viewModel.sendPoem(file, viewModel.PoemTitle, "temp.txt");
                                    if(!bean.isStatus()){
                                        runOnUiThread(()->
                                            Toast.makeText(this,bean.getMessage(),Toast.LENGTH_SHORT).show()
                                        );
                                    }
                                }
                                ).start();
                                setButtonNoPress();
                            }else {
                                Toast.makeText(this,"请输入诗歌标题",Toast.LENGTH_SHORT).show();
                            }

                            break;
                        case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域，显示密码
                            setButtonPress();
                            break;
                        default:
                            break;
                    }
                }else {
                    setButtonOrigin();
                }
                break;
            case R.id.edit_poem_cancel:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域，密码隐藏
                        dataBinding.editPoemCancel.setTextColor(Color.rgb(170,170,170));
                        onBackPressed();
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域，显示密码
                        dataBinding.editPoemCancel.setTextColor(Color.rgb(34,153,84));
                        break;
                    default:
                        break;
                }
                break;
        }
        return true;
    }

    private void setButtonPress(){
        dataBinding.editPoemSendButton.setBackgroundResource(R.drawable.button_add_send_have_text_press);
        dataBinding.editPoemSendButton.setTextColor(Color.WHITE);
    }

    private void setButtonNoPress(){
        dataBinding.editPoemSendButton.setBackgroundResource(R.drawable.button_add_send_have_text);
        dataBinding.editPoemSendButton.setTextColor(Color.WHITE);
    }

    private void setButtonOrigin(){
        dataBinding.editPoemSendButton.setBackgroundResource(R.drawable.button_add_send_no_text);
        dataBinding.editPoemSendButton.setTextColor(Color.rgb(223,223,223));
    }
}
