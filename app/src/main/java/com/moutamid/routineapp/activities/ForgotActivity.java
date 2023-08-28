package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.routineapp.databinding.ActivityForgotBinding;
import com.moutamid.routineapp.utils.Constants;

public class ForgotActivity extends AppCompatActivity {
    ActivityForgotBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        binding.toolbar.tittle.setText("Forgot Password?");

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.next.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                Constants.auth().sendPasswordResetEmail(binding.email.getEditText().getText().toString())
                        .addOnSuccessListener(unused -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, "A Password Reset link is sent to your email", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    private boolean valid() {
        if (binding.email.getEditText().getText().toString().isEmpty()){
            binding.email.setErrorEnabled(true);
            binding.email.setError("Email is Empty!");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()){
            binding.email.setErrorEnabled(true);
            binding.email.setError("Email is not valid!");
            return false;
        }
        return true;
    }
}