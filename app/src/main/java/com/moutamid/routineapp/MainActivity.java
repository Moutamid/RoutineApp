package com.moutamid.routineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.routineapp.utils.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Constants.checkApp(this);
    }
}