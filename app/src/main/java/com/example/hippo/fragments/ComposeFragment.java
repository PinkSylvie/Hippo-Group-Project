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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ComposeFragment extends Fragment {
    public static final String TAG = "ComposeFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    DatePickerDialog datePicker;
    TimePickerDialog timePicker;
    private EditText etTaskDescription;
    private EditText etTitle;
    private EditText etDate;
    private EditText etTime;
    private EditText etReminderTime;
    private ImageView ivTaskImage;
    private File photoFile;

    private Button btnAdd;
    private Button btnCaptureImage;

    public String photoFileName = "photo.jpg";
    private  String amPm;

    public ComposeFragment(){}

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
        ivTaskImage = view.findViewById(R.id.ivTaskImage);
        etReminderTime = view.findViewById(R.id.etReminderTime);
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
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

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
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

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileproviderHippo", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivTaskImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);

        return file;
    }

    private void SaveTask(ParseUser currentUser, String title, String description, Date dueTime, Date reminderTime) {
        Task task = new Task();
        task.put(Task.KEY_USER,currentUser);
        task.setCompletion(false);
        task.setDescription(description);
        task.setDueTime(dueTime);
        task.setTitle(title);
        task.setReminder(reminderTime);
        task.setAttachment(new ParseFile(photoFile));
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
    }
}


