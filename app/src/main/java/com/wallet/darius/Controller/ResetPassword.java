package com.wallet.darius.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wallet.darius.R;

public class ResetPassword extends AppCompatActivity {

    EditText inputNewPass, inputCfNewPass;
    TextView errorNewPass, errorCfNewPass;
    Button resetPassBtn;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputNewPass = findViewById(R.id.newPassword);
        inputCfNewPass = findViewById(R.id.cfNewPassword);
        errorNewPass = findViewById(R.id.error_newPass);
        errorCfNewPass = findViewById(R.id.error_cfNewPass);
        resetPassBtn = findViewById(R.id.resetPassBtn);

        user = FirebaseAuth.getInstance().getCurrentUser();

        resetPassBtn.setOnClickListener(view -> {
            clearError();

            String newPass = inputNewPass.getText().toString();
            String cfNewPass = inputCfNewPass.getText().toString();

            if (newPass.isEmpty()) {
                errorNewPass.setText("Required");
                return;
            }
            if (cfNewPass.isEmpty()) {
                errorCfNewPass.setText("Required");
                return;
            }
            if (!newPass.equals(cfNewPass)) {
                errorCfNewPass.setText("Not Match!!");
                return;
            }

            user.updatePassword(newPass).addOnSuccessListener(aVoid -> {
                Toast.makeText(ResetPassword.this, "Password updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResetPassword.this, LoginScreen.class));
                finish();
                FirebaseAuth.getInstance().signOut();
            }).addOnFailureListener(e -> {
                Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void clearError() {
        errorNewPass.setText("");
        errorCfNewPass.setText("");
    }
}