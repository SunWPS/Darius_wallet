package com.wallet.darius.ui.Password;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.darius.R;
import com.wallet.darius.ui.BasicView;

public class ForgotPasswordActivity extends AppCompatActivity implements BasicView {

    EditText emailToSend;
    TextView errorEmail;
    Button sendEmailBtn;

    ForgotPasswordPresenter forgotPasswordPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        emailToSend = findViewById(R.id.email_to_send);
        errorEmail = findViewById(R.id.error_email_to_send);
        sendEmailBtn = findViewById(R.id.emailSendBtn);

        forgotPasswordPresenter = new ForgotPasswordPresenter(this);

        // setup button
        sendEmailBtn.setOnClickListener(view -> onSendEmailBtnClick());

    }

    private void onSendEmailBtnClick() {
        // clear error
        errorEmail.setText("");

        String email = emailToSend.getText().toString();

        if (email.isEmpty()) {
            errorEmail.setText("Email is Missing");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorEmail.setText("Email is not Validated");
            return;
        }

        forgotPasswordPresenter.sendResetPassLink(email);

    }


    @Override
    public void onSuccess() {
        Toast.makeText(ForgotPasswordActivity.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFail(String message) {
        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}