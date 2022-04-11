package com.example.hippo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SignInActivity extends AppCompatActivity {

    public static final String TAG = "SignInActivity";
    private TextView tvSignUp;
    private EditText etSignInEmail;
    private EditText etSignInPassword;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        tvSignUp = findViewById(R.id.tvSignUpTap);
        tvSignUp.setOnClickListener(goSignUpActivity);

        if(ParseUser.getCurrentUser()!=null){
            //comentado de momento para probar las distintas partes del sign in / log in
            // Crear el boton de log out para que esta parte sea funcional, dejando tu usuario en la app
            //goMainActivity();
        }
        etSignInEmail = findViewById(R.id.etSignInEmail);
        etSignInPassword = findViewById(R.id.etSignInPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick Log in/Sign in Button");
                String username = etSignInEmail.getText().toString();
                String password = etSignInPassword.getText().toString();
                if(canSignIn(username,password)) {
                    loginUser(username, password);
                }
            }
        });
    }


    public boolean canSignIn(String username, String password){
        if(username.isEmpty() && password.isEmpty()){
            Toast.makeText(this, "Type your credentials!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(username.isEmpty()){
            Toast.makeText(this, "Type your email!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.isEmpty()){
            Toast.makeText(this, "No password has been typed", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to Log In"+ username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e !=null){
                    Toast.makeText(getApplicationContext(), "Log in unsuccessful. Verify your email and password try again!", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Issue with user trying to log in",e);
                    return;
                }
                goMainActivity();
            }
        });

    }

    private void goMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private View.OnClickListener goSignUpActivity = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(view.getContext(), SignUpActivity.class);
            startActivity(i);
            finish();
        }
    };
}