package com.poetrypavilion.poetrypavilion.Items;

import android.content.res.Resources;
import android.content.res.TypedArray;

import com.poetrypavilion.poetrypavilion.R;


//painting里面记录了点开之后展示的detail页面的信息
public class Painting {

    private final String title;

    private Painting(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public static Painting[] getAllPaintings(Resources res) {
//        String[] titles = res.getStringArray(R.array.paintings_titles);
        String[] titles = {"第一个用户名","第二个用户名","第三个用户名","第四个用户名","第五个用户名","第六个用户名"};

        int size = titles.length;
        Painting[] paintings = new Painting[size];

        for (int i = 0; i < size; i++) {
            paintings[i] = new Painting(titles[i]);
        }

        return paintings;
    }

}
