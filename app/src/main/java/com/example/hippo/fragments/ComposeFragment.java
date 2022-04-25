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

    public static final String[] reminders = new String[]{"Never", "10 minutes before", "30 minutes before", "1 hour before",
            "2 hours before", "1 day before", "2 days before", "1 week before", "2 weeks before", "1 month before"};

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    DatePickerDialog datePicker;
    TimePickerDialog timePicker;
    private EditText etTaskDescription;
    private EditText etTitle;
    private EditText etDate;
    private EditText etTime;

    private Spinner spinReminder;

    private ArrayAdapter<String> spinAdapter;

    private Button btnAdd;

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

        spinAdapter = new ArrayAdapter<>(getContext(), R.layout.hippo_spinner_item, reminders);
        spinReminder.setAdapter(spinAdapter);
        spinReminder.setOnItemSelectedListener(this);

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
                        etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
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
                        if (pHour >= 12) {
                            //  amPm = "PM";
                        } else {
                            // amPm = "AM";
                        }
                        //etTime.setText(pHour + ":" + pMinute + " " + amPm);
                        // etReminderTime.setText(pHour-2+":"+pMinute+ amPm);
                    }
                }, hour, minutes, false);
                timePicker.show();
            }
        });

/*        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser currentUser = HippoUser.getCurrentUser();
                String Title = etTitle.getText().toString();
                String Description = etTaskDescription.getText().toString();
                String Date = etDate.getText().toString();
                String Time = etTime.getText().toString();
              //  String Reminder = etReminderTime.getText().toString();
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
       // task.setAttachment(new ParseFile(photoFile));
        task.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if(e!=null) {
                    Log.e(TAG, "error while saving", e);
                    Toast.makeText(getContext(),"Error while Saving", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "task was successfully saved");
                etDate.setText("");
                etTaskDescription.setText("");
                etTime.setText("");
                etTitle.setText("");
                etReminderTime.setText("");
                ivTaskImage.setImageResource(0);
            }
        });
    }*/
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}


