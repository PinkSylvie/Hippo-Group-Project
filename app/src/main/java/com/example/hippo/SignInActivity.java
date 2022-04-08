package com.example.hippo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    private TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        tvSignUp = findViewById(R.id.tvSignUpTap);
        tvSignUp.setOnClickListener(goSignUpActivity);
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