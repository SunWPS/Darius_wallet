package com.wallet.darius.ui.transfer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.R;

import java.math.BigDecimal;

import jnr.constants.platform.WaitFlags;

public class TransferActivity extends AppCompatActivity {

    private TextView availableBalance, currentNetwork, errorAddress, errorAmount;
    private EditText receiverAddress, amountText;
    private Button letTransferBtn;

    WalletAPI myWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        availableBalance = findViewById(R.id.availableBalance);
        currentNetwork = findViewById(R.id.currentPassword);
        errorAddress = findViewById(R.id.errorAddress);
        errorAmount = findViewById(R.id.errorAmount);
        receiverAddress = findViewById(R.id.receiverAddress);
        amountText = findViewById(R.id.amountText);
        letTransferBtn = findViewById(R.id.letTransferBtn);

        Bundle extras = getIntent().getExtras();
        myWallet = extras.getParcelable("wallet");
        availableBalance.setText(myWallet.retrieveBalance() + " ETH");

        letTransferBtn.setOnClickListener(view -> {
            clearError();

            if (receiverAddress.getText().toString().isEmpty()) {
                errorAddress.setText("Address is Required");
                return;
            }
            if (amountText.getText().toString().isEmpty()) {
                errorAmount.setText("Amount is Required");
                return;
            }

            BigDecimal amount = new BigDecimal(amountText.getText().toString());
            if (amount.compareTo(new BigDecimal(0.0)) <=  0) {
                errorAmount.setText("Amount should greater than 0");
                return;
            }

            myWallet.makeTransaction(receiverAddress.getText().toString(), amount);

            // TODO: confirm, Progress

            clearEditText();
        });
    }

    private void clearError() {
        errorAddress.setText("");
        errorAmount.setText("");
    }

    private void clearEditText() {
        receiverAddress.getText().clear();
        amountText.getText().clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}