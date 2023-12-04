package com.moutamid.routineapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import com.fxn.stash.Stash;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.moutamid.routineapp.MainActivity;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.adapters.AddStepsChildAdapter;
import com.moutamid.routineapp.bottomsheets.AddStepsFragment;
import com.moutamid.routineapp.databinding.ActivityCustomRoutineBinding;
import com.moutamid.routineapp.listners.BottomSheetDismissListener;
import com.moutamid.routineapp.models.AddStepsChildModel;
import com.moutamid.routineapp.models.CompletedDaysModel;
import com.moutamid.routineapp.models.RoutineModel;
import com.moutamid.routineapp.models.StepsLocalModel;
import com.moutamid.routineapp.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CustomRoutineActivity extends AppCompatActivity implements BottomSheetDismissListener {
    ActivityCustomRoutineBinding binding;
    private static final String TAG = "CustomRoutineActivity";
    List<String> context;
    ArrayAdapter<String> partiesAdapter;
    ArrayList<AddStepsChildModel> list;
    AddStepsChildAdapter adapter;
    int minute = 0;
    long reminder = 0;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomRoutineBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());

        binding.toolbar.tittle.setText(getResources().getString(R.string.add_routine));
        Constants.initDialog(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,getString(R.string.AD_Interstitial_ID), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
        if (!Stash.getBoolean(Constants.IS_VIP)){
            Stash.put(Constants.IS_VIP, false);
            showAd();
        }

        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }


        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        context = new ArrayList<>();
        String[] stringArray = getResources().getStringArray(R.array.my_context_array);
        context.addAll(Arrays.asList(stringArray));

        binding.stepsRC.setLayoutManager(new LinearLayoutManager(this));
        binding.stepsRC.setHasFixedSize(false);

        if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("ALL")) {
            binding.name.getEditText().setText("");
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("MORNING")) {
            binding.name.getEditText().setText(getString(R.string.morning_routine));
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("EVENING")) {
            binding.name.getEditText().setText(getString(R.string.evening_routine));
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("WORK")) {
            binding.name.getEditText().setText(getString(R.string.ready_for_work_routine));
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("SELFCARE")) {
            binding.name.getEditText().setText(getString(R.string.selfcare_routine));
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("STUDY")) {
            binding.name.getEditText().setText(getString(R.string.study_routine));
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
                            Toast.makeText(this, getString(R.string.routine_created), Toast.LENGTH_SHORT).show();
                            if (!Stash.getBoolean(Constants.IS_VIP)){
                                showAdInter();
                            } else {
                                Log.d("TAG", "User account is premium");
                                startActivity(new Intent(CustomRoutineActivity.this, MainActivity.class));
                                finish();
                            }

                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    private void showAdInter() {
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.");
                mInterstitialAd = null;
                startActivity(new Intent(CustomRoutineActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                mInterstitialAd = null;
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
            }
        });
        if (mInterstitialAd != null) {
            mInterstitialAd.show(CustomRoutineActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
            startActivity(new Intent(CustomRoutineActivity.this, MainActivity.class));
            finish();
        }
    }

    private void showAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                binding.placeholder.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
    }

    private void openTimePicker() {
        int format = Stash.getBoolean(Constants.SHOW_24) ? TimeFormat.CLOCK_24H : TimeFormat.CLOCK_12H;
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(format)
                .setHour(12)
                .setMinute(0)
                .setTitleText(getString(R.string.select_time))
                .setPositiveButtonText(getString(R.string.add))
                .setNegativeButtonText(getString(R.string.no_reminder))
                .build();


        timePicker.addOnNegativeButtonClickListener(view -> {
            binding.timeText.setText(getString(R.string.no_reminder));
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
            Toast.makeText(this, getString(R.string.please_add_name_for_the_routine), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (days.size() == 0){
            Toast.makeText(this, getString(R.string.please_add_at_least_one_day), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (list.size() == 0){
            Toast.makeText(this, getString(R.string.please_add_at_least_one_step), Toast.LENGTH_SHORT).show();
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
            String formattedTime =  getString(R.string.total_time) + " " + Constants.convertMinutesToHHMM(min) + "h";
            binding.totalTime.setText(formattedTime);
        }

        adapter = new AddStepsChildAdapter(CustomRoutineActivity.this, list, null);
        binding.stepsRC.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        Stash.clear(Constants.Steps);
        startActivity(new Intent(this, AddActivity.class));
        finish();
    }
}