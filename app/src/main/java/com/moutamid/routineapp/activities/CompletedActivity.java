package com.moutamid.routineapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.routineapp.MainActivity;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.adsense.Ads;
import com.moutamid.routineapp.databinding.ActivityCompletedBinding;
import com.moutamid.routineapp.models.CompletedDaysModel;
import com.moutamid.routineapp.models.RoutineModel;
import com.moutamid.routineapp.models.StepsLocalModel;
import com.moutamid.routineapp.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CompletedActivity extends AppCompatActivity {
    ActivityCompletedBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompletedBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());

        binding.bg.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));


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

        Constants.initDialog(this);

        RoutineModel model = (RoutineModel) Stash.getObject("CONGRATS", RoutineModel.class);
        ArrayList<StepsLocalModel> mainList = Stash.getArrayList(model.getID(), StepsLocalModel.class);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));

        binding.steps.setText(mainList.size()+"");
        int min = 0;
        for (StepsLocalModel list : mainList){
            min += Constants.extractSingleTimeValue(list.getTime());
        }
        String formattedTime = Constants.convertMinutesToHHMM(min) + "h";
        binding.time.setText(formattedTime);

        binding.done.setOnClickListener(v -> {
            Constants.showDialog();
            Map<String, Object> completedDays = new HashMap<>();
            completedDays.put(getToday(Constants.getToday()), true);
            Constants.databaseReference().child(Constants.Routines).child(Constants.auth().getCurrentUser().getUid()).child(model.getID())
                    .child("daysCompleted").updateChildren(completedDays).addOnSuccessListener(unused -> {
                        Constants.dismissDialog();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }).addOnFailureListener(e-> {
                        Constants.dismissDialog();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

    }

    private String getToday(String today) {

        if (today.equalsIgnoreCase("Sun")) {
            return "sunday";
        }
        if (today.equalsIgnoreCase("Mon")) {
            return "monday";
        }
        if (today.equalsIgnoreCase("Tue")) {
            return "tuesday";
        }
        if (today.equalsIgnoreCase("Wed")) {
            return "wednesday";
        }
        if (today.equalsIgnoreCase("Thu")) {
            return "thursday";
        }
        if (today.equalsIgnoreCase("Fri")) {
            return "friday";
        }
        if (today.equalsIgnoreCase("Sat")) {
            return "saturday";
        }
        return "";
    }

}