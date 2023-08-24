package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.routineapp.R;
import com.moutamid.routineapp.databinding.ActivitySettingBinding;
import com.moutamid.routineapp.utils.Constants;
import com.thebluealliance.spectrum.SpectrumPalette;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.tittle.setText("Settings");
        Constants.initDialog(this);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.colorPicker.setSelectedColor(getResources().getColor(R.color.light));

        binding.colorPicker.setOnColorSelectedListener(color -> {

        });
    }
}