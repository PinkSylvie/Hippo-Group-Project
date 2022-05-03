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
import com.example.hippo.HippoUser;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;
import java.util.regex.Pattern;


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
    public String change="";

    public SettingsActivity(){

    }

    SignUpActivity signUp = new SignUpActivity();

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
            String newEmail = etNewEmail.getText().toString();
            ParseUser user = HippoUser.getCurrentUser();
            try {
                if(canChangeEmail()==true){
                    user.setEmail(newEmail);
                }
            } catch (InvalidEmailException e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener changeName = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            canChangeName();
        }

    };


    private View.OnClickListener changePassword = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String NewPassword = etNewPassword.getText().toString();
            ParseUser user = HippoUser.getCurrentUser();
            try {
                if(canChangePassword()==true){
                    user.setPassword(NewPassword);
                }
            } catch (InvalidPasswordException e) {
                e.printStackTrace();
            }
        }
    };

    public void canChangeName(){
        String firstName = etFirstNameUpdated.getText().toString();
        String lastName = etLastNameUpdated.getText().toString();
        ParseUser user = HippoUser.getCurrentUser();

        if(!(firstName.isEmpty() || lastName.isEmpty())) {
            user.put(HippoUser.KEY_FIRST_NAME, firstName);
            user.put(HippoUser.KEY_LAST_NAME, lastName);
            user.saveInBackground();
            Toast.makeText(this, "your name was changed to: " + firstName, Toast.LENGTH_SHORT).show();
            etFirstNameUpdated.setText("");
            etLastNameUpdated.setText("");
        }else{
            Toast.makeText(this, "Name or last name cannot be empty", Toast.LENGTH_SHORT).show();
        }

    }

    public Boolean canChangeEmail() throws InvalidEmailException {
        String oldEmail = etOldEmail.getText().toString();
        String newEmail = etNewEmail.getText().toString();
        ParseUser user = HippoUser.getCurrentUser();
        if(oldEmail.isEmpty() && newEmail.isEmpty()){
            Toast.makeText(this, "New email or old email cannot be empty!", Toast.LENGTH_SHORT).show();
        }

        if(!(user.getEmail().equals(oldEmail))){
            Toast.makeText(this, "The email you typed in is not the same as your current one!", Toast.LENGTH_SHORT).show();
            etOldEmail.setText("");
            etNewEmail.setText("");
        }
        try{
            isValidEmail(newEmail);
        } catch (InvalidEmailException e) {

        }
        return true;
    }

    public Boolean canChangePassword() throws InvalidPasswordException {
        String OldPassword = etOldPassword.getText().toString();
        String NewPassword = etNewPassword.getText().toString();

        ParseUser.logInInBackground(ParseUser.getCurrentUser().getUsername(), OldPassword, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user!=null){
                    change = "true";
                }else{
                    change="false";
                }
            }
        });

            try {
                isValidPassword(NewPassword);
            } catch (InvalidPasswordException e) {
                Log.w(TAG, e.getMessage() + e.printMessage());
                Toast.makeText(this, e.printMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }
        return true;
    }



    public void isValidPassword(String password) throws InvalidPasswordException{

        if(password.length() < 8 || password.length() > 15){
            throw new InvalidPasswordException(1);
        }

        if(password.contains(" ")){
            throw new InvalidPasswordException(2);
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for(int i = 0; i < password.length(); i++){
            if(Character.isUpperCase(password.charAt(i))){
                hasUpperCase = true;
            }else if(Character.isLowerCase(password.charAt(i))){
                hasLowerCase = true;
            }else if(Character.isDigit(password.charAt(i))){
                hasDigit = true;
            }else{
                hasSpecialChar = true;
            }
            if(hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar){break;}
        }
        if(!hasUpperCase){
            throw new InvalidPasswordException(5);
        }

        if (!hasLowerCase) {
            throw  new InvalidPasswordException(6);
        }

        if(!hasDigit){
            throw new InvalidPasswordException(3);
        }

        if(!hasSpecialChar){
            throw new InvalidPasswordException(4);
        }


    }


    public void isValidEmail(String email) throws InvalidEmailException{

        if(email.isEmpty()){throw new InvalidEmailException(3);}

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if(!pat.matcher(email).matches()){
            throw new InvalidEmailException(1);
        }

    }
}

