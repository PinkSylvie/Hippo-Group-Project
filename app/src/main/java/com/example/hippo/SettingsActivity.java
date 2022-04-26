package com.example.hippo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hippo.SignUpActivity;
import com.parse.ParseUser;


public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = "SettingsActivity";
    private EditText etFirstNameUpdated;
    private EditText etLastNameUpdated;
    private EditText etOldEmail;
    private EditText etNewEmail;
    private EditText etOldPassword;
    private EditText etNewPassword;
    private Button btnChangeEmail;
    private Button btnChangeName;
    private Button btnChangePassword;

    public SettingsActivity(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        etFirstNameUpdated = findViewById(R.id.etFirstNameUpdated);
        etLastNameUpdated = findViewById(R.id.etLastNameUpdated);
        etOldEmail = findViewById(R.id.etOldEmail);
        etNewEmail = findViewById(R.id.etNewEmail);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnChangeEmail = findViewById(R.id.btnChangeEmail);
        btnChangeName = findViewById(R.id.btnChangeName);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        btnChangeEmail.setOnClickListener(changeEmail);
        btnChangeName.setOnClickListener(changeName);
        btnChangePassword.setOnClickListener(changePassword);
    }


    private View.OnClickListener changeEmail = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //on process
        }
    };

    private View.OnClickListener changeName = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //on process
            String firstName = etFirstNameUpdated.getText().toString();
            String lastName = etLastNameUpdated.getText().toString();
            ParseUser.getCurrentUser();







        }
    };

    private View.OnClickListener changePassword = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //on process

        }
    };


}
