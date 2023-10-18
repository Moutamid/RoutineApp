package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import com.fxn.stash.Stash;
import com.moutamid.routineapp.MainActivity;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.adsense.Ads;
import com.moutamid.routineapp.databinding.ActivityAddBinding;
import com.moutamid.routineapp.databinding.ActivitySettingBinding;
import com.moutamid.routineapp.utils.Constants;

public class AddActivity extends AppCompatActivity {
    ActivityAddBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());

        binding.toolbar.tittle.setText(getResources().getString(R.string.add_routine));
        Constants.initDialog(this);


        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        if (!Stash.getBoolean(Constants.IS_VIP)){
            Stash.put(Constants.IS_VIP, false);
            Ads.init(this);
            Ads.showBanner(binding.adView);
//            Ads.loadIntersAD(this);
        } else {
            binding.adView.setVisibility(View.GONE);
        }

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.custom.setOnClickListener(v -> {
            Stash.put(Constants.STEPS_LIST, "ALL");
            Ads.showInterstitial(this, this, CustomRoutineActivity.class);
        });

        binding.morning.setOnClickListener(v -> {
            Stash.put(Constants.STEPS_LIST, "MORNING");
            Ads.showInterstitial(this, this, CustomRoutineActivity.class);
        });

        binding.evening.setOnClickListener(v -> {
            Stash.put(Constants.STEPS_LIST, "EVENING");
            Ads.showInterstitial(this, this, CustomRoutineActivity.class);
        });

        binding.work.setOnClickListener(v -> {
            Stash.put(Constants.STEPS_LIST, "WORK");
            Ads.showInterstitial(this, this, CustomRoutineActivity.class);
        });

        binding.brain.setOnClickListener(v -> {
            Stash.put(Constants.STEPS_LIST, "SELFCARE");
            Ads.showInterstitial(this, this, CustomRoutineActivity.class);
        });

        binding.study.setOnClickListener(v -> {
            Stash.put(Constants.STEPS_LIST, "STUDY");
            Ads.showInterstitial(this, this, CustomRoutineActivity.class);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.custom.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.custom.setBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        binding.image1.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.image2.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.image3.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.image4.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.image5.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));

    }

    @Override
    public void onBackPressed() {
        Ads.showInterstitial(this, this, MainActivity.class);
    }
}