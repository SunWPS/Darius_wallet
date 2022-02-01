package com.wallet.darius.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.ui.BasicView;
import com.wallet.darius.ui.dashboard.DashboardActivity;
import com.wallet.darius.R;

public class VerifyEmailActivity extends AppCompatActivity implements BasicView {

    Button backToLoginBtn;
    TextView emailLabel;

    VerifyEmailPresenter verifyEmailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        emailLabel = findViewById(R.id.emailLabel);
        backToLoginBtn = findViewById(R.id.backToLoginBtn);

        verifyEmailPresenter = new VerifyEmailPresenter(this);

        emailLabel.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        backToLoginBtn.setOnClickListener(view -> {
            startActivity(new Intent(VerifyEmailActivity.this, DashboardActivity.class));
            finish();
        });

        verifyEmailPresenter.sendVerifyEmail();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(VerifyEmailActivity.this, "Verification Email Sent.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFail(String message) {
        Toast.makeText(VerifyEmailActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}