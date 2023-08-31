package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.fxn.stash.Stash;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.adsense.Ads;
import com.moutamid.routineapp.databinding.ActivityAddCustomStepsBinding;
import com.moutamid.routineapp.models.AddStepsChildModel;
import com.moutamid.routineapp.utils.Constants;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;

public class AddCustomStepsActivity extends AppCompatActivity {
    ActivityAddCustomStepsBinding binding;
    int val = 14;
    String[] data = {
            "1 min", "2 min", "3 min", "4 min", "5 min", "6 min", "7 min", "8 min", "9 min", "10 min", "11 min", "12 min", "13 min", "14 min", "15 min",
            "16 min", "17 min", "18 min", "19 min", "20 min", "21 min", "22 min", "23 min", "24 min", "25 min", "26 min", "27 min", "28 min", "29 min", "30 min",
            "31 min", "32 min", "33 min", "34 min", "35 min", "36 min", "37 min", "38 min", "39 min", "40 min", "41 min", "42 min", "43 min", "44 min", "45 min"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCustomStepsBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());

        binding.toolbar.tittle.setText("Add Custom Sep");


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

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.numberPicker.setMinValue(1);
        binding.numberPicker.setMaxValue(data.length);
        binding.numberPicker.setDisplayedValues(data);
        binding.numberPicker.setValue(15);

//        Typeface airbnb = Typeface.createFromAsset(getAssets(), "font/airbnb.otf");
//        Typeface baloo = Typeface.createFromAsset(getAssets(), "font/baloo.ttf");

//        binding.numberPicker.setTypeface(baloo);
//        binding.numberPicker.setSelectedTypeface(baloo);


        binding.numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            val = newVal - 1;
        });

        binding.explicit.setOnCheckedChangeListener((compoundButton, b) -> {
            binding.numberPicker.setEnabled(!b);
        });

        binding.save.setOnClickListener(v -> {
            String time = binding.explicit.isChecked() ? "0 min" : data[val];
            AddStepsChildModel model = new AddStepsChildModel(binding.name.getEditText().getText().toString(), time);
            ArrayList<AddStepsChildModel> list = Stash.getArrayList(Constants.Steps, AddStepsChildModel.class);
            list.add(model);
            Stash.put(Constants.Steps, list);
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.name.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.save.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.numberPicker.setBackgroundTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
    }
}