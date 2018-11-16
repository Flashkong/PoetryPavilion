package com.poetrypavilion.poetrypavilion.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.poetrypavilion.poetrypavilion.R;
import com.poetrypavilion.poetrypavilion.Utils.StatusBarUtils;
import com.poetrypavilion.poetrypavilion.ViewModels.activityviewmodels.EditUserInfoActivityViewModel;
import com.poetrypavilion.poetrypavilion.databinding.EditUserInfoBinding;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class EditUserInfoActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private EditUserInfoBinding dataBinding;
    private TextView CancelTextView;
    private TextView CameraTextView;
    private TextView PhotoTextView;
    private View DialogView;
    private Dialog bottomDialog;
    private EditUserInfoActivityViewModel viewModel;
    private boolean IsInitDislog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        //让LinearLayout的背景适配状态栏
        StatusBarUtils.setImage(this);
        //设置databinding
        dataBinding = DataBindingUtil.setContentView(this, R.layout.edit_user_info);
        viewModel = ViewModelProviders.of(this).get(EditUserInfoActivityViewModel.class);

        init();
        initListener();
    }

    private void init() {
        DialogView = LayoutInflater.from(this).inflate(R.layout.dialog_content_normal, null);
        CancelTextView = DialogView.findViewById(R.id.edit_user_info_dialog_cancel_button);
        CameraTextView = DialogView.findViewById(R.id.edit_user_info_dialog_camera_button);
        PhotoTextView = DialogView.findViewById(R.id.edit_user_info_dialog_photo_button);

        viewModel.IsHavePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission
                .WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if(viewModel.Storage_URL==null)
            viewModel.Storage_URL = Environment.getExternalStorageDirectory() + "/PoetryPavilion/camera";

        //如果旋转屏幕的话，保证图片还是原来拍摄的
        if(viewModel.HeadBtmap!=null)
            dataBinding.editUserInfoUserHead.setImageBitmap(viewModel.HeadBtmap);
    }

    private void initListener() {
        dataBinding.editUserInfoUserHead.setOnClickListener(this);
        CancelTextView.setOnClickListener(this);
        CameraTextView.setOnClickListener(this);
        PhotoTextView.setOnClickListener(this);
        dataBinding.editUserInfoOk.setOnTouchListener(this);
        viewModel.setOnResponseBackListener((status, message) -> {
            //修改状态
            viewModel.IsSendRequest = false;
            if (status) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "编辑成功！", Toast.LENGTH_SHORT).show();
                    //TODO 这里写好跳转的代码
                });
            } else {
                switch (message) {
                    case "连接服务器时出错":
                        runOnUiThread(() -> {
                            Toast.makeText(this, "连接超时，请稍后重试", Toast.LENGTH_SHORT).show();
                        });
                        break;
                    case "用户没有注册成功，请重试！":
                        runOnUiThread(() -> {
                            Toast.makeText(this, "该用户没有注册成功，请重新注册", Toast.LENGTH_LONG).show();
                        });
                        //直接返回上一步
                        onBackPressed();
                        break;
                    case "修改失败，请重试！":
                        runOnUiThread(() -> {
                            Toast.makeText(this, "修改失败，请重试", Toast.LENGTH_SHORT).show();
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_user_info_userHead:
                if (!IsInitDislog) {
                    initDialog();
                    IsInitDislog = true;
                }
                bottomDialog.show();
                break;
            case R.id.edit_user_info_dialog_camera_button:
                viewModel.option = EditUserInfoActivityViewModel.Option.Camera;
                if (!viewModel.IsHavePermission)
                    requestPermission();
                else {
                    openCamera();
                }
                break;
            case R.id.edit_user_info_dialog_photo_button:
                viewModel.option = EditUserInfoActivityViewModel.Option.Photo;
                if (!viewModel.IsHavePermission)
                    requestPermission();
                else {
                    openPhoto();
                }
                break;
            case R.id.edit_user_info_dialog_cancel_button:
                bottomDialog.cancel();
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.edit_user_info_ok:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域，密码隐藏
                        dataBinding.editUserInfoOk.setImageResource(R.drawable.ok);
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域，显示密码
                        dataBinding.editUserInfoOk.setImageResource(R.drawable.okwhite);
                        //首先要检测是否已经有了权限
                        viewModel.option = EditUserInfoActivityViewModel.Option.Ok;
                        if (!viewModel.IsHavePermission)
                            requestPermission();
                        else {
                            if(!viewModel.IsSendRequest)
                                sendRequest();
                            else
                                Toast.makeText(this,"正在修改中，请勿重复点击",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    private void sendRequest() {
        String user_name = dataBinding.editUserInfoUserName.getText().toString();
        if (checkUserName(user_name)) {
            if (viewModel.HeadBtmap == null) {
                BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.default_user_head_img);
                Bitmap bitmap = drawable.getBitmap();
                //修改状态
                viewModel.IsSendRequest = true;
                new Thread(() ->
                        viewModel.editUserInfo(saveBitmapFile(bitmap), user_name, bitmap)
                ).start();
            } else {
                new Thread(() ->
                        viewModel.editUserInfo(saveBitmapFile(viewModel.HeadBtmap), user_name, viewModel.HeadBtmap)
                ).start();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                //对应从相机打开
                if (resultCode == RESULT_OK) {
                    // 裁剪图片
                    cropPhoto(data.getData());
                }
                break;
            case 2:
                //对应从相机打开
                if (resultCode == RESULT_OK) {
                    // 裁剪图片
                    cropPhoto(viewModel.imageUri);
                }
                break;
            case 3:
                //剪裁图片完成
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap head = Objects.requireNonNull(extras).getParcelable("data");
                    if (head != null) {
                        //显示头像
                        dataBinding.editUserInfoUserHead.setImageBitmap(head);
                        //传到上层
                        viewModel.HeadBtmap = head;
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkUserName(String user_name) {
        if (user_name.length() < 3) {
            Toast.makeText(this, "用户名至少是三位", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void initDialog() {
        //初始化dialog
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        bottomDialog.setContentView(DialogView);
        ViewGroup.LayoutParams layoutParams = DialogView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        DialogView.setLayoutParams(layoutParams);
        Objects.requireNonNull(bottomDialog.getWindow()).setGravity(Gravity.BOTTOM);
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    public File saveBitmapFile(Bitmap bitmap) {
        File file = new File(viewModel.Storage_URL, "head.png");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
        return file;
    }

    private void openCamera() {
        bottomDialog.cancel();
        //得到系统当前的时间
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        String dateNowStr = new SimpleDateFormat("yyyyMMddhhmmss").format(date);
        //检测是否有给定的文件夹，如果没有则创建
        File destDir = new File(viewModel.Storage_URL);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        //创建文件
        File outputImage = new File(viewModel.Storage_URL, dateNowStr + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            viewModel.imageUri = Uri.fromFile(outputImage);
        } else {
            viewModel.imageUri = FileProvider.getUriForFile(this, "com.poetrypavilion.poetrypavilion.fileprovider", outputImage);
        }
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, viewModel.imageUri);
        // 采用ForResult打开
        startActivityForResult(intentCamera, 2);
    }

    private void openPhoto() {
        bottomDialog.cancel();
        Toast.makeText(this, "打开了图库", Toast.LENGTH_LONG).show();
        Intent intentPhoto = new Intent(Intent.ACTION_PICK, null);
        //打开文件
        intentPhoto.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentPhoto, 1);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditUserInfoActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.IsHavePermission = true;
                    if (viewModel.option.equals(EditUserInfoActivityViewModel.Option.Camera)) {
                        openCamera();
                    } else if (viewModel.option.equals(EditUserInfoActivityViewModel.Option.Photo)) {
                        openPhoto();
                    } else if (viewModel.option.equals(EditUserInfoActivityViewModel.Option.Ok)) {
                        sendRequest();
                    }
                } else {
                    if (viewModel.option.equals(EditUserInfoActivityViewModel.Option.Camera)) {
                        Toast.makeText(this, "您已禁止了权限，只有开启权限才能使用相机！", Toast.LENGTH_LONG).show();
                    } else if (viewModel.option.equals(EditUserInfoActivityViewModel.Option.Photo)) {
                        Toast.makeText(this, "您已禁止了权限，只有开启权限才能使用图库！", Toast.LENGTH_LONG).show();
                    } else if (viewModel.option.equals(EditUserInfoActivityViewModel.Option.Ok)) {
                        Toast.makeText(this, "您已禁止了权限，只有开启权限才能保存修改！", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                break;
        }
    }

}
