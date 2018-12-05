package com.poetrypavilion.poetrypavilion;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String args[]) {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String dateNowStr = sdf.format(date);
        System.out.println(dateNowStr);

        String a = "this";
        String b = a.replace('t', '=');
        System.out.println(a + "   " + b);

        if ("\nthis".substring(0,1).equals("\n")) {
            System.out.println("\nthis".substring(1));
        }

        String thisss = "thisss";
        String ss=thisss.replace('s','_');
        System.out.println(thisss);
        System.out.println(ss);
    }
}
