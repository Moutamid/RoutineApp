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
        title.setText(getString(R.string.add_steps));
        back.setOnClickListener(v -> {
            dismiss();
        });

        custom.setOnClickListener(v -> {
            startActivity(new Intent(view.getContext(), AddCustomStepsActivity.class));
            dismiss();
        });

        custom.setBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        custom.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));

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
        child1.add(new AddStepsChildModel(getString(R.string.make_breakfast), "15 min"));
        child1.add(new AddStepsChildModel(getString(R.string.brush_teeth), "3 min"));
        child1.add(new AddStepsChildModel(getString(R.string.make_bed), "2 min"));
        child1.add(new AddStepsChildModel(getString(R.string.get_sunlight), "10 min"));
        child1.add(new AddStepsChildModel(getString(R.string.stretch), "10 min"));
        child1.add(new AddStepsChildModel(getString(R.string.write_down_goals), "10 min"));
        child1.add(new AddStepsChildModel(getString(R.string.wash_face), "1 min"));
        child1.add(new AddStepsChildModel(getString(R.string.take_shower), "15 min"));
        child1.add(new AddStepsChildModel(getString(R.string.meditate), "10 min"));
        child1.add(new AddStepsChildModel(getString(R.string.drink_water), "1 min"));
        child1.add(new AddStepsChildModel(getString(R.string.make_coffee), "8 min"));
        child1.add(new AddStepsChildModel(getString(R.string.write_grateful), "15 min"));
        list.add(new AddStepsModel(getString(R.string.morning_routine), child1));
    }

    private void eveningSteps() {
        ArrayList<AddStepsChildModel> child2 = new ArrayList<>();
        child2.add(new AddStepsChildModel(getString(R.string.wash_face_2), "1 min"));
        child2.add(new AddStepsChildModel(getString(R.string.write_grateful_2), "15 min"));
        child2.add(new AddStepsChildModel(getString(R.string.reflect_on_day), "15 min"));
        child2.add(new AddStepsChildModel(getString(R.string.read_fiction), "30 min"));
        child2.add(new AddStepsChildModel(getString(R.string.talk_to_family), "20 min"));
        child2.add(new AddStepsChildModel(getString(R.string.meditate_2), "10 min"));
        child2.add(new AddStepsChildModel(getString(R.string.write_down_goals_2), "10 min"));
        child2.add(new AddStepsChildModel(getString(R.string.take_shower_2), "15 min"));
        child2.add(new AddStepsChildModel(getString(R.string.prepare_outfit), "10 min"));
        child2.add(new AddStepsChildModel(getString(R.string.turn_off_electronics), "2 min"));
        child2.add(new AddStepsChildModel(getString(R.string.brush_teeth_2), "3 min"));

        list.add(new AddStepsModel(getString(R.string.evening_routine), child2));
    }

    private void workSteps() {
        ArrayList<AddStepsChildModel> child3 = new ArrayList<>();
        child3.add(new AddStepsChildModel(getString(R.string.answer_emails), "15 min"));
        child3.add(new AddStepsChildModel(getString(R.string.short_break), "10 min"));
        child3.add(new AddStepsChildModel(getString(R.string.deep_work), "45 min"));
        child3.add(new AddStepsChildModel(getString(R.string.write_down_priorities), "8 min"));
        child3.add(new AddStepsChildModel(getString(R.string.prepare_meetings), "20 min"));
        child3.add(new AddStepsChildModel(getString(R.string.breathing_exercise), "3 min"));
        child3.add(new AddStepsChildModel(getString(R.string.make_coffee_3), "8 min"));
        child3.add(new AddStepsChildModel(getString(R.string.long_break), "30 min"));
        child3.add(new AddStepsChildModel(getString(R.string.pomodoro), "25 min"));

        list.add(new AddStepsModel(getString(R.string.ready_for_work_routine), child3));
    }

    private void selfcareSteps() {
        ArrayList<AddStepsChildModel> child4 = new ArrayList<>();
        child4.add(new AddStepsChildModel(getString(R.string.write_todo_list), "10 min"));
        child4.add(new AddStepsChildModel(getString(R.string.exercise), "20 min"));
        child4.add(new AddStepsChildModel(getString(R.string.write_grateful_4), "15 min"));
        child4.add(new AddStepsChildModel(getString(R.string.take_warm_bath), "40 min"));
        child4.add(new AddStepsChildModel(getString(R.string.take_loved_ones), "30 min"));
        child4.add(new AddStepsChildModel(getString(R.string.meditate_4), "10 min"));
        child4.add(new AddStepsChildModel(getString(R.string.visualize_success), "15 min"));

        list.add(new AddStepsModel(getString(R.string.selfcare_routine), child4));
    }

    private void studySteps() {
        ArrayList<AddStepsChildModel> child5 = new ArrayList<>();
        child5.add(new AddStepsChildModel(getString(R.string.practice_problems), "30 min"));
        child5.add(new AddStepsChildModel(getString(R.string.study), "25 min"));
        child5.add(new AddStepsChildModel(getString(R.string.read_subject), "20 min"));
        child5.add(new AddStepsChildModel(getString(R.string.breathing_exercise_5), "3 min"));
        child5.add(new AddStepsChildModel(getString(R.string.figure_out), "15 min"));
        child5.add(new AddStepsChildModel(getString(R.string.write_task), "10 min"));
        child5.add(new AddStepsChildModel(getString(R.string.deep_work_5), "45 min"));
        child5.add(new AddStepsChildModel(getString(R.string.prepare_desk), "3 min"));

        list.add(new AddStepsModel(getString(R.string.study_routine), child5));
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
