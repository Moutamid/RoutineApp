package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.routineapp.R;
import com.moutamid.routineapp.databinding.ActivityCompletedBinding;

public class CompletedActivity extends AppCompatActivity {
    ActivityCompletedBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompletedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}