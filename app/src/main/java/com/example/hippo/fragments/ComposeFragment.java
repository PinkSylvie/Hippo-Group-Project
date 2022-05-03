package com.example.hippo.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kotlin.jvm.internal.CollectionToArray;

public class ComposeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public static final String TAG = "ComposeFragment";

    private static final int FILE_SELECT_CODE = 0;

    public static final String TIME12HOUR = "hh:mm a";
    public static final String TIME24HOUR = "HH:mm";

    public static final String[] REMINDERS = new String[]{"Never", "10 minutes before", "30 minutes before", "1 hour before",
            "2 hours before", "1 day before", "2 days before", "1 week before", "2 weeks before", "1 month before"};

    private int reminderSelection = 0;


    DatePickerDialog datePicker;
    TimePickerDialog timePicker;

    private EditText etTaskDescription;
    private EditText etTitle;
    private EditText etDate;
    private EditText etTime;

    private TextView tvAttachment;

    private Spinner spinReminder;

    private ArrayAdapter<String> spinAdapter;

    private Button btnAdd;

    private Calendar dueDate;
    private File attachment;

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

        tvAttachment = view.findViewById(R.id.tvCompose_task_attachment);


        etDate.setInputType(InputType.TYPE_NULL);
        etTime.setInputType(InputType.TYPE_NULL);

        spinAdapter = new ArrayAdapter<>(getContext(), R.layout.hippo_spinner_item, REMINDERS);
        spinReminder.setAdapter(spinAdapter);
        spinReminder.setOnItemSelectedListener(this);

        etDate.setOnClickListener(selectDate);
        etTime.setOnClickListener(selectTime);

        tvAttachment.setOnClickListener(attachFile);

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
            task.setReminder(setReminder().getTime());
            task.setCompletion(false);

            String hard_coded_path = "/Internal storage/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images/ IMG-20220413-WA0010.jpg";

            if(attachment != null){
                try {
                    byte[] data = Files.readAllBytes(Paths.get(hard_coded_path));
                    ParseFile file = new ParseFile("attachment", data);
                    task.setAttachment(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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

    View.OnClickListener attachFile = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            try{
                startActivityForResult(
                        Intent.createChooser(intent, "Select a file to attach"), FILE_SELECT_CODE);
            }catch (android.content.ActivityNotFoundException e){
                Toast.makeText(view.getContext(), "Please install a File Manager", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data){ // Deprecated function. What's the alternative?
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == FILE_SELECT_CODE){
                Uri selectedData = data.getData();

                if(null != selectedData){
                    attachment = new File(selectedData.getPath());
                    Log.i(TAG, "file is: " + attachment);
                    tvAttachment.setText(attachment.getAbsolutePath());
                }
            }
        }
    }

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

    private Calendar setReminder(){
        Calendar reminder = (Calendar) dueDate.clone();
        switch (reminderSelection){
            case 1:
                reminder.add(Calendar.MINUTE, -10);
                return reminder;
            case 2:
                reminder.add(Calendar.MINUTE, -30);
                return reminder;
            case 3:
                reminder.add(Calendar.HOUR_OF_DAY, -1);
                return reminder;
            case 4:
                reminder.add(Calendar.HOUR_OF_DAY, -2);
                return reminder;
            case 5:
                reminder.add(Calendar.DAY_OF_MONTH, -1);
                return reminder;
            case 6:
                reminder.add(Calendar.DAY_OF_MONTH, -2);
                return reminder;
            case 7:
                reminder.add(Calendar.WEEK_OF_MONTH, -1);
                return reminder;
            case 8:
                reminder.add(Calendar.WEEK_OF_MONTH, -2);
                return reminder;
            case 9:
                reminder.add(Calendar.MONTH, -1);
                return reminder;
            default:
                return reminder;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        reminderSelection = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}


