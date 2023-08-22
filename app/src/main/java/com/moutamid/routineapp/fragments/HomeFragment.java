package com.moutamid.routineapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.adapters.RoutineAdapter;
import com.moutamid.routineapp.databinding.FragmentHomeBinding;
import com.moutamid.routineapp.models.AddStepsChildModel;
import com.moutamid.routineapp.models.CompletedDaysModel;
import com.moutamid.routineapp.models.RoutineModel;
import com.moutamid.routineapp.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    ArrayList<RoutineModel> list;
    RoutineAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        String today = getToday();
        updateCalender(today);

        Constants.initDialog(requireContext());

        binding.monday.setOnClickListener(v -> updateClick("Mon"));
        binding.tuesday.setOnClickListener(v -> updateClick("Tue"));
        binding.wednessday.setOnClickListener(v -> updateClick("Wed"));
        binding.thursday.setOnClickListener(v -> updateClick("Thu"));
        binding.friday.setOnClickListener(v -> updateClick("Fri"));
        binding.saturday.setOnClickListener(v -> updateClick("Sat"));
        binding.sunday.setOnClickListener(v -> updateClick("Sun"));

        binding.routineRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.routineRC.setHasFixedSize(false);

        list = new ArrayList<>();
        getData();

        binding.inCompleted.setOnClickListener(v -> {
            boolean show = binding.routineRC.getVisibility() == View.VISIBLE;

            if (show){
                binding.routineRC.setVisibility(View.GONE);
                binding.inCompleted.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.upload, 0);
            } else {
                binding.routineRC.setVisibility(View.VISIBLE);
                binding.inCompleted.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.download, 0);
            }

        });

        binding.completed.setOnClickListener(v -> {
            boolean show = binding.routineRC.getVisibility() == View.VISIBLE;

            if (show){
                binding.routineRC.setVisibility(View.GONE);
                binding.completed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.upload, 0);
            } else {
                binding.routineRC.setVisibility(View.VISIBLE);
                binding.completed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.download, 0);
            }
        });


        return binding.getRoot();
    }

    private void getData() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.Routines).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            RoutineModel model = dataSnapshot.getValue(RoutineModel.class);

                            RoutineModel model = new RoutineModel();
                            String ID = dataSnapshot.child("id").getValue(String.class);
                            String name = dataSnapshot.child("name").getValue(String.class);
                            String context = dataSnapshot.child("context").getValue(String.class);
                            int minutes = dataSnapshot.child("minutes").getValue(Integer.class);
                            ArrayList<String> days = new ArrayList<>();
                            ArrayList<AddStepsChildModel> steps = new ArrayList<>();

                            for (DataSnapshot day : dataSnapshot.child("days").getChildren()){
                                String d = day.getValue(String.class);
                                days.add(d);
                            }

                            for (DataSnapshot step : dataSnapshot.child("steps").getChildren()){
                                AddStepsChildModel d = step.getValue(AddStepsChildModel.class);
                                steps.add(d);
                            }

                            CompletedDaysModel completedDays = dataSnapshot.child("daysCompleted").getValue(CompletedDaysModel.class);

                            model.setID(ID);
                            model.setName(name);
                            model.setContext(context);
                            model.setMinutes(minutes);
                            model.setDays(days);
                            model.setSteps(steps);
                            model.setDaysCompleted(completedDays);

                            list.add(model);
                        }

                        adapter = new RoutineAdapter(binding.getRoot().getContext(), list);
                        binding.routineRC.setAdapter(adapter);

                        String today = getToday();
                        if (today.equalsIgnoreCase("Sun")) {
                            today = "Sunday";
                        }
                        if (today.equalsIgnoreCase("Mon")) {
                            today = "Monday";
                        }
                        if (today.equalsIgnoreCase("Tue")) {
                            today = "Tuesday";
                        }
                        if (today.equalsIgnoreCase("Wed")) {
                            today = "Wednesday";
                        }
                        if (today.equalsIgnoreCase("Thu")) {
                            today = "Thursday";
                        }
                        if (today.equalsIgnoreCase("Fri")) {
                            today = "Friday";
                        }
                        if (today.equalsIgnoreCase("Sat")) {
                            today = "Saturday";
                        }

                        adapter.getFilter().filter(today);
                        Constants.dismissDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(binding.getRoot().getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateClick(String clicked) {

        String today = getToday();
        if (today.equals(clicked)) {
            binding.tittle.setText("Today Routines");
        } else {
            binding.tittle.setText(clicked + " Routines");
        }

        if (clicked.equalsIgnoreCase("Sun")) {
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.text));
            adapter.getFilter().filter("Sunday");
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));

        }
        if (clicked.equalsIgnoreCase("Mon")) {
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.text));
            adapter.getFilter().filter("Monday");
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Tue")) {
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.text));
            adapter.getFilter().filter("Tuesday");
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Wed")) {
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.text));
            adapter.getFilter().filter("Wednesday");
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Thu")) {
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.text));
            adapter.getFilter().filter("Thursday");
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Fri")) {
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.text));
            adapter.getFilter().filter("Friday");
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Sat")) {
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.text));
            adapter.getFilter().filter("Saturday");
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    private void updateCalender(String today) {
        binding.tittle.setText("Today Routines");
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