package com.moutamid.routineapp.activities;

import static java.util.UUID.randomUUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.moutamid.routineapp.MainActivity;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.adapters.AddStepsChildAdapter;
import com.moutamid.routineapp.adsense.Ads;
import com.moutamid.routineapp.bottomsheets.AddStepsFragment;
import com.moutamid.routineapp.databinding.ActivityCustomRoutineBinding;
import com.moutamid.routineapp.listners.BottomSheetDismissListener;
import com.moutamid.routineapp.listners.StepClickListner;
import com.moutamid.routineapp.models.AddStepsChildModel;
import com.moutamid.routineapp.models.CompletedDaysModel;
import com.moutamid.routineapp.models.RoutineModel;
import com.moutamid.routineapp.models.StepsLocalModel;
import com.moutamid.routineapp.utils.Constants;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomRoutineActivity extends AppCompatActivity implements BottomSheetDismissListener {
    ActivityCustomRoutineBinding binding;
    List<String> context;
    ArrayAdapter<String> partiesAdapter;
    ArrayList<AddStepsChildModel> list;
    AddStepsChildAdapter adapter;
    int minute = 0;
    long reminder = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomRoutineBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());

        binding.toolbar.tittle.setText("Add Routine");
        Constants.initDialog(this);

        if (!Stash.getBoolean(Constants.IS_VIP)){
            Stash.put(Constants.IS_VIP, false);
            Ads.init(this);
            Ads.showBanner(binding.adView);
            Ads.showInterstitial(this, this);
        }

        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }


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

        if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("ALL")) {
            binding.name.getEditText().setText("");
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("MORNING")) {
            binding.name.getEditText().setText("Morning Routine");
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("EVENING")) {
            binding.name.getEditText().setText("Evening Routine");
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("WORK")) {
            binding.name.getEditText().setText("Work Routine");
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("SELFCARE")) {
            binding.name.getEditText().setText("Self Care Routine");
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("STUDY")) {
            binding.name.getEditText().setText("Study Routine");
        }

        partiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, context);
        binding.addContext.setAdapter(partiesAdapter);

        binding.addSteps.setOnClickListener(v -> {
            AddStepsFragment bottomSheetFragment = new AddStepsFragment();
            bottomSheetFragment.setListener(this);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });

        binding.addReminder.setOnClickListener(v -> {
            openTimePicker();
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
                String ID = UUID.randomUUID().toString();
                ArrayList<String> days = getDays();
                CompletedDaysModel daysCompleted = new CompletedDaysModel(false,false,false,false,false,false,false);
                RoutineModel model = new RoutineModel(
                        ID, binding.name.getEditText().getText().toString(), binding.context.getEditText().getText().toString(),
                        minute, days, daysCompleted, list, reminder
                );
                Constants.databaseReference().child(Constants.Routines).child(Constants.auth().getCurrentUser().getUid())
                        .child(ID).setValue(model).addOnSuccessListener(unused -> {
                            Constants.dismissDialog();
                            ArrayList<StepsLocalModel> localList = new ArrayList<>();
                            for(AddStepsChildModel childModel : list){
                                localList.add(new StepsLocalModel(ID, childModel.getName(), childModel.getTime(), false));
                            }
                            Stash.put(ID, localList);
                            Stash.clear(Constants.Steps);
                            Toast.makeText(this, "Routine Created", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    private void openTimePicker() {
        int format = Stash.getBoolean(Constants.SHOW_24) ? TimeFormat.CLOCK_24H : TimeFormat.CLOCK_12H;
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(format)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Time")
                .setPositiveButtonText("Add")
                .setNegativeButtonText("No Reminder")
                .build();


        timePicker.addOnNegativeButtonClickListener(view -> {
            binding.timeText.setText("No reminder");
        });

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                reminder = calendar.getTimeInMillis();
                String form = Stash.getBoolean(Constants.SHOW_24) ? "HH:mm" : "hh:mm";
                String formattedTime = new SimpleDateFormat(form, Locale.getDefault()).format(reminder);
                binding.timeText.setText(formattedTime);
            }
        });

        timePicker.show(getSupportFragmentManager(), "timePicker");
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

    @Override
    public void onBottomSheetDismissed() {
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        updateViews();
    }

    private void updateViews() {
        binding.save.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.addReminder.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.addReminder.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        binding.addSteps.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.addSteps.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        binding.timeText.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.totalTime.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.name.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.context.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.addIco.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));

        binding.monday.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.monday.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.monday.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));

        binding.tuesday.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.tuesday.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.tuesday.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));

        binding.wednesday.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.wednesday.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.wednesday.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));

        binding.thursday.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.thursday.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.thursday.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));

        binding.friday.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.friday.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.friday.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));

        binding.sat.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.sat.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.sat.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));

        binding.sun.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.sun.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.sun.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));
    }

    private void getData() {
        list = Stash.getArrayList(Constants.Steps, AddStepsChildModel.class);
        if (list.size() >= 1){
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

        adapter = new AddStepsChildAdapter(CustomRoutineActivity.this, list, model -> {

        });
        binding.stepsRC.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Stash.clear(Constants.Steps);
    }
}