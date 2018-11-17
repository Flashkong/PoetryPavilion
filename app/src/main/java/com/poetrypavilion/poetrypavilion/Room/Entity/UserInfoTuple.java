package com.poetrypavilion.poetrypavilion.Room.Entity;

import android.arch.persistence.room.ColumnInfo;

public class UserInfoTuple {
    @ColumnInfo(name="user_name")
    public String user_name;

    @ColumnInfo(name="user_head_img")
    public byte[] user_photo;
}