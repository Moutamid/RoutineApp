package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fxn.stash.Stash;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.moutamid.routineapp.adapters.AddStepsChildAdapter;
import com.moutamid.routineapp.bottomsheets.AddStepsFragment;
import com.moutamid.routineapp.databinding.ActivityCustomRoutineBinding;
import com.moutamid.routineapp.listners.BottomSheetDismissListener;
import com.moutamid.routineapp.listners.StepClickListner;
import com.moutamid.routineapp.models.AddStepsChildModel;
import com.moutamid.routineapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomRoutineActivity extends AppCompatActivity implements BottomSheetDismissListener {
    ActivityCustomRoutineBinding binding;
    List<String> context;
    ArrayAdapter<String> partiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomRoutineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.tittle.setText("Add Routine");
        Constants.initDialog(this);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        context = new ArrayList<>();
        context.add("After waking up");
        context.add("After coming home");
        context.add("After watching YouTube");
        context.add("After brushing teeth");
        context.add("At 3 Am");
        context.add("After checking Instagram");

        binding.stepsRC.setLayoutManager(new LinearLayoutManager(this));
        binding.stepsRC.setHasFixedSize(false);

        partiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, context);
        binding.addContext.setAdapter(partiesAdapter);

        binding.addSteps.setOnClickListener(v -> {
            AddStepsFragment bottomSheetFragment = new AddStepsFragment();
            bottomSheetFragment.setListener(this);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });

    }

    @Override
    public void onBottomSheetDismissed() {
        ArrayList<AddStepsChildModel> list = Stash.getArrayList(Constants.Steps, AddStepsChildModel.class);
        if (list.size() > 1){
            binding.stepsRC.setVisibility(View.VISIBLE);
            binding.totalTime.setVisibility(View.VISIBLE);
            int min = 0;
            List<Integer> timeValues = extractTimeValues(list);
            for (int value : timeValues) {
                min += value;
            }
            String formattedTime = "Total time " + convertMinutesToHHMM(min) + "h";
            binding.totalTime.setText(formattedTime);
        }

        AddStepsChildAdapter adapter = new AddStepsChildAdapter(CustomRoutineActivity.this, list, model -> {

        });
        binding.stepsRC.setAdapter(adapter);
    }

    public static List<Integer> extractTimeValues(ArrayList<AddStepsChildModel> timeStrings) {
        List<Integer> timeValues = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d+) min");

        for (AddStepsChildModel timeString : timeStrings) {
            Matcher matcher = pattern.matcher(timeString.getTime());
            if (matcher.find()) {
                int value = Integer.parseInt(matcher.group(1));
                timeValues.add(value);
            }
        }

        return timeValues;
    }

    public String convertMinutesToHHMM(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;

        return String.format("%02d:%02d", hours, remainingMinutes);
    }


}