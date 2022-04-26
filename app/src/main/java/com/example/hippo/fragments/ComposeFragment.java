package com.example.hippo.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.EventLogTags;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.hippo.MainActivity;
import com.example.hippo.R;
import com.example.hippo.HippoUser;
import com.example.hippo.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kotlin.jvm.internal.CollectionToArray;

public class ComposeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public static final String TAG = "ComposeFragment";

    public static final String TIME12HOUR = "hh:mm a";
    public static final String TIME24HOUR = "HH:mm";

    public static final String[] REMINDERS = new String[]{"Never", "10 minutes before", "30 minutes before", "1 hour before",
            "2 hours before", "1 day before", "2 days before", "1 week before", "2 weeks before", "1 month before"};


    DatePickerDialog datePicker;
    TimePickerDialog timePicker;

    private EditText etTaskDescription;
    private EditText etTitle;
    private EditText etDate;
    private EditText etTime;

    private Spinner spinReminder;

    private ArrayAdapter<String> spinAdapter;

    private Button btnAdd;

    private Calendar dueDate;

    public ComposeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTaskDescription = view.findViewById(R.id.etCompose_task_description);
        etTitle = view.findViewById(R.id.etCompose_task_title);
        etDate = view.findViewById(R.id.etCompose_task_date);
        etTime = view.findViewById(R.id.etCompose_task_time);
        btnAdd = view.findViewById(R.id.btnCompose_task_create);
        spinReminder = view.findViewById(R.id.spinCompose_task_reminder);

        etDate.setInputType(InputType.TYPE_NULL);
        etTime.setInputType(InputType.TYPE_NULL);

        spinAdapter = new ArrayAdapter<>(getContext(), R.layout.hippo_spinner_item, REMINDERS);
        spinReminder.setAdapter(spinAdapter);
        spinReminder.setOnItemSelectedListener(this);

        etDate.setOnClickListener(selectDate);
        etTime.setOnClickListener(selectTime);

        dueDate = Calendar.getInstance();

        btnAdd.setOnClickListener(saveTask);
    }


    View.OnClickListener saveTask = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String title = etTitle.getText().toString();
            if(title.isEmpty()){
                Toast.makeText(getContext(), "Task must have a title", Toast.LENGTH_SHORT).show();
                return;
            }

            Task task = new Task();

            ParseUser user = HippoUser.getCurrentUser();
            task.setUser(user);
            task.setTitle(title);
            task.setDescription(etTaskDescription.getText().toString());
            task.setDueTime(dueDate.getTime());
            task.setCompletion(false);

            task.saveInBackground(new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if(e!=null) {
                        Log.e(TAG, "Error while saving", e);
                        Toast.makeText(getContext(),"Error while saving", Toast.LENGTH_SHORT).show();
                    }
                    Log.i(TAG, "Task was successfully saved");
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, new TaskFragment()).commit();
                }
            });

        }
    };

    View.OnClickListener selectDate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    dueDate.set(Calendar.YEAR, year);
                    dueDate.set(Calendar.MONTH, monthOfYear);
                    dueDate.set(Calendar.DATE, dayOfMonth);
                    etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }, year, month, day);
            datePicker.show();
        }
    };

    View.OnClickListener selectTime = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int pHour, int pMinute) {
                    String time24Hour = String.valueOf(pHour) + ":" + String.valueOf(pMinute);

                    SimpleDateFormat time12HourFormat = new SimpleDateFormat(TIME12HOUR);
                    SimpleDateFormat time24HourFormat = new SimpleDateFormat(TIME24HOUR);

                    try {
                        Date time24 = time24HourFormat.parse(time24Hour);
                        etTime.setText(time12HourFormat.format(time24));
                        dueDate.set(Calendar.HOUR_OF_DAY, pHour);
                        dueDate.set(Calendar.MINUTE, pMinute);
                        dueDate.set(Calendar.SECOND, 0);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, hour, minutes, false);
            timePicker.show();
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}


