package com.moutamid.routineapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.routineapp.R;
import com.moutamid.routineapp.databinding.ActivitySelectionBinding;

public class SelectionActivity extends AppCompatActivity {
    ActivitySelectionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.tittle.setText("Select Which Describes You");

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
    }
}