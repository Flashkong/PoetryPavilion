package com.poetrypavilion.poetrypavilion.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(ShowActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
