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
import com.wallet.darius.ui.universalView.BasicView;
import com.wallet.darius.ui.verifyEmail.VerifyEmailActivity;

public class SignUpActivity extends AppCompatActivity implements BasicView {

    private EditText signupEmail, signupPassword, confirmPassword;
    private TextView errorEmail, errorPass, errorCfPass;
    private Button signupBtn;

    private SignUpPresenter signUpPresenter;
    private String realPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupEmail = findViewById(R.id.su_signup_email);
        signupPassword = findViewById(R.id.su_signup_password);
        confirmPassword = findViewById(R.id.su_confirm_password);

        errorEmail = findViewById(R.id.su_error_email);
        errorPass = findViewById(R.id.su_error_pass);
        errorCfPass = findViewById(R.id.su_error_cfpass);

        signupBtn = findViewById(R.id.su_signup_btn);

        realPassword = "";

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

        realPassword = password;
        signUpPresenter.signUp(email, password);
    }

    public void clearError(){
        errorEmail.setText("");
        errorPass.setText("");
        errorCfPass.setText("");
    }


    @Override
    public void onSuccess() {
        Intent intent = new Intent(SignUpActivity.this, VerifyEmailActivity.class);
        intent.putExtra("password", realPassword);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onFail(String message) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}