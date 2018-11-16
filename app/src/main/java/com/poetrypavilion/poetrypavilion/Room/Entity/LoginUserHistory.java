package com.poetrypavilion.poetrypavilion.Room.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "loginhistory")
public class LoginUserHistory {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_email")
    private String UserEmail;

    @ColumnInfo(name = "user_name")
    private String user_name;

    @ColumnInfo(name = "is_login")
    private int is_login;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB ,name = "user_head_img")
    private byte[] user_head_image;

    public LoginUserHistory(@NonNull String UserEmail, String user_name, int is_login, byte[] user_head_image){
        this.is_login=is_login;
        this.user_head_image = user_head_image;
        this.user_name=user_name;
        this.UserEmail = UserEmail;
    }

    @NonNull
    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(@NonNull String userEmail) {
        UserEmail = userEmail;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getIs_login() {
        return is_login;
    }

    public void setIs_login(int is_login) {
        this.is_login = is_login;
    }

    public byte[] getUser_head_image() {
        return user_head_image;
    }

    public void setUser_head_image(byte[] user_head_image) {
        this.user_head_image = user_head_image;
    }
}

