package com.wallet.darius.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.MainActivity;
import com.wallet.darius.R;

public class LoginScreen extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    TextView errorEmail, errorPassword;
    Button loginBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        firebaseAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);

        errorEmail = findViewById(R.id.error_email);
        errorPassword = findViewById(R.id.error_pass);

        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(view -> {

            clearError();

            // extract and validate
            if (loginEmail.getText().toString().isEmpty()) {
                errorEmail.setText("Email is Missing");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(loginEmail.getText().toString()).matches()) {
                errorEmail.setText("Email is not Validated");
            }
            if (loginPassword.getText().toString().isEmpty()) {
                errorPassword.setText("Password is Missing");
            }

            // login
            firebaseAuth.signInWithEmailAndPassword(loginEmail.getText().toString(),
                    loginPassword.getText().toString()).addOnSuccessListener(authResult -> {

                        startActivity(new Intent(LoginScreen.this, Dashboard.class));
                        finish();

                    }).addOnFailureListener(e -> Toast.makeText(LoginScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        });

    }

    public void clearError() {
        errorEmail.setText("");
        errorPassword.setText("");
    }

    public void signUpOnClick(View view) {
        startActivity(new Intent(LoginScreen.this, SignUp.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginScreen.this, Dashboard.class));
            finish();
        }
    }
}