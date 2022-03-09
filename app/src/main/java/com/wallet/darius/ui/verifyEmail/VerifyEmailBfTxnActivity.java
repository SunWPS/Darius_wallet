package com.wallet.darius.ui.verifyEmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wallet.darius.R;
import com.wallet.darius.ui.depositAndTransfer.DepositActivity;
import com.wallet.darius.ui.depositAndTransfer.TransferActivity;
import com.wallet.darius.ui.universalView.BasicView;

public class VerifyEmailBfTxnActivity extends AppCompatActivity implements BasicView {

    private TextView infoText, emailText, recheckLabel;
    private Button sendEmailBtn;

    private FirebaseUser user;
    private String nextPageCode;
    private Bundle extras;
    private VerifyEmailPresenter verifyEmailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email_bf_txn);

        infoText = findViewById(R.id.vf_bf_txn_info);
        emailText = findViewById(R.id.vf_bf_txn_email_label);
        recheckLabel = findViewById(R.id.vf_bf_txn_recheck);
        sendEmailBtn = findViewById(R.id.vf_bf_txn_send_email_btn);

        user = FirebaseAuth.getInstance().getCurrentUser();
        extras = getIntent().getExtras();
        nextPageCode = extras.getString("nextPage");
        verifyEmailPresenter = new VerifyEmailPresenter(this);

        emailText.setText(user.getEmail());

        sendEmailBtn.setOnClickListener(view -> verifyEmailPresenter.sendVerifyEmail());

        setUp();
    }

    private void setUp() {
        String baseText = "Please verify the email before ";
        switch (nextPageCode) {
            case "dp":
                infoText.setText(baseText + "deposit");
                setUpRecheckBtn(DepositActivity.class);
                break;
            case "tf":
                infoText.setText(baseText + "transfer");
                setUpRecheckBtn(TransferActivity.class);
                break;
        }
    }

    private void setUpRecheckBtn(Class<?> nextPage) {
        recheckLabel.setOnClickListener(view -> {
            if (!verifyEmailPresenter.checkVerify()) {
                Toast.makeText(VerifyEmailBfTxnActivity.this, "Your email is not verify", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(VerifyEmailBfTxnActivity.this, nextPage);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onSuccess() {
        Toast.makeText(VerifyEmailBfTxnActivity.this, "Verification Email Sent.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFail(String message) {
        Toast.makeText(VerifyEmailBfTxnActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}