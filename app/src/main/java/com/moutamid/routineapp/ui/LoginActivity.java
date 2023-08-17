package com.moutamid.routineapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.moutamid.routineapp.R;
import com.moutamid.routineapp.databinding.ActivityLoginBinding;
import com.moutamid.routineapp.utils.Constants;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.tittle.setText("Continue with E-Mail");

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.forgot.setOnClickListener(v -> startActivity(new Intent(this, ForgotActivity.class)));
        binding.create.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));

    }
}