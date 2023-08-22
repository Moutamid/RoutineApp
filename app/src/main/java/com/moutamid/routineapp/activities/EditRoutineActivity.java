package com.moutamid.routineapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.material.chip.Chip;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.adapters.AddStepsChildAdapter;
import com.moutamid.routineapp.bottomsheets.AddStepsFragment;
import com.moutamid.routineapp.databinding.ActivityEditRoutineBinding;
import com.moutamid.routineapp.listners.BottomSheetDismissListener;
import com.moutamid.routineapp.models.AddStepsChildModel;
import com.moutamid.routineapp.models.CompletedDaysModel;
import com.moutamid.routineapp.models.RoutineModel;
import com.moutamid.routineapp.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditRoutineActivity extends AppCompatActivity implements BottomSheetDismissListener {
    ActivityEditRoutineBinding binding;
    List<String> context;
    ArrayAdapter<String> partiesAdapter;
    ArrayList<AddStepsChildModel> list;
    AddStepsChildAdapter adapter;
    int minute = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditRoutineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.tittle.setText("Edit Routine");
        Constants.initDialog(this);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        RoutineModel model = (RoutineModel) Stash.getObject(Constants.MODEL, RoutineModel.class);

        binding.name.getEditText().setText(model.getName());
        binding.context.getEditText().setText(model.getContext());

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

        list = new ArrayList<>();
        list.addAll(model.getSteps());

        Stash.put(Constants.Steps, list);

        if (list.size() > 1){
            binding.stepsRC.setVisibility(View.VISIBLE);
            binding.totalTime.setVisibility(View.VISIBLE);
            int min = 0;
            List<Integer> timeValues = Constants.extractTimeValues(list);
            for (int value : timeValues) {
                min += value;
                minute = min;
            }
            String formattedTime = "Total time " + Constants.convertMinutesToHHMM(min) + "h";
            binding.totalTime.setText(formattedTime);
        }

        adapter = new AddStepsChildAdapter(EditRoutineActivity.this, list, m -> {

        });
        binding.stepsRC.setAdapter(adapter);

        for (int i = 0; i < model.getDays().size(); i++) {
            for (int j = 0; j < binding.days.getChildCount(); j++) {
                String name = model.getDays().get(i);
                Chip chip = (Chip) binding.days.getChildAt(j);
                if (name.equals(chip.getText().toString())){
                    chip.setChecked(true);
                }
            }
        }

        binding.addSteps.setOnClickListener(v -> {
            AddStepsFragment bottomSheetFragment = new AddStepsFragment();
            bottomSheetFragment.setListener(this);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });

        ItemTouchHelper.Callback ithCallback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Collections.swap(list, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Stash.put(Constants.Steps, list);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
        ItemTouchHelper ith = new ItemTouchHelper(ithCallback);
        ith.attachToRecyclerView(binding.stepsRC);

        binding.save.setOnClickListener(v -> {
            if (valid()){
                Constants.showDialog();
                String ID = model.getID();
                ArrayList<String> days = getDays();
                CompletedDaysModel daysCompleted = model.getDaysCompleted();
                RoutineModel updateModel = new RoutineModel(
                        ID, binding.name.getEditText().getText().toString(), binding.context.getEditText().getText().toString(),
                        minute, days, daysCompleted, list
                );
                Constants.databaseReference().child(Constants.Routines).child(Constants.auth().getCurrentUser().getUid())
                        .child(ID).setValue(updateModel).addOnSuccessListener(unused -> {
                            Constants.dismissDialog();
                            Stash.clear(Constants.Steps);
                            Toast.makeText(this, "Routine Updated", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    private ArrayList<String> getDays() {
        ArrayList<String> day = new ArrayList<>();
        for (int i = 0; i < binding.days.getChildCount(); i++) {
            Chip chip = (Chip) binding.days.getChildAt(i);
            if (chip.isChecked()){
                day.add(chip.getText().toString());
            }
        }
        return day;
    }

    private void getData() {
        list.clear();
        list = Stash.getArrayList(Constants.Steps, AddStepsChildModel.class);
        if (list.size() > 1){
            binding.stepsRC.setVisibility(View.VISIBLE);
            binding.totalTime.setVisibility(View.VISIBLE);
            int min = 0;
            List<Integer> timeValues = Constants.extractTimeValues(list);
            for (int value : timeValues) {
                min += value;
                minute = min;
            }
            String formattedTime = "Total time " + Constants.convertMinutesToHHMM(min) + "h";
            binding.totalTime.setText(formattedTime);
        }

        adapter = new AddStepsChildAdapter(EditRoutineActivity.this, list, model -> {

        });
        binding.stepsRC.setAdapter(adapter);
    }

    private boolean valid() {
        ArrayList<String> days = getDays();
        if (binding.name.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Please add name for the routine", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (days.size() == 0){
            Toast.makeText(this, "Please add at least one day", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (list.size() == 0){
            Toast.makeText(this, "Please add at least one step", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onBottomSheetDismissed() {
        getData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Stash.clear(Constants.Steps);
    }
}