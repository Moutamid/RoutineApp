package com.moutamid.routineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.moutamid.routineapp.databinding.ActivityMainBinding;
import com.moutamid.routineapp.fragments.HomeFragment;
import com.moutamid.routineapp.fragments.InsightsFragment;
import com.moutamid.routineapp.activities.AddActivity;
import com.moutamid.routineapp.activities.SettingActivity;
import com.moutamid.routineapp.utils.Constants;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new HomeFragment()).commit();

        binding.home.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new HomeFragment()).commit());
        binding.insights.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new InsightsFragment()).commit());

        binding.settings.setOnClickListener(v -> startActivity(new Intent(this, SettingActivity.class)));
        binding.add.setOnClickListener(v -> startActivity(new Intent(this, AddActivity.class)));


    }
}