package com.aura.YoteCompanion.Models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Habit implements Serializable {
    private String HabitName;
    private String date;
    private String time;
    private String numOfTimes;


    public String getHabitName() {
        return HabitName;
    }

    public void setHabitName(String habitName) {
        HabitName = habitName;
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

    public String getNumOfTimes() {
        return numOfTimes;
    }

    public void setNumOfTimes(String numOfTimes) {
        this.numOfTimes = numOfTimes;
    }

    public Habit(String habitName, String date, String time, String numOfTimes) {
        this.setHabitName(habitName);
        this.setDate(date);
        this.setTime(time);
        this.setNumOfTimes(numOfTimes);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("habitName", getHabitName().toString());
        result.put("date",getDate());
        result.put("time",getTime());
        result.put("Number Of Times", getNumOfTimes());
        return result;
    }

}
