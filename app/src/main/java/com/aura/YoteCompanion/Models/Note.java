package com.aura.YoteCompanion.Models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Note implements Serializable {
    private String Title;
    private String Details;
    private Date DateSaved;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public Date getDateSaved() {
        return DateSaved;
    }

    public void setDateSaved(Date dateSaved) {
        DateSaved = dateSaved;
    }

    public Note() {//Constructor
    }

    public Note(String title, String details, String date) {
        this.setTitle(title);
        this.setDetails(details);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss zzz");
        try {
            this.setDateSaved(sdf.parse(date));
        }
        catch (ParseException | NullPointerException e){
            this.setDateSaved(new Date());
        }
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", getTitle());
        result.put("details", getDetails());
        result.put("dateSaved", getDateSaved().toString());
        return result;
    }

}
