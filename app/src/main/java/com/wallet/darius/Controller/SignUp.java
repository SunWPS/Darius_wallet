package com.wallet.darius.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.R;

public class SignUp extends AppCompatActivity {

    EditText signupEmail, signupPassword, confirmPassword;
    TextView errorEmail, errorPass, errorCfPass;
    Button signupBtn;
    FirebaseAuth fAuth;

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

        fAuth = FirebaseAuth.getInstance();

        signupBtn.setOnClickListener(view -> {

            clearError();

            // extract data
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

            // data is validated and sign up the user using firebase
            Toast.makeText(SignUp.this, "Data Validated", Toast.LENGTH_SHORT).show();

            fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {

                fAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(unused -> {
                    Toast.makeText(SignUp.this, "Verification Email Sent.", Toast.LENGTH_SHORT).show();
                });

                // send user to next page
                Intent intent = new Intent(SignUp.this, VerifyEmail.class);
                Bundle b = new Bundle();
                b.putString("email", email);
                intent.putExtras(b);
                startActivity(intent);
                finish();

            }).addOnFailureListener(e -> Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show());

        });
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
}