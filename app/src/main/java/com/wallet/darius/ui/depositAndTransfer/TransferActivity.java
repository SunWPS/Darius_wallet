package com.wallet.darius.ui.depositAndTransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.R;

import java.math.BigDecimal;


public class TransferActivity extends AppCompatActivity {

    private TextView availableBalance, currentNetwork, errorAddress, errorAmount;
    private EditText receiverAddress, amountText;
    private Button letTransferBtn;

    private WalletAPI myWallet;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        availableBalance = findViewById(R.id.tf_available_balance);
        currentNetwork = findViewById(R.id.tf_current_network);
        errorAddress = findViewById(R.id.tf_error_address);
        errorAmount = findViewById(R.id.tf_error_amount);
        receiverAddress = findViewById(R.id.tf_receiver_address);
        amountText = findViewById(R.id.tf_amount);
        letTransferBtn = findViewById(R.id.tf_transfer_btn);

        dialog = new Dialog(this);

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

            confirmDialog(receiverAddress.getText().toString(), amount);
        });
    }

    private void confirmDialog(String address, BigDecimal amount) {
        dialog.setContentView(R.layout.dialog_confirm_transaction);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button noBtn = dialog.findViewById(R.id.noConfirmBtn);
        Button yesBtn = dialog.findViewById(R.id.yesConfirmBtn);
        TextView confirmReceiverAddress = dialog.findViewById(R.id.confirmReceiverAddress);
        TextView confirmAmountText = dialog.findViewById(R.id.confirmAmountText);

        confirmReceiverAddress.setText(address);
        confirmAmountText.setText(amount + " ETH");

        noBtn.setOnClickListener(view -> {
            dialog.dismiss();
        });

        yesBtn.setOnClickListener(view -> {

            dialog.setContentView(R.layout.dialog_processing);
            dialog.show();

            Thread backgroundThread = new Thread(() -> {
                Log.i("xx", address);
                boolean successful = myWallet.makeTransaction(address, amount);

                runOnUiThread(() -> {
                    if (successful) {
                        dialog.setContentView(R.layout.dialog_successful);

                        Button okBtn = dialog.findViewById(R.id.okSuccessfulBtn);
                        okBtn.setOnClickListener(view1 -> {
                            dialog.dismiss();
                            clearEditText();
                        });
                    } else {
                        dialog.setContentView(R.layout.dailog_failed);

                        Button okBtn = dialog.findViewById(R.id.okFailedBtn);
                        okBtn.setOnClickListener(view1 -> {
                            dialog.dismiss();
                            clearEditText();
                        });
                    }
                    dialog.show();
                });
            });
            backgroundThread.start();
        });
        dialog.show();
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