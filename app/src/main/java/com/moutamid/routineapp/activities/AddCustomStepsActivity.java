package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import com.fxn.stash.Stash;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.databinding.ActivityAddCustomStepsBinding;
import com.moutamid.routineapp.models.AddStepsChildModel;
import com.moutamid.routineapp.utils.Constants;

import java.util.ArrayList;

public class AddCustomStepsActivity extends AppCompatActivity {
    ActivityAddCustomStepsBinding binding;
    int val = 14;
    String[] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCustomStepsBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());

        binding.toolbar.tittle.setText(getResources().getString(R.string.add_custom_sep));

        data = getResources().getStringArray(R.array.time_intervals);

        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        if (!Stash.getBoolean(Constants.IS_VIP)){
            Stash.put(Constants.IS_VIP, false);
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