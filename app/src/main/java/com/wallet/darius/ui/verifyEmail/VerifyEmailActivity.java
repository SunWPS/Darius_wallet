package com.wallet.darius.ui.verifyEmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.internal.InternalTokenProvider;
import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.ui.pin.CreatePinActivity;
import com.wallet.darius.ui.universalView.BasicView;
import com.wallet.darius.R;
import com.wallet.darius.ui.dashboard.DashboardActivity;

public class VerifyEmailActivity extends AppCompatActivity implements BasicView {

    private Button nextToUsername;
    private TextView emailLabel;

    private VerifyEmailPresenter verifyEmailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        emailLabel = findViewById(R.id.vf_email_label);
        nextToUsername = findViewById(R.id.vf_finish_btn);

        verifyEmailPresenter = new VerifyEmailPresenter(this);

        emailLabel.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        setUpButton();

        verifyEmailPresenter.sendVerifyEmail();
    }

    private void setUpButton() {
        nextToUsername.setOnClickListener(view -> {

            Bundle extras = getIntent().getExtras();
            WalletAPI myWallet = verifyEmailPresenter.getWallet(extras.getString("password"), getFilesDir().toString());

            Intent intent = new Intent(VerifyEmailActivity.this, CreatePinActivity.class);
            intent.putExtra("wallet", myWallet);
            startActivity(intent);
            finish();

        });
    }

    @Override
    public void onSuccess() {
        Toast.makeText(VerifyEmailActivity.this, "Verification Email Sent.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFail(String message) {
        Toast.makeText(VerifyEmailActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}