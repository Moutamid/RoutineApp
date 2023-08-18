package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.moutamid.routineapp.databinding.ActivityAddBinding;
import com.moutamid.routineapp.databinding.ActivitySettingBinding;
import com.moutamid.routineapp.utils.Constants;

public class AddActivity extends AppCompatActivity {
    ActivityAddBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.tittle.setText("Add Routine");
        Constants.initDialog(this);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.custom.setOnClickListener(v -> startActivity(new Intent(this, CustomRoutineActivity.class)));

    }
}