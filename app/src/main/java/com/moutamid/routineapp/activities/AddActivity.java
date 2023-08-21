package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.fxn.stash.Stash;
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

        binding.custom.setOnClickListener(v -> {
            Stash.put(Constants.STEPS_LIST, "ALL");
            startActivity(new Intent(this, CustomRoutineActivity.class));
        });

        binding.morning.setOnClickListener(v -> {
            Stash.put(Constants.STEPS_LIST, "MORNING");
            startActivity(new Intent(this, CustomRoutineActivity.class));
        });

        binding.evening.setOnClickListener(v -> {
            Stash.put(Constants.STEPS_LIST, "EVENING");
            startActivity(new Intent(this, CustomRoutineActivity.class));
        });

        binding.work.setOnClickListener(v -> {
            Stash.put(Constants.STEPS_LIST, "WORK");
            startActivity(new Intent(this, CustomRoutineActivity.class));
        });

        binding.brain.setOnClickListener(v -> {
            Stash.put(Constants.STEPS_LIST, "SELFCARE");
            startActivity(new Intent(this, CustomRoutineActivity.class));
        });

        binding.study.setOnClickListener(v -> {
            Stash.put(Constants.STEPS_LIST, "STUDY");
            startActivity(new Intent(this, CustomRoutineActivity.class));
        });

    }
}