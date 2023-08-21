package com.moutamid.routineapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.routineapp.R;
import com.moutamid.routineapp.databinding.FragmentHomeBinding;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        String today = getToday();
        updateCalender(today);

        binding.monday.setOnClickListener(v -> updateClick("Mon"));
        binding.tuesday.setOnClickListener(v -> updateClick("Tue"));
        binding.wednessday.setOnClickListener(v -> updateClick("Wed"));
        binding.thursday.setOnClickListener(v -> updateClick("Thu"));
        binding.friday.setOnClickListener(v -> updateClick("Fri"));
        binding.saturday.setOnClickListener(v -> updateClick("Sat"));
        binding.sunday.setOnClickListener(v -> updateClick("Sun"));

        return binding.getRoot();
    }

    private void updateClick(String clicked){
        if (clicked.equalsIgnoreCase("Sun")) {
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.text));

            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Mon")) {
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.text));

            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Tue")) {
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.text));

            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Wed")) {
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.text));

            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Thu")) {
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.text));

            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Fri")) {
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.text));

            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Sat")) {
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.text));

            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    private void updateCalender(String today) {
        if (today.equalsIgnoreCase("Sun")) {
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.text));
            binding.sunday.setStrokeColor(getResources().getColor(R.color.light));
        }
        if (today.equalsIgnoreCase("Mon")) {
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.text));
            binding.monday.setStrokeColor(getResources().getColor(R.color.light));
        }
        if (today.equalsIgnoreCase("Tue")) {
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.text));
            binding.tuesday.setStrokeColor(getResources().getColor(R.color.light));
        }
        if (today.equalsIgnoreCase("Wed")) {
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.text));
            binding.wednessday.setStrokeColor(getResources().getColor(R.color.light));
        }
        if (today.equalsIgnoreCase("Thu")) {
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.text));
            binding.thursday.setStrokeColor(getResources().getColor(R.color.light));
        }
        if (today.equalsIgnoreCase("Fri")) {
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.text));
            binding.friday.setStrokeColor(getResources().getColor(R.color.light));
        }
        if (today.equalsIgnoreCase("Sat")) {
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.text));
            binding.saturday.setStrokeColor(getResources().getColor(R.color.light));
        }
    }

    private String getToday() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        String dayOfWeekString;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                dayOfWeekString = "Sun";
                break;
            case Calendar.MONDAY:
                dayOfWeekString = "Mon";
                break;
            case Calendar.TUESDAY:
                dayOfWeekString = "Tue";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekString = "Wed";
                break;
            case Calendar.THURSDAY:
                dayOfWeekString = "Thu";
                break;
            case Calendar.FRIDAY:
                dayOfWeekString = "Fri";
                break;
            case Calendar.SATURDAY:
                dayOfWeekString = "Sat";
                break;
            default:
                dayOfWeekString = "";
                break;
        }
        return dayOfWeekString;
    }
}