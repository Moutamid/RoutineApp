package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.moutamid.routineapp.MainActivity;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.SplashScreenActivity;
import com.moutamid.routineapp.databinding.ActivitySettingBinding;
import com.moutamid.routineapp.utils.Constants;
import com.thebluealliance.spectrum.SpectrumPalette;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    int currentTheme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        Log.d("COLOR123", theme+"");
        setTheme(theme);
        setContentView(binding.getRoot());

        Constants.changeTheme(this);

        currentTheme = theme;

        binding.toolbar.tittle.setText("Settings");
        Constants.initDialog(this);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.colorPicker.setSelectedColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));

        binding.time24Switch.setOn(Stash.getBoolean(Constants.SHOW_24, false));
        binding.darkSwitch.setOn(Stash.getBoolean(Constants.DARK_MODE, false));

        binding.time24Switch.setOnToggledListener((toggleableView, isOn) -> Stash.put(Constants.SHOW_24, isOn));
        binding.darkSwitch.setOnToggledListener((toggleableView, isOn) -> {
            Stash.put(Constants.DARK_MODE, isOn);
            onBackPressed();
        });

        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            binding.defaultLanguage.setText("English");
        } else {
            binding.defaultLanguage.setText("Spanish");
        }

        binding.language.setOnClickListener(v -> {
            startActivity(new Intent(this, LanguageActivity.class));
            finish();
        });

        binding.pro.setOnClickListener(v -> {
            startActivity(new Intent(this, PaymentActivity.class));
        });

        binding.colorPicker.setOnColorSelectedListener(color -> {
            switch (color) {
                case -9409538:
                    currentTheme = R.style.Theme_RoutineApp;
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.light));
                    Log.d("COLOR123", currentTheme+"Light");
                    break;
                case -34401:
                    currentTheme = R.style.Theme_Pink;
                    Log.d("COLOR123", currentTheme+"PINK");
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.pink));
                    break;
                case -14297152:
                    currentTheme = R.style.Theme_SeaGreen;
                    Log.d("COLOR123", currentTheme+"");
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.seeGreen));
                    break;
                case -11561105:
                    currentTheme = R.style.Theme_Green;
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.green));
                    Log.d("COLOR123", currentTheme+"");
                    break;
                case -6847533:
                    currentTheme = R.style.Theme_Purple;
                    Log.d("COLOR123", currentTheme+"");
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.purple));
                    break;
                case -13994498:
                    currentTheme = R.style.Theme_Blue;
                    Log.d("COLOR123", currentTheme+"");
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.blue));
                    break;
            }
            Stash.put(Constants.THEME, currentTheme);
            recreate();
        });

        binding.faq1.setOnClickListener(v -> {
            int vis = binding.faq1Text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            binding.faq1Text.setVisibility(vis);
        });

        binding.faq2.setOnClickListener(v -> {
            int vis = binding.faq2Text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            binding.faq2Text.setVisibility(vis);
        });

        binding.faq3.setOnClickListener(v -> {
            int vis = binding.faq3Text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            binding.faq3Text.setVisibility(vis);
        });

        binding.faq4.setOnClickListener(v -> {
            int vis = binding.faq4Text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            binding.faq4Text.setVisibility(vis);
        });

        binding.faq5.setOnClickListener(v -> {
            int vis = binding.faq5Text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            binding.faq5Text.setVisibility(vis);
        });

        binding.faq6.setOnClickListener(v -> {
            int vis = binding.faq6Text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            binding.faq6Text.setVisibility(vis);
        });

        binding.logout.setOnClickListener(v ->{
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Do you really want to logout?")
                    .setNegativeButton("No", ((dialogInterface, i) -> dialogInterface.dismiss()))
                    .setPositiveButton("Yes", ((dialogInterface, i) -> {
                        Constants.auth().signOut();
                        startActivity(new Intent(this, SplashScreenActivity.class));
                        finish();
                    }))
                    .show();
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
        finish();
    }
}