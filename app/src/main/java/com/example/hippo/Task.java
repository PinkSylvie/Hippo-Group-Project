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

    String getTitle(){
        return getString(KEY_TITLE);
    }

    String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    Date getDueTime(){
        return getDate(KEY_DUE_TIME);
    }

    ParseFile getAttachment(){
        return getParseFile(KEY_ATTACHMENT);
    }

    ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    boolean isDone(){
        return getBoolean(KEY_COMPLETED);
    }

    Date getReminder(){
        return getDate(KEY_REMINDER_TIME);
    }

    Date getCreationDate(){
        return getDate(KEY_CREATED_TIME);
    }

    Date getUpdatedDate(){
        return getDate(KEY_UPDATED_TIME);
    }

}
