package com.poetrypavilion.poetrypavilion.Room.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.poetrypavilion.poetrypavilion.Room.Entity.LoginUserHistory;
import com.poetrypavilion.poetrypavilion.Room.Entity.UserInfoTuple;

@Dao
public interface LoginUserHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertLoginHistory(LoginUserHistory history);

    @Query("SELECT user_email FROM loginhistory WHERE is_login is 1")
    String[] getCurrentLoginUserEmail();

    @Query("SELECT user_name FROM loginhistory WHERE is_login is 1")
    String[] getCurrentLoginUserName();

    @Update
    void updateUserLoginHistory(LoginUserHistory history);

    @Query("SELECT user_name,user_head_img FROM loginhistory WHERE is_login is 1")
    UserInfoTuple[] getUserNameAndHeadImg();
}
