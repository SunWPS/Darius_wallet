package com.wallet.darius.ui.password;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.ui.login.LoginActivity;
import com.wallet.darius.R;
import com.wallet.darius.ui.BasicView;

public class ResetPasswordActivity extends AppCompatActivity implements BasicView {

    EditText inputCurrentPass, inputNewPass, inputCfNewPass;
    TextView errorCurrentPass, errorNewPass, errorCfNewPass;
    Button resetPassBtn;

    ResetPasswordPresenter resetPasswordPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputCurrentPass = findViewById(R.id.currentPassword);
        inputNewPass = findViewById(R.id.newPassword);
        inputCfNewPass = findViewById(R.id.cfNewPassword);
        errorCurrentPass = findViewById(R.id.error_currentPass);
        errorNewPass = findViewById(R.id.error_newPass);
        errorCfNewPass = findViewById(R.id.error_cfNewPass);
        resetPassBtn = findViewById(R.id.resetPassBtn);

        resetPasswordPresenter = new ResetPasswordPresenter(this);

        // setup button
        resetPassBtn.setOnClickListener(view -> onResetPassBtnClick());
    }

    private void onResetPassBtnClick() {
        clearError();

        String currentPass = inputCurrentPass.getText().toString();
        String newPass = inputNewPass.getText().toString();
        String cfNewPass = inputCfNewPass.getText().toString();

        if (currentPass.isEmpty()) {
            errorCurrentPass.setText("Required");
            return;
        }
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

        resetPasswordPresenter.resetPass(currentPass, newPass);
    }

    private void clearError() {
        errorCurrentPass.setText("");
        errorNewPass.setText("");
        errorCfNewPass.setText("");
    }

    @Override
    public void onSuccess() {
        Toast.makeText(ResetPasswordActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
        finish();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onFail(String message) {
        Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}