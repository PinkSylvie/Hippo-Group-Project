package com.example.hippo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.ParseException;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

   private static final String TAG = "SignUpActivity";

   private EditText etFirstName;
   private EditText etLastName;
   private EditText etEmail;
   private EditText etPassword;
   private EditText etPassword2;

   private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPassword2 = findViewById(R.id.etPassword2);

        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(signUpUser);

    }


    private View.OnClickListener signUpUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String rewrite = etPassword2.getText().toString();

            if(canSignUp(firstName, lastName, email, password, rewrite)){
                signUp(firstName, lastName, email, password);
            }
        }
    };



    public void signUp(String firstName, String lastName, String email, String password){
        HippoUser user = new HippoUser();
        user.put(user.KEY_FIRST_NAME, firstName);
        user.put(user.KEY_LAST_NAME, lastName);
        user.setUsername(email);
        user.setEmail(email);
        user.setPassword(password);

        user.signUpInBackground(e->{
            if(e == null){
                HippoUser.logOut();
            }else{
                Log.e(TAG, e.getMessage());
                if(e.getCode() == ParseException.USERNAME_TAKEN){
                    Toast.makeText(this, "Email already in use", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, e.getMessage());
                }else if(e.getCode() == ParseException.INVALID_SESSION_TOKEN){
                    Log.w(TAG, e.getMessage()+ ": " + user.getSessionToken());
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        finish();
    }

    public boolean canSignUp(String firstName, String lastName, String email, String password, String rewrite){
        if(firstName.isEmpty()){
            Toast.makeText(this, "No name has been typed", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            isValidEmail(email);
        } catch (InvalidEmailException e) {
            Log.w(TAG, e.getMessage() + e.printMessage());
            Toast.makeText(this, e.printMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            isValidPassword(password, rewrite);
        } catch (InvalidPasswordException e) {
            Log.w(TAG, e.getMessage() + e.printMessage());
            Toast.makeText(this, e.printMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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

    public void isValidPassword(String password, String rewrite) throws InvalidPasswordException{

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

        if(!password.equals(rewrite)){
            throw new InvalidPasswordException(7);
        }
    }

}