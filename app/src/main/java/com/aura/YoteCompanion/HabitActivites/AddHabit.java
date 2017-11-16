package com.aura.YoteCompanion.HabitActivites;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

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

/**
 * Created by micha on 11/3/2017.
 */

public class AddHabit extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private EditText habit_name, details;
    private CheckBox checkBox;
    private Button datePickerButton, btnTimePicker;
    TextView tvDate, tvTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        habit_name = (EditText) findViewById(R.id.edit_habit_name);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        datePickerButton=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        tvDate=(TextView)findViewById(R.id.tv_date);
        tvTime=(TextView)findViewById(R.id.tv_time);

        details = (EditText) findViewById(R.id.edit_habit_details);
        datePickerButton.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        checkBox = (CheckBox) findViewById(R.id.isDoneCheckBox);

        if (mFirebaseUser == null) {
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
        habit.setHabitId(key);
        habit.setHabitName(habit_name.getText().toString());
        habit.setDetails(details.getText().toString());
        habit.setDate(tvDate.getText().toString());
        habit.setTime(tvTime.getText().toString());
        habit.setIsChecked(checkBoxValue());
        Map<String, Object> habitValues = habit.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Habits/" + mFirebaseUser.getUid() + "/" + key, habitValues);
        mDatabase.updateChildren(childUpdates);
        addNotification();
        finish();
    }

    public void onCancel(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == datePickerButton) {
            // Get Current Date
            final Calendar cal = Calendar.getInstance();
            mYear = cal.get(Calendar.YEAR);
            mMonth = cal.get(Calendar.MONTH);
            mDay = cal.get(Calendar.DAY_OF_MONTH);
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
            final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            String am_pm = "";
                            timePicker.is24HourView();
                            Calendar cal = Calendar.getInstance();
                            if (cal.get(Calendar.AM_PM) == Calendar.AM) {am_pm = "AM";}
                            else if (cal.get(Calendar.AM_PM) == Calendar.PM) {am_pm = "PM";}
                            String strHrsToShow = (cal.get(Calendar.HOUR) == 0) ?"12":cal.get(Calendar.HOUR)+"";
                            tvTime.setText( strHrsToShow+":"+cal.get(Calendar.MINUTE)+" "+am_pm );

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    private void addNotification() {

        Calendar calender = Calendar.getInstance();
        calender.set(Calendar.HOUR_OF_DAY, 12);
        calender.set(Calendar.MINUTE, 00);
        calender.set(Calendar.SECOND,00);
        Intent intent = new Intent(getApplicationContext(), Notification_reciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private boolean checkBoxValue(){
        boolean value;
        if(checkBox.isChecked()) {value = true;
        }else {value = false;}
        return value;
    }
}
