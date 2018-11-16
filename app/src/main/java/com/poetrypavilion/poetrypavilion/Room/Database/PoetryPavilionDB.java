package com.poetrypavilion.poetrypavilion.Room.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.poetrypavilion.poetrypavilion.Room.Dao.LoginUserHistoryDao;
import com.poetrypavilion.poetrypavilion.Room.Entity.LoginUserHistory;
import com.poetrypavilion.poetrypavilion.Utils.MyApplication;

@Database(entities = {LoginUserHistory.class},version = 1,exportSchema = false)
public abstract class PoetryPavilionDB extends RoomDatabase {
    public abstract LoginUserHistoryDao loginUserHistoryDao();
    private static PoetryPavilionDB poetryPavilionDB;

    public static PoetryPavilionDB getDatabase(){
        if(poetryPavilionDB==null){
            poetryPavilionDB = Room.databaseBuilder(MyApplication.getContext(),
                    PoetryPavilionDB.class,"poetrypaviliondb.db").build();
        }
        return poetryPavilionDB;
    }
}
