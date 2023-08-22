package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fxn.stash.Stash;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.adapters.RoutineStartAdapter;
import com.moutamid.routineapp.databinding.ActivityRoutineStartBinding;
import com.moutamid.routineapp.models.AddStepsChildModel;
import com.moutamid.routineapp.models.RoutineModel;
import com.moutamid.routineapp.utils.Constants;

import java.util.ArrayList;

public class RoutineStartActivity extends AppCompatActivity {
    ActivityRoutineStartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoutineStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.tittle.setText("Start Routine");
        Constants.initDialog(this);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.edit.setOnClickListener(v -> {
            startActivity(new Intent(this, EditRoutineActivity.class));
            finish();
        });

        RoutineModel model = (RoutineModel) Stash.getObject(Constants.MODEL, RoutineModel.class);

        String startNow = Constants.calculateTimeRange(Constants.getCurrentTime(), model.getMinutes());

        binding.name.setText(model.getName());
        binding.time.setText(startNow);
        String s = model.getSteps().size() > 1 ? " steps" : " step";
        binding.totalSteps.setText(model.getSteps().size() + s);

        binding.totalTime.setText("Total time " + Constants.convertMinutesToHHMM(model.getMinutes()) + " h" );

        if (model.getDaysCompleted().isMonday()){
            binding.mondayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isTuesday()){
            binding.teusdayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isWednesday()){
            binding.wednessCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isThursday()){
            binding.thursdayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isFriday()){
            binding.fridayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isSaturday()){
            binding.saturdayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isSunday()){
            binding.sundayCheck.setVisibility(View.VISIBLE);
        }

        binding.routineRC.setLayoutManager(new LinearLayoutManager(this));
        binding.routineRC.setHasFixedSize(false);

        ArrayList<AddStepsChildModel> list = new ArrayList<>();
        list.addAll(model.getSteps());

        RoutineStartAdapter adapter = new RoutineStartAdapter(this, list);
        binding.routineRC.setAdapter(adapter);

    }
}