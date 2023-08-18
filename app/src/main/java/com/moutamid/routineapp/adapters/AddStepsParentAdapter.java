package com.moutamid.routineapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.routineapp.R;
import com.moutamid.routineapp.listners.StepClickListner;
import com.moutamid.routineapp.models.AddStepsModel;

import java.util.ArrayList;
import java.util.Collections;

public class AddStepsParentAdapter extends RecyclerView.Adapter<AddStepsParentAdapter.AddPaVH> {

    Context context;
    ArrayList<AddStepsModel> list;
    StepClickListner listner;
    AddStepsChildAdapter childItemAdapter;

    public AddStepsParentAdapter(Context context, ArrayList<AddStepsModel> list, StepClickListner listner) {
        this.context = context;
        this.list = list;
        this.listner = listner;
    }

    @NonNull
    @Override
    public AddPaVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddPaVH(LayoutInflater.from(context).inflate(R.layout.add_steps_parent, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddPaVH holder, int position) {
        AddStepsModel model  = list.get(holder.getAdapterPosition());
        holder.title.setText(model.getTitle());

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.rc.getContext());

        layoutManager.setInitialPrefetchItemCount(model.getList().size());

        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        childItemAdapter = new AddStepsChildAdapter(context, model.getList(), listner);
        holder.rc.setLayoutManager(layoutManager);
        holder.rc.setHasFixedSize(false);
        holder.rc.setAdapter(childItemAdapter);
        holder.rc.setRecycledViewPool(viewPool);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AddPaVH extends RecyclerView.ViewHolder{
        TextView title;
        RecyclerView rc;
        public AddPaVH(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            rc = itemView.findViewById(R.id.rc);

        }
    }

}
