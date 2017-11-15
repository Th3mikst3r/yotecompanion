package com.aura.YoteCompanion.Models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Habit implements Serializable {
    private String HabitName;
    private String date;
    private String time;
    //private String numOfTimes;
    private String details;
    private String habitId;
    private Boolean isChecked;

    public Habit(String habitName, String details, String date, String time, /*String numOfTimes , */ String habitId, Boolean isChecked) {
        this.setHabitId(habitId);
        this.setHabitName(habitName);
        this.setDetails(details);
        this.setDate(date);
        this.setTime(time);
        //this.setNumOfTimes(numOfTimes);
        this.setIsChecked(isChecked);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("habitName", getHabitName().toString());
        result.put("details", getDetails().toString());
        result.put("date",getDate());
        result.put("time",getTime());
        //result.put("Number Of Times", getNumOfTimes());
        result.put("isChecked", getIsChecked());
        result.put("habitId" , getHabitId());
        return result;
    }

    public String getHabitName() {
        return HabitName;
    }

    public void setHabitName(String habitName) {
        HabitName = habitName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Habit() {//Constructor
    }

    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
/*
    public String getNumOfTimes() {
        return numOfTimes;
    }

    public void setNumOfTimes(String numOfTimes) {
        this.numOfTimes = numOfTimes;
    }*/

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public String getHabitId() {
        return habitId;
    }
}
