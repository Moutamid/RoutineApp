package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.fxn.stash.Stash;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.adsense.Ads;
import com.moutamid.routineapp.databinding.ActivityTimerBinding;
import com.moutamid.routineapp.models.RoutineModel;
import com.moutamid.routineapp.models.StepsLocalModel;
import com.moutamid.routineapp.notification.NotificationHelper;
import com.moutamid.routineapp.utils.Constants;

import java.util.ArrayList;

public class TimerActivity extends AppCompatActivity {
    ActivityTimerBinding binding;
    private CountDownTimer countDownTimer;
    private long remainingTimeInMillis, initialTimeInMillis;
    private boolean isPaused = true;
    int count = 0;
    ArrayList<StepsLocalModel> stepsList;
    StepsLocalModel stepsModel;
    RoutineModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimerBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());


        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        if (!Stash.getBoolean(Constants.IS_VIP)){
            Stash.put(Constants.IS_VIP, false);
            Ads.init(this);
            Ads.showBanner(binding.adView);
            Ads.showInterstitial(this, this);
        }

        model = (RoutineModel) Stash.getObject(Constants.ROUTINE_LIST, RoutineModel.class);

        binding.toolbar.tittle.setText("Start Routine");
        Constants.initDialog(this);

        ArrayList<StepsLocalModel> modelList = Stash.getArrayList(model.getID(), StepsLocalModel.class);
        stepsList = new ArrayList<>();
        for (StepsLocalModel model : modelList) {
            if (!model.isCompleted()) {
                stepsList.add(model);
            }
        }

        stepsModel = stepsList.get(count);

        initialTimeInMillis = (long) Constants.extractSingleTimeValue(stepsModel.getTime()) * 60 * 1000;
        remainingTimeInMillis = initialTimeInMillis;
        setValue(stepsModel, count);

        binding.startPause.setOnClickListener(v -> {
            if (isPaused) {
                startTimer();
            } else {
                pauseTimer();
            }
        });

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.resetTime.setOnClickListener(v -> {
            resetTimer();
        });

        binding.skip.setOnClickListener(v -> {
            nextTask();
        });

        binding.completed.setOnClickListener(v -> {
            taskComplete();
        });

    }

    private void nextTask(){
        count = count + 1;
        stepsModel = stepsList.get(count);
        setValue(stepsModel, count);
    }

    private void taskComplete() {
        ArrayList<StepsLocalModel> mainList = Stash.getArrayList(model.getID(), StepsLocalModel.class);
        for (int i =0; i<mainList.size(); i++){
            StepsLocalModel list = mainList.get(i);
            for (StepsLocalModel list2 : stepsList){
                if (list.getName().equals(list2.getName())){
                    mainList.get(i).setCompleted(true);
                }
            }
        }
        Stash.put(model.getID(), mainList);

        if (count < stepsList.size()-1){
            nextTask();
        } else {
            finishTask();
        }

    }

    private void finishTask() {
        Stash.put("CONGRATS", model);
        startActivity(new Intent(this, CompletedActivity.class));
        finish();
    }

    private void setValue(StepsLocalModel stepsModel, int count) {
        binding.minutesSchedule.setText(Constants.extractSingleTimeValue(stepsModel.getTime()) + " mint scheduled." );
        binding.stepName.setText(stepsModel.getName());
        String startNow = Constants.finishTime(Constants.getCurrentTime(), Constants.extractSingleTimeValue(stepsModel.getTime()));
        binding.finishTime.setText(startNow);
        binding.remainingSteps.setText(""+stepsList.size());

        if (count < stepsList.size()-1){
            binding.nextStep.setText(stepsList.get(count + 1).getName());
        } else {
            binding.skip.setVisibility(View.INVISIBLE);
            binding.nextStep.setText("You're done with everything!");
        }
        binding.clock.setText(Constants.extractSingleTimeValue(stepsModel.getTime()) + ":00 min.");
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        } else {
            NotificationHelper helper = new NotificationHelper(this);
            String body = stepsModel.getName() + " " + stepsModel.getTime() + " left";
            helper.sendHighPriorityNotification("Start Routine", body, TimerActivity.class);
        }

        countDownTimer = new CountDownTimer(remainingTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isPaused) {
                    remainingTimeInMillis = millisUntilFinished;
                    updateCountdownText();
                    updateProgressBar();
                }
            }

            @Override
            public void onFinish() {
                binding.clock.setText("0:00");
                binding.progressBar.setProgress(100);
                taskComplete();
            }
        };

        countDownTimer.start();
        isPaused = false;
        binding.startPauseText.setText("Pause");
        binding.startPauseIcon.setImageResource(R.drawable.pause);
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isPaused = true;
            binding.startPauseText.setText("Resume");
            binding.startPauseIcon.setImageResource(R.drawable.play);
        }
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        remainingTimeInMillis = initialTimeInMillis;
        updateCountdownText();
        updateProgressBar();
        isPaused = true;
        binding.startPauseText.setText("Start");
        binding.startPauseIcon.setImageResource(R.drawable.play);
    }

    private void updateCountdownText() {
        long minutes = (remainingTimeInMillis / 1000) / 60;
        long seconds = (remainingTimeInMillis / 1000) % 60;
        String formattedTime = String.format("%d:%02d", minutes, seconds);
        binding.clock.setText(formattedTime + " min.");
    }

    private void updateProgressBar() {
        int progress = (int) (((float) (initialTimeInMillis - remainingTimeInMillis) / initialTimeInMillis) * 100);
        binding.progressBar.setProgress(progress);
    }

}