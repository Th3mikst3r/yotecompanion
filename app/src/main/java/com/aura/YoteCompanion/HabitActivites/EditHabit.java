package com.aura.YoteCompanion.HabitActivites;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aura.YoteCompanion.Authentication.SignInActivity;
import com.aura.YoteCompanion.Models.Habit;
import com.aura.YoteCompanion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditHabit extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private EditText habit_name, number_of_times, details;

    private Button datePickerButton, btnTimePicker;
    TextView tvDate, tvTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_save);
        fab.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        final Habit habit = (Habit) intent.getSerializableExtra("Habit");

        habit_name = (EditText) findViewById(R.id.edit_habit_name);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        datePickerButton=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        tvDate=(TextView)findViewById(R.id.tv_date);
        tvTime=(TextView)findViewById(R.id.tv_time);

        number_of_times = (EditText) findViewById(R.id.edit_text_number_of_times);
        details = (EditText) findViewById(R.id.edit_habit_details);
        datePickerButton.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        try {
            habit_name.setText(habit.getHabitName());
            tvDate.setText(habit.getDate());
            tvTime.setText(habit.getTime());
            details.setText(habit.getDetails());
            number_of_times.setText(habit.getNumOfTimes());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Cant Fetch Habit :( ", Toast.LENGTH_SHORT).show();
        }


        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
        }

    }

    public void onSave(View view){

        String key = mDatabase.child("Habits").push().getKey();
        Habit habit = new Habit();
        habit.setHabitName(habit_name.getText().toString());
        habit.setDetails(details.getText().toString());
        habit.setDate(tvDate.getText().toString());
        habit.setTime(tvTime.getText().toString());
        habit.setNumOfTimes(number_of_times.getText().toString());
        habit.setIsChecked(false);

        Map<String, Object> habitValues = habit.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Habits/" + mFirebaseUser.getUid() + "/" + key, habitValues);
        mDatabase.updateChildren(childUpdates);
        finish();

    }

    public void onCancel(View view) {
        finish();
    }

    public void onClick(View v) {
        if (v == datePickerButton) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    tvDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                }
            },
                    mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            tvTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}
