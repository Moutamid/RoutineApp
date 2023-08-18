package com.moutamid.routineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.moutamid.routineapp.databinding.ActivityWelcomeBinding;
import com.moutamid.routineapp.activities.LoginActivity;
import com.moutamid.routineapp.utils.Constants;

public class WelcomeActivity extends AppCompatActivity {
    ActivityWelcomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.light));


        binding.continueBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

    }
}