package com.wallet.darius.ui.changeEmail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.darius.R;
import com.wallet.darius.ui.password.ForgotPasswordActivity;
import com.wallet.darius.ui.universalView.BasicView;

public class ChangeEmailActivity extends AppCompatActivity implements BasicView {

    private TextView currentEmail, errorNewEmail;
    private EditText newEmail;
    private Button submitBtn;

    private ChangeEmailPresenter changeEmailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        currentEmail = findViewById(R.id.cm_current_email);
        errorNewEmail = findViewById(R.id.cm_error_new_email);
        newEmail = findViewById(R.id.cm_new_email);
        submitBtn = findViewById(R.id.cm_submit_btn);

        changeEmailPresenter = new ChangeEmailPresenter(this);

        currentEmail.setText(changeEmailPresenter.getCurrentEmail());

        setUpBtn();

    }

    private void setUpBtn() {
        submitBtn.setOnClickListener(view -> {
            errorNewEmail.setText("");

            String email = newEmail.getText().toString();

            if (email.isEmpty()) {
                errorNewEmail.setText("New Email is Missing");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                errorNewEmail.setText("New Email is not Validated");
                return;
            }

            changeEmailPresenter.changeEmail(email);
            newEmail.getText().clear();
        });
    }

    @Override
    public void onSuccess() {
        Toast.makeText(ChangeEmailActivity.this, "Change Email Successful", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFail(String message) {
        Toast.makeText(ChangeEmailActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}