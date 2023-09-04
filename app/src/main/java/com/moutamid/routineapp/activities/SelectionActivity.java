package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.routineapp.MainActivity;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.adsense.Ads;
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
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());
        binding.toolbar.tittle.setText(getString(R.string.select_which_describes_you));
        Constants.initDialog(this);


        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

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
                        Stash.put("backShow", false);
                        startActivity(new Intent(this, LanguageActivity.class));
                        finish();
                    }).addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.pickOne.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.next.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
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