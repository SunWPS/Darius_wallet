package com.wallet.darius.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.wallet.darius.ui.BasicView;
import com.wallet.darius.ui.dashboard.DashboardActivity;
import com.wallet.darius.R;
import com.wallet.darius.ui.password.ForgotPasswordActivity;
import com.wallet.darius.ui.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity implements BasicView {

    EditText loginEmail, loginPassword;
    TextView errorEmail, errorPassword, singUp, forgotPass;
    Button loginBtn;

    LoginPresenter loginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        errorEmail = findViewById(R.id.error_email);
        errorPassword = findViewById(R.id.error_pass);
        singUp = findViewById(R.id.signup);
        forgotPass = findViewById(R.id.fg_pass);
        loginBtn = findViewById(R.id.loginBtn);

        loginPresenter = new LoginPresenter(this);

        // setup Button
        loginBtn.setOnClickListener(view -> onLoginBtnClick());

        singUp.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        forgotPass.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

    }

    public void onLoginBtnClick() {
        clearError();

        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        if (email.isEmpty()) {
            errorEmail.setText("Email is Missing");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorEmail.setText("Email is not Validated");
            return;
        }
        if (password.isEmpty()) {
            errorPassword.setText("Password is Missing");
            return;
        }

        // login
        loginPresenter.login(email, password);
    }

    public void clearError() {
        errorEmail.setText("");
        errorPassword.setText("");
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }
    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        finish();
    }

    @Override
    public void onFail(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}