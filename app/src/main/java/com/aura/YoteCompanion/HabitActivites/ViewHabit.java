package com.aura.YoteCompanion.HabitActivites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.aura.YoteCompanion.Models.Habit;
import com.aura.YoteCompanion.R;

public class ViewHabit extends AppCompatActivity {
    private TextView lbl_title;
    private TextView lbl_details;
    private TextView lbl_date;
    private TextView lbl_time;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final Habit habit = (Habit) intent.getSerializableExtra("Habit");

        lbl_title = (TextView) findViewById(R.id.lbl_habit_title);
        lbl_details = (TextView) findViewById(R.id.lbl_habit_details);
        lbl_date = (TextView) findViewById(R.id.lbl_habit_date);
        lbl_time = (TextView) findViewById(R.id.lbl_habit_time);
        checkBox = (CheckBox) findViewById(R.id.cbCompleted);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EditHabit.class);
                intent.putExtra("Habit", habit);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()) {
                    habit.setIsChecked(true);
                }else {
                    habit.setIsChecked(false);
                }
            }
        });

        try {
            lbl_title.setText(habit.getHabitName());
            lbl_details.setText(habit.getDetails());
            lbl_date.setText(habit.getDate());
            lbl_time.setText(habit.getTime());
            checkBox.setChecked(habit.getIsChecked());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Cant Fetch Habit :( ", Toast.LENGTH_SHORT).show();
        }

        lbl_title.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), EditHabit.class);
                in.putExtra("Habit", habit);
                startActivity(in);
            }
        });
        lbl_details.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), EditHabit.class);
                in.putExtra("Habit", habit);
                startActivity(in);
            }
        });
        lbl_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), EditHabit.class);
                in.putExtra("Habit", habit);
                startActivity(in);
            }
        });

        lbl_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), EditHabit.class);
                in.putExtra("Habit", habit);
                startActivity(in);
            }
        });


    }
}
