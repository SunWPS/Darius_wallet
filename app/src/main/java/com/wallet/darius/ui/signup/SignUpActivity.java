package com.wallet.darius.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.darius.R;
import com.wallet.darius.ui.BasicView;

public class SignUpActivity extends AppCompatActivity implements BasicView {

    EditText signupEmail, signupPassword, confirmPassword;
    TextView errorEmail, errorPass, errorCfPass;
    Button signupBtn;

    SignUpPresenter signUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);
        confirmPassword = findViewById(R.id.confirmPassword);

        errorEmail = findViewById(R.id.error_email);
        errorPass = findViewById(R.id.error_pass);
        errorCfPass = findViewById(R.id.error_cfpass);

        signupBtn = findViewById(R.id.signupBtn);

        signUpPresenter = new SignUpPresenter(this);

        // setup button
        signupBtn.setOnClickListener(view -> onSignUpBtnClick());
    }

    public void onSignUpBtnClick() {
        clearError();

        String email = signupEmail.getText().toString();
        String password = signupPassword.getText().toString();
        String cfPassword = confirmPassword.getText().toString();

        if (email.isEmpty()) {
            errorEmail.setText("Email is Required");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorEmail.setText("Email is not Validated");
            return;
        }
        if (password.isEmpty()) {
            errorPass.setText("Password is Required");
            return;
        }
        if (cfPassword.isEmpty()) {
            errorCfPass.setText("Confirm Password is Required");
            return;
        }
        if (!password.equals(cfPassword)) {
            errorCfPass.setText("Password Don't Match");
            return;
        }

        // sign up and send data to firebase
        signUpPresenter.signUp(email, password);
    }

    public void clearError(){
        errorEmail.setText("");
        errorPass.setText("");
        errorCfPass.setText("");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(SignUpActivity.this, VerifyEmailActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFail(String message) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}