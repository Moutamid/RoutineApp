package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.routineapp.MainActivity;
import com.moutamid.routineapp.databinding.ActivitySelectionBinding;
import com.moutamid.routineapp.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class SelectionActivity extends AppCompatActivity {
    ActivitySelectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.tittle.setText("Select Which Describes You");
        Constants.initDialog(this);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.procastinating.setOnClickListener(v -> {
            binding.procastinating.setChecked(true);
            binding.adhd.setChecked(false);
            binding.time.setChecked(false);
            binding.focus.setChecked(false);
            binding.habit.setChecked(false);
            binding.habit.setChecked(false);
        });
        binding.adhd.setOnClickListener(v -> {
            binding.procastinating.setChecked(false);
            binding.adhd.setChecked(true);
            binding.time.setChecked(false);
            binding.focus.setChecked(false);
            binding.habit.setChecked(false);
            binding.other.setChecked(false);
        });
        binding.time.setOnClickListener(v -> {
            binding.procastinating.setChecked(false);
            binding.adhd.setChecked(false);
            binding.time.setChecked(true);
            binding.focus.setChecked(false);
            binding.habit.setChecked(false);
            binding.other.setChecked(false);
        });
        binding.focus.setOnClickListener(v -> {
            binding.procastinating.setChecked(false);
            binding.adhd.setChecked(false);
            binding.time.setChecked(false);
            binding.focus.setChecked(true);
            binding.habit.setChecked(false);
            binding.other.setChecked(false);
        });
        binding.habit.setOnClickListener(v -> {
            binding.procastinating.setChecked(false);
            binding.adhd.setChecked(false);
            binding.time.setChecked(false);
            binding.focus.setChecked(false);
            binding.habit.setChecked(true);
            binding.other.setChecked(false);
        });
        binding.other.setOnClickListener(v -> {
            binding.procastinating.setChecked(false);
            binding.adhd.setChecked(false);
            binding.time.setChecked(false);
            binding.focus.setChecked(false);
            binding.habit.setChecked(false);
            binding.other.setChecked(true);
        });

        binding.next.setOnClickListener(v -> {
            Constants.showDialog();
            String goal = getGoal();
            Map<String, Object> map = new HashMap<>();
            map.put("goal", goal);

            Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                    .updateChildren(map).addOnSuccessListener(unused -> {
                        Constants.dismissDialog();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }).addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        });

    }

    private String getGoal() {
        if (binding.procastinating.isChecked()) {
            return binding.procastinating.text();
        } else if (binding.adhd.isChecked()) {
            return binding.adhd.text();
        } else if (binding.time.isChecked()) {
            return binding.time.text();
        } else if (binding.focus.isChecked()) {
            return binding.focus.text();
        } else if (binding.habit.isChecked()) {
            return binding.habit.text();
        } else if (binding.other.isChecked()) {
            return binding.other.text();
        }
        return "";
    }
}