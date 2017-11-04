package com.aura.YoteCompanion.Helpers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.aura.YoteCompanion.Models.Habit;
import com.aura.YoteCompanion.R;
import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.MyViewHolder> {
    private List<Habit> habitList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, date, time, numOfTimes;

        public MyViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.lbl_habitName);
            date = (TextView) view.findViewById(R.id.lbl_date);
            time = (TextView) view.findViewById(R.id.lbl_time);
            numOfTimes = (TextView) view.findViewById(R.id.lbl_numOfTimes);
        }
    }

    public HabitAdapter(List<Habit> habitList) {
        this.habitList = habitList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_list_row, parent, false);
        return  new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Habit habit = habitList.get(position);
        holder.name.setText(habit.getHabitName().toUpperCase());
        holder.date.setText(habit.getDate());
        holder.time.setText(habit.getTime());
        holder.numOfTimes.setText(habit.getNumOfTimes());
    }

    @Override
    public int getItemCount(){
        return  habitList.size();
    }
}
