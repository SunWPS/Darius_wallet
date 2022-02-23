package com.wallet.darius.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.ui.UniversalView.BasicView;
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

        emailLabel = findViewById(R.id.emailLabel);
        nextToUsername = findViewById(R.id.next_to_username);

        try {
            verifyEmailPresenter = new VerifyEmailPresenter(this,
                    getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        emailLabel.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        setUpButton();

        verifyEmailPresenter.sendVerifyEmail();
    }

    private void setUpButton() {
        nextToUsername.setOnClickListener(view -> {

            Bundle extras = getIntent().getExtras();

            WalletAPI myWallet = verifyEmailPresenter.getWallet(extras.getString("password"), getFilesDir().toString());

            Intent intent = new Intent(VerifyEmailActivity.this, DashboardActivity.class);
            intent.putExtra("wallet", myWallet);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
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