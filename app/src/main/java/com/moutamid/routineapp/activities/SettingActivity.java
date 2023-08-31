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
import com.moutamid.routineapp.adsense.Ads;
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


        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        if (!Stash.getBoolean(Constants.IS_VIP)){
            Stash.put(Constants.IS_VIP, false);
            Ads.init(this);
            Ads.showBanner(binding.adView);
            Ads.showInterstitial(this, this);
        }

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
                    Stash.put(Constants.COLOR_TEXT, getResources().getColor(R.color.text));
                    Log.d("COLOR123", currentTheme+"Light");
                    break;
                case -34401:
                    currentTheme = R.style.Theme_Pink;
                    Log.d("COLOR123", currentTheme+"PINK");
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.pink));
                    Stash.put(Constants.COLOR_TEXT, getResources().getColor(R.color.text_pink));
                    break;
                case -14297152:
                    currentTheme = R.style.Theme_SeaGreen;
                    Log.d("COLOR123", currentTheme+"");
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.seeGreen));
                    Stash.put(Constants.COLOR_TEXT, getResources().getColor(R.color.text_seagreen));
                    break;
                case -11561105:
                    currentTheme = R.style.Theme_Green;
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.green));
                    Stash.put(Constants.COLOR_TEXT, getResources().getColor(R.color.text_green));
                    Log.d("COLOR123", currentTheme+"");
                    break;
                case -7785801:
                    currentTheme = R.style.Theme_Purple;
                    Log.d("COLOR123", currentTheme+"");
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.purple));
                    Stash.put(Constants.COLOR_TEXT, getResources().getColor(R.color.text_purple));
                    break;
                case -16571060:
                    currentTheme = R.style.Theme_Blue;
                    Log.d("COLOR123", currentTheme+"");
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.blue));
                    Stash.put(Constants.COLOR_TEXT, getResources().getColor(R.color.text_blue));
                    break;
            }

            Log.d("COLOR123", color+"\t\tcolor");
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
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    private void updateViews() {
        binding.text1.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text2.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text3.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text4.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text5.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text6.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text7.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text8.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text9.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text10.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text11.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text12.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.languageTile.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.logout.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.logout.setBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));

        binding.time24Switch.setColorBorder(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.time24Switch.setColorOn(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.darkSwitch.setColorBorder(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.darkSwitch.setColorOn(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
        finish();
    }
}