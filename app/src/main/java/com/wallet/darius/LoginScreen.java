package com.wallet.darius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }

    public void signUpOnClick(View view) {
        startActivity(new Intent(LoginScreen.this, SignUp.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}