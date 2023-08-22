package com.moutamid.routineapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.models.AddStepsChildModel;

import java.util.ArrayList;

public class RoutineStartAdapter extends RecyclerView.Adapter<RoutineStartAdapter.RoutineVH> {

    Context context;
    ArrayList<AddStepsChildModel> list;

    public RoutineStartAdapter(Context context, ArrayList<AddStepsChildModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RoutineVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoutineVH(LayoutInflater.from(context).inflate(R.layout.steps_done, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineVH holder, int position) {
        AddStepsChildModel model = list.get(holder.getAdapterPosition());
        holder.time.setText(model.getTime());
        holder.title.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RoutineVH extends RecyclerView.ViewHolder{
        TextView title, time;
        MaterialRadioButton switchDone;
        public RoutineVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            switchDone = itemView.findViewById(R.id.switchDone);
        }
    }

}
