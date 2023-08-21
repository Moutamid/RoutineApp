package com.moutamid.routineapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.routineapp.R;
import com.moutamid.routineapp.models.RoutineModel;
import com.moutamid.routineapp.utils.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineVH> implements Filterable {
    Context context;
    ArrayList<RoutineModel> list;
    ArrayList<RoutineModel> listAll;

    public RoutineAdapter(Context context, ArrayList<RoutineModel> list) {
        this.context = context;
        this.list = list;
        listAll = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public RoutineVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoutineVH(LayoutInflater.from(context).inflate(R.layout.routine_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineVH holder, int position) {
        RoutineModel model = list.get(holder.getAdapterPosition());
        String s = model.getSteps().size() > 1 ? " steps" : " step";
        holder.totalStep.setText(model.getSteps().size() + s);
        holder.name.setText(model.getName());
        int min = 0;
        List<Integer> timeValues = Constants.extractTimeValues(model.getSteps());
        for (int value : timeValues) {
            min += value;
        }
        String formattedTime = Constants.convertMinutesToHHMM(min) + "h";
        holder.totalTime.setText(formattedTime);
        String steps = "";
        for (int i = 0; i < model.getSteps().size(); i++) {
            String st = model.getSteps().get(i).getName();
            steps += (i+1) + ". " + st + "\n";
            if (i == 2){
                steps += "...";
                break;
            }
        }

        holder.steps.setText(steps);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<RoutineModel> filterList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filterList.addAll(listAll);
            } else {
                for (RoutineModel listModel : listAll) {
                    for (String days : listModel.getDays()){
                        if (
                                days.equalsIgnoreCase(charSequence.toString())
                        ) {
                            filterList.add(listModel);
                        }
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends RoutineModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class RoutineVH extends RecyclerView.ViewHolder {
        TextView totalTime, totalStep, name, steps;
        View view1, view2, view3, view4, view5, view6, view7;

        public RoutineVH(@NonNull View itemView) {
            super(itemView);

            totalTime = itemView.findViewById(R.id.totalTime);
            totalStep = itemView.findViewById(R.id.totalStep);
            name = itemView.findViewById(R.id.name);
            steps = itemView.findViewById(R.id.steps);

            view1 = itemView.findViewById(R.id.view1);
            view2 = itemView.findViewById(R.id.view2);
            view3 = itemView.findViewById(R.id.view3);
            view4 = itemView.findViewById(R.id.view4);
            view5 = itemView.findViewById(R.id.view5);
            view6 = itemView.findViewById(R.id.view6);
            view7 = itemView.findViewById(R.id.view7);

        }
    }

}
