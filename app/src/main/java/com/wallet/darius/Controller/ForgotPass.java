package com.wallet.darius.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.R;

public class ForgotPass extends AppCompatActivity {

    EditText emailToSend;
    TextView errorEmail;
    Button sendEmailBtn;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        emailToSend = findViewById(R.id.email_to_send);
        errorEmail = findViewById(R.id.error_email_to_send);
        sendEmailBtn = findViewById(R.id.emailSendBtn);

        auth = FirebaseAuth.getInstance();

        sendEmailBtn.setOnClickListener(view -> {
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

            auth.sendPasswordResetEmail(email).addOnSuccessListener(aVoid -> {
                Toast.makeText(ForgotPass.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(ForgotPass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        });
    }
}