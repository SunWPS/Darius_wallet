package com.wallet.darius.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.Function.DownloadWalletFunction;
import com.wallet.darius.ui.UniversalView.LoginView;
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

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = ai.metaData;

            downloadWalletFunction = new DownloadWalletFunction(this,
                    getFilesDir().toString(),
                    metaData.getString("infuraURI"));

        } catch (Exception e) {
            e.getMessage();
        }

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
        loginPresenter.login(email, password);
    }

    public void clearError() {
        errorEmail.setText("");
        errorPassword.setText("");
    }

    @Override
    public void onFail(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToDashBoard(WalletAPI wallet) {
        Log.i("xx", "LoginActivity " + wallet.getCredential().getAddress());
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.putExtra("wallet", wallet);

        startActivity(intent);
        finish();
    }

}