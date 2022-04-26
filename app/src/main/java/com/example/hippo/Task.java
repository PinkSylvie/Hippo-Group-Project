package com.example.hippo;

import android.os.Parcelable;

import androidx.versionedparcelable.VersionedParcelize;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;
import org.parceler.ParcelConverter;
import org.parceler.ParcelPropertyConverter;
import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@VersionedParcelize
@ParseClassName("Task")
public class Task extends ParseObject implements Parcelable {
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DUE_TIME = "dueTime";
    public static final String KEY_ATTACHMENT = "attachment";
    public static final String KEY_USER = "User";
    public static final String KEY_COMPLETED = "isDone";
    public static final String KEY_REMINDER_TIME = "reminderTime";
    public static final String KEY_CREATED_TIME = "createdAt";
    public static final String KEY_UPDATED_TIME = "updatedAt";

    public static final String DATE_FORMAT = "MM/dd/yyyy";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String TIME_FORMAT_12HOUR = "hh:mm a";

    // Empty Constructor Added to use Parceler
    public Task(){}

    // Parceler Converter



    //Setters

    public void setTitle(String title){
        put(KEY_TITLE, title);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public void setDueTime(Date dueTime){
        put(KEY_DUE_TIME, dueTime);
    }

    public void setAttachment(ParseFile file){
        put(KEY_ATTACHMENT, file);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public void setCompletion(boolean isDone){
        put(KEY_COMPLETED, isDone);
    }

    public void setReminder(Date reminder){
        put(KEY_REMINDER_TIME, reminder);
    }

    // Getters

    public String getTitle(){
        return getString(KEY_TITLE);
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public Date getDueTime(){
        return getDate(KEY_DUE_TIME);
    }

    public ParseFile getAttachment(){
        return getParseFile(KEY_ATTACHMENT);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    boolean isDone(){
        return getBoolean(KEY_COMPLETED);
    }

    public Date getReminder(){
        return getDate(KEY_REMINDER_TIME);
    }

    public Date getCreationDate(){
        return getDate(KEY_CREATED_TIME);
    }

    public Date getUpdatedDate(){
        return getDate(KEY_UPDATED_TIME);
    }


    //Methods

    public String getDueDateStr(){
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String dueDateAsString = df.format(getDueTime());
        return dueDateAsString;
    }

    public String getDueTimeStr(){
        DateFormat df = new SimpleDateFormat(TIME_FORMAT);
        String dueTimeAsString = df.format(getDueTime());
        return dueTimeAsString;
    }

    public String getDueTime12HourStr(){
        DateFormat df = new SimpleDateFormat(TIME_FORMAT_12HOUR);
        String dueTimeAsString = df.format(getDueTime());
        return dueTimeAsString;
    }



}



