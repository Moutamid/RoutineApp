package com.moutamid.routineapp.bottomsheets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.activities.AddCustomStepsActivity;
import com.moutamid.routineapp.adapters.AddStepsParentAdapter;
import com.moutamid.routineapp.listners.BottomSheetDismissListener;
import com.moutamid.routineapp.listners.StepClickListner;
import com.moutamid.routineapp.models.AddStepsChildModel;
import com.moutamid.routineapp.models.AddStepsModel;
import com.moutamid.routineapp.utils.Constants;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddStepsFragment extends BottomSheetDialogFragment {

    RecyclerView recyler;
    Context context;
    AddStepsParentAdapter adapter;
    ArrayList<AddStepsModel> list;
    private BottomSheetDismissListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_steps_fragment, container, false);
        View toolbar = view.findViewById(R.id.toolbar);

        Button custom = view.findViewById(R.id.custom);
        TextView title = toolbar.findViewById(R.id.tittle);
        ImageView back = toolbar.findViewById(R.id.back);

        context = view.getContext();

        back.setImageResource(R.drawable.round_close_24);
        title.setText("Add Steps");
        back.setOnClickListener(v -> {
            dismiss();
        });

        custom.setOnClickListener(v -> {
            startActivity(new Intent(view.getContext(), AddCustomStepsActivity.class));
            dismiss();
        });

        recyler = view.findViewById(R.id.recyler);
        recyler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyler.setHasFixedSize(false);

        list = new ArrayList<>();

        if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("ALL")) {
            addData();
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("MORNING")) {
            morningSteps();
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("EVENING")) {
            eveningSteps();
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("WORK")) {
            workSteps();
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("SELFCARE")) {
            selfcareSteps();
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("STUDY")) {
            studySteps();
        }

        adapter = new AddStepsParentAdapter(view.getContext(), list, listner);
        recyler.setAdapter(adapter);


        return view;
    }

    private void morningSteps() {
        ArrayList<AddStepsChildModel> child1 = new ArrayList<>();
        child1.add(new AddStepsChildModel("Make Breakfast", "15 min"));
        child1.add(new AddStepsChildModel("Brush my teeth", "3 min"));
        child1.add(new AddStepsChildModel("Make my bed", "2 min"));
        child1.add(new AddStepsChildModel("Get some sunlight", "10 min"));
        child1.add(new AddStepsChildModel("Stretch", "10 min"));
        child1.add(new AddStepsChildModel("Write down goals", "10 min"));
        child1.add(new AddStepsChildModel("Wash my face", "1 min"));
        child1.add(new AddStepsChildModel("Take a shower", "15 min"));
        child1.add(new AddStepsChildModel("Meditate", "10 min"));
        child1.add(new AddStepsChildModel("Drink Water", "1 min"));
        child1.add(new AddStepsChildModel("Make some coffee", "8 min"));
        child1.add(new AddStepsChildModel("Write down what i'm grateful for", "15 min"));
        list.add(new AddStepsModel("Morning Routine", child1));
    }

    private void eveningSteps() {
        ArrayList<AddStepsChildModel> child2 = new ArrayList<>();
        child2.add(new AddStepsChildModel("Wash my face", "1 min"));
        child2.add(new AddStepsChildModel("Write down what i'm grateful for", "15 min"));
        child2.add(new AddStepsChildModel("Reflect on my day", "15 min"));
        child2.add(new AddStepsChildModel("Read some fiction", "30 min"));
        child2.add(new AddStepsChildModel("Talk to my family", "20 min"));
        child2.add(new AddStepsChildModel("Meditate", "10 min"));
        child2.add(new AddStepsChildModel("Write down goals", "10 min"));
        child2.add(new AddStepsChildModel("Take a shower", "15 min"));
        child2.add(new AddStepsChildModel("Prepare outfit for next day", "10 min"));
        child2.add(new AddStepsChildModel("Turn off electronics", "2 min"));
        child2.add(new AddStepsChildModel("Brush my teeth", "3 min"));
        list.add(new AddStepsModel("Evening Routine", child2));
    }

    private void workSteps() {
        ArrayList<AddStepsChildModel> child3 = new ArrayList<>();
        child3.add(new AddStepsChildModel("Answer emails", "15 min"));
        child3.add(new AddStepsChildModel("Short break", "10 min"));
        child3.add(new AddStepsChildModel("Deep work", "45 min"));
        child3.add(new AddStepsChildModel("Write down priorities", "8 min"));
        child3.add(new AddStepsChildModel("Prepare meetings", "20 min"));
        child3.add(new AddStepsChildModel("Do a breathing exercise", "3 min"));
        child3.add(new AddStepsChildModel("Make some coffee", "8 min"));
        child3.add(new AddStepsChildModel("Long break", "30 min"));
        child3.add(new AddStepsChildModel("Pomodoro", "25 min"));
        list.add(new AddStepsModel("Ready for work", child3));
    }

    private void selfcareSteps() {
        ArrayList<AddStepsChildModel> child4 = new ArrayList<>();
        child4.add(new AddStepsChildModel("Write my todo list", "10 min"));
        child4.add(new AddStepsChildModel("Exercise", "20 min"));
        child4.add(new AddStepsChildModel("Write down what i'm grateful for", "15 min"));
        child4.add(new AddStepsChildModel("Take a warm bath", "40 min"));
        child4.add(new AddStepsChildModel("Take loved ones", "30 min"));
        child4.add(new AddStepsChildModel("Meditate", "10 min"));
        child4.add(new AddStepsChildModel("Visualize Success", "15 min"));
        list.add(new AddStepsModel("Selfcare Routine", child4));
    }

    private void studySteps() {
        ArrayList<AddStepsChildModel> child5 = new ArrayList<>();
        child5.add(new AddStepsChildModel("Do practice problems", "30 min"));
        child5.add(new AddStepsChildModel("Study", "25 min"));
        child5.add(new AddStepsChildModel("Read about the subject", "20 min"));
        child5.add(new AddStepsChildModel("Do a breathing exercise", "3 min"));
        child5.add(new AddStepsChildModel("Figure out what you don't yet understand", "15 min"));
        child5.add(new AddStepsChildModel("Write down your task", "10 min"));
        child5.add(new AddStepsChildModel("Deep work", "45 min"));
        child5.add(new AddStepsChildModel("Prepare my desk", "3 min"));
        list.add(new AddStepsModel("Study Routine", child5));
    }

    private void addData() {
        morningSteps();
        eveningSteps();
        workSteps();
        selfcareSteps();
        studySteps();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set BottomSheet behavior to go full screen
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            BottomSheetBehavior.from((View) getView().getParent()).setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onBottomSheetDismissed();
        }
    }

    public void setListener(BottomSheetDismissListener listener) {
        this.listener = listener;
    }

    StepClickListner listner = model -> {
        ArrayList<AddStepsChildModel> list = Stash.getArrayList(Constants.Steps, AddStepsChildModel.class);
        list.add(model);
        Stash.put(Constants.Steps, list);
        this.dismiss();
    };

}
