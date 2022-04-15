package com.example.hippo.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.EventLogTags;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hippo.R;
import com.example.hippo.HippoUser;
import com.example.hippo.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class composeFragment extends Fragment {
    public static final String TAG = "composeFragment";
    DatePickerDialog datePicker;
    TimePickerDialog timePicker;
    private EditText etTaskDescription;
    private EditText etTitle;
    private EditText etDate;
    private EditText etTime;
    private EditText etReminderTime;
    private Button btnAdd;
    String amPm;

    public composeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTaskDescription = view.findViewById(R.id.etTaskDescription);
        etTitle = view.findViewById(R.id.etTitle);
        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);
        etReminderTime = view.findViewById(R.id.etReminderTime);

        FloatingActionButton btnAdd =  view.findViewById(R.id.btnAdd);


        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);


                datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etDate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                    }
                }, year,month,day);
                datePicker.show();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int pHour, int pMinute) {
                        if(pHour>=12){
                            amPm = "PM";
                        }
                        else{
                            amPm = "AM";
                        }
                        etTime.setText(pHour + ":" + pMinute + " " + amPm);
                        etReminderTime.setText(pHour-2+":"+pMinute+ amPm);
                    }
                }, hour, minutes, false);
                timePicker.show();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser currentUser = HippoUser.getCurrentUser();
                String Title = etTitle.getText().toString();
                String Description = etTaskDescription.getText().toString();
                String Date = etDate.getText().toString();
                String Time = etTime.getText().toString();
                String Reminder = etReminderTime.getText().toString();
                SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy hh:mm");


                Date reminderTime= null;
                Date dueTime = null;
                try {
                    dueTime = formatDate.parse(Date + " " + Time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    reminderTime = formatDate.parse(Date + " " + Reminder);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                //System.out.println(dueTime);
                System.out.println(reminderTime);
                if (Title.isEmpty()) {
                    Toast.makeText(getContext(), "Title can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Description.isEmpty()) {
                    Toast.makeText(getContext(), "Description can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                SaveTask(currentUser,Title, Description, dueTime, reminderTime);
            }
        });
    }

    private void SaveTask(ParseUser currentUser, String title, String description, Date dueTime, Date reminderTime) {
        Task task = new Task();
        task.put(Task.KEY_USER,currentUser);
        task.setCompletion(false);
        task.setDescription(description);
        task.setDueTime(dueTime);
        task.setTitle(title);
        task.setReminder(reminderTime);
        task.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if(e!=null) {
                    Log.e(TAG, "error while saving", e);
                    Toast.makeText(getContext(),"Error while Saving", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "task was succesfully saved");
                etDate.setText("");
                etTaskDescription.setText("");
                etTime.setText("");
                etTitle.setText("");
                etReminderTime.setText("");
            }
        });
    }
}


