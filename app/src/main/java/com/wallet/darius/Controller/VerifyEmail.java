package com.wallet.darius.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.wallet.darius.R;

public class VerifyEmail extends AppCompatActivity {

    Button backToLoginBtn;
    TextView emailLabel;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        emailLabel = findViewById(R.id.emailLabel);
        backToLoginBtn = findViewById(R.id.backToLoginBtn);

        Bundle b = getIntent().getExtras();
        if (b != null)
            email = b.getString("email");

        emailLabel.setText(email);

        backToLoginBtn.setOnClickListener(view -> {
            startActivity(new Intent(VerifyEmail.this, Dashboard.class));
            finish();
        });
    }

    public void setEmail(String email) {
        this.email = email;
    }
}