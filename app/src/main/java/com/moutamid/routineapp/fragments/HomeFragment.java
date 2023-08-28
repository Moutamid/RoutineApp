package com.moutamid.routineapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.adapters.RoutineAdapter;
import com.moutamid.routineapp.databinding.FragmentHomeBinding;
import com.moutamid.routineapp.models.AddStepsChildModel;
import com.moutamid.routineapp.models.CompletedDaysModel;
import com.moutamid.routineapp.models.RoutineModel;
import com.moutamid.routineapp.models.StepsLocalModel;
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

        String today = Constants.getToday();
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
                binding.routineRCCompleted.setVisibility(View.GONE);
                binding.completed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.upload, 0);
            } else {
                binding.routineRCCompleted.setVisibility(View.VISIBLE);
                binding.completed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.download, 0);
            }
        });


        return binding.getRoot();
    }

    ArrayList<Long> times = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        if (list.size() == 0){
            getData();
        }
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
                            long reminder = dataSnapshot.child("reminder").getValue(Long.class);

                            ArrayList<String> days = new ArrayList<>();
                            ArrayList<AddStepsChildModel> steps = new ArrayList<>();

                            if (reminder != 0){
                                times.add(reminder);
                            }

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
                            model.setReminder(reminder);

                            list.add(model);
                        }


                        if (list.size() > 0){
                            for (int i = 0; i < list.size(); i++) {
                                RoutineModel model = list.get(i);
                                String ID = model.getID();

                                ArrayList<StepsLocalModel> localList = Stash.getArrayList(ID, StepsLocalModel.class);
                                if (localList.size() == 0) {
                                    for (AddStepsChildModel childModel : model.getSteps()) {
                                        localList.add(new StepsLocalModel(ID, childModel.getName(), childModel.getTime(), false));
                                    }
                                    Stash.put(ID, localList);
                                }
                            }
                        }

                        adapter = new RoutineAdapter(binding.getRoot().getContext(), list);
                        binding.routineRC.setAdapter(adapter);

                        String today = Constants.getToday();
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
                        Stash.put(Constants.TIME_LIST, times);
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

        String today = Constants.getToday();
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
}