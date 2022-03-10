package com.wallet.darius.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.Function.DownloadWalletFunction;
import com.wallet.darius.ui.universalView.LoginView;
import com.wallet.darius.ui.dashboard.DashboardActivity;
import com.wallet.darius.R;
import com.wallet.darius.ui.password.ForgotPasswordActivity;
import com.wallet.darius.ui.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private EditText loginEmail, loginPassword;
    private TextView errorEmail, errorPassword, singUp, forgotPass;
    private Button loginBtn;

    private LoginPresenter loginPresenter;
    private DownloadWalletFunction downloadWalletFunction;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        loginEmail = findViewById(R.id.log_login_email);
        loginPassword = findViewById(R.id.log_login_password);
        errorEmail = findViewById(R.id.log_error_email);
        errorPassword = findViewById(R.id.log_error_pass);
        singUp = findViewById(R.id.log_signup);
        forgotPass = findViewById(R.id.log_fg_pass);
        loginBtn = findViewById(R.id.log_login_btn);

        dialog = new Dialog(this);

        downloadWalletFunction = new DownloadWalletFunction(this,
                getFilesDir().toString());

        loginPresenter = new LoginPresenter(this, downloadWalletFunction);

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

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Thread backgroundThread = new Thread(() -> {
            loginPresenter.login(email, password);
        });
        backgroundThread.start();

    }

    public void clearError() {
        errorEmail.setText("");
        errorPassword.setText("");
    }

    @Override
    public void onFail(String message) {
        dialog.dismiss();
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToNextPage(WalletAPI wallet) {
        dialog.dismiss();
        Log.i("xx", "LoginActivity " + wallet.getCredential().getAddress());
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.putExtra("wallet", wallet);

        startActivity(intent);
        finish();
    }

}