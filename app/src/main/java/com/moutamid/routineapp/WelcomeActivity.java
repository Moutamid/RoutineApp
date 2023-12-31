package com.moutamid.routineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.fxn.stash.Stash;
import com.moutamid.routineapp.databinding.ActivityWelcomeBinding;
import com.moutamid.routineapp.activities.LoginActivity;
import com.moutamid.routineapp.utils.Constants;

public class WelcomeActivity extends AppCompatActivity {
    ActivityWelcomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        Constants.changeTheme(this);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.light));
        window.setStatusBarColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.bg.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));



        binding.continueBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}