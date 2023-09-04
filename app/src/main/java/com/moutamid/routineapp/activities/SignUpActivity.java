package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.adsense.Ads;
import com.moutamid.routineapp.databinding.ActivitySignUpBinding;
import com.moutamid.routineapp.models.UserModel;
import com.moutamid.routineapp.utils.Constants;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());


        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        Constants.initDialog(this);
        binding.toolbar.tittle.setText(getString(R.string.create_account));
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        binding.create.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                Constants.auth().createUserWithEmailAndPassword(
                        binding.email.getEditText().getText().toString(),
                        binding.password.getEditText().getText().toString()
                ).addOnSuccessListener(authResult -> {
                    UserModel model = new UserModel(
                            Constants.auth().getCurrentUser().getUid(),
                            binding.name.getEditText().getText().toString(),
                            binding.email.getEditText().getText().toString(),
                            binding.password.getEditText().getText().toString(), ""
                    );
                    Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).setValue(model)
                            .addOnSuccessListener(unused -> {
                                Constants.dismissDialog();
                                startActivity(new Intent(SignUpActivity.this, SelectionActivity.class));
                                finish();
                            }).addOnFailureListener(e -> {
                                Constants.dismissDialog();
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.name.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.email.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.password.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.create.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.login.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
    }

    private boolean valid() {
        if (binding.name.getEditText().getText().toString().isEmpty()) {
            binding.name.setErrorEnabled(true);
            binding.name.setError(getString(R.string.name_is_empty));
            return false;
        }
        if (binding.email.getEditText().getText().toString().isEmpty()) {
            binding.email.setErrorEnabled(true);
            binding.email.setError(getString(R.string.email_is_empty));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()) {
            binding.email.setErrorEnabled(true);
            binding.email.setError(getString(R.string.email_is_not_valid));
            return false;
        }
        if (binding.password.getEditText().getText().toString().isEmpty()) {
            binding.password.setErrorEnabled(true);
            binding.password.setError(getString(R.string.password_is_empty));
            return false;
        }
        return true;
    }
}