package com.poetrypavilion.poetrypavilion;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String args[]){
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String dateNowStr = sdf.format(date);
        System.out.println(dateNowStr);

        String a= "this";
        String b= a.replace('t','=');
        System.out.println(a+"   "+b);
    }
}
