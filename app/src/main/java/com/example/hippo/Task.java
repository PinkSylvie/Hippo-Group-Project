package com.example.hippo;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Task")
public class Task extends ParseObject {
    public static final String KEY_TITLE = "Title";
    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_DUE_TIME = "dueTime";
    public static final String KEY_ATTACHMENT = "Attachment";
    public static final String KEY_USER = "User";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_COMPLETED = "isDone";
    public static final String KEY_REMINDER_TIME = "reminderTime";
    public static final String KEY_CREATED_TIME = "createdAt";
    public static final String KEY_UPDATED_TIME = "updatedAt";

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

    public void setUser(HippoUser user){
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

    public ParseFile getImage(){ return getParseFile(KEY_IMAGE); }

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

}
