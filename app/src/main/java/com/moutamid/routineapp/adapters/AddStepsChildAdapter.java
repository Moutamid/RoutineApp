package com.moutamid.routineapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.routineapp.R;
import com.moutamid.routineapp.listners.StepClickListner;
import com.moutamid.routineapp.models.AddStepsChildModel;

import java.util.ArrayList;

public class AddStepsChildAdapter extends RecyclerView.Adapter<AddStepsChildAdapter.ChildVH> {
    Context context;
    ArrayList<AddStepsChildModel> list;
    StepClickListner listner;

    public AddStepsChildAdapter(Context context, ArrayList<AddStepsChildModel> list, StepClickListner listner) {
        this.context = context;
        this.list = list;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ChildVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChildVH(LayoutInflater.from(context).inflate(R.layout.add_steps_child, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChildVH holder, int position) {
        AddStepsChildModel model = list.get(holder.getAdapterPosition());
        holder.time.setText(model.getTime());
        holder.title.setText(model.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listner != null){
                listner.onClick(list.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChildVH extends RecyclerView.ViewHolder{
        TextView title, time;
        public ChildVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
        }
    }

}
